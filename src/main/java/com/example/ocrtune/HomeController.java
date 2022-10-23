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
import javafx.scene.control.*;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class HomeController implements Initializable {
    @FXML
    Button settingsButton, newPlaylistButton, playPauseButton;

    @FXML
    private Label titleLabel;

    private Stage stage;
    private Scene scene;


    @FXML
    private GridPane centreGridPane;

    @FXML
    private AnchorPane topCentreAnchorPane;

    @FXML
    private VBox dynamicPlaylistVbox;

    @FXML
    private Label songLengthLabel, currentTime, currentSongLabel, currentArtistLabel;

    @FXML
    private Slider songSlider, volumeSlider;

   private static String lastButtonSelected;

   private static boolean isSongPlaying = false;
   private static Song currentSong;
   private static AudioPlayer player;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Load Playlists for viewing
        // TODO: 19/10/2022 Add an edit playlist (rename)
        // TODO: 22/10/2022 Add option to delete profile
        AccountDetails.ACCOUNT_DIRECTORY = "src/main/Account Details/" + Account.getCurrentUser() + ".txt";
        try {
            loadPlaylistsToGUI();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Load nodes to GUI
    public void loadPlaylistsToGUI() throws IOException {

        //Clear previous playlists
        dynamicPlaylistVbox.getChildren().clear();

        List<Playlist> listOfPlaylists = new AccountDetails().getPlaylists(Account.getCurrentUser());


        for (int i = 0; i < AccountDetails.getNumberOfPlaylists(); i++) {
            Button playlistBtn = new Button(listOfPlaylists.get(i).getPlaylistName());
            playlistBtn.setPrefWidth(150);
            playlistBtn.setPrefHeight(30);

            //SEND HELP
            playlistBtn.setOnAction(e -> {
                try {
                    loadPlaylistSongsToGUI(playlistBtn.getText());
                    topCentreAnchorPane.setVisible(true);
                    lastButtonSelected = playlistBtn.getText();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            dynamicPlaylistVbox.getChildren().add(playlistBtn);
        }
    }

    private void loadPlaylistSongsToGUI(String playlistName) throws IOException {
        centreGridPane.getChildren().clear();

        titleLabel.setText(playlistName);

        //Titles
        Label songTitle = createTitle("Name");
        Label artistTitle = createTitle("Artist");
        Label durationTitle = createTitle("Duration");
        Label genreTitle = createTitle("Genre");

        centreGridPane.add(songTitle, 0, 0);
        centreGridPane.add(artistTitle, 2, 0);
        centreGridPane.add(durationTitle, 1, 0);
        centreGridPane.add(genreTitle, 3, 0);


        List<Song> songList = AccountDetails.getSongsFromPlaylist(playlistName);
        String songName;
        String artist;
        String duration;
        String genre;
        for (int i = 0; i < songList.size(); i++) {
            songName = songList.get(i).getName();
            artist = songList.get(i).getArtist();

            //Decimal Format
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            duration = String.valueOf(decimalFormat.format(songList.get(i).getDuration()));

            genre = songList.get(i).getGenre();

            Label songNameLabel = createSongLabel(songName);
            Label artistLabel = createSongLabel(artist);
            Label durationLabel = createSongLabel(duration);
            Label genreLabel = createSongLabel(genre);
            Button playSongButton = new Button("Select");

            String finalSongName = songName;
            String finalArtist = artist;
            double finalDuration = Double.parseDouble(duration);
            String finalGenre = genre;
            playSongButton.setOnAction(e -> {
                currentSong = new Song(finalSongName, finalArtist, finalDuration, finalGenre);
                //Update UI
                currentSongLabel.setText(currentSong.getName());
                currentSongLabel.setVisible(true);
                currentArtistLabel.setText(currentSong.getArtist());
                currentArtistLabel.setVisible(true);

                songLengthLabel.setText(String.valueOf(new DecimalFormat("0.00").format(currentSong.getDuration())));

                // TODO: 23/10/2022 Automatically play the song
                player = new AudioPlayer();
                player.play();
            });

            centreGridPane.add(songNameLabel, 0, i + 1);
            centreGridPane.add(durationLabel, 1, i + 1);
            centreGridPane.add(artistLabel, 2, i + 1);
            centreGridPane.add(genreLabel, 3, i + 1);
            centreGridPane.add(playSongButton, 4, i + 1);

        }

    }

    public void deletePlaylistButtonPress() {
        //Get playlist selected (updated in loadPlaylistToGUI)
        //Call delete function
        Playlist.deletePlaylist(lastButtonSelected);
        //Update home scene
        try {
            loadPlaylistsToGUI();
            centreGridPane.getChildren().clear();
            topCentreAnchorPane.setVisible(false);
        } catch (IOException e) {
            System.out.println("Error: Reloading home scene failed");
        }
    }


    //Scene Switchers
    public void goToSettings() {

        final Stage settings = new Stage();
        settings.initModality(Modality.APPLICATION_MODAL);
        settings.initOwner(stage);
        settings.setResizable(false);

        VBox settingsVbox = new VBox(10);
        settingsVbox.setPadding(new Insets(0, 20, 0, 20));
        settingsVbox.setAlignment(Pos.TOP_CENTER);
        Label title = new Label("Account Settings");
        title.setFont(new Font(16));

        List<String> accountDetails = AccountDetails.getAccountDetails();


        HBox artistHBox = new HBox(10);
        Label artistLabel = new Label("Favourite Artist:");
        TextField artistTextField = new TextField();
        artistTextField.setText(accountDetails.get(3));
        artistHBox.getChildren().addAll(artistLabel, artistTextField);

        HBox genreHBox = new HBox(10);
        Label genreLabel = new Label("Favourite Genre:");
        TextField genreTextField = new TextField(accountDetails.get(4));
        genreHBox.getChildren().addAll(genreLabel, genreTextField);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            //Update data
            String newFavArtist = artistTextField.getText();
            String newFavGenre = genreTextField.getText();

            List<String> updatedData = AccountDetails.readFile(AccountDetails.ACCOUNT_DIRECTORY);
            updatedData.set(3, newFavArtist);
            updatedData.set(4, newFavGenre);

            try {
                AccountDetails.addNewData(updatedData, AccountDetails.ACCOUNT_DIRECTORY);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            settings.close();
        });
        settingsVbox.setAlignment(Pos.TOP_CENTER);
        genreHBox.setAlignment(Pos.TOP_CENTER);
        artistHBox.setAlignment(Pos.TOP_CENTER);
        settingsVbox.getChildren().addAll(title, artistHBox, genreHBox, saveButton);
        Scene dialogScene = new Scene(settingsVbox, 400, 150);
        settings.setScene(dialogScene);
        settings.show();
    }

    public void goToNewPlaylist(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(HomeController.class.getResource("NewPlaylistScene.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void exportSongs() throws IOException {

        final Stage exportSongs = new Stage();
        exportSongs.initModality(Modality.APPLICATION_MODAL);
        exportSongs.initOwner(stage);
        exportSongs.setResizable(false);


        //Vbox
        VBox exportSongsVBox = new VBox(10);
        exportSongsVBox.setPadding(new Insets(0, 20, 0, 20));
        exportSongsVBox.setAlignment(Pos.CENTER);
        exportSongsVBox.setSpacing(15);

        //Gridpane
        GridPane pane = new GridPane();

        List<String> artistList = AccountDetails.getUniqueArtists();
        int numberOfArtists = artistList.size();

        //Decides number of rows
        int numberOfRows = 1;
        if (numberOfArtists >= 4) {
            numberOfRows = (numberOfArtists / 4) + 1;
        }

        //Number of columns
        int numberOfColumns = 8;
        if (numberOfArtists < 4) {
            numberOfColumns = numberOfArtists * 2;
        }

        //Update Grid Pane
        pane.addColumn(numberOfColumns);
        pane.addRow(numberOfRows);
        pane.setPadding(new Insets(0, 20, 0, 20));
        pane.setHgap(15);
        pane.setVgap(5);


        List<String> checkBoxCoords = new ArrayList<>(); //Column: 5 Row: 2 (TARGET)
        int artistCounter = 0;
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {

                   if(j % 2 == 0) {

                       try {
                           pane.add(new Label(artistList.get(artistCounter)), j, i);
                       } catch (IndexOutOfBoundsException e) {
                           break;
                       }


                       if(artistCounter == artistList.size()) {
                           break;
                       } else {
                           artistCounter++;
                       }
                   } else {
                       pane.add(new CheckBox(), j, i);
                       checkBoxCoords.add(j + "," + i);
                   }


            }
        }



        //Add text file name text field
        TextField textField = new TextField();
        textField.setPromptText("Name Your File");
        textField.setAlignment(Pos.CENTER);
        textField.setMaxWidth(200);



        //Nodes
        Label title = new Label("Export Songs");
        title.setFont(new Font(16));

        //Error Label
        Label errorLabel = Main.createErrorLabel();


        Button exportButton = new Button("Export");
        exportButton.setOnAction(e -> {

            //Get selected artists
            String[] splitCoords;

            List<String> artistSelected = new ArrayList<>();
            int row;
            int column;
            Label artistLabel;
            for (int i = 0; i < checkBoxCoords.size(); i++) {
                splitCoords = checkBoxCoords.get(i).split(",");
                try {
                    CheckBox checkBox;
                    row = Integer.parseInt((splitCoords[0]));
                    column = Integer.parseInt(splitCoords[1]);
                    checkBox = ((CheckBox) getNodeFromGridPane(pane, row, column));
                    if(checkBox.isSelected()) {
                        artistLabel = (Label) getNodeFromGridPane(pane, row-1, column);
                        artistSelected.add(artistLabel.getText());
                    }
                } catch (Exception d) {
                    System.out.println(d);
                    exportSongs.close();
                }

            }

            //Checks if no checkboxes are selected -> Gives error
            boolean artistsValid = false;
            if(!artistSelected.isEmpty()) {
                artistsValid = true;
            } else {
                artistsValid = false;
                errorLabel.setText("Error: Select at least one artist");
                errorLabel.setVisible(true);
            }

            //Text File Name Check
            boolean validFileName = false;
            if(AccountDetails.isFileNameValid(textField.getText())) {
                validFileName = true;
            } else {
                validFileName = false;
                errorLabel.setText("Error: Please enter a valid file name");
                errorLabel.setVisible(true);
            }

            try {

                if(validFileName && artistsValid) {
                    AccountDetails.exportArtistSongs(artistSelected, textField.getText());
                    exportSongs.close();
                }

            } catch (IOException ex) {
                System.err.println("Error: Exporting Songs Failed");
                System.out.println(ex);
            }
        });

        exportSongsVBox.getChildren().addAll(title, pane, textField, errorLabel, exportButton);
        exportSongsVBox.setPadding(new Insets(25, 5, 30, 5));


        Scene exportSongsScene = new Scene(exportSongsVBox);
        exportSongs.setScene(exportSongsScene);
        exportSongs.show();
    }

    public void logout(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("WelcomeScene.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setX(400);
        stage.setY(100);
        stage.show();

    }

    //Music UI
    

    public void playPauseSong(ActionEvent actionEvent) {
        //Label control
        Button btn = (Button) actionEvent.getSource();
        if(btn.getText().equals("Play")) {
            btn.setText("Pause");
            //Play Song
        } else {
            btn.setText("Play");
            //Pause song
        }
    }

    public void nextSong(ActionEvent actionEvent) {

    }

    public void previousSong(ActionEvent actionEvent) {
    }


    public void updateVolume(MouseDragEvent mouseEvent) {
        double sliderValue = volumeSlider.getValue() / 100;
        player.setVolume(sliderValue);
    }









    //Utility Methods
    private Label createSongLabel(String text) {
        Label label = new Label(text);
        label.setTextFill(Color.WHITE);
        return label;
    }

    private Label createTitle(String text) {
        Label title = new Label(text);
        title.setFont(new Font(20));
        title.setTextFill(Color.WHITE);
        return title;
    }

    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

    public static Song getCurrentSong() {
        return currentSong;
    }
}
