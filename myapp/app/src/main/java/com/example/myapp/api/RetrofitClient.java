package com.example.myapp.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    // For physical device: use your computer's local IP address
    // For emulator: use 10.0.2.2
    // Your computer's IP: 172.20.10.4
    private static final String BASE_URL = "http://172.20.10.4:3000/";
    private static Retrofit retrofit = null;

    public static ApiService getApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }
}
