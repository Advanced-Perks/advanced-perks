package de.fabilucius.advancedperks.core.database;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import de.fabilucius.advancedperks.core.logging.APLogger;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

public abstract class AbstractDatabase implements Database {

    @Inject
    private APLogger logger;

    private Connection connection;
    private final String connectionUrl;
    private final Credentials credentials;

    protected AbstractDatabase(String connectionUrl, Credentials credentials) {
        this.connectionUrl = connectionUrl;
        this.credentials = credentials;
    }

    @Override
    public void connectToDatabase() {
        try {
            if (credentials.isAuthEmpty()) {
                this.connection = DriverManager.getConnection(connectionUrl);
            } else {
                this.connection = DriverManager.getConnection(connectionUrl, credentials.getUserName(), credentials.getPassword());
            }
            this.logger.info("The plugin successfully connected to the database.");
        } catch (Exception sqlException) {
            this.logger.log(Level.SEVERE, "There was error while connecting to a database:", sqlException);
            throw new IllegalStateException("Cannot initialize an instance of "
                    + this.getClass().getSimpleName() + ", the connection to the database failed.");
        }
    }

    @Override
    @SuppressWarnings("UnstableApi")
    public void runSqlScript(String scriptPath) {
        try {
            String scriptContent = IOUtils.toString(Objects.requireNonNull(AbstractDatabase.class.getClassLoader().getResourceAsStream(scriptPath)), StandardCharsets.UTF_8);
            List<String> sqlQueries = Arrays.stream(scriptContent.split(";"))
                    .map(String::trim)
                    .filter(string -> !Strings.isNullOrEmpty(string.trim())).toList();
            for (String sqlQuery : sqlQueries) {
                try (PreparedStatement preparedStatement = this.createPreparedStatement(sqlQuery)) {
                    preparedStatement.execute();
                } catch (SQLException e) {
                    this.logger.log(Level.SEVERE, "An unexpected SQLException was thrown while executing the sql script %s.".formatted(scriptPath), e);
                }
            }
        } catch (IOException e) {
            this.logger.log(Level.SEVERE, "An unexpected IOException was thrown while executing the sql script %s.".formatted(scriptPath), e);
        }
    }

    @Override
    public final boolean isConnected() {
        if (this.getConnection() != null) {
            try {
                return !this.getConnection().isClosed();
            } catch (SQLException sqlException) {
                this.logger.log(Level.SEVERE, "There was an error while trying to check the connection to the database:", sqlException);
            }
        }
        return false;
    }

    @Override
    public PreparedStatement createPreparedStatement(String query) {
        try {
            return this.connection.prepareStatement(query);
        } catch (SQLException e) {
            this.logger.log(Level.INFO, "Error while creating a prepared statement with query %s.".formatted(query), e);
            return null;
        }
    }

    /* the getter and setter of the class */

    private String getConnectionUrl() {
        return connectionUrl;
    }

    private Credentials getCredentials() {
        return credentials;
    }

    public Connection getConnection() {
        return connection;
    }
}
