package com.example.imdb.main;

import com.example.imdb.client.IMDbAPIClient;
import com.example.imdb.config.Config;
import com.example.imdb.exception.MovieNotFoundException;
import com.example.imdb.model.Movie;
import com.example.imdb.utils.CSVUtils;
import com.opencsv.exceptions.CsvException;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/*
 * ZADATAK:
 *
 * Implementirati funkcionalnost za prikaz podataka o filmu na osnovu unetog IMDb ID.
 * Potrebno je prikazati sledeće informacije:
 *
 * 1. Naziv filma
 * 2. Godinu izlaska filma
 * 3. Režisera filma
 * 4. Glumce u filmu
 * 5. Opis filma
 *
 * Postupak je sledeći:
 *
 * 1. **Provera u CSV datoteci:** Prvo, proveriti da li film sa unetim IMDb ID-om postoji u datoteci `movies.csv`.
 *    Ako film postoji, pročitajte i prikažite njegove podatke iz ove datoteke.
 *
 * 2. **Dohvatanje sa API-ja:** Ako film sa unetim IMDb ID-om nije pronađen u `movies.csv`, preuzmite podatke o filmu preko API-ja.
 *
 * 3. **Ažuriranje CSV datoteke:** Nakon što dobijete podatke putem API-ja, sačuvajte ih u `movies.csv` datoteku za buduće potrebe.
 *
 * 4. **Prikaz najnovijih filmova:** Na kraju prikazati najnovije filmove iz datoteke `movies.csv` tako što se prikažu poslednja 3 filmva iz ove datoteke.
 *
 *  Unos IMDb ID-a se vrši preko tastature.
 */

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

            System.out.println("◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆");
            System.out.println("The newest movies:");

            List<Movie> newestMovies = CSVUtils.getTheNewestMovies(Config.CSV_FILE_PATH);

            for (Movie m : newestMovies) {
                System.out.println("-------------------------");
                System.out.println("Title: " + m.getTitle());
                System.out.println("Year: " + m.getYear());
                System.out.println("Director: " + m.getDirector());
                System.out.println("Actors: " + m.getActors());
                System.out.println("Plot: " + m.getPlot());
            }

            System.out.println("-------------------------");

        } catch (IOException | InterruptedException e) {
            System.err.println("An error occurred while processing the request.");
            e.printStackTrace();
        } catch (CsvException e) {
            System.err.println("An error occurred while handling the CSV file.");
            e.printStackTrace();
        }  catch (MovieNotFoundException e) {
            System.err.println("Movie not found: " + e.getMessage());
        } finally {
            scanner.close();  // Zatvaranje scanner-a u finally bloku
        }
    }
}