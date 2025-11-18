package com.demo.dietiestates25.dashboards.real_estate_agent_area.services.off_mng.real_estate_offers;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.dashboards.real_estate_agent_area.services.off_mng.OfferManagementActivity;
import com.demo.dietiestates25.support_classes.Utility;
import com.demo.dietiestates25.support_classes.dialog_utils.DialogManager;

import java.util.List;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.CardViewHolder> {
    private List<OfferData> offersList;
    private final OfferManagementActivity offerMActivity;
    private int selectedPosition = RecyclerView.NO_POSITION;
    private static final String PAYLOAD_UPDATE_STATUS = "update_status";
    private final Utility utility;

    public OfferAdapter(List<OfferData> offersList, OfferManagementActivity offerMActivity) {
        this.offersList = offersList;
        this.offerMActivity = offerMActivity;
        this.utility = new Utility(offerMActivity);
    }

    public void setOffersList(List<OfferData> newOffersList) {
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new MyOffersDiffCallback(offersList, newOffersList));
        this.offersList = newOffersList;
        result.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        offerMActivity.defaultStateImgButtons();

        if(isEmptyList()) {
            view = getView(parent, R.layout.empty_property_recycler);
            emptyListViewHolder(view);
        }
        else {
            view = getView(parent, R.layout.client_offer_card);
        }

        return new CardViewHolder(view);
    }

    private void emptyListViewHolder(View view) {
        EmptyListViewHolder holder = new EmptyListViewHolder(view);
        holder.txtMsg.setText(utility.getXMLString(R.string.empty_offer_list));
    }

    private static View getView(ViewGroup parent, int layout) {
        return LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
    }

    private boolean isEmptyList() { return offersList.isEmpty(); }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position, @NonNull List<Object> payloads) {
        if(payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        }
        else {
            for(Object payload : payloads) {
                if(payload.equals(PAYLOAD_UPDATE_STATUS)) {
                    OfferData offer = offersList.get(position);

                    holder.txtOfferStatus.setText(offer.getOfferStatus());

                    holder.txtOfferStatus.setTextColor(offerMActivity.buttonTxtVColorChanger());

                    saveAction(offer);

                    clickableCounteroffer(offer, holder);
                }
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        if(!isEmptyList()) {
            OfferData offer = offersList.get(position);

            holder.clientImgV.setImageResource(offer.getClientImg());
            holder.txtClientId.setText(String.valueOf(offer.getClientId()));
            holder.txtPropertyId.setText(String.valueOf(offer.getPropertyId()));
            String offerString = "€ " + offer.getOffer();
            holder.txtOffer.setText(offerString);

            holder.txtNameSurname.setText(offer.getNameSurname());
            holder.txtTypeProperty.setText(offer.getTypeProperty());
            holder.txtTimestamp.setText(offer.getTimestamp());
            holder.txtNotes.setText(offer.getNotes());
            holder.txtOfferStatus.setText(offer.getOfferStatus());

            holder.cardV.setOnClickListener(view -> {
                int adapterPosition = holder.getAdapterPosition();
                onCardClicked(adapterPosition);
            });
        }
    }

    private void onCardClicked(int adapterPos) {
        if(adapterPos != RecyclerView.NO_POSITION) {
            selectedPosition = adapterPos;
            offerMActivity.enableImgButtons(adapterPos);
        }
    }

    private void saveAction(OfferData offerData) {
        int propertyId = offerData.getPropertyId();
        String offerStatus = offerData.getOfferStatus();

        Toast.makeText(offerMActivity, "Stato offerta [annuncio: " + propertyId + "]: " + offerStatus, Toast.LENGTH_SHORT).show();
    }

    private void clickableCounteroffer(OfferData offer, CardViewHolder holder) {
        if(offer.getOfferStatus().contains(utility.getXMLString(R.string.counteroffer))) {
            holder.txtOfferStatus.setClickable(true);
            holder.txtOfferStatus.setFocusable(true);
            holder.txtOfferStatus.setPaintFlags(holder.txtOfferStatus.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            holder.txtOfferStatus.setOnClickListener(view -> new DialogManager(offerMActivity).showDialogCounterofferAgent(holder.txtOfferStatus));
        }
        else {
            holder.txtOfferStatus.setClickable(false);
            holder.txtOfferStatus.setFocusable(false);
            holder.txtOfferStatus.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
        }
    }

    public void updateOfferStatus(int position, String newStatus) {
        if(position == selectedPosition && position < offersList.size()) {
            offersList.get(position).setOfferStatus(newStatus);
            notifyItemChanged(position, PAYLOAD_UPDATE_STATUS);
        }
    }

    @Override
    public int getItemCount() { return offersList.isEmpty() ? 1 : offersList.size(); }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        CardView cardV;
        ImageView clientImgV;
        TextView txtClientId;
        TextView txtNameSurname;
        TextView txtPropertyId;
        TextView txtTypeProperty;
        TextView txtOffer;
        TextView txtTimestamp;
        TextView txtNotes;
        TextView txtOfferStatus;
        public CardViewHolder(@NonNull View itemV) {
            super(itemV);

            cardV = itemV.findViewById(R.id.recCard);
            clientImgV = itemV.findViewById(R.id.clientImg);
            txtClientId = itemV.findViewById(R.id.contentClientId);
            txtNameSurname = itemV.findViewById(R.id.contentNameSurname);
            txtPropertyId = itemV.findViewById(R.id.contentPropertyId);
            txtTypeProperty = itemV.findViewById(R.id.contentTypeProperty);
            txtOffer = itemV.findViewById(R.id.contentOffer);
            txtTimestamp = itemV.findViewById(R.id.contentTimestamp);
            txtNotes = itemV.findViewById(R.id.contentNotes);
            txtOfferStatus = itemV.findViewById(R.id.contentOfferStatus);
        }
    }

    public static class EmptyListViewHolder extends RecyclerView.ViewHolder {
        TextView txtMsg;

        public EmptyListViewHolder(@NonNull View v) {
            super(v);

            txtMsg = v.findViewById(R.id.recTitle);
        }
    }
}
