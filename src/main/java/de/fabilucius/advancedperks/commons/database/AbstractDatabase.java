package de.fabilucius.advancedperks.commons.database;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import de.fabilucius.advancedperks.commons.database.details.Credentials;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class AbstractDatabase implements Database {

    private static final Logger LOGGER = Bukkit.getLogger();

    private Connection connection;
    private final String connectionUrl;
    private final Credentials credentials;

    protected AbstractDatabase(String connectionUrl, Credentials credentials) {
        Preconditions.checkNotNull(connectionUrl, "connectionUrl cannot be null");
        Preconditions.checkNotNull(credentials, "credentials cannot be null");
        this.connectionUrl = connectionUrl;
        this.credentials = credentials;
        this.connectToDatabase();
    }

    private void connectToDatabase() {
        try {
            if (credentials.isAuthEmpty()) {
                this.connection = DriverManager.getConnection(connectionUrl);
            } else {
                this.connection = DriverManager.getConnection(connectionUrl, credentials.getUserName(), credentials.getPassword());
            }
        } catch (SQLException sqlException) {
            LOGGER.log(Level.SEVERE, "There was error while connecting to a database:", sqlException);
            throw new IllegalStateException("Cannot initialize an instance of "
                    + this.getClass().getSimpleName() + ", the connection to the database failed.");
        }
    }

    @Override
    public void customUpdate(String query) {
        Preconditions.checkState(!Strings.isNullOrEmpty(query), "query cannot be empty or null");
        this.ensureConnectionIntegrity();
        try {
            PreparedStatement preparedStatement = this.getConnection().prepareStatement(query);
            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            LOGGER.log(Level.WARNING, "Couldn't execute the following custom update:"
                    + System.lineSeparator() + query, sqlException);
        }
    }

    @Override
    public final ResultSet customQuery(String query) {
        Preconditions.checkState(!Strings.isNullOrEmpty(query), "query cannot be empty or null");
        this.ensureConnectionIntegrity();
        try {
            PreparedStatement preparedStatement = this.getConnection().prepareStatement(query);
            return preparedStatement.executeQuery();
        } catch (SQLException sqlException) {
            LOGGER.log(Level.WARNING, "Couldn't execute the following custom query:"
                    + System.lineSeparator() + query, sqlException);
        }
        return null;
    }

    @Override
    public final void updateQuery(String table, String setLogic, String whereLogic) {
        Preconditions.checkState(!Strings.isNullOrEmpty(table), "table cannot be null or empty");
        Preconditions.checkState(!Strings.isNullOrEmpty(setLogic), "setLogic cannot be null or empty");
        Preconditions.checkState(!Strings.isNullOrEmpty(whereLogic), "whereLogic cannot be null or empty");
        Preconditions.checkState(this.isConnected(), "database doesn't seem to be connected");
        this.ensureConnectionIntegrity();
        String updateQuery = "UPDATE " + table + " SET " + setLogic + " WHERE " + whereLogic;
        try {
            PreparedStatement preparedStatement = this.getConnection().prepareStatement(updateQuery);
            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            LOGGER.log(Level.WARNING, "Couldn't execute the following update query:"
                    + System.lineSeparator() + updateQuery, sqlException);
        }
    }

    @Override
    public final void insertQuery(String table, List<String> columnsToInsert, List<String> valuesToInsert) {
        Preconditions.checkState(!Strings.isNullOrEmpty(table), "table cannot be null or empty");
        Preconditions.checkNotNull(columnsToInsert, "columnsToInsert cannot be null");
        Preconditions.checkNotNull(valuesToInsert, "valuesToInsert cannot be null");
        Preconditions.checkState(this.isConnected(), "database doesn't seem to be connected");
        this.ensureConnectionIntegrity();
        String columns = String.join(",", columnsToInsert);
        String values = valuesToInsert.stream().map(value -> "'" + value + "'").collect(Collectors.joining(","));
        String insertQuery = "INSERT INTO " + table + " (" + columns + ") VALUES (" + values + ")";
        try {
            PreparedStatement preparedStatement = this.getConnection().prepareStatement(insertQuery);
            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            LOGGER.log(Level.WARNING, "Couldn't execute the following insert query:"
                    + System.lineSeparator() + insertQuery, sqlException);
        }
    }

    @Override
    public final ResultSet selectQuery(String table, List<String> columnsToSelect, String whereLogic) {
        Preconditions.checkState(!Strings.isNullOrEmpty(table), "table cannot be null or empty");
        Preconditions.checkNotNull(columnsToSelect, "columnsToSelect cannot be null");
        Preconditions.checkNotNull(whereLogic, "whereLogic cannot be null");
        this.ensureConnectionIntegrity();
        String columns = String.join(",", columnsToSelect);
        String selectQuery = "SELECT " + columns + " FROM " + table + (whereLogic.isEmpty() ? "" : " WHERE " + whereLogic);
        try {
            PreparedStatement preparedStatement = this.getConnection().prepareStatement(selectQuery);
            return preparedStatement.executeQuery();
        } catch (SQLException sqlException) {
            LOGGER.log(Level.WARNING, "Couldn't execute the following select query:"
                    + System.lineSeparator() + selectQuery, sqlException);
        }
        return null;
    }

    @Override
    public final boolean isConnected() {
        if (this.getConnection() != null) {
            try {
                return !this.getConnection().isClosed();
            } catch (SQLException sqlException) {
                LOGGER.log(Level.SEVERE, "There was an error while trying to check the connection to the database:", sqlException);
            }
        }
        return false;
    }

    @Override
    public final boolean valueExistQuery(String table, String whereLogic) {
        Preconditions.checkState(!Strings.isNullOrEmpty(table), "table cannot be null or empty");
        Preconditions.checkNotNull(whereLogic, "whereLogic cannot be null");
        this.ensureConnectionIntegrity();
        ResultSet resultSet = this.selectQuery(table, Lists.newArrayList("*"), whereLogic);
        try {
            return resultSet != null && resultSet.next();
        } catch (SQLException ignored) {
            return false;
        }
    }

    @Override
    public final void insertOrUpdateQuery(String table, List<String> columnsToInsert, List<String> valuesToInsert, String whereLogic, String updateLogic) {
        this.ensureConnectionIntegrity();
        if (this.valueExistQuery(table, whereLogic)) {
            this.updateQuery(table, updateLogic, whereLogic);
        } else {
            this.insertQuery(table, columnsToInsert, valuesToInsert);
        }
    }

    @Override
    public void deleteQuery(String table, String whereLogic) {
        Preconditions.checkState(!Strings.isNullOrEmpty(table), "table cannot be null or empty");
        Preconditions.checkNotNull(whereLogic, "whereLogic cannot be null");
        this.ensureConnectionIntegrity();
        String deleteQuery = "DELETE FROM " + table + " WHERE " + whereLogic;
        try {
            PreparedStatement preparedStatement = this.getConnection().prepareStatement(deleteQuery);
            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            LOGGER.log(Level.WARNING, "Couldn't execute the following delete query:"
                    + System.lineSeparator() + deleteQuery, sqlException);
        }
    }

    @Override
    public void closeConnection() {
        try {
            this.getConnection().close();
        } catch (SQLException sqlException) {
            LOGGER.log(Level.SEVERE, "There was an error while closing the connection to the database:", sqlException);
        }
    }

    protected final void ensureConnectionIntegrity() {
        if (!this.isConnected()) {
            LOGGER.log(Level.INFO, "The connection to the database was interrupted, trying to reestablish connection...");
            this.connectToDatabase();
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
