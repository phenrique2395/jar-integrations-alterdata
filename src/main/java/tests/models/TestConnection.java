package tests.models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestConnection {
    private String path="localhost";
    private Integer port=3336;
    private String driverName="mysql";
    private String schema="teste_db";
    private String username="root";
    private String password="ttCHANGE123";
//    private String driverClass="org.mysql.Driver";
//    private String driverClass="com.mysql.cj.jdbc.Driver";
    private Connection connection;

    public TestConnection(){
        try {
            connection = DriverManager.getConnection(generateFullUrl(),this.username,this.password);
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


//    URL	jdbc:postgresql://localhost:3308/agrinvestProtheus

    private String generateFullUrl(){
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append("jdbc:");
//        urlBuilder.append(this.driverName.toLowerCase()).append("://").append(this.path).append(":").append(this.port).append("/").append(this.schema).append("?useTimezone=true&serverTimezone=GMT-03&characterEncoding=utf8");
        urlBuilder.append(this.driverName.toLowerCase()).append("://").append(this.path).append(":").append(this.port).append("/").append(this.schema);
        return urlBuilder.toString();
    }

    public Connection getConnection() {
        return connection;
    }
}
