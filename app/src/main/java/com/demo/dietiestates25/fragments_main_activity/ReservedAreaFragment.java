package com.demo.dietiestates25.fragments_main_activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.demo.dietiestates25.R;
import com.demo.dietiestates25.data.model.Role;
import com.demo.dietiestates25.databinding.FragmentReservAreaBinding;
import com.demo.dietiestates25.features.auth.ui.LoginProfessionalFragment;
import com.demo.dietiestates25.features.auth.viewmodel.ReservedAreaViewModel;
import com.demo.dietiestates25.support_classes.dialog_utils.DialogManager;
import com.demo.dietiestates25.utils.FragmentUtils;

public class ReservedAreaFragment extends Fragment {

    private FragmentReservAreaBinding binding;
    private ReservedAreaViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReservAreaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ReservedAreaViewModel.class);
        DialogManager dialogManager = new DialogManager(requireContext());

        binding.headerLayout.title.setText(R.string.reserved);

        setupListeners();
        observeViewModel(dialogManager);

    }

    private void setupListeners() {
        binding.spinAreaLogin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewModel.onRoleSelected(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        binding.btnRALogin.setOnClickListener(view -> viewModel.onLoginButtonClicked());
    }

    private void observeViewModel(DialogManager dialogManager) {
        // Il Fragment osserva l'ORDINE di Navigazione
        viewModel.selectedRole().observe(getViewLifecycleOwner(), role -> {
            if (role != null) {
                String message = getString(R.string.role_selected_message, role.name().replace("_", " "));
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getNavigateToLogin().observe(getViewLifecycleOwner(), event ->{
            Role roleToPass = event.getContentIfNotHandled();
            if (roleToPass != null) {
                // E ESEGUE L'AZIONE DI NAVIGAZIONE
                LoginProfessionalFragment loginFragment = LoginProfessionalFragment.newInstance(roleToPass);
                FragmentUtils.replaceFragment(getParentFragmentManager(), loginFragment);
            }
        });

        viewModel.getShowRoleError().observe(getViewLifecycleOwner(), event -> {
            Integer errorId = event.getContentIfNotHandled();
            if (errorId != null) {
                dialogManager.showErrorDialog("ReservedAreaFragment", getString(errorId));
            }
        });
    }
}