package com.example.imdb.main;

import com.example.imdb.client.IMDbAPIClient;
import com.example.imdb.config.Config;
import com.example.imdb.exception.MovieNotFoundException;
import com.example.imdb.model.Movie;
import com.example.imdb.util.CSVUtils;
import com.opencsv.exceptions.CsvException;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Enter IMDb ID: ");
            String imdbId = scanner.nextLine();  // Unos IMDb ID-a


            Movie movie = CSVUtils.getMovieFromCSV(Config.CSV_FILE_PATH, imdbId);

            if (movie != null) {
                System.out.println("Movie found in CSV:");
                System.out.println(movie);
            } else {
                IMDbAPIClient client = new IMDbAPIClient();
                System.out.println("Movie not found in CSV. Fetching from IMDb...");
                movie = client.fetchMovieData(imdbId);

                if (movie != null) {
                    System.out.println("Movie fetched from IMDb:");
                    System.out.println(movie);
                    boolean isMovieAdded = CSVUtils.addMovieToCSV(Config.CSV_FILE_PATH, movie);

                    if (isMovieAdded)
                        System.out.println("Movie added to CSV.");
                    else
                        System.out.println("Movie not added to CSV.");
                } else {
                    System.out.println("Movie not found on IMDb.");
                }
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("An error occurred while processing the request.");
            e.printStackTrace();
        } catch (CsvException e) {
            System.err.println("An error occurred while handling the CSV file.");
            e.printStackTrace();
        } catch (MovieNotFoundException e) {
            System.err.println("Movie not found: " + e.getMessage());
        } finally {
            scanner.close();  // Zatvaranje scanner-a u finally bloku
        }
    }
}