package hnz.mitra.siteware.omie.models.apiSources;

import br.com.mitra.actionJar.ActionContext;
import br.com.mitra.actionJar.SourceData;
import com.google.gson.JsonObject;
import hnz.mitra.siteware.omie.DAO.DmlDAO;
import hnz.mitra.siteware.omie.models.APISource;
import hnz.mitra.siteware.omie.service.APISourceService;
import hnz.mitra.siteware.omie.utils.PrintUtils;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ImportDataSource extends APISource {

    public ImportDataSource(APISource next){
        this.description="Execute query";
        this.next=next;
    }

    @Override
    public void process(JsonObject data, Object params) throws SQLException {
        Long executionStart = System.currentTimeMillis();
        ActionContext actionContext = (ActionContext) APISourceService.persistentParams.get("actionContext");
        String id = ((HashMap<String, String>) params).get("id");
        PrintUtils.printDebug("Importing data to table: "+id);
        DmlDAO dmlDAO = new DmlDAO(actionContext.getConnection());
        dmlDAO.save(data.get("value").getAsJsonArray(),id);
        PrintUtils.durationDebug("Finished importing data of table: "+id,executionStart);
    }

    @Override
    public void getParams(ActionContext actionContext) {
        ArrayList<HashMap<String, String>> queriesParams = new ArrayList<>();
        for (SourceData sourceData : actionContext.getSourceContent()) {
            HashMap<String, String> param = new HashMap<>();
            String id = ((String) sourceData.getField("id")).toLowerCase();
            param.put("id", id);
            String apiUrl = ((String) sourceData.getField("apiUrl"));
            param.put("apiUrl", apiUrl);
            queriesParams.add(param);
        }
        APISourceService.persistentParams.put("queries", queriesParams);
    }


    @Override
    public void validadeSourceParams(Object params) {
        String id = ((HashMap<String, String>) params).get("id");
        String apiUrl = ((HashMap<String, String>) params).get("apiUrl");

        if(Objects.isNull(id)||id.equals("")||id.equals("access_params")){
            throw new RuntimeException("Id is invalid: "+id);
        }

        if(Objects.isNull(apiUrl)||apiUrl.equals("")||apiUrl.equals("access_params")){
            throw new RuntimeException("Url is invalid: "+apiUrl);
        }
    }
}
