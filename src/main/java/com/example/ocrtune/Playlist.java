package com.example.ocrtune;

import java.io.*;
import java.util.*;

public class Playlist {
    private List<Song> playlist = new ArrayList<>();

    static boolean firstLoad = true;
    private String playlistName;

    public static List<Song> loadSongLibrary() throws IOException { // TODO: 22/10/2022 OPTIMIZE THIS
        List<Song> songLibrary = new ArrayList<>();
        List<String> fileData = AccountDetails.readFile("src/main/songLibrary.txt");

        //Sort alphabetically
        fileData.sort(String.CASE_INSENSITIVE_ORDER);

        for(String line : fileData) {
            String[] splitLine = line.split("/");
            songLibrary.add(new Song(splitLine[0], splitLine[1], Double.parseDouble(splitLine[2]), splitLine[3]));
        }

        return songLibrary;
    }

    public List<Song> getPlaylist() {
        return playlist;
    }

    public void setPlaylist(List<Song> playlist) {
        this.playlist = playlist;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public static List<Song> generatePlaylist(String genre, double duration) throws IOException {

        double timeLeft = duration;

        List<Song> possibleSongs;
        List<Song> generatedPlaylist = new ArrayList<>();

        if (genre != null && timeLeft == -1) {
            generatedPlaylist = getSongsFromGenre(genre);
        } else if (genre != null && timeLeft > AccountDetails.getShortestSongLength()) {
            //Add Songs from genre subtracting time (loop)
            generatedPlaylist = new ArrayList<>();
            possibleSongs = getSongsFromGenre(genre);
            for (Song song : possibleSongs) {
                if ((checkTotalPlaylistDuration(generatedPlaylist) + song.getDuration()) < duration) {
                    generatedPlaylist.add(song);
                }
            }

        } else if (genre == null && timeLeft > AccountDetails.getShortestSongLength()) {
            //Add random songs subtracting time (loop)
            List<Song> songLibrary = loadSongLibrary();
            for (Song song : songLibrary) {
                if (checkTotalPlaylistDuration(generatedPlaylist) + song.getDuration() < duration) {
                    generatedPlaylist.add(song);
                }
            }


        } else if (genre == null && timeLeft == -1) {
            //Return song Library
            generatedPlaylist = Playlist.loadSongLibrary();
        }

        return generatedPlaylist;

    }

    private static List<Song> getSongsFromGenre(String genre) throws IOException {
        List<Song> songList = new ArrayList<>();
        //If statement checks if genre exists

        for (Song song : Playlist.loadSongLibrary()) {
            if(song.getGenre().equalsIgnoreCase(genre)) {
                songList.add(song);
            }
        }
        return songList;
    }

    private static double getAllSongDuration() throws IOException {
        List<Song> songLibrary = loadSongLibrary();
        double totalDuration = 0;
        for(Song song : songLibrary) {
            totalDuration += song.getDuration();
        }
        return totalDuration;
    }

    private static double getGenreDuration(String genre) throws IOException {
        List<Song> songs = getSongsFromGenre(genre);
        double duration = 0;
        for(Song song : songs) {
            duration =+ song.getDuration();
        }
        return duration;
    }

    private static double checkTotalPlaylistDuration(List<Song> list) {
        double duration = 0;

        for(Song song : list) {
            duration = duration + song.getDuration();
        }

        return duration;
    }

    public static void deletePlaylist(String playlistName) {
        try {
            var playlistList = new AccountDetails().getPlaylists(Account.getCurrentUser());
            List<Playlist> newPlaylistList = new ArrayList<>();
            for(Playlist playlist : playlistList) {
                if(!playlist.getPlaylistName().equals(playlistName)) {
                    newPlaylistList.add(playlist);
                }
            }

            //Have list of all wanted playlists
            //Need to turn them into format for file storage
            //1. Get details
            //2. Add playlists using format playlists method
            List<String> accountDetails = AccountDetails.getAccountDetails(); // FIXME: 23/10/2022 HUH
            List<String> newFileData = new ArrayList<>(accountDetails);

            //Turn playlist data to String for file
            for(Playlist playlist : newPlaylistList) {
                newFileData.addAll(AccountDetails.formatNewPlaylistForFile(playlist));
            }

            AccountDetails.addNewData(newFileData, AccountDetails.ACCOUNT_DIRECTORY);


        } catch (IOException e) {
            System.out.println("Error: Removing Playlist Failed");
        }
    }

    public static List<String> alphabetizeSongLibrary(List<String> songLibrary) {
        songLibrary.sort(String.CASE_INSENSITIVE_ORDER);
        return songLibrary;
    }



}