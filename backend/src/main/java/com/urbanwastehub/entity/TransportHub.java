package com.urbanwastehub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "transport_hub")
public class TransportHub {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Hub code is required")
    @Column(name = "hub_code", nullable = false, unique = true)
    private String hubCode;

    @NotBlank(message = "Hub name is required")
    @Column(name = "hub_name", nullable = false)
    private String hubName;

    @NotBlank(message = "Location is required")
    @Column(nullable = false)
    private String location;

    @NotNull(message = "Maximum capacity is required")
    @DecimalMin(value = "1.0", message = "Hub capacity must be greater than 0")
    @Column(name = "maximum_capacity", nullable = false)
    private Double maximumCapacity; // in kg

    @NotNull(message = "Current waste cannot be null")
    @DecimalMin(value = "0.0", message = "Current waste cannot be negative")
    @Column(name = "current_waste", nullable = false)
    private Double currentWaste = 0.0;

    @Column(nullable = false)
    private Double utilization = 0.0; // percentage

    @Column(nullable = false)
    private String status = "ACTIVE"; // ACTIVE, STANDBY, MAINTENANCE, FULL

    public TransportHub() {
    }

    public TransportHub(String hubCode, String hubName, String location, Double maximumCapacity, Double currentWaste, String status) {
        this.hubCode = hubCode;
        this.hubName = hubName;
        this.location = location;
        this.maximumCapacity = maximumCapacity;
        this.currentWaste = currentWaste != null ? currentWaste : 0.0;
        this.status = status != null ? status : "ACTIVE";
        updateUtilization();
    }

    public void updateUtilization() {
        if (maximumCapacity != null && maximumCapacity > 0) {
            double u = (this.currentWaste / this.maximumCapacity) * 100.0;
            this.utilization = Math.round(u * 10.0) / 10.0;
            if (this.utilization >= 100.0) {
                this.status = "FULL";
            }
        } else {
            this.utilization = 0.0;
        }
    }

    @PrePersist
    @PreUpdate
    protected void onSave() {
        updateUtilization();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        updateUtilization();
    }

    public Double getCurrentWaste() {
        return currentWaste;
    }

    public void setCurrentWaste(Double currentWaste) {
        this.currentWaste = currentWaste != null ? currentWaste : 0.0;
        updateUtilization();
    }

    public Double getUtilization() {
        return utilization;
    }

    public void setUtilization(Double utilization) {
        this.utilization = utilization;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
