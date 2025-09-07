package hnz.mitra.siteware.omie.DAO;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import hnz.mitra.siteware.omie.utils.PrintUtils;
import hnz.mitra.siteware.omie.utils.SQLUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

public class DmlDAO {

    private Connection connection;
    private Gson gson = new Gson();

    public DmlDAO(Connection connection){
        this.connection=connection;
    }

    public void save(JsonArray data, String id) throws SQLException {
        Long executionStart = System.currentTimeMillis();
        PrintUtils.printDebug("Saving data of table: "+id);
        String insertQuery = "INSERT INTO "+id+" (CONTENT) VALUES (?)";
        PreparedStatement statement= null;
        try {
            statement = Objects.requireNonNull(this.connection).prepareStatement(insertQuery);
            SQLUtils.fillBatch(statement,data);
            statement.executeBatch();
            this.connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            if(statement!=null){
                statement.close();
            }
        }
        PrintUtils.durationDebug("Finished saving data of table: ",executionStart);
    }
}
