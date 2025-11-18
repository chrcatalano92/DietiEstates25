package com.demo.dietiestates25.dashboards.real_estate_agent_area.services.tracking;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.databinding.ItemTrackedOfferBinding;
import com.demo.dietiestates25.utils.FormatUtils;
import com.demo.dietiestates25.utils.ImageLoaderUtils;

import java.util.List;

public class TrackedOfferAdapter extends ListAdapter<TrackedOfferData, TrackedOfferAdapter.TrackedOfferViewHolder> {
    private final Context context;
    private OnItemClickListener onItemClickListener;
    private int selectedPosition = RecyclerView.NO_POSITION;
    private static final String PAYLOAD_SELECTION_UPDATE = "PAYLOAD_SELECTION_UPDATE";
    private static final String PAYLOAD_STATUS_UPDATE = "PAYLOAD_STATUS_UPDATE";
    private static final DiffUtil.ItemCallback<TrackedOfferData> DIFF_CALLBACK = new TrackedOfferDiffRecyclerViewCallback();

    public interface OnItemClickListener {
        void onItemTrackedClick(TrackedOfferData offerData, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public TrackedOfferAdapter(@NonNull Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    @NonNull
    @Override
    public TrackedOfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTrackedOfferBinding binding = ItemTrackedOfferBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TrackedOfferViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackedOfferViewHolder holder, int position, @NonNull List<Object> payloads) {
        if(payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
        }
        else {
            Bundle diffBundle = (Bundle) payloads.get(0);
            TrackedOfferData currentOffer = getItem(position);

            if(currentOffer != null) {
                boolean selectionHandled = processPayloadUpdates(holder, position, diffBundle, currentOffer);

                if(!selectionHandled) {
                    holder.updateSelectionState(position == selectedPosition, context);
                }
            }
        }
    }

    private boolean processPayloadUpdates(@NonNull TrackedOfferViewHolder holder, int position, Bundle diffBundle, TrackedOfferData currentOffer) {
        boolean selectionHandled = false;

        for(String key : diffBundle.keySet()) {
            if(PAYLOAD_STATUS_UPDATE.equals(key)) {
                holder.updateStatus(currentOffer.getOffersStatus(), context);
            }
            if(PAYLOAD_SELECTION_UPDATE.equals(key)) {
                holder.updateSelectionState(position == selectedPosition, context);
                selectionHandled = true;
            }
        }
        return selectionHandled;
    }

    @Override
    public void onBindViewHolder(@NonNull TrackedOfferViewHolder holder, int position) {
        TrackedOfferData currentOffer = getItem(position);

        if(currentOffer != null) {
            holder.bind(currentOffer, context, position == selectedPosition, onItemClickListener, this);
        }
    }

    public int getSelectedPosition() { return selectedPosition; }

    public void setSelectedPosition(int position) {
        if(position == selectedPosition) {
            selectedPosition = RecyclerView.NO_POSITION;

            if(isValidPosition(position)) {
                notifyItemChanged(position, createSelectionPayload());
            }
            else {
                int previousSelected = selectedPosition;
                selectedPosition = isValidPosition(position) ? position : RecyclerView.NO_POSITION;

                if(isValidPosition(previousSelected)) {
                    notifyItemChanged(previousSelected, createSelectionPayload());
                }

                if(selectedPosition != RecyclerView.NO_POSITION) {
                    notifyItemChanged(selectedPosition, createSelectionPayload());
                }
            }
        }
    }

    private boolean isValidPosition(int position) { return position >= 0 && position < getItemCount(); }

    private Bundle createSelectionPayload() {
        Bundle payload = new Bundle();
        payload.putBoolean(PAYLOAD_SELECTION_UPDATE, true);

        return payload;
    }

    @Nullable
    public TrackedOfferData getSelectedItem() {
        if(selectedPosition != RecyclerView.NO_POSITION && selectedPosition < getItemCount()) {
            return getItem(selectedPosition);
        }
        return null;
    }

    public static class TrackedOfferViewHolder extends RecyclerView.ViewHolder {
        private final ItemTrackedOfferBinding binding;

        TrackedOfferViewHolder(ItemTrackedOfferBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(TrackedOfferData currOffer, Context context, boolean isSelected, OnItemClickListener listener, TrackedOfferAdapter adapter) {
            ImageLoaderUtils.loadImgFromUrl(binding.clientImgTracked, currOffer.getClientImgUrl(), R.drawable.Person);

            binding.offerPriceTracked.setText(FormatUtils.formatPrice(currOffer.getOfferPrice()));
            binding.clientFirstnameSurnameTracked.setText(currOffer.getClientNameSurname());
            binding.clientIdTracked.setText(currOffer.getClientId());
            binding.propertyIdTracked.setText(currOffer.getPropertyId());
            binding.actionDateTimeTracked.setText(currOffer.getActionDateTime());
            binding.offersStatusTracked.setText(currOffer.getOffersStatus());

            manualOffer(currOffer);

            updateStatus(currOffer.getOffersStatus(), context);
            updateSelectionState(isSelected, context);
            updateAllTextColorFields(isSelected, context);

            itemView.setOnClickListener(v -> {
                if(listener != null) {
                    int currPosition = getBindingAdapterPosition();

                    if(currPosition != RecyclerView.NO_POSITION) {
                        adapter.setSelectedPosition(currPosition);
                        listener.onItemTrackedClick(adapter.getItem(currPosition), currPosition);
                    }
                }
            });
        }

        private void manualOffer(TrackedOfferData offerData) {
            if(offerData.isManual()) {
                binding.offlineIndicator.setVisibility(View.VISIBLE);
            }
            binding.offlineIndicator.setVisibility(View.GONE);
        }

        void updateStatus(String status, Context context) {
            binding.offersStatusTracked.setText(status);
            updateOfferStatusColor(status, context);
        }

        void updateOfferStatusColor(String status, Context context) {
            int statusColor;

            if(status.equalsIgnoreCase(context.getString(R.string.accepted))) {
                statusColor = updateColor(context, R.color.green_accepted);
            } else if(status.equalsIgnoreCase(context.getString(R.string.rejected))) {
                statusColor = updateColor(context, R.color.red_rejected);
            }
            else {
                statusColor = updateColor(context, R.color.Text);
            }

            binding.offersStatusTracked.setTextColor(statusColor);
        }

        void updateSelectionState(boolean isSelected, Context context) {
            binding.getRoot().setBackgroundColor(isSelected ?
                    updateColor(context, R.color.Background) :
                    updateColor(context, android.R.color.transparent)
            );
        }

        void updateAllTextColorFields(boolean isSelected, Context context) {
            int color = isSelected ? R.color.black : R.color.gray;

            updateTxtColor(binding.propertyIdLabelTracked, context, color);
            updateTxtColor(binding.actionDateTimeLabelTracked, context, color);
            updateTxtColor(binding.offerStatusLabelTracked, context, color);

            updateTxtColor(binding.clientIdTracked, context, color);
            updateTxtColor(binding.clientFirstnameSurnameTracked, context, color);
            updateTxtColor(binding.propertyIdTracked, context, color);
            updateTxtColor(binding.actionDateTimeTracked, context, color);
        }

        private void updateTxtColor(TextView txtV, Context context, int color) {
            txtV.setTextColor(updateColor(context, color));
        }

        int updateColor(Context context, int colorId) {
            return ContextCompat.getColor(context, colorId);
        }
    }
}
