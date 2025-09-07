package hnz.mitra.siteware.omie.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.cj.MysqlType;
import org.postgresql.util.PGobject;


public class SQLUtils {

        public static void fillBatch(PreparedStatement statement, JsonArray data) throws SQLException {
            for (JsonElement rowJson : data) {
                JsonObject row = rowJson.getAsJsonObject();
                String jsonString = row.toString();
                statement.setObject(1, jsonString, MysqlType.JSON);
                statement.addBatch();
            }
        }

    public static String buildSimpleInsertQuery(String tableName, ArrayList<String> fields){
        StringBuilder insertQuerySB = new StringBuilder("INSERT INTO ");
        insertQuerySB.append(tableName);
        insertQuerySB.append(" (");
        fields.forEach(field->insertQuerySB.append(field.concat(",")));
        insertQuerySB.replace(insertQuerySB.length()-1,insertQuerySB.length(),"");
        insertQuerySB.append(") VALUES(");
        fields.forEach(field->insertQuerySB.append("?,"));
        insertQuerySB.replace(insertQuerySB.length()-1,insertQuerySB.length(),")");
        return insertQuerySB.toString();
    }
}
