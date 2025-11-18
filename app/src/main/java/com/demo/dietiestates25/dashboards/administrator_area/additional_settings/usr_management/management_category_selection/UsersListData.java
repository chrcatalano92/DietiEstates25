package com.demo.dietiestates25.dashboards.administrator_area.additional_settings.usr_management.management_category_selection;

import androidx.annotation.Nullable;

import java.util.Objects;

public class UsersListData {
    private final String userRule;
    private final String fullName;
    private final String professionalEmail;
    private final String userDateCreated;
    private final String status;

    public UsersListData(String userRule, String fullName, String professionalEmail, String userDateCreated, String status) {
        this.userRule = userRule;
        this.fullName = fullName;
        this.professionalEmail = professionalEmail;
        this.userDateCreated = userDateCreated;
        this.status = status;
    }

    public String getUserRule() { return userRule; }

    public String getFullName() { return fullName; }

    public String getProfessionalEmail() { return professionalEmail; }

    public String getUserDateCreated() { return userDateCreated; }

    public String getStatus() { return status; }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;

        UsersListData that = (UsersListData) obj;

        return Objects.equals(userRule, that.userRule) &&
                Objects.equals(fullName, that.fullName) &&
                Objects.equals(professionalEmail, that.professionalEmail) &&
                Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userRule, fullName, professionalEmail, status);
    }
}
