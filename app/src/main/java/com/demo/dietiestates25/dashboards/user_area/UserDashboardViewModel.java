package com.demo.dietiestates25.dashboards.user_area;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.demo.dietiestates25.features.auth.util.SessionManager;

/**
 * ViewModel per la UserDashboard.
 * La sua UNICA Responsabilità è RECUPERARE I DATI DELL'UTENTE LOGGATO
 * E ESPORLI ALLA View (Fragment) tramite LiveData.
 */
public class UserDashboardViewModel extends AndroidViewModel {

    private final SessionManager sessionManager;

    // LiveData per esporre i dati dell'Utente alla UI
    private final MutableLiveData<String> username = new MutableLiveData<>();
    public LiveData<String> getUsername() { return username; }

    private final MutableLiveData<String> email = new MutableLiveData<>();
    public LiveData<String> getEmail() { return email; }

    public UserDashboardViewModel(@NonNull Application application) {
        super(application);
        this.sessionManager = new SessionManager(application.getApplicationContext());
        loadUserData();
    }

    /**
     * Carica i Dati dell'Utente dalla sessione e li espone tramite LiveData.
     */
    private void loadUserData() {
        if (sessionManager.isLoggedIn()) {
            username.setValue(sessionManager.getUsername());
            email.setValue(sessionManager.getEmail());
        } else {
            username.setValue("Utente non autenticato");
            email.setValue("");
        }
    }
}
