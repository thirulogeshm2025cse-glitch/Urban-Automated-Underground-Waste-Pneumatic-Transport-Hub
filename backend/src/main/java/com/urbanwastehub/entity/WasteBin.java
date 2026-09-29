package com.urbanwastehub.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "waste_bin")
public class WasteBin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Bin code is required")
    @Column(name = "bin_code", nullable = false, unique = true)
    private String binCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "collection_point_id", nullable = false)
    @JsonIgnoreProperties("bins")
    private CollectionPoint collectionPoint;

    @NotBlank(message = "Waste type is required")
    @Column(name = "waste_type", nullable = false)
    private String wasteType; // ORGANIC, RECYCLABLE, PLASTIC, PAPER, GENERAL, HAZARDOUS

    @NotNull(message = "Capacity is required")
    @DecimalMin(value = "0.1", message = "Capacity must be greater than 0")
    @Column(nullable = false)
    private Double capacity;

    @NotNull(message = "Current waste cannot be null")
    @DecimalMin(value = "0.0", message = "Current waste cannot be negative")
    @Column(name = "current_waste", nullable = false)
    private Double currentWaste = 0.0;

    @Column(name = "fill_percentage")
    private Double fillPercentage = 0.0;

    @Column(nullable = false)
    private String status = "NORMAL"; // NORMAL, WARNING, FULL

    @Column(name = "last_emptied")
    private LocalDateTime lastEmptied;

    public WasteBin() {
    }

    public WasteBin(String binCode, CollectionPoint collectionPoint, String wasteType, Double capacity, Double currentWaste) {
        this.binCode = binCode;
        this.collectionPoint = collectionPoint;
        this.wasteType = wasteType;
        this.capacity = capacity;
        this.currentWaste = currentWaste != null ? currentWaste : 0.0;
        updateMetrics();
    }

    public void updateMetrics() {
        if (capacity != null && capacity > 0) {
            double percent = (this.currentWaste / this.capacity) * 100.0;
            this.fillPercentage = Math.round(percent * 10.0) / 10.0;
            if (this.fillPercentage >= 80.0) {
                this.status = "FULL";
            } else if (this.fillPercentage >= 60.0) {
                this.status = "WARNING";
            } else {
                this.status = "NORMAL";
            }
        } else {
            this.fillPercentage = 0.0;
            this.status = "NORMAL";
        }
    }

    @PrePersist
    @PreUpdate
    protected void onSave() {
        updateMetrics();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBinCode() {
        return binCode;
    }

    public void setBinCode(String binCode) {
        this.binCode = binCode;
    }

    public CollectionPoint getCollectionPoint() {
        return collectionPoint;
    }

    public void setCollectionPoint(CollectionPoint collectionPoint) {
        this.collectionPoint = collectionPoint;
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
        updateMetrics();
    }

    public Double getCurrentWaste() {
        return currentWaste;
    }

    public void setCurrentWaste(Double currentWaste) {
        this.currentWaste = currentWaste != null ? currentWaste : 0.0;
        updateMetrics();
    }

    public Double getFillPercentage() {
        return fillPercentage;
    }

    public void setFillPercentage(Double fillPercentage) {
        this.fillPercentage = fillPercentage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getLastEmptied() {
        return lastEmptied;
    }

    public void setLastEmptied(LocalDateTime lastEmptied) {
        this.lastEmptied = lastEmptied;
    }
}
