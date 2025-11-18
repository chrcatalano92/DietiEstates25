package com.demo.dietiestates25.support_classes.nominatim_service;

import com.google.gson.annotations.SerializedName;

public class NominatimResult {
    @SerializedName("lat")
    public String latitude;

    @SerializedName("lon")
    public String longitude;

    public double getLatitudeDouble() { return Double.parseDouble(latitude); }

    public double getLongitudeDouble() { return Double.parseDouble(longitude); }
}
