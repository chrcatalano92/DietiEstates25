package com.demo.dietiestates25.features.auth.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.List;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * Classe Helper per la gestione della Sessione Utente.
 * SRP: La sua unica responsabilità è custodire, fornire e distruggere la chiave (il token).
 * Gestisce la Sessione Utente in modo sicuro.
 * Salva e recupera il Token JWT utilizzando EncryptedSharedPreferences per la massima sicurezza.
 */
public class SessionManager {

    private static final String PREF_NAME = "dieti_estates_auth_session";
    private static final String KEY_AUTH_TOKEN = "auth_token";
    private final SharedPreferences sharedPreferences;

    /**
     * Costruttore che inizializza le preferenze crittografate..
     */
    @SuppressWarnings("deprecation")
    public SessionManager(Context context) {

        try {
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            // Crea un File di preferenze crittografato
            this.sharedPreferences = EncryptedSharedPreferences.create(
                    context,
                    PREF_NAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            // In un'app di produzione, gestisci questo errore in modo più robusto
            throw new SessionManagerException("Impossibile creare EncryptedSharedPreferences", e);
        }
    }

    /**
     * Salva il Token JWT ricevuto dal backend dopo un login di successo.
     * Questo metodo è unico per tutti i ruoli.
     * @param token Il token JWT da salvare.
     */
    public void saveAuthToken(String token) {
        sharedPreferences.edit().putString(KEY_AUTH_TOKEN, token).apply();
    }

    /**
     * Recupera il Token JWT salvato
     * Verrà usato dall'AuthInterceptor per autenticare le chiamate API.
     * @return il token, o null se non esiste.
     */
    public String getAuthToken() {
        return sharedPreferences.getString(KEY_AUTH_TOKEN, null);
    }

    /**
     * Cancella la Sessione Utente (per esempio, LOGOUT).
     * Verrà usato dai metodi di Logout nelle varie Dashboard.
     */
    public void clearSession() {
        sharedPreferences.edit().remove(KEY_AUTH_TOKEN).apply();
    }

    /**
     * Controlla se l'utente è attualmente loggato.
     * Controlla l'esistenza e la validità (non scadenza) del token.
     * @return true se esiste un Token valido e non scaduto.
     */
    public boolean isLoggedIn() {
        String token = getAuthToken();
        if (token == null) {
            return false;
        }
        try {
            DecodedJWT jwt = JWT.decode(token);

            // Prendi la Data di Scadenza dal Token
            Date expiresAt = jwt.getExpiresAt();
            if (expiresAt == null) {
                return false;
            }

            // Calcola l'istante attuale meno la tolleranza di 10 secondi. Prova: System.currentTimeMillis() - (10 * 1000) oppure come sta giù.
            long nowWithLeeway = System.currentTimeMillis() + 10000; // 10 secondi di tolleranza

            // Il Token è valido se la sua Data di Scadenza è DOPO (after) l'istante attuale meno la tolleranza.
            return expiresAt.after(new Date(nowWithLeeway));
        } catch (Exception e) {
            // Se il Token è malformato, l'utente non è loggato.
            return false;
        }
    }

    /**
     * Recupera il Ruolo dell'utente loggato dal Token JWT.
     * @return La stringa del ruolo (esempio "ROLE_USER"), o null se non è loggato.
     */
    public String getUserRole() {
        String token = getAuthToken();
        if (token == null) {
            return null;
        }
        try {
            DecodedJWT jwt = JWT.decode(token);

            // Usa getClaim() per estrarre il claim "roles"
            List<String> roles = jwt.getClaim("roles").asList(String.class);

            // Restituisce il Primo Ruolo nella lista (solitamente ce n'è uno solo)
            if (roles != null && !roles.isEmpty()) {
                return roles.get(0);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Recupera lo username (l'identificatore univoco) dell'utente loggato dal token.
     */
    public String getUsername() {
        String token = getAuthToken();
        if (token == null) return null;
        try {
            DecodedJWT jwt = JWT.decode(token);
            // Il CLAIM "sub" (Subject) contiene l'identificatore univcoo
            return jwt.getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Recupera l'Email dal Token.
     */
    public String getEmail() {
        String token = getAuthToken();
        if (token == null) return null;
        try {
            DecodedJWT jwt = JWT.decode(token);
            // ssumendo che il Backend inserisca un claim "email"
            return jwt.getClaim("email").asString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Recupera l'ID numerico dell'utente dal Token JWT.
     * Restituisce -1 se non trovato o errore.
     * NOTA: Assicurarsi che il backend inserisca un claim chiamato "userId" o "id".
     */
    public long getUserId() {
        String token = getAuthToken();
        if (token == null) return -1;
        try {
            DecodedJWT jwt = JWT.decode(token);

            // PROVA 1: Cerca il claim "userId"
            Long userId = jwt.getClaim("userId").asLong();

            // PROVA 2: Se è nullo, cerca "id"
            if (userId == null) {
                userId = jwt.getClaim("id").asLong();
            }

            return (userId != null) ? userId : -1;
        } catch (Exception e) {
            return -1;
        }
    }
}
