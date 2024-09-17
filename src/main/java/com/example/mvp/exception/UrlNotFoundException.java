package com.example.mvp.exception;

public class UrlNotFoundException extends RuntimeException {
    public UrlNotFoundException() {
        super("Short url not found");
    }
}