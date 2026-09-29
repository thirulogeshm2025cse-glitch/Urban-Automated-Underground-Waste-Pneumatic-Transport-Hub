package com.urbanwastehub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "maintenance")
public class Maintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Equipment type is required")
    @Column(name = "equipment_type", nullable = false)
    private String equipmentType; // PIPELINE, COLLECTION_POINT, WASTE_BIN, TRANSPORT_HUB

    @NotBlank(message = "Equipment ID is required")
    @Column(name = "equipment_id", nullable = false)
    private String equipmentId;

    @NotBlank(message = "Description is required")
    @Column(nullable = false, length = 1000)
    private String description;

    @Column(name = "maintenance_date", nullable = false)
    private LocalDate maintenanceDate;

    @NotBlank(message = "Technician name is required")
    @Column(nullable = false)
    private String technician;

    @NotNull(message = "Cost cannot be null")
    @DecimalMin(value = "0.0", message = "Cost cannot be negative")
    @Column(nullable = false)
    private Double cost = 0.0;

    @Column(nullable = false)
    private String status = "PENDING"; // PENDING, IN_PROGRESS, COMPLETED, CANCELLED

    public Maintenance() {
    }

    public Maintenance(String equipmentType, String equipmentId, String description, LocalDate maintenanceDate, String technician, Double cost, String status) {
        this.equipmentType = equipmentType;
        this.equipmentId = equipmentId;
        this.description = description;
        this.maintenanceDate = maintenanceDate != null ? maintenanceDate : LocalDate.now();
        this.technician = technician;
        this.cost = cost != null ? cost : 0.0;
        this.status = status != null ? status : "PENDING";
    }

    @PrePersist
    protected void onCreate() {
        if (maintenanceDate == null) {
            maintenanceDate = LocalDate.now();
        }
        if (status == null) {
            status = "PENDING";
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
