package com.demo.dietiestates25.support_classes.real_estate_ads.advanced_search.filters_layout_manager;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.support_classes.PermissionLocationManager;
import com.demo.dietiestates25.support_classes.dialog_utils.DialogManager;
import com.demo.dietiestates25.support_classes.google_map.CurrentLocationHelper;
import com.demo.dietiestates25.support_classes.google_map.map_point_clicked.MapPointClickedActivity;
import com.demo.dietiestates25.support_classes.real_estate_ads.advanced_search.filters_layout_manager.support_query_filters_helper.AdvSearchFiltersRequest;
import com.demo.dietiestates25.support_classes.real_estate_ads.advanced_search.filters_layout_manager.support_query_filters_helper.AdvancedSearchQueryFilterHelper;
import com.demo.dietiestates25.support_classes.retrofit_utility.PropertyServiceAPI;
import com.demo.dietiestates25.support_classes.retrofit_utility.RetrofitClientHelper;
import com.demo.dietiestates25.utils.ActivityUtils;
import com.demo.dietiestates25.utils.ResourceUtils;

public class FiltersResult {
    private final RetrofitClientHelper clientHelper;
    private final DialogManager dialogManager;
    private final Activity activity;
    private final String classifyProperty;
    private final Double price;
    private Integer rooms;
    private Boolean moreThanTenRooms;
    private final String energyClass;
    private final String city;
    private final String radius;
    private final Boolean locationSwitch;
    private final PermissionLocationManager permissionManager;
    private final AdvancedSearchQueryFilterHelper searchQueryFilterHelper;
    private final AdvSearchFiltersRequest searchFiltersRequest;

    public FiltersResult(Context context, MyViewModel filterData, ViewFilterHelper viewFilterHelper) {
        this.dialogManager = new DialogManager(context);
        this.activity = ActivityUtils.getActivityFromContext(context);
        this.clientHelper = new RetrofitClientHelper(context, activity, "USER", null);
        this.permissionManager = new PermissionLocationManager(activity, dialogManager);

        View filterViewPrices = viewFilterHelper.getPriceFilterV();
        View filterViewRooms = viewFilterHelper.getRoomsFilterV();
        View filterViewRadius = viewFilterHelper.getRadiusFilterV();

        MutableLiveData<String> dataClassifyProperty = filterData.getClassifyProperty();
        MutableLiveData<Double> fieldPrice = filterData.getFieldPrice();
        MutableLiveData<Integer> pickerPrice = filterData.getPickerPrice();
        MutableLiveData<Integer> dataRooms = filterData.getRooms();
        MutableLiveData<Boolean> dataMoreThanTenRooms = filterData.getMoreThanTenRooms();
        MutableLiveData<String> dataEnergyClass = filterData.getEnergyClass();
        MutableLiveData<String> dataCity = filterData.getCity();
        MutableLiveData<String> dataRadius = filterData.getRadius();
        MutableLiveData<Boolean> dataAccuratePosition = filterData.getAccurateLocation();

        this.classifyProperty = dataClassifyProperty != null ? dataClassifyProperty.getValue() : null;
        this.price = FilterLayoutValidator.validatePriceValue(pickerPrice, fieldPrice, filterData, filterViewPrices);
        this.rooms = (dataRooms != null && dataRooms.getValue() != null) ? dataRooms.getValue() : 0;
        this.moreThanTenRooms = (dataMoreThanTenRooms != null && dataMoreThanTenRooms.getValue() != null) ? dataMoreThanTenRooms.getValue() : Boolean.FALSE;
        this.energyClass = dataEnergyClass != null ? dataEnergyClass.getValue() : "-";
        this.city = dataCity != null ? dataCity.getValue() : null;
        this.radius = dataRadius != null ? dataRadius.getValue() : "-";
        this.locationSwitch = (dataAccuratePosition != null && dataAccuratePosition.getValue() != null) ? dataAccuratePosition.getValue() : Boolean.FALSE;

        this.searchQueryFilterHelper = new AdvancedSearchQueryFilterHelper(permissionManager, dialogManager, city);
        this.searchFiltersRequest = new AdvSearchFiltersRequest();

        FilterLayoutValidator.ValidationRoomsValueResult validationRoomsValueResult = FilterLayoutValidator.validateRoomsValue(filterData, filterViewRooms);
        rooms = validationRoomsValueResult.getRooms();
        moreThanTenRooms = validationRoomsValueResult.isMoreThanTenRooms();

        FilterLayoutValidator.validateRadiusValue(filterData, filterViewRadius, radius);

        validateAllValuesAndRunQuery();
    }

    private int getMetersFromRadius() {
        switch (radius) {
            case "<= 100 m":
                return 100;
            case "200 m":
                return 200;
            case "500 m":
                return 500;
            case "1 km":
                return 1000;
            case "2 km":
                return 2000;
            case "5 km":
                return 5000;
            case ">= 10 km":
                return 10000;
            default:
        }
        return 0;
    }

    private void validateAllValuesAndRunQuery() {
        boolean isMoreThanTenRooms = Boolean.TRUE.equals(moreThanTenRooms);
        boolean isPreciseLocation = Boolean.TRUE.equals(locationSwitch);

        if((classifyProperty == null) && (price == 0.0) && !isMoreThanTenRooms && (rooms == 0) && (energyClass == null || energyClass.equals("-")) && (city == null) && (radius == null || radius.equals("-")) && !isPreciseLocation) {
            String errorMsg = ResourceUtils.getXMLString(activity, R.string.err_validation_advanced_search);
            dialogManager.showInfoDialog(errorMsg); // Usa la versione con 1 parametro
        }
        else if((classifyProperty != null) && (price != 0.0) && !isMoreThanTenRooms && (rooms > 0) && (energyClass != null && !energyClass.equals("-")) && (city != null) && (radius != null) && isPreciseLocation) {
            searchFiltersRequest.setRooms(rooms);
            clientHelper.initializeRetrofit(this::adsByAllAdvancedSearchFilters);
        }
        else if((classifyProperty != null) && (price != 0.0) && isMoreThanTenRooms && (rooms == 0) && (energyClass != null && !energyClass.equals("-")) && (city != null) && (radius != null) && isPreciseLocation) {
            searchFiltersRequest.setRooms(11);
            clientHelper.initializeRetrofit(this::adsByAllAdvancedSearchFilters);
        }
        checkSingleFilter(isMoreThanTenRooms, isPreciseLocation);
    }

    private void checkSingleFilter(boolean isMoreThanTenRooms, boolean isPreciseLocation) {
        if((classifyProperty != null) && (price == 0.0) && !isMoreThanTenRooms && (rooms == 0) && (energyClass == null) && (city == null) && (radius == null) && !isPreciseLocation) {
            clientHelper.initializeRetrofit(this::adsByClassifyProperty);
        }
        else if((classifyProperty == null) && (price != 0.0) && !isMoreThanTenRooms && (rooms == 0) && (energyClass == null) && (city == null) && (radius == null) && !isPreciseLocation) {
            clientHelper.initializeRetrofit(this::adsByPrice);
        }
        else if((classifyProperty == null) && (price == 0.0) && !isMoreThanTenRooms && (rooms > 0) && (energyClass == null) && (city == null) && (radius == null) && !isPreciseLocation) {
            clientHelper.initializeRetrofit(this::adsByRooms);
        }
        else if((classifyProperty == null) && (price == 0.0) && isMoreThanTenRooms && (rooms == 0) && (energyClass == null) && (city == null) && (radius == null) && !isPreciseLocation) {
            clientHelper.initializeRetrofit(this::adsByMoreThanTenRooms);
        }
        else if((classifyProperty == null) && (price == 0.0) && !isMoreThanTenRooms && (rooms == 0) && (energyClass != null && !energyClass.equals("-")) && (city == null) && (radius == null) && !isPreciseLocation) {
            clientHelper.initializeRetrofit(this::adsByEnergyClass);
        }
        else if((classifyProperty == null) && (price == 0.0) && !isMoreThanTenRooms && (rooms == 0) && (energyClass == null) && (city != null) && (radius == null) && !isPreciseLocation) {
            clientHelper.initializeRetrofit(this::adsByCity);
        }
        locationManagerSwitch(isMoreThanTenRooms, isPreciseLocation);
    }

    private void locationManagerSwitch(boolean isMoreThanTenRooms, boolean isPreciseLocation) {
        if((classifyProperty == null) && (price == 0.0) && !isMoreThanTenRooms && (rooms == 0) && (energyClass == null) && (city == null) && (radius != null && !radius.equals("-")) && isPreciseLocation) {
            clientHelper.initializeRetrofit(this::adsByRadiusAndPreciseLocation);
        }
        else if((classifyProperty == null) && (price == 0.0) && !isMoreThanTenRooms && (rooms == 0) && (energyClass == null) && (city == null) && (radius != null && !radius.equals("-"))){
            ActivityUtils.openActivityWithDataIntent(activity, MapPointClickedActivity.class, "metersRadius", getMetersFromRadius());
        }
    }

    private void adsByAllAdvancedSearchFilters(Context context, PropertyServiceAPI serviceAPI) {
        searchFiltersRequest.setClassifyProperty(classifyProperty);
        searchFiltersRequest.setPrice(price);
        searchFiltersRequest.setEnergyClass(energyClass);
        searchFiltersRequest.setCity(city);
        searchFiltersRequest.setMetersRadius(getMetersFromRadius());

        CurrentLocationHelper.LocationCallback callback = new CurrentLocationHelper.LocationCallback() {
            @Override
            public void onLocationResult(double latitude, double longitude) {
                searchFiltersRequest.setUserLatitude(latitude);
                searchFiltersRequest.setUserLongitude(longitude);

                searchQueryFilterHelper.listFromAllFilters(context, serviceAPI, searchFiltersRequest);
            }

            @Override
            public void onPermissionDenied() {
                String errorMsg = ResourceUtils.getXMLString(context, R.string.err_permission_location_denied);
                Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String msg) {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        };
        CurrentLocationHelper locationHelper = new CurrentLocationHelper(activity, permissionManager);
        locationHelper.requestCurrentLocation(callback);
    }

    private void adsByClassifyProperty(Context context, PropertyServiceAPI serviceAPI) {
        searchQueryFilterHelper.listFromFilter(context, serviceAPI, "address_by_classify_property", classifyProperty);
    }

    private void adsByPrice(Context context, PropertyServiceAPI serviceAPI) {
        searchQueryFilterHelper.listFromFilter(context, serviceAPI, "address_by_price", price.toString());
    }

    private void adsByRooms(Context context, PropertyServiceAPI serviceAPI) {
        searchQueryFilterHelper.listFromFilter(context, serviceAPI, "address_by_room", rooms.toString());
    }

    private void adsByMoreThanTenRooms(Context context, PropertyServiceAPI serviceAPI) {
        searchQueryFilterHelper.listFromFilter(context, serviceAPI, "address_by_more_than_ten_rooms", "11");
    }

    private void adsByEnergyClass(Context context, PropertyServiceAPI serviceAPI) {
        searchQueryFilterHelper.listFromFilter(context, serviceAPI, "address_by_energy_class", energyClass);
    }

    private void adsByCity(Context context, PropertyServiceAPI serviceAPI) {
        searchQueryFilterHelper.listFromFilter(context, serviceAPI, "address_by_city", city);
    }

    private void adsByRadiusAndPreciseLocation(Context context, PropertyServiceAPI serviceAPI) {
        CurrentLocationHelper.LocationCallback callback = new CurrentLocationHelper.LocationCallback() {
            @Override
            public void onLocationResult(double userLatitude, double userLongitude) {
                int meters = getMetersFromRadius();

                Toast.makeText(context, "lon:" + userLongitude + ", lan: " + userLatitude + ", meters: " + meters, Toast.LENGTH_SHORT).show();
                searchQueryFilterHelper.listFromFilterRadius(context, serviceAPI, userLongitude, userLatitude, meters);
            }

            @Override
            public void onPermissionDenied() {
                String errorMsg = ResourceUtils.getXMLString(context, R.string.err_permission_location_denied);
                Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String msg) {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        };
        CurrentLocationHelper locationHelper = new CurrentLocationHelper(activity, permissionManager);
        locationHelper.requestCurrentLocation(callback);
    }
}
