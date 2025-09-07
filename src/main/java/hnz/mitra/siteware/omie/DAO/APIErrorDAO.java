package hnz.mitra.siteware.omie.DAO;


import com.google.gson.Gson;
import hnz.mitra.siteware.omie.models.APIError;
import hnz.mitra.siteware.omie.utils.PrintUtils;
import hnz.mitra.siteware.omie.utils.SQLUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;

public class APIErrorDAO {

    private final String TABLE_NAME="ERROR_LOG";
    private Connection connection;
    private Gson gson = new Gson();

    public APIErrorDAO(Connection connection){
        this.connection=connection;
    }

    public void save(APIError apiError) {
        Long startExecution = System.currentTimeMillis();
        PrintUtils.printDebug("Started saving API Error in "+TABLE_NAME);
        ArrayList<String> insertFields = new ArrayList<>(Arrays.asList(
                "OCURRENCEDATE",
                "ID",
                "REQUEST",
                "ERRORCODE",
                "ERRORMESSAGE",
                "ERRORRESPONSEBODY"
        ));
        String insertQuery = SQLUtils.buildSimpleInsertQuery(TABLE_NAME,insertFields);
        PrintUtils.printDebug("Insert statement: "+insertQuery);
        PreparedStatement insertStatement = null;
        try {
            insertStatement = connection.prepareStatement(insertQuery.toString());
            int index = 1;
            insertStatement.setTimestamp(index,new Timestamp(apiError.getOccurrenceDate().getTime()));
            insertStatement.setString(++index, apiError.getId());
            insertStatement.setString(++index, apiError.getRequest());
            insertStatement.setInt(++index, apiError.getErrorCode());
            insertStatement.setString(++index, apiError.getErrorMessage().length()>4000?apiError.getErrorMessage().substring(0,3999):apiError.getErrorMessage());
            insertStatement.setString(++index, apiError.getErrorResponseBody().length()>4000?apiError.getErrorResponseBody().substring(0,3999):apiError.getErrorResponseBody());
            insertStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally{
            if(Boolean.TRUE.equals(insertStatement!=null)){
                try {
                    insertStatement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        PrintUtils.durationDebug("Saved API Error in "+TABLE_NAME,startExecution);
    }
}
