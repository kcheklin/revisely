package com.example.myapp.api;

import android.content.Context;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    // For physical device: use your computer's local IP address
    // For emulator: use 10.0.2.2
    // Your computer's IP: 172.20.10.4
    private static final String BASE_URL = "http://172.20.10.4:3000/";
    private static Retrofit retrofit = null;
    private static Retrofit authenticatedRetrofit = null;

    // Get API service without authentication (for login/signup)
    public static ApiService getApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }

    // Get API service with authentication (for protected endpoints)
    public static ApiService getAuthenticatedApiService(Context context) {
        if (authenticatedRetrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor(context))
                    .build();

            authenticatedRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return authenticatedRetrofit.create(ApiService.class);
    }

    // Reset authenticated client (e.g., after logout)
    public static void resetAuthenticatedClient() {
        authenticatedRetrofit = null;
    }
}
