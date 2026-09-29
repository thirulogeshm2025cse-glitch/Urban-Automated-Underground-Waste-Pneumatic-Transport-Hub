package com.urbanwastehub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "waste_processing")
public class WasteProcessing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hub_id", nullable = false)
    private TransportHub hub;

    @NotBlank(message = "Waste type is required")
    @Column(name = "waste_type", nullable = false)
    private String wasteType; // ORGANIC, RECYCLABLE, PLASTIC, PAPER, GENERAL, HAZARDOUS

    @NotNull(message = "Quantity is required")
    @DecimalMin(value = "0.1", message = "Quantity must be greater than 0")
    @Column(nullable = false)
    private Double quantity; // in kg

    @NotBlank(message = "Processing method is required")
    @Column(name = "processing_method", nullable = false)
    private String processingMethod;

    @Column(name = "processing_date")
    private LocalDateTime processingDate;

    @Column(nullable = false)
    private String status = "PENDING"; // PENDING, IN_PROGRESS, COMPLETED

    public WasteProcessing() {
    }

    public WasteProcessing(TransportHub hub, String wasteType, Double quantity, String processingMethod, String status) {
        this.hub = hub;
        this.wasteType = wasteType;
        this.quantity = quantity;
        this.processingMethod = processingMethod;
        this.status = status != null ? status : "COMPLETED";
        this.processingDate = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (processingDate == null) {
            processingDate = LocalDateTime.now();
        }
        if (status == null) {
            status = "COMPLETED";
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TransportHub getHub() {
        return hub;
    }

    public void setHub(TransportHub hub) {
        this.hub = hub;
    }

    public String getWasteType() {
        return wasteType;
    }

    public void setWasteType(String wasteType) {
        this.wasteType = wasteType;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getProcessingMethod() {
        return processingMethod;
    }

    public void setProcessingMethod(String processingMethod) {
        this.processingMethod = processingMethod;
    }

    public LocalDateTime getProcessingDate() {
        return processingDate;
    }

    public void setProcessingDate(LocalDateTime processingDate) {
        this.processingDate = processingDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
