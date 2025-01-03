package org.example;

public class BaseConfig {
    public static final String BASE_URL = "http://localhost:7081";

    public static String getEndpointUrl(String endpoint) {
        return BASE_URL + endpoint;
    }
}