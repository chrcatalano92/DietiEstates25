package com.demo.dietiestates25.features.auth.util;

/**
 * Eccezione custom per errori durante l'inizializzazione del SessionManager.
 */
public class SessionManagerException extends RuntimeException {
    public SessionManagerException(String message, Throwable cause) {
        super(message, cause);
    }
}
