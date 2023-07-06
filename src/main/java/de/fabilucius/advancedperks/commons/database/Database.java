package de.fabilucius.advancedperks.commons.database;

import java.sql.ResultSet;
import java.util.List;

public interface Database {

    boolean isConnected();

    void closeConnection();

    void customUpdate(String query);

    ResultSet customQuery(String query);

    void updateQuery(String table, String setLogic, String whereLogic);

    void insertQuery(String table, List<String> columnsToInsert, List<String> valuesToInsert);

    ResultSet selectQuery(String table, List<String> columnsToSelect, String whereLogic);

    boolean valueExistQuery(String table, String whereLogic);

    void insertOrUpdateQuery(String table, List<String> columnsToInsert, List<String> valuesToInsert, String whereLogic, String updateLogic);

    void deleteQuery(String table, String whereLogic);

}
