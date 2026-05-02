package com.example.myapp.api;

import android.content.Context;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "http://10.131.156.144:3000/";

    private static Retrofit retrofit = null;
    private static Retrofit authenticatedRetrofit = null;

    public static ApiService getApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }

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
    public static void resetAuthenticatedClient() {
        retrofit = null;
        authenticatedRetrofit = null;
    }
}