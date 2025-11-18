package com.demo.dietiestates25.dashboards.administrator_area.additional_settings.usr_management.management_category_selection;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.dietiestates25.databinding.UserManagementItemBinding;

import java.util.List;

public class UsersListAdapter extends ListAdapter<UsersListData, UsersListAdapter.UsersListViewHolder> {
    private final Context context;
    private OnClickItemCardViewSpecificUserListener specificUserListener;
    private int selectedPosition = RecyclerView.NO_POSITION;
    private static final DiffUtil.ItemCallback<UsersListData> DIFF_CALLBACK = new UserDiffRecyclerViewCallback();

    public interface OnClickItemCardViewSpecificUserListener {
        void onItemUserClick(UsersListData userData, int position);
    }

    public void setSpecificUserListener(OnClickItemCardViewSpecificUserListener listener) {
        this.specificUserListener = listener;
    }

    public UsersListAdapter(@NonNull Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    @NonNull
    @Override
    public UsersListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        UserManagementItemBinding binding = UserManagementItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new UsersListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersListViewHolder holder, int position, @NonNull List<Object> payloads) {
        if(payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        }
        else {
            Bundle diffBundle = (Bundle) payloads.get(0);
            UsersListData currentUser = getItem(position);

            if(currentUser != null) {
                for(String key : diffBundle.keySet()) {
                    if("PAYLOAD_STATUS_UPDATE".equals(key)) {
                    }
                }
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull UsersListViewHolder holder, int position) {
        UsersListData currentUser = getItem(position);

        if(currentUser != null) {
            holder.bind(currentUser, context, position == selectedPosition, specificUserListener, this);
        }
    }

    public void setSelectedPosition(int position) {
        int previousPosition = selectedPosition;

        selectedPosition = position;

        if(previousPosition != RecyclerView.NO_POSITION) {
            notifyItemChanged(previousPosition);
        }
        if(selectedPosition != RecyclerView.NO_POSITION) {
            notifyItemChanged(selectedPosition);
        }
    }

    public static class UsersListViewHolder extends RecyclerView.ViewHolder {
        private final UserManagementItemBinding binding;

        UsersListViewHolder(UserManagementItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(UsersListData currUsr, Context context, boolean isSelected, OnClickItemCardViewSpecificUserListener specificUserListener, UsersListAdapter adapter) {
            binding.userRule.setText(currUsr.getUserRule());
            binding.userFullName.setText(currUsr.getFullName());
            binding.professionalEmail.setText(currUsr.getProfessionalEmail());
            binding.userDateCreated.setText(currUsr.getUserDateCreated());
            binding.status.setText(currUsr.getStatus());

            itemView.setOnClickListener(view -> {
                if(specificUserListener != null) {
                    int currPosition = getAdapterPosition();

                    if(currPosition != RecyclerView.NO_POSITION) {
                        adapter.setSelectedPosition(currPosition);
                        specificUserListener.onItemUserClick(adapter.getItem(currPosition), currPosition);
                    }
                }
            });
        }
    }
}
