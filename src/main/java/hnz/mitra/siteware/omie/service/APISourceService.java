package hnz.mitra.siteware.omie.service;

import br.com.mitra.actionJar.ActionContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import hnz.mitra.siteware.omie.DAO.APIErrorDAO;
import hnz.mitra.siteware.omie.DAO.AccessParamsDAO;
import hnz.mitra.siteware.omie.DAO.DdlDAO;
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
import java.util.Map;
import java.util.Objects;

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
                apiSource.validadeSourceParams(params);
                String id = params.get("id");
                String apiUrl = params.get("apiUrl");
                Boolean tableAlreadyExists = ddlService.truncateIfExists(id);
                if (!Boolean.TRUE.equals(tableAlreadyExists)) ddlService.createTable(id);
                int nextIntegerPageToSkip = 0;
                boolean hasNextCall = Boolean.TRUE;
                while (hasNextCall) {
                    PrintUtils.printDebug("Executing request skipping next " + nextIntegerPageToSkip + " lines");
                    String json = apiUtils.executeRequest(accessParams, id, nextIntegerPageToSkip, apiUrl);
                    JsonObject resultJson = new JsonParser().parse(json).getAsJsonObject();
                    apiSource.process(resultJson, params);
                    hasNextCall = !(resultJson.get("value").getAsJsonArray().isJsonNull() || resultJson.get("value").getAsJsonArray().size() == 0);
                    nextIntegerPageToSkip += 500;
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
}
