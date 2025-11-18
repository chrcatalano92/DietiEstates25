package com.demo.dietiestates25.data.source_api_auth;

import com.demo.dietiestates25.data.dto.AuthResponse;
import com.demo.dietiestates25.data.dto.CitySuggestion;
import com.demo.dietiestates25.data.dto.LoginRequestDTO;
import com.demo.dietiestates25.data.dto.SignUpRequestDTO;
import com.demo.dietiestates25.data.dto.SocialLoginRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthApiService {
    @POST("/api/auth/register")
    Call<AuthResponse> register(@Body SignUpRequestDTO signUpRequestDTO);


    @POST("/api/auth/login")
    Call<AuthResponse> login(@Body LoginRequestDTO loginRequestDTO);


    @POST("/api/auth/login-social")
    Call<AuthResponse> loginWithSocial(@Body SocialLoginRequest request);

    /**
     * Definisce il contratto per la ricerca delle città.
     */
    @GET("/api/locations/search")
    Call<List<CitySuggestion>> searchCities(@Query("query") String query);
}