package com.urbanwastehub.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class WasteTransportRequest {

    @NotNull(message = "Bin ID is required")
    private Long binId;

    @NotNull(message = "Pipeline ID is required")
    private Long pipelineId;

    @NotNull(message = "Hub ID is required")
    private Long hubId;

    @DecimalMin(value = "0.1", message = "Quantity must be greater than 0")
    private Double quantity;

    private String wasteType;

    public WasteTransportRequest() {
    }

    public Long getBinId() {
        return binId;
    }

    public void setBinId(Long binId) {
        this.binId = binId;
    }

    public Long getPipelineId() {
        return pipelineId;
    }

    public void setPipelineId(Long pipelineId) {
        this.pipelineId = pipelineId;
    }

    public Long getHubId() {
        return hubId;
    }

    public void setHubId(Long hubId) {
        this.hubId = hubId;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getWasteType() {
        return wasteType;
    }

    public void setWasteType(String wasteType) {
        this.wasteType = wasteType;
    }
}
