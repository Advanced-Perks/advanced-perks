package de.fabilucius.advancedperks.commons.sql;

import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class SqlConnection {

    private static final Logger LOGGER = Bukkit.getLogger();

    private static final String INSERT_QUERY = "INSERT INTO %s (%s) VALUES (%s)";
    private static final String UPDATE_QUERY = "UPDATE %s SET %s WHERE %s";
    private static final String SELECT_QUERY = "SELECT %s FROM %s";
    private static final String SELECT_WHERE_QUERY = "SELECT %s FROM %s WHERE %s";

    private Connection connection;

    public SqlConnection(String connectionUrl) {
        this(connectionUrl, "", "");
    }

    public SqlConnection(String connectionUrl, String userName, String password) {
        try {
            if (userName.isEmpty() || password.isEmpty() || userName.equalsIgnoreCase("example-username") ||
                    password.equalsIgnoreCase("example-password")) {
                this.connection = DriverManager.getConnection(connectionUrl);
            } else {
                this.connection = DriverManager.getConnection(connectionUrl, userName, password);
            }
            LOGGER.log(Level.INFO, "The plugin has successfully connected to the database.");
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, String.format("An error occurred while the plugin " +
                    "tried to create a connection to the database with the url %s:", connectionUrl), exception);
        }
    }

    public final void insertOrUpdateQuery(String table, List<String> columnsToInsert, List<String> valuesToInsert, String whereLogic, List<String> updateLogic) {
        if (this.valueExistQuery(table, "")) {
            this.updateQuery(table, updateLogic, whereLogic);
        } else {
            this.insertQuery(table, columnsToInsert, valuesToInsert);
        }
    }

    public final boolean valueExistQuery(String table, String whereLogic) {
        ResultSet resultSet = this.selectQuery(Collections.singletonList("*"), table, whereLogic);
        try {
            return resultSet != null && resultSet.next();
        } catch (Exception ignored) {
            return false;
        }
    }

    public final void insertQuery(String table, List<String> columnsToInsert, List<String> valuesToInsert) {
        if (this.isConnected()) {
            String columns = String.join(",", columnsToInsert);
            String values = valuesToInsert.stream().map(value -> "'" + value + "'").collect(Collectors.joining(","));
            String insertQuery = String.format(INSERT_QUERY, table, columns, values);
            try {
                PreparedStatement preparedStatement = this.getConnection().prepareStatement(insertQuery);
                preparedStatement.executeUpdate();
            } catch (Exception exception) {
                LOGGER.log(Level.WARNING, "Couldn't successfully execute a insert query"
                        + System.lineSeparator() + insertQuery + ":", exception);
            }
        }
    }

    public void updateQuery(String table, List<String> updateLogic, String whereLogic) {
        if (this.isConnected()) {
            String update = String.join(",", updateLogic);
            String updateQuery = String.format(UPDATE_QUERY, table, update, whereLogic);
            try {
                PreparedStatement preparedStatement = this.getConnection().prepareStatement(updateQuery);
                preparedStatement.executeUpdate();
            } catch (Exception exception) {
                LOGGER.log(Level.WARNING, "Couldn't execute an update query successfully"
                        + System.lineSeparator() + updateQuery + ":", exception);
            }
        }
    }

    public void customQuery(String query) {
        if (this.isConnected()) {
            try {
                PreparedStatement preparedStatement = this.getConnection().prepareStatement(query);
                preparedStatement.executeUpdate();
            } catch (Exception exception) {
                LOGGER.log(Level.WARNING, "Couldn't execute a custom query successfully"
                        + System.lineSeparator() + query + ":", exception);
            }
        }
    }

    public ResultSet selectQuery(List<String> columnsToSelect, String table, String whereStatement) {
        if (this.isConnected()) {
            String columns = String.join(",", columnsToSelect);

            String selectQuery = "";
            if (whereStatement.isEmpty()) {
                selectQuery = String.format(SELECT_QUERY, columns, table);
            } else {
                selectQuery = String.format(SELECT_WHERE_QUERY, columns, table, whereStatement);
            }

            try {
                PreparedStatement preparedStatement = this.getConnection().prepareStatement(selectQuery);
                return preparedStatement.executeQuery();
            } catch (Exception exception) {
                LOGGER.log(Level.WARNING, "Couldn't successfully execute a select query:", exception);
            }
        }
        return null;
    }

    public boolean isConnected() {
        if (this.connection != null) {
            try {
                return !this.connection.isClosed();
            } catch (Exception exception) {
                LOGGER.log(Level.SEVERE, "There was an error while trying to check the connection to the database:", exception);
            }
        }
        return false;
    }

    public void closeConnection() {
        try {
            this.getConnection().close();
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, "There was an error while trying to close the connection to the database:", exception);
        }
    }

    /* the getter and setter of this class */

    public Connection getConnection() {
        return connection;
    }
}
