package com.example.imdb.exception;

public class MovieNotFoundException extends Exception {
    public MovieNotFoundException(String message) {
        super(message);
    }
}