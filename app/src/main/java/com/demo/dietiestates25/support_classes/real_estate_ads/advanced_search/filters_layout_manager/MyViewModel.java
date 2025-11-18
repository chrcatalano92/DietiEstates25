package com.demo.dietiestates25.support_classes.real_estate_ads.advanced_search.filters_layout_manager;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyViewModel extends ViewModel {

    private final MutableLiveData<String> classifyProperty = new MutableLiveData<>();
    private final MutableLiveData<Double> fieldPrice = new MutableLiveData<>();
    private final MutableLiveData<Integer> pickerPrice = new MutableLiveData<>();
    private final MutableLiveData<Integer> rooms = new MutableLiveData<>();
    private final MutableLiveData<Boolean> moreThanTenRooms = new MutableLiveData<>();
    private final MutableLiveData<String> energyClass = new MutableLiveData<>();
    private final MutableLiveData<String> city = new MutableLiveData<>();
    private final MutableLiveData<String> radius = new MutableLiveData<>();
    private final MutableLiveData<Boolean> accuratePosition = new MutableLiveData<>();

    public void setClassifyProperty(String classifyProperty) { this.classifyProperty.setValue(classifyProperty); }
    public void setFieldPrice(Double fieldPrice) {
        this.fieldPrice.setValue(fieldPrice);
    }

    public void setPickerPrice(Integer pickerPrice) {
        this.pickerPrice.setValue(pickerPrice);
    }

    public void setRooms(Integer rooms) {
        this.rooms.setValue(rooms);
    }
    public void setMoreThanTenRooms(Boolean moreThanTenRooms) { this.moreThanTenRooms.setValue(moreThanTenRooms); }
    public void setEnergyClass(String energyClass) {
        this.energyClass.setValue(energyClass);
    }
    public void setCity(String city) {
        this.city.setValue(city);
    }
    public void setRadius(String radius) { this.radius.setValue(radius); }
    public void setAccuratePosition(Boolean accuratePosition) { this.accuratePosition.setValue(accuratePosition); }

    public MutableLiveData<String> getClassifyProperty() {
        return classifyProperty;
    }
    public MutableLiveData<Double> getFieldPrice() {
        return fieldPrice;
    }
    public MutableLiveData<Integer> getPickerPrice() { return pickerPrice; }
    public MutableLiveData<Integer> getRooms() {
        return rooms;
    }
    public MutableLiveData<Boolean> getMoreThanTenRooms() {
        return moreThanTenRooms;
    }
    public MutableLiveData<String> getEnergyClass() {
        return energyClass;
    }
    public MutableLiveData<String> getCity() {
        return city;
    }
    public MutableLiveData<String> getRadius() {
        return radius;
    }
    public MutableLiveData<Boolean> getAccurateLocation() { return accuratePosition; }
}
