package com.demo.dietiestates25.data.dto;

import com.google.gson.annotations.SerializedName;

/**
 * DTO (POJO) immutabile per inviare un Token ID (es. da Google/Facebook)
 * al backend per l'autenticazione social.
 * GSON serializza questa classe in un JSON.
 */
public final class SocialLoginRequest {

    /**
     * Il campo è 'final' perché il DTO è immutabile (buona pratica).
     * GSON userà reflection per serializzarlo.
     */
    @SerializedName("idToken")
    private final String idToken;

    public SocialLoginRequest(String idToken) {
        this.idToken = idToken;
    }

    /**
     * Aggiungere un getter è una buona pratica (Clean Code)
     * e risolve il warning "field is never accessed".
     */
    public String getIdToken() {
        return idToken;
    }
}