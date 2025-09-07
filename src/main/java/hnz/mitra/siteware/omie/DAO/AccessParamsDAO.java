package hnz.mitra.siteware.omie.DAO;

import com.google.gson.Gson;
import hnz.mitra.siteware.omie.models.AccessParams;
import hnz.mitra.siteware.omie.utils.PrintUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class AccessParamsDAO {

    private Connection connection;
    private Gson gson = new Gson();

    public AccessParamsDAO(Connection connection){
        this.connection = connection;
    }

    public AccessParams get() throws SQLException {
        Long executionStart = System.currentTimeMillis();
        PrintUtils.printDebug("Retrieving API Access Params from DataBase.");
        AccessParams accessParams = new AccessParams();
        PreparedStatement statement= null;
        ResultSet rs = null;
        try {
            statement = Objects.requireNonNull(this.connection).prepareStatement("SELECT * FROM ACCESS_PARAMS");
            PrintUtils.printDebug("Query for retrieving API Access Params from DataBase: "+statement.toString());
            rs = statement.executeQuery();
            rs.next();
            accessParams.setToken(rs.getString("TOKEN"));
            accessParams.setUrl(rs.getString("URL"));
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            if(rs!=null){
                rs.close();
            }
            if(statement!=null){
                statement.close();
            }
        }
        PrintUtils.durationDebug("Finished retrieving API Access Params from DataBase with success.",executionStart);
        return accessParams;
    }
}
