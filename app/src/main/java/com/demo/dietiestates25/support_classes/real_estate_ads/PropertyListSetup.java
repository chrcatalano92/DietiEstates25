package com.demo.dietiestates25.support_classes.real_estate_ads;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.dietiestates25.support_classes.ProgressBarHelper;
import com.demo.dietiestates25.support_classes.dialog_utils.DialogManager;
import com.demo.dietiestates25.support_classes.retrofit_utility.PropertyServiceAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PropertyListSetup {
    public static final String PROPERTY_LIST_SETUP = "PropertyListSetup";

    private PropertyListSetup(){}

    private static void setLayoutManagerToRecyclerView(Context context, RecyclerView recV) { recV.setLayoutManager(new LinearLayoutManager(context)); }

    public static void loadRealEstateAds(PropertyServiceAPI serviceApi, List<RealEstateAd> dataList, RecyclerView recV, Context context, DialogManager dialogManager, ProgressBarHelper barHelper) {
        if(serviceApi != null) {
            setLayoutManagerToRecyclerView(context, recV);

            Call<List<RealEstateAd>> call = serviceApi.getAllProperties();

            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<List<RealEstateAd>> call, @NonNull Response<List<RealEstateAd>> response) {
                    if (response.isSuccessful()) {
                        List<RealEstateAd> ads = response.body();

                        if (ads != null && !ads.isEmpty()) {
                            dataList.clear();

                            for (RealEstateAd ad : ads) {
                                ad.setPrice(ad.getPrice());
                                ad.setLastUpdated(ad.getLastUpdated());

                                ad.setCoverImageUrl(ad.getCoverImageUrl());
                                ad.setTitle(ad.getTitle());
                                ad.setCityName(ad.getCityName());
                                ad.setRegionName(ad.getRegionName());

                                ad.setClassifyProperty(ad.getClassifyProperty());
                                ad.setTypeProperty(ad.getTypeProperty());
                                ad.setDescription(ad.getDescription());
                                ad.setAddress(ad.getAddress());
                                ad.setElevator(ad.getElevator());
                                ad.setEnergyClass(ad.getEnergyClass());
                                ad.setServices(ad.getServices());

                                ad.setDimensions(ad.getDimensions());
                                ad.setRooms(ad.getRooms());
                                ad.setLevel(ad.getLevel());

                                ad.setNearSchools(ad.getNearSchools());
                                ad.setNearParks(ad.getNearParks());
                                ad.setNearPublicTransport(ad.getNearPublicTransport());

                                dataList.add(ad);
                            }
                        }
                    }
                    PropertyAdapter propertyAdapter = new PropertyAdapter(dataList, context, false, null);
                    recV.setAdapter(propertyAdapter);

                    barHelper.hideProgress();
                }

                @Override
                public void onFailure(@NonNull Call<List<RealEstateAd>> call, @NonNull Throwable t) {
                    barHelper.hideProgress();
                    dialogManager.showErrorDialog(PROPERTY_LIST_SETUP, t.getMessage());
                }
            });
        }
    }
}
