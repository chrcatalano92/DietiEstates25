package com.demo.dietiestates25.dashboards.administrator_area.additional_settings.usr_management.management_category_selection;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.demo.dietiestates25.dashboards.administrator_area.additional_settings.usr_management.management_category_selection.repository.UserManagementRepository;
import com.demo.dietiestates25.dashboards.administrator_area.additional_settings.usr_management.management_category_selection.repository.UserManagementRepositoryImpl;

import java.util.List;
import java.util.Objects;

public class UserManagementViewModel extends ViewModel {
    private final UserManagementRepository repository;
    private final MutableLiveData<List<UsersListData>> usersListLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoadingLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorEvent = new MutableLiveData<>();
    private final MutableLiveData<String> selectedUser = new MutableLiveData<>(null);

    public UserManagementViewModel() {
        this.repository = new UserManagementRepositoryImpl();
    }

    public MutableLiveData<List<UsersListData>> getUsersListLiveData() {
        return usersListLiveData;
    }

    public MutableLiveData<Boolean> getIsLoadingLiveData() {
        return isLoadingLiveData;
    }

    public MutableLiveData<String> getErrorEvent() {
        return errorEvent;
    }

    public MutableLiveData<String> getSelectedUser() {
        return selectedUser;
    }

    public void loadUserByRole(String roleType) {
        isLoadingLiveData.setValue(true);
        selectedUser.setValue(null);

        repository.fetchUsersByRole(roleType, new UserManagementRepository.LoadUsersCallback() {
            @Override
            public void onUsersLoaded(List<UsersListData> users) {
                usersListLiveData.postValue(users);
                isLoadingLiveData.postValue(false);
            }

            @Override
            public void onError(String message) {
//                errorEvent.postValue(new Event<>(message));
                isLoadingLiveData.postValue(false);
            }
        });
    }

    public void onUserSelected(UsersListData usersList) {
        if(selectedUser.getValue() != null && Objects.equals(selectedUser.getValue(), usersList.getFullName())) {
            selectedUser.setValue(null);
        }
        else {
            selectedUser.setValue(usersList.getFullName());
        }
    }
}
