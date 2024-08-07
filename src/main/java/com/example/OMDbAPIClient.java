package com.example;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;

public class OMDbAPIClient {
    private static final String API_KEY = "94fac0ef";
    private static final String BASE_URL = "http://www.omdbapi.com/";

    public static void main(String[] args) {
        String imdbId = "tt0841927";  // Primer IMDb ID
        try {
            String response = fetchMovieData(imdbId);
            if (response != null) {
                JSONObject jsonObject = new JSONObject(response);
                System.out.println("Title: " + jsonObject.getString("Title"));
                System.out.println("Year: " + jsonObject.getString("Year"));
                System.out.println("Director: " + jsonObject.getString("Director"));
                System.out.println("Actors: " + jsonObject.getString("Actors"));
                System.out.println("Plot: " + jsonObject.getString("Plot"));
            } else {
                System.out.println("Failed to retrieve data.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static String fetchMovieData(String imdbId) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "?i=" + imdbId + "&apikey=" + API_KEY))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return response.body();
        } else {
            return null;
        }
    }
}
