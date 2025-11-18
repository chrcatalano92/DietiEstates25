package com.demo.dietiestates25.data.dto;

import com.google.gson.annotations.SerializedName;

/**
 * DTO per inviare le credenziali di Login al Backend.
 */
public class LoginRequestDTO {

    @SerializedName("username")
    private final String username;

    @SerializedName("password")
    private final String password;

    public LoginRequestDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Aggiungere i Getter
    public String getUsername() { return username; }
    public String getPassword() { return password; }
}