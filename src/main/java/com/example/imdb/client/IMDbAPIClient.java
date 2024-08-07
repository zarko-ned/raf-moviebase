package com.example.imdb.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.example.imdb.config.Config;
import com.example.imdb.exception.MovieNotFoundException;
import com.example.imdb.model.Movie;
import org.json.JSONException;
import org.json.JSONObject;

public class IMDbAPIClient {

    public Movie fetchMovieData(String imdbId) throws IOException, InterruptedException, MovieNotFoundException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Config.BASE_URL + "?i=" + imdbId + "&apikey=" + Config.API_KEY))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return parseMovieData(response.body(), imdbId);
        } else {
            throw new IOException("Failed to retrieve data from OMDb API");
        }
    }

    private Movie parseMovieData(String responseBody, String imdbId) throws MovieNotFoundException {
        try {
            JSONObject jsonObject = new JSONObject(responseBody);

            // Check if the response indicates an error
            if (jsonObject.optString("Response").equals("False")) {
                String errorMessage = jsonObject.optString("Error");

                // Extend the error handling to cover additional cases
                if (errorMessage.contains("Incorrect IMDb ID")) {
                    throw new MovieNotFoundException("Incorrect IMDb ID: " + imdbId);
                } else if (errorMessage.contains("Conversion from string")) {
                    // Handle specific error message from API response
                    throw new MovieNotFoundException("Invalid IMDb ID format: " + imdbId);
                } else {
                    throw new MovieNotFoundException("Movie not found: " + errorMessage);
                }
            }

            return new Movie(
                    imdbId,
                    jsonObject.optString("Title"),
                    jsonObject.optString("Year"),
                    jsonObject.optString("Director"),
                    jsonObject.optString("Actors"),
                    jsonObject.optString("Plot")
            );
        } catch (JSONException e) {
            throw new MovieNotFoundException("Error parsing movie data: " + e.getMessage());
        }
    }
}