package com.demo.dietiestates25.data.dto;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;

/**
 * DTO (POJO) per il Frontend.
 * La sua UNICA RESPONSABILITÀ (SRP) è mappare i dati per la richiesta
 * di registrazione. I nomi dei campi (@SerializedName) DEVONO
 * corrispondere al JSON atteso dal backend (SignUpRequestDTO.java).
 */
public class SignUpRequestDTO {

    @SerializedName("firstName")
    private final String firstName;

    @SerializedName("lastName")
    private final String lastName;

    @SerializedName("username")
    private final String username;

    @SerializedName("email")
    private final String email;

    @SerializedName("password")
    private final String password;

    @SerializedName("dateOfBirth")
    private final String dateOfBirth;

    @SerializedName("placeOfBirth")
    private final String placeOfBirth;

    // Costruttore che l'AuthViewModel (Frontend) userà
    public SignUpRequestDTO(String firstName, String lastName, String username, String email, String password, LocalDate dateOfBirth, String placeOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.dateOfBirth = (dateOfBirth != null) ? dateOfBirth.toString() : null;
        this.placeOfBirth = placeOfBirth;
    }
    
    /* GSON ha bisogno di questi metodi (o che i campi siano pubblici) per serializzare l'oggetto in JSON. */
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getDateOfBirth() { return dateOfBirth; }
    public String getPlaceOfBirth() { return placeOfBirth; }
}