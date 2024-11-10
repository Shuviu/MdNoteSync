package org.example;

import java.io.*;
import java.sql.*;
import org.example.Dataclasses.File;

public class DBManager {

    private static Connection dbConnection; // Database Connection
    private static Statement dbStatement; // Statement to execute sql query via the connection
    private static ResultSet dbResult; // Result of the executed sql query
    private static String dbTable = "table"; // Name of the database table to query
    final static String url = "database"; // URL to the database
    final static String driver = "com.mysql.cj.jdbc.Driver"; // Drivers for the MySQL connection
    final static String dbUser = "user"; // Database User
    final static String dbPassword = "secure_password"; // Database Password

    public static boolean establishConnection(int databaseType) {
        // Connection to a MariaDB Database
        if (databaseType == 0) {
            try {
                // Connection init
                dbConnection = DriverManager.getConnection(
                        url,
                        dbUser, dbPassword
                );
                // Statement init
                dbStatement = dbConnection.createStatement();
                return true;

            } catch (Exception e) {
                Logger.logException(e, "Connecting to Database: ");
                return false;
            }
        }
        // Connection to a MySQL Database
        else if (databaseType == 1) {
            try {
                // Driver init
                Class.forName(driver);
                // Connection init
                dbConnection = DriverManager.getConnection(
                        url,
                        dbUser, dbPassword
                );
                // Statement init
                dbStatement = dbConnection.createStatement();
                return true;
            }
            catch (Exception e) {
                Logger.logException(e, "Connecting to Database: ");
                return false;
            }
        }
        return false;
    }

    public static void closeConnection() {
        try {
            dbConnection.close();
            dbStatement.close();
            if (dbResult != null) {
                dbResult.close();
            }
        }
        catch (Exception e) {
            Logger.logException(e, "Closing database connection: ");
        }
    }

    @SuppressWarnings("DuplicatedCode")
    public static boolean insertEntry(File file){
        try {
            dbResult =
                    dbStatement.executeQuery("Select iId from " + dbTable
                            + " Order by iId DESC LIMIT 0,1");
            dbResult.next();
            int id = dbResult.getInt("iId") + 1;

            dbStatement.executeUpdate(
                    "INSERT INTO " + dbTable + " VALUES (" +
                            "" + id + ", '" + file.location + "', '" + file.name + "', '" + file.content + "')");

            return true;

        } catch (SQLException e) {
            Logger.logException(e, "Inserting entry: ");
            return false;
        }
    }

    public static boolean removeEntry(File file){
        try {
            dbStatement.execute(
                    "DELETE FROM " + dbTable + " WHERE sName = '" + file.name + "' && sWO = " + " '" + file.location + "';" );
            return true;
        }
        catch (Exception e) {
            Logger.logException(e, "removing entry: ");
            return false;
        }
    }
}
