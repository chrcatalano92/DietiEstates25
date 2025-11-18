package com.demo.dietiestates25.support_classes.real_estate_ads.advanced_search.filters_layout_manager.support_query_filters_helper;

public class AdvSearchFiltersRequest {
    private String classifyProperty;
    private double price;
    private int rooms;
    private String energyClass;
    private String city;
    private double userLongitude;
    private double userLatitude;
    private int metersRadius;

    public void setClassifyProperty(String classifyProperty) { this.classifyProperty = classifyProperty; }

    public void setPrice(double price) { this.price = price; }

    public void setRooms(int rooms) { this.rooms = rooms; }

    public void setEnergyClass(String energyClass) { this.energyClass = energyClass; }

    public void setCity(String city) { this.city = city; }

    public void setUserLongitude(double userLongitude) { this.userLongitude = userLongitude; }

    public void setUserLatitude(double userLatitude) { this.userLatitude = userLatitude; }

    public void setMetersRadius(int metersRadius) { this.metersRadius = metersRadius; }

    public String getClassifyProperty() { return classifyProperty; }

    public double getPrice() { return price; }

    public int getRooms() { return rooms; }

    public String getEnergyClass() { return energyClass; }

    public String getCity() { return city; }

    public double getUserLongitude() { return userLongitude; }

    public double getUserLatitude() { return userLatitude; }

    public int getMetersRadius() { return metersRadius; }
}
