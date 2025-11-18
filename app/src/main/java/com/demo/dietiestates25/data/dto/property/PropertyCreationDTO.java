package com.demo.dietiestates25.data.dto.property;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * DTO (POJO) per INVIARE i dati di creazione di un immobile al Backend.
 * GSON serializza questa classe in un JSON.
 * Questa classe usa 'setter' per essere riempita passo-passo dal ViewModel.
 */
public class PropertyCreationDTO {

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("price")
    private Double price;

    @SerializedName("dimensions")
    private String dimensions;

    @SerializedName("rooms")
    private String rooms;

    @SerializedName("level")
    private String level;

    @SerializedName("services")
    private List<String> services;

    @SerializedName("address")
    private String address;

    @SerializedName("hasElevator")
    private Boolean hasElevator;

    @SerializedName("energyClass")
    private String energyClass;

    @SerializedName("classifyProperty")
    private String classifyProperty;

    @SerializedName("typeProperty")
    private String typeProperty;

    @SerializedName("latitude")
    private Double latitude;

    @SerializedName("longitude")
    private Double longitude;

    /* Impostare l'ID dell'Agente Immobiliare per impostare il BASSO ACCOPPIAMENTO.
    * La responsabilità di conoscere l'utente loggato appartiene alla View (l'Activity). L'Activity deve recuperare l'ID e passarlo al ViewModel. */

    // Aggiungi un setter per ogni campo
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(Double price) { this.price = price; }
    public void setDimensions(String dimensions) { this.dimensions = dimensions; }
    public void setRooms(String rooms) { this.rooms = rooms; }
    public void setLevel(String level) { this.level = level; }
    public void setServices(List<String> services) { this.services = services; }
    public void setAddress(String address) { this.address = address; }
    public void setHasElevator(Boolean hasElevator) { this.hasElevator = hasElevator; }
    public void setEnergyClass(String energyClass) { this.energyClass = energyClass; }
    public void setClassifyProperty(String classifyProperty) { this.classifyProperty = classifyProperty; }
    public void setTypeProperty(String typeProperty) { this.typeProperty = typeProperty; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

}