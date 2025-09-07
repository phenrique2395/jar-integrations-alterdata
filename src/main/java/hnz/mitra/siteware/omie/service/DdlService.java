package hnz.mitra.siteware.omie.service;

import br.com.mitra.actionJar.ActionContext;
import br.com.mitra.actionJar.SourceData;
import hnz.mitra.siteware.omie.DAO.DdlDAO;
import hnz.mitra.siteware.omie.utils.PrintUtils;

import java.sql.SQLException;

public class DdlService {

    private static ActionContext actionContext;
    private DdlDAO ddlDAO;

    public DdlService(ActionContext actionContext){
        if(actionContext.getConnection()==null){
            PrintUtils.printError("Finishing execution: Connection from Action Context is null!!");
            throw new RuntimeException("Connection from Action Context is null!!");
        }
        this.actionContext=actionContext;
        this.ddlDAO=new DdlDAO(actionContext.getConnection());
    }

    public Boolean truncateIfExists(String id) throws SQLException {
        return ddlDAO.truncateIfExists(id);
    }

    public void createTable(String id) throws SQLException {
        Long executionStart = System.currentTimeMillis();
        PrintUtils.printDebug("Starting table "+id+" creation.");
        ddlDAO.createTableUsingColumnar(id);
        PrintUtils.printDebug("Table "+id+" was created.");
        PrintUtils.durationDebug("Finished preparing DDL related to table: "+id,executionStart);
    }
}
