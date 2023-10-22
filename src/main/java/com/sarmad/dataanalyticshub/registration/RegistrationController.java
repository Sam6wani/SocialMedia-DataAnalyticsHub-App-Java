package com.sarmad.dataanalyticshub.registration;

import com.sarmad.dataanalyticshub.DataAnalyticsHubApplication;
import com.sarmad.dataanalyticshub.dao.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class RegistrationController {
    @FXML
    public TextField newUsername;
    @FXML
    public PasswordField newPassword;
    @FXML
    public TextField newFName;
    @FXML
    public TextField newLName;

    public void onRegisterButtonClick(ActionEvent actionEvent) {
        System.out.println("Register button clicked");
        System.out.println("Username: " + newUsername.getText());
        System.out.println("Password: " + newPassword.getText());
        System.out.println("First Name: " + newFName.getText());
        System.out.println("Last Name: " + newLName.getText());

        try {
            UserDAO.insertUser(newUsername.getText(), newPassword.getText(), newFName.getText(), newLName.getText());

            FXMLLoader registrationFxmlLoader = new FXMLLoader(DataAnalyticsHubApplication.class.getResource("login-view.fxml"));

            Button button = (Button) actionEvent.getSource();
            Scene scene = button.getScene();

            Pane pane = (Pane) scene.lookup("#formContainer");
            System.out.println(pane.getId());
            try {
                pane.getChildren().clear();
                pane.getChildren().add(registrationFxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
