package com.pizzadelivery.utils;

import com.pizzadelivery.config.DbConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class DatabaseManager {

    /**
     * Establishes a database connection using the database configuration specified in DbConfig.
     *
     * @return A Connection object representing the established database connection.
     * @throws SQLException If a database access error occurs or the url is null.
     */
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DbConfig.URL, DbConfig.USER, DbConfig.PASSWORD);
    }

    /**
     * Executes a given SQL query and returns the resulting ResultSet.
     * The method prepares the statement to prevent SQL injection and ensures that the
     * query executes safely.
     *
     * @param sqlQuery The SQL query to be executed.
     * @param type     The type of query to be executed. 0 for a simple statement, 1 for a prepared statement.
     * @return A ResultSet object containing the data produced by the given query; never null.
     * @throws SQLException If there is a problem executing the query.
     */
    public static ResultSet sendQuery(String sqlQuery, int type) throws SQLException {
        Connection connection = getConnection();
        try {
            if (type == 0) {
                Statement statement = connection.createStatement();
                return statement.executeQuery(sqlQuery);
            } else {
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery,
                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                return preparedStatement.executeQuery();
            }
        } catch (SQLException e) {
            System.out.println("Problème lors de l'exécution de la requete : " + sqlQuery);
            throw e;
        }
    }

    /**
     * Prints the result set from a database query to the console in a formatted table. Each row of the table
     * is displayed in a separate line, with column values separated by a pipe symbol "|". The method dynamically adjusts
     * the width of each column based on the largest content width in that column, ensuring that the table is neatly aligned.
     * A line of dashes "-" is used to separate the column headers from the data rows.
     *
     * @param resultSet The ResultSet object containing the data to be printed.
     * @throws SQLException If an SQL error occurs while processing the ResultSet.
     */
    public static void printFormattedResults(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Determine the width of each column based on the column header name lengths
        int[] columnWidths = new int[columnCount];
        for (int i = 0; i < columnCount; i++) {
            columnWidths[i] = metaData.getColumnName(i + 1).length();
        }

        // Adjust the column widths based on the width of the data in each column
        while (resultSet.next()) {
            for (int i = 1; i <= columnCount; i++) {
                int dataLength = 0;
                if (resultSet.getString(i) == null) {
                    dataLength = 4; // "null" is 4 characters long
                } else {
                    dataLength = resultSet.getString(i).length();
                }
                if (dataLength > columnWidths[i - 1]) {
                    columnWidths[i - 1] = dataLength; // Update column width if data is wider
                }
            }
        }

        // Move the cursor to the front of the ResultSet before printing
        resultSet.beforeFirst();

        // Print the column headers
        for (int i = 1; i <= columnCount; i++) {
            System.out.printf("%-" + columnWidths[i - 1] + "s | ", metaData.getColumnName(i));
        }
        System.out.println();

        // Print the separation line that matches the header width
        for (int i = 0; i < columnCount; i++) {
            for (int j = 0; j < columnWidths[i]; j++) {
                System.out.print("-");
            }
            // Add an extra dash for the separator and check if it's not the last column
            System.out.print(i < columnCount - 1 ? "-|-" : "-|");
        }
        System.out.println();

        // Print the data rows
        while (resultSet.next()) {
            for (int i = 1; i <= columnCount; i++) {
                System.out.printf("%-" + columnWidths[i - 1] + "s | ", resultSet.getString(i));
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        try {
            printFormattedResults(sendQuery("SELECT * FROM VIEW_ORDER_SUMMARY", 1));
        } catch (SQLException e) {
            Logger logger = LoggerFactory.getLogger(DatabaseManager.class);
            logger.error("SQL Exception occurred", e);
        }
    }
}
