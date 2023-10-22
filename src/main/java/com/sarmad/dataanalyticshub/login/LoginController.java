package com.sarmad.dataanalyticshub.login;

import com.sarmad.dataanalyticshub.DataAnalyticsHubApplication;
import com.sarmad.dataanalyticshub.dao.UserDAO;
import com.sarmad.dataanalyticshub.dashboard.DashboardController;
import com.sarmad.dataanalyticshub.models.User;
import com.sarmad.dataanalyticshub.utils.AlertUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class LoginController {
    @FXML
    public TextField username;
    @FXML
    public PasswordField password;

    @FXML
    private Button loginButton;

    public void onLoginButtonClick(ActionEvent actionEvent) {
        System.out.println(username.getText());
        System.out.println(password.getText());

        User user = UserDAO.searchByUsernameAndPassword(username.getText(), password.getText());
        if (user != null) {
            System.out.println("User found on search: " + user.getFirstName());
            Button eventButton = (Button) actionEvent.getSource();
            openDashboardView(user);
        } else {
            AlertUtil.showAlert(
                    Alert.AlertType.ERROR,
                    "Login Error",
                    "Incorrect credentials",
                    "Entered username and password didn't match any user. Please try again or to create a new user click on the link.");
        }
    }

    public void openDashboardView(User user) {
        FXMLLoader dashboardFxmlLoader = new FXMLLoader(DataAnalyticsHubApplication.class.getResource("dashboard-view.fxml"));
        Parent parent = null;
        try {
            parent = dashboardFxmlLoader.load();
            DashboardController dashboardController = dashboardFxmlLoader.getController();
            dashboardController.setLoggedInUser(user.getUsername());
            dashboardController.setUser(user);

            Scene scene = loginButton.getScene();
            Pane pane = (Pane) scene.lookup("#mainContainer");

            pane.getChildren().clear();
            pane.getChildren().add(parent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadRegistrationForm(MouseEvent mouseEvent) {
        FXMLLoader registrationFxmlLoader = new FXMLLoader(DataAnalyticsHubApplication.class.getResource("registration-view.fxml"));

        Label label = (Label) mouseEvent.getSource();
        Scene scene = label.getScene();

        Pane vBox = (Pane) scene.lookup("#formContainer");
        System.out.println(vBox.getId());
        try {
            vBox.getChildren().clear();
            vBox.getChildren().add(registrationFxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
