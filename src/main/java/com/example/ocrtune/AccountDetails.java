package com.example.ocrtune;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountDetails {

    public static String ACCOUNT_DIRECTORY; //Updated in home controller initializable


    //Reads entire file and returns an arraylist with read lines
    public static List<String> readFile(String dir) {
        List<String> readFile = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(dir));
            String readLine;
            do {
                readLine = reader.readLine();
                if (readLine != null) {
                    readFile.add(readLine);
                }
            } while (readLine != null);

        } catch (IOException e) {
            e.getStackTrace();
        }
        return readFile;
    }

    //Needs arraylist with new file data and directory -> file will be overwritten
    public static void addNewData(List<String> newData, String dir) throws IOException {
        //Delete Contents from File
        BufferedWriter eraser = new BufferedWriter(new FileWriter(dir, false));
        eraser.write("");
        eraser.close();

        //Writes all new Data from ArrayList
        BufferedWriter writer = new BufferedWriter(new FileWriter(dir));
        for (int i = 0; i < newData.size(); i++) {
            if (newData.get(i) != null) {
                writer.write(newData.get(i));
                writer.newLine();
            }
        }
        writer.close();

    }

    //Gets file names
    public static File[] saveFileNames() {
        File folder = new File("src/main/Account Details");
        return folder.listFiles();
    }

    //Takes playlist -> makes it into string for file storage
    public static List<String> formatNewPlaylistForFile(Playlist playlist) {
        List<String> newPlaylistFormat = new ArrayList<>();
        newPlaylistFormat.add("---");

        //Add Playlist Name
        newPlaylistFormat.add(playlist.getPlaylistName());

        //Adds Songs
        String songName;
        String duration;
        String artist;
        String genre;
        Song currentSongSelected;
        List<Song> newSongs = playlist.getPlaylist();
        for (int i = 0; i < newSongs.size(); i++) {
            currentSongSelected = newSongs.get(i);
            if (currentSongSelected != null) { //Null Check
                songName = currentSongSelected.getName();
                duration = String.valueOf(currentSongSelected.getDuration());
                artist = currentSongSelected.getArtist();
                genre = currentSongSelected.getGenre();
                newPlaylistFormat.add(songName + "/" + duration + "/" + artist + "/" + genre);
            }
        }

        return newPlaylistFormat;
    }

    //Takes playlist -> Saves new playlist to file on the end of current data
    public static void addsNewPlaylistToFile(Playlist playlist) throws IOException {

        List<String> fileData = readFile(ACCOUNT_DIRECTORY);
        List<String> newPlaylist = formatNewPlaylistForFile(playlist);
        List<String> combinedLists = new ArrayList<>();

        for (int i = 0; i < fileData.size(); i++) {
            if (fileData.get(i) != null) {
                combinedLists.add(fileData.get(i));
            }
        }
        for (int i = 0; i < newPlaylist.size(); i++) {
            if (newPlaylist.get(i) != null) {
                combinedLists.add(newPlaylist.get(i));
            }
        }

        addNewData(combinedLists, ACCOUNT_DIRECTORY);
    }

    //Returns top 5 lines of file (name, password, DOB, fav artist, fav genre)
    public static List<String> getAccountDetails() { // FIXME: 23/10/2022 RETURNS WHOLE FILe
        List<String> accountDetails = new ArrayList<>();
        List<String> entireFile = readFile(ACCOUNT_DIRECTORY);
        try {
            int counter = 0;
            do {
                accountDetails.add(entireFile.get(counter));
                counter++;

            } while (!entireFile.get(counter).equals("---"));
        } catch (IndexOutOfBoundsException ignored) {

        }
        return accountDetails;
    }

    //Takes user -> Returns all their playlists
    public List<Playlist> getPlaylists(String user) throws IOException {

        List<String> rawData = readFile("src/main/Account Details/" + user + ".txt");
        List<Playlist> listOfPlaylists = new ArrayList<>();

        //Removes account details
        try {
            for (int i = 0; i < 5; i++) {
                if (rawData.get(0) != null) {
                    rawData.remove(0);
                }
            }
        } catch (NullPointerException e) {
            System.out.println(e.getStackTrace());
        }

        int counter = 0;
        String name;
        String duration;
        String artist;
        String genre;
        String[] splitLine;
        Playlist placeholderPlaylist;

        for (int i = 0; i < getNumberOfPlaylists(); i++) {
            placeholderPlaylist = new Playlist();
            List<Song> placeholderSongList = new ArrayList<>();

            rawData.remove(counter); //Removes dash line
            placeholderPlaylist.setPlaylistName(rawData.get(counter)); //Gets playlist name

            try {
                do {

                    //Splits line into song components
                    if (!(rawData.get(counter).equals("---")) && !(rawData.get(counter).equals(placeholderPlaylist.getPlaylistName()))) {

                        splitLine = rawData.get(counter).split("/");
                        name = splitLine[0];
                        duration = splitLine[1];
                        artist = splitLine[2];
                        genre = splitLine[3];
                        placeholderSongList.add(new Song(name, artist, Double.parseDouble(duration), genre));
                    }
                    if (counter == rawData.size()) {
                        counter--;
                    } else {
                        counter++;
                    }

                } while (!rawData.get(counter).equals("---") && rawData.get(counter) != null);
            } catch (IndexOutOfBoundsException ignored) {

            }

            placeholderPlaylist.setPlaylist(placeholderSongList);
            listOfPlaylists.add(placeholderPlaylist);

        }






        return listOfPlaylists;
}

    //Returns number of playlists for current user
    //TODO: 22/10/2022 UPDate this so user is a parameter
    public static int getNumberOfPlaylists() {
        List<String> rawData = readFile(ACCOUNT_DIRECTORY);
        int dashCounter = 0;
        for (int i = 0; i < rawData.size(); i++) {
            if (rawData.get(i).equals("---")) {
                dashCounter++;
            }
        }
        return dashCounter;
    }

    //Takes name of playlist -> Returns songs in playlist
    public static List<Song> getSongsFromPlaylist(String playlistName) throws IOException {
        List<Playlist> allPlaylists = new AccountDetails().getPlaylists(Account.getCurrentUser());
        List<Song> songList = new ArrayList<>();
        for (int i = 0; i < allPlaylists.size(); i++) {
            if (playlistName.equals(allPlaylists.get(i).getPlaylistName())) {
                songList = allPlaylists.get(i).getPlaylist();
            }
        }
        return songList;
    }

    public static List<String> getUniqueArtists() throws IOException {
        List<Song> songLibrary = Playlist.loadSongLibrary();

        LinkedHashSet<String> set = new LinkedHashSet<>();
        for (int i = 0; i < songLibrary.size(); i++) {
            set.add(songLibrary.get(i).getArtist());
        }

        List<String> uniqueArtistList = set.stream().toList();


        return uniqueArtistList;
    }

    public static void exportArtistSongs(List<String> artists, String fileName) throws IOException {
        String dir = "src/main/Export Songs/" + fileName + ".txt";
        File exportFile = new File(dir);
        if (fileName != null || fileName.equals("")) {
            exportFile.createNewFile();
        }


        //Get Songs from artists

        List<Song> songLibrary = Playlist.loadSongLibrary();
        List<Song> songsToBeExported = new ArrayList<>();

        for (int i = 0; i < artists.size(); i++) {
            for (int j = 0; j < songLibrary.size(); j++) {
                if (artists.get(i).equals(songLibrary.get(j).getArtist())) {
                    songsToBeExported.add(songLibrary.get(j));
                }
            }
        }

        //Convert Song List to String List
        List<String> songsAsText = new ArrayList<>();
        String songName;
        String duration;
        String artist;
        String genre;
        for (int i = 0; i < songsToBeExported.size(); i++) {
            songName = songsToBeExported.get(i).getName();
            duration = String.valueOf(songsToBeExported.get(i).getDuration());
            artist = songsToBeExported.get(i).getArtist();
            genre = songsToBeExported.get(i).getGenre();
            songsAsText.add(songName + "/" + duration + "/" + artist + "/" + genre);
        }

        //Write Data to File

        addNewData(songsAsText, dir);

    }

    public static List<String> getGenreWithAverageDuration() throws IOException {
        List<String> genres = getAllGenres();
        List<String> genresAndDuration = new ArrayList<>();
        double duration = -1;
        for (String genre : genres) {
            duration = getAverageDuration(genre);
            genresAndDuration.add(genre + "," + duration);

        }

        return genresAndDuration;
    }

    private static List<String> getAllGenres() throws IOException {
        List<Song> songLibrary = Playlist.loadSongLibrary();

        LinkedHashSet<String> set = new LinkedHashSet<>();
        for (Song songs : songLibrary) {
            set.add(songs.getGenre());
        }

        List<String> genreSet = set.stream().toList();


        return genreSet;
    }

    private static Double getAverageDuration(String genre) throws IOException {
        List<Song> songLibrary = Playlist.loadSongLibrary();
        double totalDuration = 0;
        int songNumber = 0;
        for (Song songs : songLibrary) {
            if (songs.getGenre().equals(genre)) {
                totalDuration += songs.getDuration();
                songNumber++;
            }
        }
        return totalDuration / songNumber;
    }

    public static Double getShortestSongLength() throws IOException {
        List<Song> songLibrary = Playlist.loadSongLibrary();
        double shortestSong = songLibrary.get(0).getDuration();
        for(Song song : Playlist.loadSongLibrary()) {
            if(shortestSong > song.getDuration()) {
                shortestSong = song.getDuration();
            }
        }
        return shortestSong;
    }

    public static boolean doesGenreExist(String genre) throws IOException {
        List<String> allGenres = getAllGenres();
        for(String str : allGenres) {
            if(str.equalsIgnoreCase(genre)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isFileNameValid(String fileName) {
        //Null Check
        if(fileName.isBlank()) {
            return false;
        }

        //Special Character Check
        Pattern pattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(fileName);
        if(matcher.find()) {
            return false;
        }

        return true;
    }

}

