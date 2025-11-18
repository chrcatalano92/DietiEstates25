package com.demo.dietiestates25.dashboards.administrator_area.additional_settings.usr_management.management_category_selection;

import android.view.View;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.support_classes.fab_group_helper.FabGroupHelper;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class FabGroupUserManagementCategorySelectionSetup {
    private final View rootView;
    private final FabGroupHelper fabGroupHelper;
    private final FabGroupHelper.FabGroup leftFabGroup;
    private final FabGroupHelper.FabGroup rightFabGroup;

    public FabGroupUserManagementCategorySelectionSetup(View rootView) {
        this.rootView = rootView;
        this.fabGroupHelper = new FabGroupHelper();

        this.leftFabGroup = createLeftGroup();
        this.rightFabGroup = createRightGroup();
    }

    private FabGroupHelper.FabGroup createLeftGroup() {
        List<ExtendedFloatingActionButton> leftGroupFabs = new ArrayList<>();
        leftGroupFabs.add(rootView.findViewById(R.id.left_fab_suspend_user));
        leftGroupFabs.add(rootView.findViewById(R.id.left_fab_remove_user));

        return fabGroupHelper.createGroup(rootView.findViewById(R.id.main_left_fab_settings_account), leftGroupFabs);
    }

    private FabGroupHelper.FabGroup createRightGroup() {
        List<ExtendedFloatingActionButton> rightGroupFabs = new ArrayList<>();
        rightGroupFabs.add(rootView.findViewById(R.id.right_fab_user));
        rightGroupFabs.add(rootView.findViewById(R.id.right_fab_admin));
        rightGroupFabs.add(rootView.findViewById(R.id.right_fab_real_estate_a_m));
        rightGroupFabs.add(rootView.findViewById(R.id.right_fab_real_estate_agent));

        return fabGroupHelper.createGroup(rootView.findViewById(R.id.main_right_fab_set_role), rightGroupFabs);
    }

    public void leftFabGroupListener() {
        leftFabGroup.setOnFabGroupStateChangeListener(new FabGroupHelper.OnFabGroupStateChangeListener() {
            @Override
            public void onGroupExpanded(FabGroupHelper.FabGroup group) {
                // Show text custom on click single fab into group
            }

            @Override
            public void onGroupClosed(FabGroupHelper.FabGroup group) {
                // Not used
            }
        });
    }

    public void rightFabGroupListener() {
        rightFabGroup.setOnFabGroupStateChangeListener(new FabGroupHelper.OnFabGroupStateChangeListener() {
            @Override
            public void onGroupExpanded(FabGroupHelper.FabGroup group) {
                // Show text custom on click single fab into group
            }

            @Override
            public void onGroupClosed(FabGroupHelper.FabGroup group) {
                // Not used
            }
        });
    }
}
