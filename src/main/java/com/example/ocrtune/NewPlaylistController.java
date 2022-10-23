package com.example.ocrtune;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.ResourceBundle;

public class NewPlaylistController implements Initializable {

    private Stage stage;
    private Scene scene;
    private boolean isFirstLoad = true;

    @FXML
    private GridPane gridPane;

    @FXML
    VBox vboxMain;

    private Label songName, artist, songDuration, genre;
    private Button addSongButton;
//    public static String playlistName;

    //public static List<Song> newPlaylist = new ArrayList<>();

    public static Playlist newPlaylist = new Playlist();



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            List<Song> clearPlaylist = new ArrayList<>();
            newPlaylist.setPlaylist(clearPlaylist);
        try {
            loadSongLibraryToGUI();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void loadSongLibraryToGUI() throws IOException {
        Playlist.firstLoad = false;
        List<Song> songLibrary = Playlist.loadSongLibrary();

        for (int i = 0; i < songLibrary.size(); i++) {
            songName = new Label(songLibrary.get(i).getName());
            songName.setFont(new Font(16));

            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            var songDurationString = decimalFormat.format(songLibrary.get(i).getDuration());
            songDuration = new Label(songDurationString);

            artist = new Label(songLibrary.get(i).getArtist());
            genre = new Label(songLibrary.get(i).getGenre());
            addSongButton = new Button("Add");
            addSongButton.setPrefWidth(50);
            int finalI = i + 2;
            addSongButton.setOnAction(e -> {
                List<Song> songList = newPlaylist.getPlaylist();
                songList.add(songLibrary.get(finalI-2));
                newPlaylist.setPlaylist(songList);
            });
            gridPane.add(songName, 0, i + 2);
            gridPane.add(artist, 1, i + 2);
            gridPane.add(songDuration, 2, i + 2);
            gridPane.add(genre, 3, i + 2);
            gridPane.add(addSongButton, 4, i + 2);


        }
    }

    public void savePlaylist(ActionEvent actionEvent) throws IOException {
        openDialogBox();
    }

    private void savePopupButtonPress() throws IOException {
        AccountDetails.addsNewPlaylistToFile(newPlaylist);
    }

    public void backToMenu(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(NewPlaylistController.class.getResource("HomeScene.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        gridPane.getChildren().clear();
    }

    private void openDialogBox() {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);
        dialog.setResizable(false);
        VBox dialogVbox = new VBox(10);
        dialogVbox.setPadding(new Insets(10, 30, 10, 30));
        dialogVbox.setAlignment(Pos.TOP_CENTER);
        dialogVbox.setPrefHeight(150);

        Label title = new Label("Name Playlist");
        title.setFont(new Font(14));

        TextField textField = new TextField();
        textField.setAlignment(Pos.CENTER);
        textField.setPrefWidth(300);
        textField.setPromptText("Playlist Name");
        Label errorLabel = Main.createErrorLabel();
        Button saveButton = new Button("Save");

        saveButton.setOnAction(e -> {
            try {
               if(AccountDetails.isFileNameValid(textField.getText())) {
                   newPlaylist.setPlaylistName(textField.getText());
                   savePopupButtonPress();
                   dialog.close();
               } else {
                   errorLabel.setText("Error: Enter a valid playlist name");
                   errorLabel.setVisible(true);
               }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        dialogVbox.getChildren().addAll(title, textField, errorLabel, saveButton);
        Scene dialogScene = new Scene(dialogVbox);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    public void generatePlaylistWindow(ActionEvent actionEvent) {
        final Stage generatePlaylistWindow = new Stage();
        generatePlaylistWindow.initModality(Modality.APPLICATION_MODAL);
        generatePlaylistWindow.initOwner(stage);
        generatePlaylistWindow.setResizable(false);


        var vbox = new VBox(12);
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setPadding(new Insets(20, 80, 50, 80));

        //GridPane
        var gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(15);
        gridPane.setVgap(5);

        //Title
        var title = new Label("Generate Playlist");
        title.setFont(new Font(18));

        //Main Body
        var genreLabel = new Label("Genre:");
        var genreCheckBox = new CheckBox();
        var genreTextField = new TextField();
        genreTextField.setOpacity(0.5);
        genreTextField.setDisable(true);
        genreCheckBox.setOnAction(e -> {
           if(genreTextField.getOpacity() == 0.5) {
               genreTextField.setOpacity(1);
               genreTextField.setDisable(false);
           } else {
               genreTextField.setOpacity(0.5);
               genreTextField.setDisable(true);
           }
        });
        gridPane.addRow(0, genreLabel, genreCheckBox, genreTextField);


        var durationLabel = new Label("Max Duration:");
        var durationCheckBox = new CheckBox();
        var durationTextField = new TextField();
        durationTextField.setOpacity(0.5);
        durationTextField.setDisable(true);
        durationCheckBox.setOnAction(e -> {

            if(durationTextField.getOpacity() == 0.5) {
                durationTextField.setOpacity(1);
                durationTextField.setDisable(false);
            } else {
                durationTextField.setOpacity(0.5);
                durationTextField.setDisable(true);
            }
        });
        gridPane.addRow(1, durationLabel, durationCheckBox, durationTextField);

        var playlistNameLabel = new Label("Playlist Name:");
        var playlistNameTextField = new TextField();
        gridPane.addRow(2, playlistNameLabel);
        gridPane.add(playlistNameTextField, 2, 2);

        //Error Label
        var errorLabel = Main.createErrorLabel();


        //Button
        var btn = new Button("Generate");

        vbox.getChildren().addAll(title, gridPane, btn, errorLabel);


        btn.setOnAction(e -> {

            boolean hasPlaylistName = false;
            boolean isGenreValid = false;
            boolean isDurationValid = false;

           String genre = null;
           double duration = -1;

           //Genre
            try {
                if(genreCheckBox.isSelected()) {
                    if(AccountDetails.doesGenreExist(genreTextField.getText())) {
                        genre = genreTextField.getText();
                        isGenreValid = true;
                    } else {
                        errorLabel.setText("Error: Invalid Genre");
                        errorLabel.setVisible(true);
                    }

                } else {
                    isGenreValid = true;
                    genre = null;
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            //Duration
            if(durationCheckBox.isSelected()) {

                try {

                    duration = Double.parseDouble(durationTextField.getText());
                    if(duration > 0) {
                        isDurationValid = true;
                    } else {
                        errorLabel.setText("Error: Invalid duration");
                        errorLabel.setVisible(true);
                    }

                } catch (Exception error) {
                    errorLabel.setText("Error: Invalid duration");
                    errorLabel.setVisible(true);
                }

            } else {
                duration = -1;
                isDurationValid = true;
            }

            //Playlist name
            if(AccountDetails.isFileNameValid(playlistNameTextField.getText())) {
                newPlaylist.setPlaylistName(playlistNameTextField.getText());
                hasPlaylistName = true;
            } else {
                errorLabel.setText("Error: Invalid Playlist Name");
                errorLabel.setVisible(true);
            }

            if(hasPlaylistName && isDurationValid && isGenreValid) {
                try {
                    List<Song> songList = Playlist.generatePlaylist(genre, duration);
                    newPlaylist.setPlaylist(null);
                    newPlaylist.setPlaylist(songList);
                    generatePlaylistWindow.close();
                    AccountDetails.addsNewPlaylistToFile(newPlaylist);
                } catch (IOException ignored) {

                }
            }
        });

        //Add elements to Vbox

        var scene = new Scene(vbox); //400, 215
        generatePlaylistWindow.setScene(scene);
        generatePlaylistWindow.show();
    }
}

