package com.example.ocrtune;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    @FXML
    private  GridPane adminGridPane;
    private Stage stage;
    private Scene scene;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            openAdminWindow();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void goToUsers(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("adminUserScene.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    public void openAdminWindow() throws IOException {


        //Get all genre and duration data
        List<String> genresAndDuration = AccountDetails.getGenreWithAverageDuration();
        String[] splitString;

        int i = 1;
        for (String str : genresAndDuration) {
            splitString = str.split(",");
            Label genreLabel = new Label(splitString[0]);
            genreLabel.setFont(new Font(15));

            //Format Duration (2 DP)
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            String duration = decimalFormat.format(Double.parseDouble(splitString[1]));

            Label durationLabel = new Label(duration);
            durationLabel.setFont(new Font(15));

            //Add Labels to grid
            adminGridPane.add(genreLabel, 0, i);
            adminGridPane.add(durationLabel, 1, i);
            i++;


        }


    }

    public void backToLogin(ActionEvent actionEvent) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("LoginScene.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
