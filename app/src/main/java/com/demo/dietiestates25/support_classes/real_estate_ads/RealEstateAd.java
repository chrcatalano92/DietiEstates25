package com.demo.dietiestates25.support_classes.real_estate_ads;

public class RealEstateAd {
    private Long idProperty;
    private String title;
    private String classifyProperty;
    private String typeProperty;
    private double price;
    private String cityName;
    private String regionName;
    private String address;
    private String lastUpdated;
    private String coverImageUrl;
    private String description;
    private String services;
    private Integer dimensions;
    private Integer rooms;
    private Integer level;
    private String energyClass;
    private String elevator;
    private Boolean nearSchools;
    private Boolean nearParks;
    private Boolean nearPublicTransport;

    RealEstateAd() {}

    public Long getIdProperty() { return idProperty; }
    public void setIdProperty(Long idProperty) { this.idProperty = idProperty; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getClassifyProperty() { return classifyProperty; }
    public void setClassifyProperty(String classifyProperty) { this.classifyProperty = classifyProperty; }

    public String getTypeProperty() { return typeProperty; }
    public void setTypeProperty(String typeProperty) { this.typeProperty = typeProperty; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getCityName() { return cityName; }
    public void setCityName(String cityName) { this.cityName = cityName; }

    public String getRegionName() { return regionName; }
    public void setRegionName(String regionName) { this.regionName = regionName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCoverImageUrl() { return coverImageUrl; }
    public void setCoverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; }

    public String getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(String lastUpdated) { this.lastUpdated = lastUpdated; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getServices() { return services; }
    public void setServices(String services) { this.services = services; }

    public String getEnergyClass() { return energyClass; }
    public void setEnergyClass(String energyClass) { this.energyClass = energyClass; }

    public String getElevator() { return elevator; }
    public void setElevator(String elevator) { this.elevator = elevator; }

    public Integer getDimensions() { return dimensions; }
    public void setDimensions(Integer dimensions) { this.dimensions = dimensions; }

    public Integer getRooms() { return rooms; }
    public void setRooms(Integer rooms) { this.rooms = rooms; }

    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }

    public Boolean getNearSchools() { return nearSchools; }
    public void setNearSchools(Boolean nearSchools) { this.nearSchools = nearSchools; }

    public Boolean getNearParks() { return nearParks; }
    public void setNearParks(Boolean nearParks) { this.nearParks = nearParks; }

    public Boolean getNearPublicTransport() { return nearPublicTransport; }
    public void setNearPublicTransport(Boolean nearPublicTransport) { this.nearPublicTransport = nearPublicTransport; }
}
