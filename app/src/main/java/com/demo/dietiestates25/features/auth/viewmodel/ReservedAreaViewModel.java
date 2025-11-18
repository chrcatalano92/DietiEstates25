package com.demo.dietiestates25.features.auth.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.demo.dietiestates25.R;
import com.demo.dietiestates25.data.model.Role;
import com.demo.dietiestates25.support_classes.arch.Event;


public class ReservedAreaViewModel extends ViewModel {

    // Contiene il ruolo attualmente selezionato nello Spinner. Inizializzato a null.
    private final MutableLiveData<Role> selectedRole = new MutableLiveData<>(null);
    public LiveData<Role> selectedRole() { return selectedRole; }


    // Evento per dire al Fragment di navigare. Contiene il Ruolo da passare.
    private final MutableLiveData<Event<Role>> navigateToLogin = new MutableLiveData<>();
    public LiveData<Event<Role>> getNavigateToLogin() { return navigateToLogin; }


    // Evento per Mostrare un Errore se non è stato selezionato nessun ruolo.
    private final MutableLiveData<Event<Integer>> showRoleError = new MutableLiveData<>();
    // La sua responsabilità è comunicare al Fragment che l'utente ha cliccato "LOGIN" senza aver selezionato un ruolo.
    // Dobbiamo aggiungere l'Observer corrispondente nel ReserveredAreaFragment.
    public LiveData<Event<Integer>> getShowRoleError() { return showRoleError; }

    /**
     * Chiamato dal Fragment quando l'utente seleziona una voce dallo Spinner.
     * Questa è la logica che prima era in 'manageSpinner' e 'getPosSpinner'.
     */
    public void onRoleSelected(int position) {
        // Ho usato un switch per Mappare la posizione all'Enum.
        switch (position) {
            case 1: selectedRole.setValue(Role.ADMIN); break;
            case 2: selectedRole.setValue(Role.AGENCY_MANAGER); break;
            case 3: selectedRole.setValue(Role.AGENT); break;
            default: selectedRole.setValue(null); break;
        }
    }

    /**
     * Chiamato dal Fragment quando l'utente clicca il pulsante di login.
     * Questa è la logica che prima era in 'manageAccount'.
     */
    public void onLoginButtonClicked() {
        Role currentRole = selectedRole.getValue();
        if (currentRole != null) {
            navigateToLogin.setValue(new Event<>(currentRole));
        } else {
            // Emetti l'ID della stringa di errore
            showRoleError.setValue(new Event<>(R.string.error_spinner));
        }
    }
}
