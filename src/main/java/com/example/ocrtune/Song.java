package com.example.ocrtune;

public class Song {

    private String name;
    private double duration;
    private String artist;
    private String genre;

    public Song(String name, String artist, double duration, String genre) {
        this.name = name;
        this.duration = duration;
        this.artist = artist;
        this.genre = genre;
    }



    public String getName() {
        return name;
    }

    public double getDuration() {
        return duration;
    }

    public String getArtist() {
        return artist;
    }

    public String getGenre() {
        return genre;
    }
}
