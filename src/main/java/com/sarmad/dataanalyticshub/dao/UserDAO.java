package com.sarmad.dataanalyticshub.dao;

import com.sarmad.dataanalyticshub.models.User;

import java.sql.*;

public class UserDAO {
    private static final String firstNameColumn = "first_name";
    private static final String lastNameColumn = "last_name";


    public static void insertUser(String username, String password, String fName, String lName) throws Exception {
        String updateStmt = "INSERT INTO USERS \n" +
                "(username, password, first_name, last_name)\n" +
                "VALUES\n" +
                "('"+username+"', '" + password + "', '"+fName+"', '"+lName+"')";

        Connection conn = Database.connect();
        Statement statement = null;
        try {
            statement = conn.createStatement();
            statement.executeUpdate(updateStmt);

            statement.close();
            conn.close();

            System.out.println("User created to the database.");
        } catch (SQLException e) {
            e.printStackTrace();
            try{
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }
    }

    public static User searchByUsernameAndPassword(String username, String password) {
        Connection conn = Database.connect();
        String selectStatement = "SELECT * FROM users WHERE username = ? AND password = ?";

        try {
            PreparedStatement statement = conn.prepareStatement(selectStatement);
            statement.setString(1, username);
            statement.setString(2, password);

            System.out.println(statement.toString());
            ResultSet resultSet = statement.executeQuery();

            return getUserFromResultSet(resultSet);

        } catch (SQLException e) {
            return null;
        } finally {
            try{
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }
    }

    private static User runDBStatementForUser(String selectStatement) {
        Connection conn = Database.connect();
        Statement statement = null;

        try {
            statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(selectStatement);

            return getUserFromResultSet(resultSet);

        }  catch (SQLException e) {
            return null;
        } finally {
            try{
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }
    }

    public static User searchById(Integer id) {
        String selectStatement = "SELECT * FROM users WHERE id=" + id;

        return runDBStatementForUser(selectStatement);
    }

    private static User getUserFromResultSet(ResultSet resultSet) throws SQLException {
        User user = null;
        if (resultSet.next()) {
            user = new User();
            user.setId(resultSet.getString("id"));
            user.setUsername(resultSet.getString("username"));
            user.setPassword(resultSet.getString("password"));
            user.setFirstName(resultSet.getString("first_name"));
            user.setLastName(resultSet.getString("last_name"));
        }

        return user;
    }

    public static boolean updatePost(User updatedUser) {
        System.out.println("UPDATE USER");
        System.out.println("ID: " + updatedUser.getId());
        System.out.println("USERNAME: " + updatedUser.getUsername());
        System.out.println("FIRST NAME: " + updatedUser.getFirstName());
        System.out.println("LAST NAME: " + updatedUser.getLastName());
        System.out.println("PASSWORD: " + updatedUser.getPassword());

        Connection conn = Database.connect();
        String selectStatement = "UPDATE users SET username=?, password=?, first_name=?, last_name=?";

        try {
            PreparedStatement statement = conn.prepareStatement(selectStatement);
            statement.setString(1, updatedUser.getUsername());
            statement.setString(2, updatedUser.getPassword());
            statement.setString(3, updatedUser.getFirstName());
            statement.setString(4, updatedUser.getLastName());

            System.out.println(statement.toString());
            Integer rowsEffected = statement.executeUpdate();

            if (rowsEffected > 0) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            return false;
        } finally {
            try{
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }
    }
}
