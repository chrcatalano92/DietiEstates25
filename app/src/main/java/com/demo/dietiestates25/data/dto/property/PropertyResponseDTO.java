package com.demo.dietiestates25.data.dto.property;

import com.google.gson.annotations.SerializedName;

/**
 * DTO (POJO) per mappare la risposta JSON dal Backend dopo la creazione
 * di un immobile.
 */
public class PropertyResponseDTO {

    @SerializedName("id")
    private Long id;

    @SerializedName("title")
    private String title;

    @SerializedName("message")
    private String message;

    // Getters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getMessage() { return message; }
}
