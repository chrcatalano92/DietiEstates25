package com.demo.dietiestates25.dashboards.administrator_area.additional_settings.usr_management.management_category_selection.repository;

import android.os.Handler;
import android.os.Looper;

import com.demo.dietiestates25.dashboards.administrator_area.additional_settings.usr_management.management_category_selection.UsersListData;

import java.util.ArrayList;
import java.util.List;

public class UserManagementRepositoryImpl implements UserManagementRepository {
    @Override
    public void fetchUsersByRole(String roleType, LoadUsersCallback callback) {
        simulateServerDelay(roleType, callback);
    }

    private void simulateServerDelay(String roleType, LoadUsersCallback callback) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            callback.onUsersLoaded(addUsersList(roleType));
            callback.onError("Errore simulato di rete. Impossibile raggiungere il server.");
        }, 1500);
    }

    private List<UsersListData> addUsersList(String roleType) {
        List<UsersListData> usersList = new ArrayList<>();

        switch(roleType) {
            case "AM":
                usersList.add(new UsersListData("AM", "Mario Rossi", "m.rossi@agencymanager.dietiestates25.com", "12/04/2025 - 23:00", "Attivo"));
                usersList.add(new UsersListData("AM", "Laura Bianchi", "l.bianchi@agencymanager.dietiestates25.com", "17/03/2025 - 12:22", "Sospeso"));
                break;
            case "A":
                usersList.add(new UsersListData("M", "Luca Verdi", "l.verdi@agent.dietiestates25.com", "11/06/2025 - 22:22", "Inattivo"));
                break;
            case "C":
                usersList.add(new UsersListData("U", "Anna Neri", "a.neri@user.dietiestates25.com", "11/02/2025 - 22:11", "Attivo"));
                break;
            default:
        }

        return usersList;
    }
}
