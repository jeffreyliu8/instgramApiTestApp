package com.example.jeff.jeff23andme.endpoints;


public abstract class BaseEndpoint {

    protected static final String BASE_URL = "https://api.instagram.com/v1/";

    protected final String accessToken;


    protected BaseEndpoint(final String accessToken) {
        this.accessToken = accessToken;
    }
}