package com.demo.dietiestates25.dashboards.administrator_area.additional_settings.usr_management.management_category_selection.repository;

import com.demo.dietiestates25.dashboards.administrator_area.additional_settings.usr_management.management_category_selection.UsersListData;

import java.util.List;

public interface UserManagementRepository {
    interface LoadUsersCallback {
        void onUsersLoaded(List<UsersListData> users);
        void onError(String message);
    }

    void fetchUsersByRole(String roleType, LoadUsersCallback callback);
}
