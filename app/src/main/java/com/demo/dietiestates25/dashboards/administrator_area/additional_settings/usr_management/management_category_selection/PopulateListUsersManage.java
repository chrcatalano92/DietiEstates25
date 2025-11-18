package com.demo.dietiestates25.dashboards.administrator_area.additional_settings.usr_management.management_category_selection;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.dashboards.administrator_area.additional_settings.usr_management.user_management.UserManagementContentKey;

public class PopulateListUsersManage implements UsersListAdapter.OnClickItemCardViewSpecificUserListener {
    private final Context context;
    private final String choiceCategory;
    private final RecyclerView recViewUsrMCategory;
    private UsersListAdapter usersListAdapter;
    private final TextView status;
    private String currUser;

    public PopulateListUsersManage(Context context, View rootView, String choiceCategory) {
        this.context = context;
        this.choiceCategory = choiceCategory;

        this.recViewUsrMCategory = rootView.findViewById(R.id.recViewUserManagementCategory);
        this.status = rootView.findViewById(R.id.status);

        recViewInit();
    }

    @Override
    public void onItemUserClick(UsersListData userData, int position) {
        usersListAdapter.setSelectedPosition(position);

        onCardActionClick(userData);
    }

    private void onCardActionClick(UsersListData usersListData) {
        currUser = usersListData.getFullName();
    }

    private void recViewInit() {
        recViewUsrMCategory.setNestedScrollingEnabled(false);
        recViewUsrMCategory.setLayoutManager(new LinearLayoutManager(context));
    }

    public void populationChoiceCategoryManage() {
        UserManagementContentKey contentKey = new UserManagementContentKey();

        usersListAdapter = new UsersListAdapter(context);
        usersListAdapter.setSpecificUserListener(this);

        if(choiceCategory.equals(contentKey.getChoiceOne())) {

        }
        else if(choiceCategory.equals(contentKey.getChoiceTwo())) {

        }
        else {

        }
    }
}
