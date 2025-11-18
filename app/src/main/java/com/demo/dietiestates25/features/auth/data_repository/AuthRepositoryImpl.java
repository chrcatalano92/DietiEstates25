package com.demo.dietiestates25.features.auth.data_repository;

import androidx.annotation.NonNull;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.data.dto.AuthResponse;
import com.demo.dietiestates25.data.dto.CitySuggestion;
import com.demo.dietiestates25.data.dto.LoginRequestDTO;
import com.demo.dietiestates25.data.dto.SignUpRequestDTO;
import com.demo.dietiestates25.data.dto.SocialLoginRequest;
import com.demo.dietiestates25.data.source_api_auth.AuthApiService;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepositoryImpl implements AuthRepository {
    private final AuthApiService apiService;

    public AuthRepositoryImpl(AuthApiService apiService) {
        this.apiService = apiService;
    }


    @Override
    public void registerUser(SignUpRequestDTO signUpRequestDTO, AuthRepository.AuthCallback callback) {
        apiService.register(signUpRequestDTO).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<AuthResponse> call, @NonNull Response<AuthResponse> response) {
                if(response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                }
                else {
                    callback.onError(R.string.registration_error_generic + " " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<AuthResponse> call, @NonNull Throwable t) {
                callback.onError("Errore di connessione" + t.getMessage());
            }
        });
    }

    @Override
    public void loginUser(LoginRequestDTO loginRequestDTO, AuthCallback callback) {
        apiService.login(loginRequestDTO).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<AuthResponse> call, @NonNull Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    // Gestisce errori HTTP specifici
                    if (response.code() == 401) {
                        callback.onError("Credenziali non valid. Riprova.");
                    } else {
                        callback.onError("Errore del server (Codice: " + response.code() + ")");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AuthResponse> call, @NonNull Throwable t) {
                // Gestisce errori di rete
                if (t instanceof IOException) {
                    callback.onError("Errore di connessione. Controlla la tua rete e che il server sia online..");
                }
                callback.onError("Errore sconosciuto: " + t.getMessage());
            }
        });
    }

    @Override
    public void loginWithSocialToken(String idToken, AuthCallback callback) {
        apiService.loginWithSocial(new SocialLoginRequest(idToken)).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<AuthResponse> call, @NonNull Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Autenticazione Social fallita (Codice: " + response.code() + ")");
                }
            }

            @Override
            public void onFailure(@NonNull Call<AuthResponse> call, @NonNull Throwable t) {
                callback.onError("Errore di connessione durante il login social: " + t.getMessage());
            }
        });
    }


    @Override
    public void searchCities(String query, CitySearchCallback callback) {
        apiService.searchCities(query).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<CitySuggestion>> call, @NonNull Response<List<CitySuggestion>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                }
                else {
                    callback.onError("Errore nella ricerca delle città (Codice: " + response.code() + ")");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<CitySuggestion>> call, @NonNull Throwable t) {
                callback.onError("Errore di rete durante la ricerca delle città: " + t.getMessage());
            }
        });
    }
}