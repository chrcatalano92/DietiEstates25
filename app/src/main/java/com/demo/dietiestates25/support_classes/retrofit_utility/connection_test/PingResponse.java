package com.demo.dietiestates25.support_classes.retrofit_utility.connection_test;

public class PingResponse {
    private String status;
    private String timestamp;
    private String timezone;
    private String location;

    public PingResponse(String status, String timestamp, String timezone, String location) {
        this.status = status;
        this.timestamp = timestamp;
        this.timezone = timezone;
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getTimezone() { return timezone; }

    public String getLocation() {
        return location;
    }

    public void setStatus(String status) { this.status = status;}

    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public void setTimezone(String timezone) { this.timezone = timezone; }

    public void setLocation(String location) { this.location = location; }
}
