package hnz.mitra.siteware.omie.service;

import br.com.mitra.actionJar.ActionContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import hnz.mitra.siteware.omie.DAO.APIErrorDAO;
import hnz.mitra.siteware.omie.DAO.AccessParamsDAO;
import hnz.mitra.siteware.omie.models.APIError;
import hnz.mitra.siteware.omie.models.APISource;
import hnz.mitra.siteware.omie.models.AccessParams;
import hnz.mitra.siteware.omie.utils.APIUtils;
import hnz.mitra.siteware.omie.utils.PrintUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

public class APISourceService {
    private static ActionContext actionContext;
    public static HashMap<String,Object> persistentParams;
    public static ArrayList<APIError> errorList = new ArrayList<>();

    public APISourceService(ActionContext actionContext){
        if(actionContext.getConnection()==null){
            PrintUtils.printError("Finishing execution: Connection from Action Context is null!!");
            throw new RuntimeException("Connection from Action Context is null!!");
        }
        this.actionContext=actionContext;
        this.persistentParams= new HashMap<>();
        this.persistentParams.put("actionContext",actionContext);
    }

    public static void importData(APISource apiSource) throws ParseException, SQLException, IOException, InterruptedException {
        Long executionStart = System.currentTimeMillis();
        PrintUtils.setApiSource(apiSource);
        AccessParamsDAO accessParamsDAO = new AccessParamsDAO(actionContext.getConnection());
        AccessParams accessParams = accessParamsDAO.get();
        APIUtils apiUtils = new APIUtils();
        apiSource.getParams(actionContext);
        DdlService ddlService = new DdlService(actionContext);
        try {
            for(HashMap<String,String> params: (ArrayList<HashMap<String,String>>)persistentParams.get("queries")) {
                apiSource.validateSourceParams(params);
                String id = params.get("id");
                String apiUrl = params.get("apiUrl");
                apiUrl = replaceUrlParamsIfNeeded(apiUrl, params);
                Boolean tableAlreadyExists = ddlService.truncateIfExists(id);
                if (!Boolean.TRUE.equals(tableAlreadyExists)) ddlService.createTable(id);
                int nextPageNumber = 1;
                boolean hasNextCall = Boolean.TRUE;
                while (hasNextCall) {
                    PrintUtils.printDebug("Executing request skipping next " + nextPageNumber + " lines");
                    String json = apiUtils.executeRequest(accessParams, id, nextPageNumber, apiUrl);
                    JsonObject resultJson = new JsonParser().parse(json).getAsJsonObject();
                    apiSource.process(resultJson, params);
                    nextPageNumber += 1;
                    hasNextCall = isHasNextCall(resultJson, nextPageNumber);
                }
            }
        }catch(Exception e ){
            e.printStackTrace();
            PrintUtils.printError("Error executing import. Error message: "+e.getMessage());
        }finally {
            if(!Boolean.TRUE.equals(errorList.isEmpty())){
                APIErrorDAO apiErrorDAO = new APIErrorDAO(actionContext.getConnection());
                errorList.forEach(apiErrorDAO::save);
            }
        }
        PrintUtils.durationDebug("Finished requisition execution",executionStart);
        apiSource.callNext();
    }

    private static String replaceUrlParamsIfNeeded(String apiUrl, HashMap<String, String> params) {
        return apiUrl
         .replace("{limite}", params.get("limite"))
         .replace("{dataCadastroInicial}", params.get("dataCadastroInicial"))
         .replace("{dataCadastroFinal}", params.get("dataCadastroFinal"))
         .replace("{codigoEmpresa}", params.get("codigoEmpresa"))
         .replace("{identificadorPessoa}", params.get("identificadorPessoa"))
         .replace("{dataVencimentoInicial}", params.get("dataVencimentoInicial"))
         .replace("{dataVencimentoFinal}", params.get("dataVencimentoFinal"));
    }

    private static boolean isHasNextCall(JsonObject resultJson, int currentPage) {
        if(resultJson.get("Paginacao") == null){
            return false;
        }

        return resultJson.get("Paginacao").getAsJsonObject().get("TotalPagina").getAsInt() >= currentPage;
    }
}
