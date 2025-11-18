package com.demo.dietiestates25.fragments_main_activity.sign_in;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.dashboards.administrator_area.AdminDashboardActivity;
import com.demo.dietiestates25.fragments_main_activity.ReservedAreaFragment;
import com.demo.dietiestates25.support_classes.dialog_utils.DialogManager;
import com.demo.dietiestates25.utils.ActivityUtils;
import com.demo.dietiestates25.utils.FragmentUtils;

public class AdminLoginFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_professional_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView title = view.findViewById(R.id.title);
        ImageView newImgV = view.findViewById(R.id.AccountSuppImg);
        ImageButton imgBtnBack = view.findViewById(R.id.imgBackBtn);
        Button btnPwdRec = view.findViewById(R.id.btnPwdRecovery);
        Button loginBtn = view.findViewById(R.id.loginBtn);

        DialogManager dialogManager = new DialogManager(requireContext());

        title.setText(R.string.reserv_entries1);
        newImgV.setImageResource(R.drawable.AdminCircle);
        FragmentUtils.imgBtnChangeFragment(imgBtnBack, getParentFragmentManager(), new ReservedAreaFragment());

        btnPwdRec.setOnClickListener(view1 -> dialogManager.showDialogPwdRecovery());

        loginBtn.setOnClickListener(view1 -> ActivityUtils.openActivity(requireContext(), AdminDashboardActivity.class));

        super.onViewCreated(view, savedInstanceState);
    }
}