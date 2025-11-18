package com.demo.dietiestates25.support_classes.real_estate_ads;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.dietiestates25.PropertyActivity;
import com.demo.dietiestates25.R;
import com.demo.dietiestates25.support_classes.retrofit_utility.UrlUtils;
import com.demo.dietiestates25.utils.FormatUtils;
import com.demo.dietiestates25.utils.ImageLoaderUtils;
import com.demo.dietiestates25.utils.ResourceUtils;

import java.util.List;

public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.CardViewPreviewProperty> {
    private List<RealEstateAd> dataList;
    private final Context context;
    private final boolean isOfferMode; // Flag per la modalità
    private final OnPropertyInteractionListener listener; // Listener per comunicare con il Fragment



    public interface OnPropertyInteractionListener {
        void onDirectOfferClick(RealEstateAd property);
    }



    public PropertyAdapter(List<RealEstateAd> dataList, Context context, boolean isOfferMode, OnPropertyInteractionListener listener) {
        this.dataList = dataList;
        this.context = context;
        this.isOfferMode = isOfferMode;
        this.listener = listener;
    }

    public void setList(List<RealEstateAd> newList) {
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new MyDiffCallback(dataList, newList));
        this.dataList = newList;
        result.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public CardViewPreviewProperty onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if(isEmptyList()) {
            view = getView(parent, R.layout.empty_property_recycler);
        }
        else {
            view = getView(parent, R.layout.recycler_real_estate_adv);
        }

        return new CardViewPreviewProperty(view);
    }

    private static View getView(ViewGroup parent, int layout) {
        return LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewPreviewProperty holder, int position) {
        if(isEmptyList()) {
            holder.txtTitle.setText(ResourceUtils.getXMLString(context, R.string.empty_list));
        }
        else {
            RealEstateAd data = dataList.get(position);
            String imgUrl = data.getCoverImageUrl();

            ImageLoaderUtils.loadImgFromUrl(holder.imgV, UrlUtils.changeImageUrl(imgUrl), R.drawable.no_img);

            holder.txtPrice.setText(FormatUtils.formatPrice(data.getPrice()));
            holder.txtTitle.setText(data.getTitle());
            String city = data.getCityName() + ",";
            holder.txtCity.setText(city);
            holder.txtRegion.setText(data.getRegionName());

            String lastUpdated = data.getLastUpdated();

            holder.txtLastUpdated.setText(FormatUtils.formatIsoDateTimeString(lastUpdated));

            holder.cardV.setOnClickListener(view -> {
                if (isOfferMode && listener != null) {
                    listener.onDirectOfferClick(data);
                } else {
                    sendDataIntent(data);
                }
            });
        }
    }

    private void sendDataIntent(RealEstateAd data) {
        Intent intent = new Intent(context, PropertyActivity.class);
        PropertyAdapterKeys keys = new PropertyAdapterKeys();

        addExtra(intent, keys.getCoverImg(), UrlUtils.changeImageUrl(data.getCoverImageUrl()));
        addExtra(intent, keys.getTitle(), data.getTitle());
        addExtra(intent, keys.getClassifyProp(), data.getClassifyProperty());
        addExtra(intent, keys.getTypeProp(), data.getTypeProperty());
        addExtra(intent, keys.getPrice(), data.getPrice());
        addExtra(intent, keys.getNameCity(), data.getCityName());
        addExtra(intent, keys.getNameRegion(), data.getRegionName());
        addExtra(intent, keys.getAddress(), data.getAddress());
        addExtra(intent, keys.getDescription(), data.getDescription());
        addExtra(intent, keys.getServices(), data.getServices());
        addExtra(intent, keys.getDescription(), data.getDescription());
        addExtra(intent, keys.getDimensions(), data.getDimensions());
        addExtra(intent, keys.getRooms(), data.getRooms());
        addExtra(intent, keys.getLevel(), data.getLevel());
        addExtra(intent, keys.getEnergyClass(), data.getEnergyClass());
        addExtra(intent, keys.getElevator(), data.getElevator());
        addExtra(intent, keys.getNearSchools(), data.getNearSchools());
        addExtra(intent, keys.getNearParks(), data.getNearParks());
        addExtra(intent, keys.getNearPublicTransport(), data.getNearPublicTransport());

        context.startActivity(intent);
    }

    private void addExtra(Intent intent, String key, Object data) {
        if(data instanceof String) {
            intent.putExtra(key, (String) data);
        } else if(data instanceof String[]) {
            intent.putExtra(key, (String[]) data);
        } else if (data instanceof Integer) {
            intent.putExtra(key, (Integer) data);
        } else if(data instanceof Boolean) {
            intent.putExtra(key, (Boolean) data);
        } else {
            intent.putExtra(key, (Double) data);
        }
    }

    @Override
    public int getItemCount() { return dataList.isEmpty() ? 1 : dataList.size(); }
    
    private boolean isEmptyList() { return dataList.isEmpty(); }

    public static class CardViewPreviewProperty extends RecyclerView.ViewHolder {
        CardView cardV;
        ImageView imgV;
        TextView txtPrice;
        TextView txtTitle;
        TextView txtCity;
        TextView txtRegion;
        TextView txtLastUpdated;

        public CardViewPreviewProperty(@NonNull View view) {
            super(view);

            cardV = view.findViewById(R.id.recCard);
            imgV = view.findViewById(R.id.recImg);
            txtPrice = view.findViewById(R.id.recPrice);
            txtTitle = view.findViewById(R.id.recTitle);
            txtCity = view.findViewById(R.id.recCity);
            txtRegion = view.findViewById(R.id.recRegion);
            txtLastUpdated = view.findViewById(R.id.recLastUpdated);
        }
    }
}