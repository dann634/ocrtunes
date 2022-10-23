package com.example.ocrtune;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;

/*
Parent root = FXMLLoader.load(getClass().getResource("startScreen.fxml"));
stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
scene = new Scene(root);
stage.setScene(scene);
stage.show();
*/

public class Main extends Application {




    @Override
    public void start(Stage stage) throws IOException, URISyntaxException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("WelcomeScene.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("OCRTunes");
        stage.setScene(scene);

        //Program Icon
        stage.getIcons().add(new Image("C:\\Users\\Dan\\IdeaProjects\\OCRTune\\src\\main\\icon.png"));

        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }

    //Create an error label (used everywhere)
    public static Label createErrorLabel() {
        Label errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);
        errorLabel.setFont(new Font(16));
        errorLabel.setAlignment(Pos.CENTER);
        errorLabel.setVisible(false);
        errorLabel.managedProperty().bind(errorLabel.visibleProperty());
        return errorLabel;
    }

}