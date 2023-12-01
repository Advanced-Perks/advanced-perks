package de.fabilucius.advancedperks.core.database;

import java.sql.PreparedStatement;

public interface Database {

    void connectToDatabase();

    boolean isConnected();

    PreparedStatement createPreparedStatement(String query);

    void runSqlScript(String scriptPath);

}
