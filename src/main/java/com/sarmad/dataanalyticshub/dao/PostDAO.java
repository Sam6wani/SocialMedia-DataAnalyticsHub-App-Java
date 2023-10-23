package com.sarmad.dataanalyticshub.dao;

import com.sarmad.dataanalyticshub.models.Post;
import javafx.scene.control.Alert;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.sarmad.dataanalyticshub.utils.AlertUtil.showAlert;

public class PostDAO {

    public static void insertPost(Post post) throws Exception {
        String updateStmt = "INSERT INTO POSTS \n" +
                "(id, content, author, likes, shares)\n" +
                "VALUES\n" +
                "('"+ post.getId() +"', '"+ post.getContent() +"', '"+ post.getAuthor() +"', '"+ post.getLikes()+"', '"+ post.getShares() +"')";

        Connection conn = Database.connect();
        Statement statement = null;
        try {
            statement = conn.createStatement();
            statement.executeUpdate(updateStmt);

            statement.close();
            conn.close();

            System.out.println("Post created persisted the database.");
        } catch (SQLException e) {
            showAlert(
                    Alert.AlertType.ERROR,
                    "Post creation error",
                    "Unable to create post",
                    e.getMessage());
            try{
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException e2) {
//                e2.printStackTrace();
            }
        }
    }

    public static List<Post> searchPostById(String id) {
        Connection conn = Database.connect();
        String selectStatement = "SELECT * FROM posts WHERE id = ?";

        try {
            PreparedStatement statement = conn.prepareStatement(selectStatement);
            statement.setString(1, id);

            ResultSet resultSet = statement.executeQuery();

            return getPostsFromResultSet(resultSet);

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

    private static List<Post> getPostsFromResultSet(ResultSet resultSet) {

        List<Post> posts = new ArrayList<>();
        while (true) {
            try {
                if (!resultSet.next()) break;
                Post post = new Post();
                post.setId(Integer.parseInt(resultSet.getString("id")));
                post.setAuthor(resultSet.getString("author"));
                post.setContent(resultSet.getString("content"));
                post.setLikes(resultSet.getInt("likes"));
                post.setShares(resultSet.getInt("shares"));

                posts.add(post);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return posts;
    }

    public static List<Post> searchTopPosts(String limit, String searchByCol) {
        Connection conn = Database.connect();

        String selectStatement = "";
        if(searchByCol.equals("likes")) {
            selectStatement = "SELECT * FROM posts ORDER BY likes DESC LIMIT ?";
        } else {
            selectStatement = "SELECT * FROM posts ORDER BY shares DESC LIMIT ?";
        }

        try {
            PreparedStatement statement = conn.prepareStatement(selectStatement);
            statement.setString(1, limit);
            System.out.println(statement.toString());
            ResultSet topResultSet = statement.executeQuery();

            return getPostsFromResultSet(topResultSet);

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

    public static List<Post> getAllPosts(String author) {
        Connection conn = Database.connect();
        String selectStatement = "SELECT * FROM posts WHERE author = ?";

        try {
            PreparedStatement statement = conn.prepareStatement(selectStatement);
            statement.setString(1, author);

            ResultSet resultSet = statement.executeQuery();

            return getPostsFromResultSet(resultSet);

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

    public static boolean deletePostById(String id) {
        Connection conn = Database.connect();
        String selectStatement = "DELETE FROM posts WHERE id = ?";

        try {
            PreparedStatement statement = conn.prepareStatement(selectStatement);
            statement.setString(1, id);

            Integer rowsEffected = statement.executeUpdate();

            if (rowsEffected > 0 ) {
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
