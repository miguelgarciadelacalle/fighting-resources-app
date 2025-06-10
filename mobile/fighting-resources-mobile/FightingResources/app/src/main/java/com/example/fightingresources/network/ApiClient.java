package com.example.fightingresources.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit = null;

    public static Retrofit getInstance(String baseUrl) {
        if (baseUrl == null || baseUrl.isEmpty()) {
            baseUrl = "http://192.168.56.101:8080/api";
        }

        if (retrofit == null || !retrofit.baseUrl().toString().equals(baseUrl.endsWith("/") ? baseUrl : baseUrl + "/")) {
            retrofit = new Retrofit.Builder().baseUrl(baseUrl.endsWith("/") ? baseUrl : baseUrl + "/").addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }

}