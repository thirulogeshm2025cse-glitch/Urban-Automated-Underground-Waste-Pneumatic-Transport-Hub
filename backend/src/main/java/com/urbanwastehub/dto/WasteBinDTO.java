package com.urbanwastehub.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class WasteBinDTO {

    @NotBlank(message = "Bin code is required")
    private String binCode;

    @NotNull(message = "Collection point ID is required")
    private Long collectionPointId;

    @NotBlank(message = "Waste type is required")
    private String wasteType;

    @NotNull(message = "Capacity is required")
    @DecimalMin(value = "0.1", message = "Capacity must be greater than 0")
    private Double capacity;

    @DecimalMin(value = "0.0", message = "Current waste cannot be negative")
    private Double currentWaste;

    public WasteBinDTO() {
    }

    public String getBinCode() {
        return binCode;
    }

    public void setBinCode(String binCode) {
        this.binCode = binCode;
    }

    public Long getCollectionPointId() {
        return collectionPointId;
    }

    public void setCollectionPointId(Long collectionPointId) {
        this.collectionPointId = collectionPointId;
    }

    public String getWasteType() {
        return wasteType;
    }

    public void setWasteType(String wasteType) {
        this.wasteType = wasteType;
    }

    public Double getCapacity() {
        return capacity;
    }

    public void setCapacity(Double capacity) {
        this.capacity = capacity;
    }

    public Double getCurrentWaste() {
        return currentWaste;
    }

    public void setCurrentWaste(Double currentWaste) {
        this.currentWaste = currentWaste;
    }
}
