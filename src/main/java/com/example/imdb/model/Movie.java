package com.example.imdb.model;

public class Movie {

    private String imdbId;
    private String title;
    private String year;
    private String director;
    private String actors;
    private String plot;

    public Movie(String imdbId, String title, String year, String director, String actors, String plot) {
        this.imdbId = imdbId;
        this.title = title;
        this.year = year;
        this.director = director;
        this.actors = actors;
        this.plot = plot;
    }

    public String getImdbId() {
        return imdbId;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setTitle(String title) {
        this.title = title;
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