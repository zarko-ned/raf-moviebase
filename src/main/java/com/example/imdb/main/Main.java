package com.example.imdb.main;

import com.example.imdb.client.IMDbAPIClient;
import com.example.imdb.exception.MovieNotFoundException;
import com.example.imdb.model.Movie;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter IMDb ID: ");
        String imdbId = scanner.nextLine();  // Unos IMDb ID-a

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