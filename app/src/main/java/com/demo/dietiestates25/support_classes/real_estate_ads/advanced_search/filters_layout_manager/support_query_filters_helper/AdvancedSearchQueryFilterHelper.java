package com.demo.dietiestates25.support_classes.real_estate_ads.advanced_search.filters_layout_manager.support_query_filters_helper;

import android.content.Context;

import com.demo.dietiestates25.support_classes.PermissionLocationManager;
import com.demo.dietiestates25.support_classes.dialog_utils.DialogManager;
import com.demo.dietiestates25.support_classes.google_map.ShowInteractiveMapDialog;
import com.demo.dietiestates25.support_classes.retrofit_utility.PropertyServiceAPI;
import com.demo.dietiestates25.support_classes.retrofit_utility.get_list_helper.RetrofitGetListHelper;

import java.util.List;

public class AdvancedSearchQueryFilterHelper {
    public static final String CLASS_TAG = "AdvancedSearchQueryHelper";

    private final PermissionLocationManager permissionManager;
    private final DialogManager dialogManager;
    private final String city;

    public AdvancedSearchQueryFilterHelper(PermissionLocationManager permissionManager, DialogManager dialogManager, String city) {
        this.permissionManager = permissionManager;
        this.dialogManager = dialogManager;
        this.city = city;
    }

    public void listFromFilter(Context context, PropertyServiceAPI serviceAPI, String filterType, String inputCriteria) {
        RetrofitGetListHelper.RetrofitGetListCallback<List<String>> callback = manageAddressCallback(context);
        listHelper(serviceAPI).getListFromFilterTableOrFilterName(filterType, inputCriteria, callback);
    }

    public void listFromFilterRadius(Context context, PropertyServiceAPI serviceAPI, double userLon, double userLan, int metersRadius) {
        RetrofitGetListHelper.RetrofitGetListCallback<List<String>> callback = manageAddressCallback(context);
        listHelper(serviceAPI).getListFromFilterRadius(userLon, userLan, metersRadius, callback);
    }

    public void listFromAllFilters(Context context, PropertyServiceAPI serviceAPI, AdvSearchFiltersRequest filtersRequest) {
        RetrofitGetListHelper.RetrofitGetListCallback<List<String>> callback = manageAddressCallback(context);
        listHelper(serviceAPI).getListFromAllAdvancedSearchFilters(callback, filtersRequest);
    }

    private RetrofitGetListHelper listHelper(PropertyServiceAPI serviceAPI) { return new RetrofitGetListHelper(serviceAPI); }

    private RetrofitGetListHelper.RetrofitGetListCallback<List<String>> manageAddressCallback(Context context) {
        return new RetrofitGetListHelper.RetrofitGetListCallback<>() {
            @Override
            public void onSuccess(List<String> addresses) {
                showInteractiveMap(context, addresses);
            }

            @Override
            public void onFailure(Exception e) {
                dialogManager.showErrorDialog(CLASS_TAG, e.getMessage());
            }
        };
    }

    private void showInteractiveMap(Context context, List<String> addresses) {
        ShowInteractiveMapDialog dialog = new ShowInteractiveMapDialog(context, addresses, city, permissionManager);
        dialog.show();
    }
}
