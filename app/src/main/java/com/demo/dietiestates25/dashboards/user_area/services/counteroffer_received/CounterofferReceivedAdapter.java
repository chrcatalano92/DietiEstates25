package com.demo.dietiestates25.dashboards.user_area.services.counteroffer_received;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.databinding.ItemTrackingCounterofferReceivedBinding;
import com.demo.dietiestates25.support_classes.Utility;

public class CounterofferReceivedAdapter extends ListAdapter<ClientCounterofferData, CounterofferReceivedAdapter.CounterofferViewHolder> {
    private OnRespondClickListener onRespondClickListener;
    private static final DiffUtil.ItemCallback<ClientCounterofferData> DIFF_CALLBACK = new CounterofferDiffRecyclerViewCallback();

    public interface OnRespondClickListener {
        void onRespondClick(ClientCounterofferData item);
    }

    public void setOnRespondClickListener(OnRespondClickListener listener) {
        this.onRespondClickListener = listener;
    }

    public CounterofferReceivedAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public CounterofferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTrackingCounterofferReceivedBinding binding = ItemTrackingCounterofferReceivedBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new CounterofferViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CounterofferViewHolder holder, int position) {
        ClientCounterofferData item = getItem(position);

        if(item != null) { holder.bind(item, onRespondClickListener); }
    }

    public static class CounterofferViewHolder extends RecyclerView.ViewHolder {
        private final ItemTrackingCounterofferReceivedBinding binding;

        CounterofferViewHolder(ItemTrackingCounterofferReceivedBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(ClientCounterofferData item, OnRespondClickListener listener) {
            Utility utility = new Utility(itemView.getContext());
            utility.loadImgFromUrl(binding.propertyImg, item.getPropertyImgUrl(), R.drawable.no_img);

            binding.propertyTitle.setText(item.getPropertyTitle());
            binding.offerAmountValue.setText(Utility.formatPrice(item.getYourOfferAmount()));
            binding.counterofferAmountValue.setText(Utility.formatPrice(item.getAgentCounterofferAmount()));

            binding.respondSelection.setOnClickListener(view -> listener.onRespondClick(item));
        }
    }
}
