package com.example.myapp.api;

import android.content.Context;
import com.example.myapp.utils.SessionManager;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private SessionManager sessionManager;

    public AuthInterceptor(Context context) {
        this.sessionManager = new SessionManager(context);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        
        // If user is logged in, add token to request
        if (sessionManager.isLoggedIn()) {
            String token = sessionManager.getToken();
            if (token != null) {
                Request newRequest = originalRequest.newBuilder()
                        .header("Authorization", "Bearer " + token)
                        .build();
                return chain.proceed(newRequest);
            }
        }
        
        return chain.proceed(originalRequest);
    }
}

