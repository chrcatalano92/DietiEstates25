package com.demo.dietiestates25.data.dto;

import com.google.gson.annotations.SerializedName;

/**
 * DTO che mappa la risposta JSON per i suggerimenti delle città.
 * Usato da Retrofit/GSON.
 */
public final class CitySuggestion {

    @SerializedName("name")
    private final String name;

    @SerializedName("province")
    private final String province;


    /**
     * Costruttore per GSON e per i test.
     * Rende la classe immutabile e risolve i warning dell'IDE.
     */
    public CitySuggestion(String name, String province) {
        this.name = name;
        this.province = province;
    }

    // Getter

    public String getName() {
        return name;
    }

    public String getProvince() {
        return province;
    }
}