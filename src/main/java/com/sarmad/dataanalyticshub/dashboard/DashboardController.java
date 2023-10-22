package com.sarmad.dataanalyticshub.dashboard;

import com.sarmad.dataanalyticshub.DataAnalyticsHubApplication;
import com.sarmad.dataanalyticshub.DataAnalyticsHubController;
import com.sarmad.dataanalyticshub.dao.PostDAO;
import com.sarmad.dataanalyticshub.dao.UserDAO;
import com.sarmad.dataanalyticshub.models.Post;
import com.sarmad.dataanalyticshub.models.User;
import com.sarmad.dataanalyticshub.pieChart.PieChartController;
import com.sarmad.dataanalyticshub.utils.AlertUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

public class DashboardController implements Initializable {

    @FXML
    public TableColumn idColumn;

    @FXML
    public TableColumn authorColumn;

    @FXML
    public TableColumn contentColumn;

    @FXML
    public TableColumn likesColumn;

    @FXML
    public TableColumn sharesColumn;

    @FXML
    public TableView postTableView;

    @FXML
    public TextField searchTopPostsText;
    @FXML
    public RadioButton searchByLikesCol;

    @FXML
    public RadioButton searchBySharesCol;

    @FXML
    public ToggleGroup searchByColGroup;

    @FXML
    public TextField deleteByIdText;

    @FXML
    public Button deletePostByIdButton;

    @FXML
    private Label welcomeLabel;

    @FXML
    private TextField searchByIdText;

    private User user;

    @FXML
    public void setLoggedInUser(String username) {
        welcomeLabel.setText("Welcome " + username);
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idColumn.setCellValueFactory(new PropertyValueFactory("id"));
        authorColumn.setCellValueFactory(new PropertyValueFactory("author"));
        contentColumn.setCellValueFactory(new PropertyValueFactory("content"));
        likesColumn.setCellValueFactory(new PropertyValueFactory("likes"));
        sharesColumn.setCellValueFactory(new PropertyValueFactory("shares"));
    }

    public void newPostButton(ActionEvent actionEvent) {
        System.out.println("Create new post " + user.getFirstName());
        showNewPostDialog(new Post());
        actionEvent.consume();
    }

    public void showNewPostDialog(Post post) {
        Dialog<Post> dialog = new Dialog<>();
        dialog.setTitle("New Post");
        dialog.setHeaderText("Please enter your post details.");

        ButtonType createButtonType = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        TextField id = new TextField();
        id.setPromptText("Post ID");

        TextField author = new TextField();
        author.setText(user.getFirstName());
        author.setDisable(true);

        TextArea content = new TextArea();
        content.setPromptText("Write your post here...");

        TextField likes = new TextField();
        likes.setPromptText("Number of Likes");

        TextField shares = new TextField();
        shares.setPromptText("Post ID");

        gridPane.add(new Label("ID:"), 0, 0);
        gridPane.add(id, 1, 0);
        gridPane.add(new Label("Author:"), 0, 1);
        gridPane.add(author, 1, 1);
        gridPane.add(new Label("Post:"), 0, 2);
        gridPane.add(content, 1, 2);
        gridPane.add(new Label("Likes:"), 0, 3);
        gridPane.add(likes, 1, 3);
        gridPane.add(new Label("Shares:"), 0, 4);
        gridPane.add(shares, 1, 4);

        dialog.getDialogPane().setContent(gridPane);

        Platform.runLater(() -> id.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == createButtonType) {
                return new Post(
                        parseInt(id.getText()),
                        content.getText(),
                        author.getText(),
                        parseInt(likes.getText()),
                        parseInt(shares.getText())
                );
            }
            return null;
        });

        if (post != null) {
            content.setText(post.getContent());
        }

        Optional<Post> result = dialog.showAndWait();

        result.ifPresent(newPost -> {
            System.out.println("id= " + newPost.getId() +
                    ", post=" + newPost.getContent() +
                    ", author= " + newPost.getAuthor() +
                    ", likes= " + newPost.getLikes() +
                    ", shares= " + newPost.getShares()
            );
            try {
                PostDAO.insertPost(newPost);

                System.out.println("Post created");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void searchPostById(ActionEvent actionEvent) {
        System.out.println("Search Post by ID: " + searchByIdText.getText());
        List<Post> posts = PostDAO.searchPostById(searchByIdText.getText());
        postTableView.getItems().clear();
        postTableView.getItems().addAll(posts);

        actionEvent.consume();
    }

    public void searchTopPosts(ActionEvent actionEvent) {
        System.out.println("Search Top Post: " + searchTopPostsText.getText());
        System.out.println("Toggle value: " + searchByColGroup.getSelectedToggle().getUserData());
        List<Post> posts = PostDAO.searchTopPosts(searchTopPostsText.getText(), (String) searchByColGroup.getSelectedToggle().getUserData());
        postTableView.getItems().clear();
        postTableView.getItems().addAll(posts);

        actionEvent.consume();
    }

    public void deletePostById(ActionEvent actionEvent) {
        System.out.println("Delete Post by ID: " + deleteByIdText.getText());
        boolean deleted = PostDAO.deletePostById(deleteByIdText.getText());
        postTableView.getItems().clear();
        System.out.println("Post deleted: " + deleted);

        actionEvent.consume();
    }

    public void logoutButton(MouseEvent mouseEvent) {
        System.out.println("logout");
        FXMLLoader loader = new FXMLLoader(DataAnalyticsHubApplication.class.getResource("main-view.fxml"));
        try {
            Parent parent = loader.load();
            Label label = (Label)mouseEvent.getSource();

            Stage stage = (Stage)label.getScene().getWindow();
            Pane pane = (Pane) label.getScene().lookup("#main");

            stage.setTitle("Data Analytics Hub");

            pane.getChildren().clear();
            pane.getChildren().add(parent);
            DataAnalyticsHubController controller = loader.getController();
            controller.loadLoginForm(stage.getScene());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        mouseEvent.consume();
    }

    public void analysisButton(ActionEvent actionEvent) {
        List<Post> posts = PostDAO.getAllPosts(user.getFirstName());
        postTableView.getItems().clear();
        postTableView.getItems().addAll(posts);

        List slice1List = posts.stream().filter(p -> (p.getShares() >= 0 && p.getShares() <= 99)).collect(Collectors.toList());
        List slice2List = posts.stream().filter(p -> (p.getShares() >= 100 && p.getShares() <= 999)).collect(Collectors.toList());
        List slice3List = posts.stream().filter(p -> p.getShares() > 1000).collect(Collectors.toList());


        System.out.println("0-99 " + slice1List.size());
        System.out.println("100-999 " + slice2List.size());
        System.out.println("> 999 " + slice3List.size());

        FXMLLoader loader = new FXMLLoader(DataAnalyticsHubApplication.class.getResource("pie-chart-view.fxml"));
        try {
            Parent root = loader.load();
            PieChartController controller = loader.getController();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Data analysis");
            dialogStage.initStyle(StageStyle.UTILITY);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setScene(new Scene(root));

            controller.setStage(dialogStage);
            controller.setData(slice1List.size(), slice2List.size(), slice3List.size());

            dialogStage.show();

            actionEvent.consume();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void updateProfileClicked(ActionEvent actionEvent) {

        User reFetchedUser = UserDAO.searchById(parseInt(user.getId()));

        Dialog<User> userUpdateDialog = new Dialog<>();
        userUpdateDialog.setTitle("Update profile");
        userUpdateDialog.setHeaderText("Update profile");

        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        userUpdateDialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        TextField id = new TextField();
        id.setPromptText("User ID");
        id.setDisable(true);

        TextField username = new TextField();
        username.setText(reFetchedUser.getId());

        PasswordField password = new PasswordField();
        password.setText(reFetchedUser.getPassword());

        TextField fName = new TextField();
        fName.setText(reFetchedUser.getFirstName());

        TextField lName = new TextField();
        lName.setText(reFetchedUser.getLastName());

        gridPane.add(new Label("ID:"), 0, 0);
        gridPane.add(id, 1, 0);
        gridPane.add(new Label("Username:"), 0, 1);
        gridPane.add(username, 1, 1);
        gridPane.add(new Label("Password:"), 0, 2);
        gridPane.add(password, 1, 2);
        gridPane.add(new Label("First name:"), 0, 3);
        gridPane.add(fName, 1, 3);
        gridPane.add(new Label("Last name:"), 0, 4);
        gridPane.add(lName, 1, 4);

        userUpdateDialog.getDialogPane().setContent(gridPane);

        Platform.runLater(() -> id.requestFocus());

        userUpdateDialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                return new User(
                        id.getText(),
                        username.getText(),
                        password.getText(),
                        fName.getText(),
                        lName.getText()
                );
            }
            return null;
        });

        if (user != null) {
            username.setText(reFetchedUser.getUsername());
            id.setText(reFetchedUser.getId());
            password.setText(reFetchedUser.getPassword());
            fName.setText(reFetchedUser.getFirstName());
            lName.setText(reFetchedUser.getLastName());
        }

        Optional<User> result = userUpdateDialog.showAndWait();

        result.ifPresent(updatedUser -> {
            System.out.println("id= " + updatedUser.getId() +
                    ", username=" + updatedUser.getUsername() +
                    ", password= " + updatedUser.getPassword()+
                    ", first name= " + updatedUser.getFirstName() +
                    ", last name= " + updatedUser.getLastName()
            );
            try {
                boolean updated = UserDAO.updatePost(updatedUser);

                if (updated) {
                    System.out.println("User updated");
                } else {
                    AlertUtil.showAlert(
                            Alert.AlertType.ERROR,
                            "Update failed",
                            "OOPS! user profile update failed",
                            "For some reason, the update failed, please retry.");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        actionEvent.consume();
    }
}
