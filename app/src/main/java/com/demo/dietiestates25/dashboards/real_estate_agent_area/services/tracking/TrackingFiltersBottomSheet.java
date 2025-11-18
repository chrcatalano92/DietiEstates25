package com.demo.dietiestates25.dashboards.real_estate_agent_area.services.tracking;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.databinding.BottomSheetTrackingFiltersBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class TrackingFiltersBottomSheet extends BottomSheetDialogFragment {
    public static final String TAG = "TrackingFiltersBottomSheet";
    private BottomSheetTrackingFiltersBinding binding;
    private FilterListener listener;

    public interface FilterListener {
        void onTrackingFiltersApplied(String sortBy, boolean isAscending);
    }

    public static TrackingFiltersBottomSheet newInstance() {
        return new TrackingFiltersBottomSheet();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof FilterListener) {
            listener = (FilterListener) context;
        }
        else if(getParentFragment() instanceof FilterListener) {
            listener = (FilterListener) getParentFragment();
        }
        else {
            throw new MissingFilterListenerException(context.getClass().getName() + "Must implement FilterListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomSheetTrackingFiltersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.sortDirectionToggleGroup.check(R.id.sort_desc);

        binding.applyFiltersButton.setOnClickListener(v -> {
            String sortBy = getSelectedSortBy();
            boolean isAscending = binding.sortDirectionToggleGroup.getCheckedButtonId() == R.id.sort_asc;

            if (listener != null) {
                listener.onTrackingFiltersApplied(sortBy, isAscending);
            }
            dismiss();
        });
    }

    private String getSelectedSortBy() {
        int checkedId = binding.sortOptionsGroup.getCheckedRadioButtonId();
        String dateSort = "date";

        if(checkedId == R.id.sort_by_client_id) {
            return "client_id";
        }
        else if(checkedId == R.id.sort_by_name) {
            return "name";
        }
        else if(checkedId == R.id.sort_by_surname) {
            return "surname";
        }
        else if(checkedId == R.id.sort_by_date) {
            return dateSort;
        }
        return dateSort;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
