package com.demo.dietiestates25.support_classes.retrofit_utility;

import android.content.Context;

import androidx.annotation.NonNull;

import com.demo.dietiestates25.features.auth.util.SessionManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Interceptor di OkHttp con la Sola Responsabilità di aggiungere il token JWT
 * all'header di ogni richiesta API in uscita.
 */
public class AuthInterceptor implements Interceptor {

    private final SessionManager sessionManager;

    public AuthInterceptor(Context context) {
        // Usa l'application context per evitare memory leak
        this.sessionManager = new SessionManager(context.getApplicationContext());
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        // Ottiene la richiesta originale
        Request originalRequest = chain.request();

        String token = sessionManager.getAuthToken();

        if (token != null) {
            Request.Builder builder = originalRequest.newBuilder()
                    .header("Authorization", "Bearer " + token);

            Request newRequest = builder.build();

            return chain.proceed(newRequest);
        }

        return chain.proceed(originalRequest);
    }
}
