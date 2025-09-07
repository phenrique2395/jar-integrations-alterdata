package hnz.mitra.siteware.omie.models;

import br.com.mitra.actionJar.ActionContext;
import com.google.gson.JsonObject;
import hnz.mitra.siteware.omie.service.APISourceService;
import hnz.mitra.siteware.omie.utils.PrintUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

public abstract class APISource {

    protected String description;
    protected APISource next;

    public abstract void validateSourceParams(Object param);

    public abstract void process(JsonObject data, Object param) throws SQLException, ParseException;

    public abstract void getParams(ActionContext actionContext);

    public void callNext() throws SQLException, ParseException, IOException, InterruptedException {
        if(next==null){
            PrintUtils.printDebug("There is no next API Source to call. Finishing chain execution");
            return;
        }
        PrintUtils.printDebug("Calling next API Source: "+next.getDescription());
        APISourceService.importData(next);
    }

    public String getDescription() {
        return description;
    }
}
