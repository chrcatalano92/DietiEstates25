package com.demo.dietiestates25.dashboards.administrator_area.additional_settings.usr_management.management_category_selection;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.demo.dietiestates25.support_classes.GenericDiffRecyclerViewCallback;

public class UserDiffRecyclerViewCallback extends GenericDiffRecyclerViewCallback<UsersListData> {
    @Override
    public boolean areItemsTheSame(@NonNull UsersListData oldItem, @NonNull UsersListData newItem) {
        return false;
    }

    @Override
    public boolean areContentsTheSame(@NonNull UsersListData oldItem, @NonNull UsersListData newItem) {
        return oldItem.equals(newItem);
    }

    @Override
    protected void populateChangePayload(@NonNull Bundle diffBundle, @NonNull UsersListData oldItem, @NonNull UsersListData newItem) {

    }
}
