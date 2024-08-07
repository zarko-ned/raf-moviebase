package com.example.imdb.util;


import com.example.imdb.model.Movie;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;


import java.io.*;

import java.util.List;

public class CSVUtils {
    // Metod za preuzimanje filma iz CSV
    public static Movie getMovieFromCSV(String filePath, String imdbId) throws IOException, CsvException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            CSVReader csvReader = new CSVReader(reader);
            List<String[]> records = csvReader.readAll();

            // Pretpostavljamo da je prvi red header
            boolean isFirstRow = true;
            for (String[] record : records) {
                if (isFirstRow) {
                    isFirstRow = false;  // Preskočite header red
                    continue;
                }

                String csvImdbId = record[0]; // Zameni 0 sa odgovarajućim indeksom
                if (csvImdbId.equals(imdbId)) {
                    String title = record[1]; // Prilagodite indekse prema strukturi CSV-a
                    String year = record[2];
                    String director = record[3];
                    String actors = record[4];
                    String plot = record[5];

                    return new Movie(csvImdbId, title, year, director, actors, plot);
                }
            }
        }

        return null;  // Vraća null ako IMDb ID nije pronađen
    }

    // Metod za dodavanje filma u CSV
    public static void addMovieToCSV(String filePath, Movie movie) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true));
             CSVWriter csvWriter = new CSVWriter(writer)) {

            String[] movieData = {
                    movie.getImdbId(),
                    movie.getTitle(),
                    movie.getYear(),
                    movie.getDirector(),
                    movie.getActors(),
                    movie.getPlot()
            };

            csvWriter.writeNext(movieData);
        }
    }
}
