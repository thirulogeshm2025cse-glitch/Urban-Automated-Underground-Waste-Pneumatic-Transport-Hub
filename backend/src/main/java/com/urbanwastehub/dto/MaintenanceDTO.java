package com.urbanwastehub.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class MaintenanceDTO {

    @NotBlank(message = "Equipment type is required")
    private String equipmentType;

    @NotBlank(message = "Equipment ID is required")
    private String equipmentId;

    @NotBlank(message = "Description is required")
    private String description;

    private LocalDate maintenanceDate;

    @NotBlank(message = "Technician is required")
    private String technician;

    @NotNull(message = "Cost cannot be null")
    @DecimalMin(value = "0.0", message = "Cost cannot be negative")
    private Double cost;

    private String status;

    public MaintenanceDTO() {
    }

    public String getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getMaintenanceDate() {
        return maintenanceDate;
    }

    public void setMaintenanceDate(LocalDate maintenanceDate) {
        this.maintenanceDate = maintenanceDate;
    }

    public String getTechnician() {
        return technician;
    }

    public void setTechnician(String technician) {
        this.technician = technician;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
