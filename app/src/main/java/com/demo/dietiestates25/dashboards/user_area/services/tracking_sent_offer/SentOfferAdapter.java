package com.demo.dietiestates25.dashboards.user_area.services.tracking_sent_offer;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.databinding.ItemTrackingSentOfferBinding;
import com.demo.dietiestates25.support_classes.Utility;

public class SentOfferAdapter extends ListAdapter<ClientSentOfferData, SentOfferAdapter.OfferViewHolder> {
    private static final DiffUtil.ItemCallback<ClientSentOfferData> DIFF_CALLBACK = new OfferDiffRecyclerViewCallback();

    public SentOfferAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTrackingSentOfferBinding binding = ItemTrackingSentOfferBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new OfferViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferViewHolder holder, int position) {
        ClientSentOfferData item = getItem(position);

        holder.bind(item);
    }

    public static class OfferViewHolder extends RecyclerView.ViewHolder {
        private final ItemTrackingSentOfferBinding binding;

        OfferViewHolder(ItemTrackingSentOfferBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(ClientSentOfferData item) {
            Utility utility = new Utility(itemView.getContext());
            utility.loadImgFromUrl(binding.propertyImg, item.getPropertyImgUrl(), R.drawable.no_img);

            binding.propertyTitle.setText(item.getPropertyTitle());
            binding.offerAmountValue.setText(Utility.formatPrice(item.getMyOfferAmount()));
            binding.offerStatusChip.setText(item.getOfferStatus());
        }
    }
}
