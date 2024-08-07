package com.example.imdb.model;

public class Movie {
    private String title;
    private String year;
    private String director;
    private String actors;
    private String plot;

    public Movie(String title, String year, String director, String actors, String plot) {
        this.title = title;
        this.year = year;
        this.director = director;
        this.actors = actors;
        this.plot = plot;
    }

    @Override
    public String toString() {
        return "Title: " + title + "\n" +
                "Year: " + year + "\n" +
                "Director: " + director + "\n" +
                "Actors: " + actors + "\n" +
                "Plot: " + plot;
    }
}