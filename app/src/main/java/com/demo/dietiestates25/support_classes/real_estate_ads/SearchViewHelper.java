package com.demo.dietiestates25.support_classes.real_estate_ads;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.support_classes.ProgressBarHelper;
import com.demo.dietiestates25.support_classes.dialog_utils.DialogManager;
import com.demo.dietiestates25.support_classes.keyboard_manager.KeyboardEventHelper;
import com.demo.dietiestates25.support_classes.retrofit_utility.PropertyServiceAPI;
import com.demo.dietiestates25.support_classes.retrofit_utility.RetrofitClientHelper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchViewHelper {
    public static final String CLASS_TAG = "SearchHelper";
    private final PropertyServiceAPI serviceAPI;
    private final RetrofitClientHelper clientHelper;
    private final RecyclerView recView;
    private final SearchView searchV;
    private final ProgressBarHelper progressBarHelper;
    private final DialogManager dialogManager;
    private final Context context;
    private final List<RealEstateAd> dataList;
    private final String httpErr;

    public SearchViewHelper(PropertyServiceAPI serviceAPI, RetrofitClientHelper clientHelper, RecyclerView recView, SearchView searchV, ProgressBarHelper progressBarHelper, Context context, List<RealEstateAd> dataList) {
        this.serviceAPI = serviceAPI;
        this.clientHelper = clientHelper;
        this.recView = recView;
        this.searchV = searchV;
        this.progressBarHelper = progressBarHelper;
        this.context = context;
        this.dataList = dataList;
        this.httpErr = context.getString(R.string.http_err);
        this.dialogManager = new DialogManager(context);
    }

    public void manageSearchView(PropertyAdapter adapter) {
        searchViewCloseCleanBtn();

        searchV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String criteriaSearch) {
                performSearch(criteriaSearch, adapter);
                hideKeyboard();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty()) {
                    reloadRealEstateAds();
                }
                return false;
            }
        });
    }

    private void searchViewCloseCleanBtn() {
        searchV.setOnCloseListener(() -> {
            searchV.setQuery("", false);
            searchV.clearFocus();
            return false;
        });
    }

    private void performSearch(String criteriaSearch, PropertyAdapter adapter) {
        if(serviceAPI != null) {
            Call<List<RealEstateAd>> call = serviceAPI.searchProperties(criteriaSearch);

            progressBarHelper.showProgress();

            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<List<RealEstateAd>> call, @NonNull Response<List<RealEstateAd>> response) {
                    if(response.isSuccessful()) {
                        List<RealEstateAd> ads = response.body();

                        adapter.setList(ads);

                        if(isEmptyList(ads)) {
                            dialogManager.showInfoDialog(R.string.info_dialog_title, R.string.search_empty_list_error);
                        }
                    }
                    else {
                        int statusCode = response.code();
                        dialogManager.showErrorDialog(CLASS_TAG, httpErr + statusCode);
                    }
                    recView.setAdapter(adapter);
                    progressBarHelper.hideProgress();
                }

                @Override
                public void onFailure(@NonNull Call<List<RealEstateAd>> call, @NonNull Throwable t) {
                    dialogManager.showErrorDialog(CLASS_TAG, t.getMessage());
                }
            });
        }
    }

    private void hideKeyboard() {
        KeyboardEventHelper eventHelper = new KeyboardEventHelper(context);
        eventHelper.hideKeyboardForSearchView(searchV);
    }

    private void reloadRealEstateAds() {
        progressBarHelper.showProgress();
        clientHelper.initializeRetrofit((context, serviceAPI) -> loadRealEstateAds(serviceAPI));
    }

    public void loadRealEstateAds(PropertyServiceAPI serviceAPI) { PropertyListSetup.loadRealEstateAds(serviceAPI, dataList, recView, context, dialogManager, progressBarHelper); }

    private boolean isEmptyList(List<RealEstateAd> list) { return list != null && list.isEmpty(); }
}
