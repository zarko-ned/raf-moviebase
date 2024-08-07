package com.example.imdb.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.example.imdb.config.Config;
import com.example.imdb.exception.MovieNotFoundException;
import com.example.imdb.model.Movie;
import org.json.JSONObject;

public class IMDbAPIClient {

    public Movie fetchMovieData(String imdbId) throws IOException, InterruptedException, MovieNotFoundException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Config.BASE_URL + "?i=" + imdbId + "&apikey=" + Config.API_KEY))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            Movie movie = parseMovieData(response.body());
            if (movie == null) {
                throw new MovieNotFoundException("Movie not found for IMDb ID: " + imdbId);
            }
            return movie;
        } else {
            throw new IOException("Failed to retrieve data from OMDb API");
        }
    }
    private Movie parseMovieData(String responseBody) {
        JSONObject jsonObject = new JSONObject(responseBody);
        return new Movie(
                jsonObject.optString("Title"),
                jsonObject.optString("Year"),
                jsonObject.optString("Director"),
                jsonObject.optString("Actors"),
                jsonObject.optString("Plot")
        );
    }
}