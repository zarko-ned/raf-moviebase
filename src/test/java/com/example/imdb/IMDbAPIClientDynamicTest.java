package com.example.imdb;

import com.example.imdb.client.IMDbAPIClient;
import com.example.imdb.exception.MovieNotFoundException;
import com.example.imdb.model.Movie;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;


import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class IMDbAPIClientDynamicTest {

    private final IMDbAPIClient client = new IMDbAPIClient();

    @TestFactory
    Stream<DynamicTest> testFetchMovieData() {
        // Podaci za testiranje
        Collection<Object[]> testData = Arrays.asList(new Object[][]{
                {"tt0111161", "The Shawshank Redemption", "1994", "Frank Darabont", "Tim Robbins, Morgan Freeman, Bob Gunton", "Over the course of several years, two convicts form a friendship, seeking consolation and, eventually, redemption through basic compassion."},
                {"tt0068646", "The Godfather", "1972", "Francis Ford Coppola", "Marlon Brando, Al Pacino, James Caan", "Don Vito Corleone, head of a mafia family, decides to hand over his empire to his youngest son, Michael. However, his decision unintentionally puts the lives of his loved ones in grave danger."}

        });

        return testData.stream().map(data -> DynamicTest.dynamicTest(
                "Testing IMDb ID: " + data[0],
                () -> {
                    String imdbId = (String) data[0];
                    String expectedTitle = (String) data[1];
                    String expectedYear = (String) data[2];
                    String expectedDirector = (String) data[3];
                    String expectedActors = (String) data[4];
                    String expectedPlot = (String) data[5];

                    try {
                        Movie movie = client.fetchMovieData(imdbId);
                        assertNotNull(movie);
                        assertEquals(expectedTitle, movie.getTitle());
                        assertEquals(expectedYear, movie.getYear());
                        assertEquals(expectedDirector, movie.getDirector());
                        assertEquals(expectedActors, movie.getActors());
                        assertEquals(expectedPlot, movie.getPlot());
                    } catch (IOException | InterruptedException | MovieNotFoundException e) {
                        fail("Exception thrown during API call: " + e.getMessage());
                    }
                }
        ));
    }

    @TestFactory
    Stream<DynamicTest> testFetchMovieDataWithInvalidId() {
        // Invalid IMDb IDs for testing
        Collection<String> invalidIds = Arrays.asList(
                "invalidId1",
                "blablabla",
                "blablabla"
        );

        return invalidIds.stream().map(id -> DynamicTest.dynamicTest(
                "Testing invalid IMDb ID: " + id,
                () -> {

                    assertThrows(MovieNotFoundException.class, () -> {
                        Movie data = client.fetchMovieData(id);
                        System.out.println(data);
                    });
                }
        ));
    }
}
