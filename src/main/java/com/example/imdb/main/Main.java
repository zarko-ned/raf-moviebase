package com.example.imdb.main;

import com.example.imdb.client.IMDbAPIClient;
import com.example.imdb.exception.MovieNotFoundException;
import com.example.imdb.model.Movie;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String imdbId = "tt14539740";  // Primer IMDb ID
        IMDbAPIClient client = new IMDbAPIClient();
        try {
            Movie movie = client.fetchMovieData(imdbId);
            if (movie != null) {
                System.out.println(movie);
            } else {
                System.out.println("Failed to retrieve data.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        catch (MovieNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}