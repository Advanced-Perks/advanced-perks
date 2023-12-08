package de.fabilucius.advancedperks.core.database;

import de.fabilucius.advancedperks.core.configuration.Configuration;
import de.fabilucius.advancedperks.core.configuration.annotation.FilePathInJar;

import java.util.Arrays;

@FilePathInJar("database.yml")
public class DatabaseConfiguration extends Configuration {

    public String getFileLocation() {
        return this.getString("file.file_location", "data.db");
    }

    public SaveType getSaveType() {
        return Arrays.stream(SaveType.values())
                .filter(saveType -> saveType.name().equalsIgnoreCase(this.getString("save_type")))
                .findFirst()
                .orElse(SaveType.UNKNOWN);
    }

    public String getSqlUrl() {
        return this.getString("database.sql_url");
    }

    public Credentials getCredentials() {
        return new Credentials(
                this.getString("database.credentials.username"),
                this.getString("database.credentials.password")
        );
    }

}
