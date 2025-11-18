package com.demo.dietiestates25.dashboards.real_estate_agent_area.services.real_estate_adv_create.collecting_data_steps;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PropertyStepsRequest {
    @SerializedName("idAgent")
    public final Long idAgent;
    @SerializedName("stringList")
    public final List<String> stringList;
    @SerializedName("integerList")
    public final List<Integer> integerList;
    @SerializedName("price")
    public final double price;

    public PropertyStepsRequest(Long idAgent, List<String> stringList, List<Integer> integerList, double price) {
        this.idAgent = idAgent;
        this.stringList = stringList;
        this.integerList = integerList;
        this.price = price;
    }
}
