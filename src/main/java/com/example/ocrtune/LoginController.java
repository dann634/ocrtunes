package com.example.ocrtune;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class LoginController {
    @FXML private Button loginButton;
    @FXML private TextField usernameTextField, passwordTextField;
    @FXML private Label errorLabel;

    private static Stage stage;
    private Scene scene;

    public void login(ActionEvent actionEvent) throws IOException {
        Account authoriseAccount = new Account();
        if(authoriseAccount.login(usernameTextField.getText(), passwordTextField.getText())) {
            Account.setCurrentUser(usernameTextField.getText());
            Parent root = FXMLLoader.load(LoginController.class.getResource("HomeScene.fxml"));
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setX(15);
            stage.setY(15);
            stage.show();


        } else {
            if(usernameTextField.getText().equals("admin") && passwordTextField.getText().equals("password")) {

                //Loads Admin Page
                Parent root = FXMLLoader.load(getClass().getResource("AdminScene.fxml"));
                stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();

            } else {
                errorLabel.setText("Error: Incorrect Username or Password");
                errorLabel.setVisible(true);
            }
        }
    }
}
