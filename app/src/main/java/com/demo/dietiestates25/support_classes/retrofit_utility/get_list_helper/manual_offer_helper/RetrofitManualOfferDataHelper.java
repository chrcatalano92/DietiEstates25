package com.demo.dietiestates25.support_classes.retrofit_utility.get_list_helper.manual_offer_helper;

import com.demo.dietiestates25.support_classes.manage_spinner.PopulatorSpinnerHelper;
import com.demo.dietiestates25.support_classes.retrofit_utility.PropertyServiceAPI;
import com.demo.dietiestates25.support_classes.retrofit_utility.get_list_helper.RetrofitGetListHelper;

import java.util.List;

public class RetrofitManualOfferDataHelper {
    private final RetrofitGetListHelper retrofitGetListHelper;

    public RetrofitManualOfferDataHelper(PropertyServiceAPI apiService) {
        this.retrofitGetListHelper = new RetrofitGetListHelper(apiService);
    }

    public void getNewItemsFromTable(String tableName, String columnName, PopulatorSpinnerHelper spinnerHelper) {
        retrofitGetListHelper.getListFromTable(tableName, columnName, new RetrofitGetListHelper.RetrofitGetListCallback<>() {
            @Override
            public void onSuccess(List<String> items) {
                addNewItemsToList(spinnerHelper, items);
            }

            @Override
            public void onFailure(Exception e) {
                responseErrList(spinnerHelper, e);
            }
        });
    }

    public void getNewItemsFromFilterTable(String filterType, String inputCriteria, PopulatorSpinnerHelper spinnerHelper) {
        retrofitGetListHelper.getListFromFilterTableOrFilterName(filterType, inputCriteria, new RetrofitGetListHelper.RetrofitGetListCallback<>() {
            @Override
            public void onSuccess(List<String> items) {
                updateNewFilteredItemsToList(spinnerHelper, items);
            }

            @Override
            public void onFailure(Exception e) {
                responseErrList(spinnerHelper, e);
            }
        });
    }

    private void addNewItemsToList(PopulatorSpinnerHelper spinnerHelper, List<String> items) {
        if(spinnerHelper != null && items != null) {
            for(String item : items) {
                spinnerHelper.addNewItemToSpinner(item);
            }
        }
    }

    private void updateNewFilteredItemsToList(PopulatorSpinnerHelper spinnerHelper, List<String> items) {
        if(spinnerHelper != null && items != null) {
            spinnerHelper.updateItemsSpinner(items);
        }
    }

    private void responseErrList(PopulatorSpinnerHelper spinnerHelper, Exception e) {
        if(spinnerHelper != null) {
            spinnerHelper.handleError(e);
        }
    }
}
