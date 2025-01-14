package com.example.imdb;

import com.example.imdb.config.Config;
import com.example.imdb.model.Movie;
import com.example.imdb.utils.CSVUtils;
import com.opencsv.exceptions.CsvException;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class CSVUtilsTest {

    @ParameterizedTest
    @CsvFileSource(resources = "/movies.csv", numLinesToSkip = 1)
    void testGetMovieFromCSVWithFile(String imdbId, String title, String year, String director, String actors, String plot) throws IOException, CsvException {
        Movie movie = CSVUtils.getMovieFromCSV("movies.csv", imdbId);
        assertNotNull(movie);
        assertEquals(title, movie.getTitle());
        assertEquals(year, movie.getYear());
        assertEquals(director, movie.getDirector());
        assertEquals(actors, movie.getActors());
        assertEquals(plot, movie.getPlot());
    }

    @Test
    void testGetMovieFromCSVWithInvalidFile() {
        String invalidFilePath = "invalid-movies.csv"; // Ovo je putanja do nepostojećeg fajla

        // Očekujemo da se desi IOException kada pokušamo da pročitajmo iz nepostojećeg fajla
        assertThrows(IOException.class, () -> {
            CSVUtils.getMovieFromCSV(invalidFilePath, "tt1234567");
        });
    }

    @Test
    void testGetMovieFromCSVWithNullPath() {
        // Putanja je null
        String nullFilePath = null;

        // Očekujemo da se desi IllegalArgumentException kada pokušamo da pročitajmo iz null putanje
        assertThrows(IllegalArgumentException.class, () -> {
            CSVUtils.getMovieFromCSV(nullFilePath, "tt1234567");
        });
    }


    @ParameterizedTest
    @MethodSource("provideMoviesForAdding")
    void testAddMovieToCSV(Movie movie) throws IOException, CsvException {

        // Kreiramo privremeni CSV fajl
        File tempFile = File.createTempFile("test-movies", ".csv");
        tempFile.deleteOnExit();
        Path filePath = tempFile.toPath();


        // Dodajemo film u CSV

        assertTrue(CSVUtils.addMovieToCSV(filePath.toString(), movie));

    }

    static Stream<Movie> provideMoviesForAdding() {
        return Stream.of(
                new Movie("tt0000001", "Test Movie 1", "2024", "Director 1", "Actor 1, Actor 2", "Test plot 1."),
                new Movie("tt0000002", "Test Movie 2", "2023", "Director 2", "Actor 3, Actor 4", "Test plot 2.")
        );
    }

    @TestFactory
    Stream<DynamicTest> testGetTheNewestMovies() throws IOException {
        List<Movie> movies = CSVUtils.getTheNewestMovies(Config.CSV_FILE_PATH);

        return Stream.of(movies).flatMap(List::stream)
                .map(movie -> DynamicTest.dynamicTest("Test movie: " + movie.getTitle(),
                        () -> {
                            assertNotNull(movie.getImdbId(), "IMDB ID should not be null");
                            assertNotNull(movie.getTitle(), "Title should not be null");
                            assertNotNull(movie.getYear(), "Year should not be null");
                            assertNotNull(movie.getDirector(), "Director should not be null");
                            assertNotNull(movie.getActors(), "Actors should not be null");
                            assertNotNull(movie.getPlot(), "Plot should not be null");
                        }));
    }
}
