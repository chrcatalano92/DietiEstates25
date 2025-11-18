package com.demo.dietiestates25.support_classes.retrofit_utility.property_save;

public class PropertySaveResponse {
    private final String msg;
    private final Long propertyId;

    public PropertySaveResponse(String msg, Long propertyId) {
        this.msg = msg;
        this.propertyId = propertyId;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public String getMsg() {
        return msg;
    }
}
