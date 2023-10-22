package com.sarmad.dataanalyticshub.dao;

import com.sarmad.dataanalyticshub.DataAnalyticsHubApplication;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {

    public static boolean isOk() {
        if (!checkDrivers()) return false;

        if (!checkConnection()) return false;

        createTables();

        return true;
    }


    private static boolean checkDrivers() {
        try {
            Class.forName("org.sqlite.JDBC");
            DriverManager.registerDriver(new org.sqlite.JDBC());
            return true;
        } catch (ClassNotFoundException | SQLException classNotFoundException) {
            Logger.getAnonymousLogger().log(Level.SEVERE, LocalDateTime.now() + ": Could not start SQLite Drivers");
            return false;
        }
    }

    private static boolean checkConnection() {
        try (Connection connection = connect()) {
            return connection != null;
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, LocalDateTime.now() + ": Could not connect to database");
            return false;
        }
    }

    private static void createTables() {
        Statement statement = null;
        try {
            Connection conn = connect();
            conn.setAutoCommit(false);
            statement = conn.createStatement();
            String createUserTableSQL = "CREATE TABLE IF NOT EXISTS users ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "username TEXT NOT NULL UNIQUE,"
                    + "password TEXT,"
                    + "first_name TEXT,"
                    + "last_name TEXT,"
                    + "created_at DATETIME DEFAULT CURRENT_TIMESTAMP,"
                    + "updated_at DATETIME DEFAULT CURRENT_TIMESTAMP)";

            String createPostsTableSQL = "CREATE TABLE IF NOT EXISTS posts ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "content TEXT, "
                    + "author TEXT , "
                    + "likes INTEGER, "
                    + "shares INTEGER, "
                    + "created_at DATETIME DEFAULT CURRENT_TIMESTAMP, "
                    + "updated_at DATETIME DEFAULT CURRENT_TIMESTAMP, "
                    + "FOREIGN KEY (author) REFERENCES USERS (id))";

            statement.executeUpdate(createUserTableSQL);
            statement.executeUpdate(createPostsTableSQL);

            String getTables = "SELECT * FROM sqlite_master where type='table'";
            ResultSet resutl = statement.executeQuery(getTables);

            while (resutl.next()) {
                String name = resutl.getString("name");
                System.out.println("Table name:" + name);
            }

            conn.commit();
            statement.close();
            conn.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    protected static Connection connect() {
        String dbPrefix = "jdbc:sqlite:";
        try {
            var location = DataAnalyticsHubApplication.class.getResource("database.db").getFile();
            Connection connection = DriverManager.getConnection(dbPrefix + location);
            return connection;
        } catch (SQLException exception) {
            Logger.getAnonymousLogger().log(Level.SEVERE,
                    LocalDateTime.now() + ": Could not connect to SQLite DB at ");
            return null;
        }
    }

}
