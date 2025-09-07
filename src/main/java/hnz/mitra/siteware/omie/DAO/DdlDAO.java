package hnz.mitra.siteware.omie.DAO;


import com.google.gson.Gson;
import hnz.mitra.siteware.omie.utils.PrintUtils;
import org.postgresql.util.PSQLException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Objects;

public class DdlDAO {

    private Connection connection;
    private Gson gson = new Gson();

    public DdlDAO(Connection connection){
        this.connection=connection;
    }

    public Boolean truncateIfExists(String id) throws SQLException {
        Long startExecution = System.currentTimeMillis();
        PrintUtils.printDebug("Trying to truncate table "+id+" if exists");
        MessageFormat mf = new MessageFormat("TRUNCATE TABLE {0}");
        PreparedStatement truncateStatement = null;
        try {
            truncateStatement = connection.prepareStatement(mf.format(new Object[]{id}));
            truncateStatement.executeUpdate();
            connection.commit();
        }catch(SQLException e){
            if(e.getMessage().contains("doesn't exist")){
                PrintUtils.printDebug("Table "+id+" does not exists");
                this.connection.rollback();
                return Boolean.FALSE;
            }
            e.printStackTrace();
        }finally {
            if(Boolean.TRUE.equals(truncateStatement!=null)){
                truncateStatement.close();
            }
        }
        PrintUtils.durationDebug("Finished table "+id+" truncation",startExecution);
        return Boolean.TRUE;
    }

    public void createTableUsingColumnar(String id) throws SQLException {
        Long executionStart = System.currentTimeMillis();
        PrintUtils.printDebug("Creating table: "+id);
        MessageFormat messageFormat = new MessageFormat("CREATE TABLE {0} (SEQ BIGINT AUTO_INCREMENT, CONTENT JSON, PRIMARY KEY (SEQ))");
        PreparedStatement statement= null;
        try {
            statement = Objects.requireNonNull(this.connection).prepareStatement(messageFormat.format(new Object[]{id}));
            PrintUtils.printDebug("Query for creating table: "+statement.toString());
            statement.execute();
            this.connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            if(statement!=null){
                statement.close();
            }
        }
        PrintUtils.durationDebug("Finished creating table",executionStart);
    }
}
