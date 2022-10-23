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
import javafx.stage.Stage;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterController {

    @FXML
    Button registerButton, backToMenu;
    @FXML
    Label errorLabel;

    @FXML
    TextField usernameTextField, passwordTextField, dateOfBirthTextField, favArtistTextField, favGenreTextField;

    private Stage stage;
    private Scene scene;

    public void register(ActionEvent actionEvent) throws IOException {

        //All variable checks
        var usernameValid = false;
        var passwordValid = false;
        var dateOfBirthValid = false;
        var favArtistValid = false;
        var favGenreValid = false;

        //Username Checks
        String username = null;
        if(usernameTextField.getText().length() < 3 ) {
            errorLabel.setText("Error: Username is too short");
            usernameValid = false;
        } else {
            username = usernameTextField.getText();
            usernameValid = true;
        }

        //Username Duplicate test
        if(Account.doesUsernameExist(usernameTextField.getText())) {
            errorLabel.setText("Error: Username taken");
            usernameValid = false;
        } else {
            username = usernameTextField.getText();
            usernameValid = true;
        }

        //Password Checks
        String password = null;
        if(passwordTextField.getText().length() < 5) {
            errorLabel.setText("Error: Password is too short");
            passwordValid = false;
        } else {
            password = passwordTextField.getText();
            passwordValid = true;

        }
        //Data of birth check
        String dateOfBirth = dateOfBirthTextField.getText();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        dateFormatter.setLenient(false);
        try {
            Date birthDate = dateFormatter.parse(dateOfBirth);
            dateOfBirthValid = true;

        } catch (IllegalArgumentException | ParseException e) {
            errorLabel.setText("Error: Invalid date");
            dateOfBirthValid = false;
        }

        //Artist Check
        String favArtist = null;
        if(!favArtistTextField.getText().isBlank()) {
            favArtist = favArtistTextField.getText();
            favArtistValid = true;
        } else {
            favArtistValid = false;
            errorLabel.setText("Error: Invalid Artist");
        }

        //Genre Check
        String favGenre = null;
        if(!favGenreTextField.getText().isBlank()) {
            favGenre = favGenreTextField.getText();
            favGenreValid = true;
        } else {
            favGenreValid = false;
            errorLabel.setText("Error: Invalid Genre");

        }

       if(usernameValid && passwordValid && dateOfBirthValid && favArtistValid && favGenreValid) {
           Account.setCurrentUser(username);
           new Account().register(username, password, dateOfBirth, favArtist, favGenre);


           Parent root = FXMLLoader.load(WelcomeController.class.getResource("HomeScene.fxml"));
           stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
           scene = new Scene(root);
           stage.setScene(scene);
           stage.setX(15);
           stage.setY(15);
           stage.show();

       }


    }

    public void backToMenu(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(WelcomeController.class.getResource("WelcomeScene.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();


    }

    public void savePlaylist(ActionEvent actionEvent) {
    }
}
