package com.demo.dietiestates25.support_classes.retrofit_utility.get_list_helper;

import androidx.annotation.NonNull;

import com.demo.dietiestates25.support_classes.real_estate_ads.advanced_search.filters_layout_manager.support_query_filters_helper.AdvSearchFiltersRequest;
import com.demo.dietiestates25.support_classes.retrofit_utility.PropertyServiceAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitGetListHelper {
    private final PropertyServiceAPI apiService;

    public RetrofitGetListHelper(PropertyServiceAPI apiService) {
        this.apiService = apiService;
    }

    public interface RetrofitGetListCallback<T> {
        void onSuccess(T result);
        void onFailure(Exception e);
    }

    public void getListFromTable(String tableName, String columnName, final RetrofitGetListCallback<List<String>> callback) {
        Call<List<String>> call = apiService.getListFromTable(tableName, columnName);
        executeCall(callback, call);
    }

    public void getListFromFilterTableOrFilterName(String filterType, String inputCriteria, RetrofitGetListCallback<List<String>> callback) {
        Call<List<String>> call = apiService.getListFromFilterTableOrFilterName(filterType, inputCriteria);
        executeCall(callback, call);
    }

    public void getListFromFilterRadius(double userLon, double userLan, int metersRadius, RetrofitGetListCallback<List<String>> callback) {
        Call<List<String>> call = apiService.getListFromFilterRadius(userLon, userLan, metersRadius);
        executeCall(callback, call);
    }

    public void getListFromAllAdvancedSearchFilters(RetrofitGetListCallback<List<String>> callback, AdvSearchFiltersRequest filtersRequest) {
        Call<List<String>> call = apiService.getListFromAllAdvancedSearchFilters(filtersRequest);
        executeCall(callback, call);
    }

    private static void executeCall(RetrofitGetListCallback<List<String>> callback, Call<List<String>> call) {
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<String>> call, @NonNull Response<List<String>> response) {
                if(response.isSuccessful()) {
                    callback.onSuccess(response.body());
                }
                else {
                    callback.onFailure(new Exception("Response error: " + response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<String>> call, @NonNull Throwable t) {
                callback.onFailure(new Exception(t));
            }
        });
    }
}