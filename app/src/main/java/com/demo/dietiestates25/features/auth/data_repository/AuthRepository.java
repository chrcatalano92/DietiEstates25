package com.demo.dietiestates25.features.auth.data_repository;

import com.demo.dietiestates25.data.dto.AuthResponse;
import com.demo.dietiestates25.data.dto.CitySuggestion;
import com.demo.dietiestates25.data.dto.LoginRequestDTO;
import com.demo.dietiestates25.data.dto.SignUpRequestDTO;

import java.util.List;

public interface AuthRepository {
    interface AuthCallback {
        void onSuccess(AuthResponse response);
        void onError(String message);
    }

    interface CitySearchCallback {
        void onSuccess(List<CitySuggestion> suggestions);
        void onError(String message);
    }

    void registerUser(SignUpRequestDTO signUpRequestDTO, AuthCallback callback);

    void loginUser(LoginRequestDTO loginRequestDTO, AuthCallback callback);

    void loginWithSocialToken(String idToken, AuthCallback callback);

    void searchCities(String query, CitySearchCallback callback);
}
