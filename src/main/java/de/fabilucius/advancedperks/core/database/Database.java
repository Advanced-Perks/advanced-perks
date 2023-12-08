package de.fabilucius.advancedperks.core.database;

import de.fabilucius.advancedperks.data.PerkData;

import java.sql.PreparedStatement;

public interface Database {

    void connectToDatabase();

    boolean isConnected();

    PreparedStatement createPreparedStatement(String query);

    boolean runSqlScript(String scriptPath);

    void savePerkData(PerkData perkData);

    boolean runPerkDataMigrateScript();
}
