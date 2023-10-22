package com.sarmad.dataanalyticshub;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DataAnalyticsHubController implements Initializable {
    @FXML
    public MenuItem logoutMenu;
    @FXML
    private Label welcomeText;

    public void quitApplication(ActionEvent actionEvent) {
        Platform.exit();
        System.exit(0);
    }

    public void logout(ActionEvent actionEvent) {
        System.out.println("logout");
        DataAnalyticsHubApplication.launch();
        actionEvent.consume();
    }

    public void loadLoginForm(Scene scene) {
        FXMLLoader loginFxmlLoader = new FXMLLoader(DataAnalyticsHubApplication.class.getResource("login-view.fxml"));
        Pane formContainer = (Pane) scene.lookup("#formContainer");
        System.out.println(formContainer.getId());

        formContainer.getChildren().clear();
        try {
            formContainer.getChildren().add(loginFxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}