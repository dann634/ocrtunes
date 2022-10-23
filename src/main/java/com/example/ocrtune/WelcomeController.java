package com.example.ocrtune;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomeController {

    @FXML
    Button loginButton, registerButton;

    private Stage stage;
    private Scene scene;


    public void goToLoginScene(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(WelcomeController.class.getResource("LoginScene.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void goToRegisterScene(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(WelcomeController.class.getResource("RegisterScene.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
}
