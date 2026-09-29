package com.urbanwastehub.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TransportHubDTO {

    @NotBlank(message = "Hub code is required")
    private String hubCode;

    @NotBlank(message = "Hub name is required")
    private String hubName;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Maximum capacity is required")
    @DecimalMin(value = "1.0", message = "Hub capacity must be greater than 0")
    private Double maximumCapacity;

    @DecimalMin(value = "0.0", message = "Current waste cannot be negative")
    private Double currentWaste;

    private String status;

    public TransportHubDTO() {
    }

    public String getHubCode() {
        return hubCode;
    }

    public void setHubCode(String hubCode) {
        this.hubCode = hubCode;
    }

    public String getHubName() {
        return hubName;
    }

    public void setHubName(String hubName) {
        this.hubName = hubName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getMaximumCapacity() {
        return maximumCapacity;
    }

    public void setMaximumCapacity(Double maximumCapacity) {
        this.maximumCapacity = maximumCapacity;
    }

    public Double getCurrentWaste() {
        return currentWaste;
    }

    public void setCurrentWaste(Double currentWaste) {
        this.currentWaste = currentWaste;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
