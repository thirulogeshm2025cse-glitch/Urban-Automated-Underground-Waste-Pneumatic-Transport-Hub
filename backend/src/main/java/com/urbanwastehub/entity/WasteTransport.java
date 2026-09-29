package com.urbanwastehub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "waste_transport")
public class WasteTransport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bin_id", nullable = false)
    private WasteBin bin;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pipeline_id", nullable = false)
    private Pipeline pipeline;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hub_id", nullable = false)
    private TransportHub hub;

    @Column(name = "waste_type", nullable = false)
    private String wasteType;

    @NotNull(message = "Waste quantity is required")
    @DecimalMin(value = "0.1", message = "Waste quantity must be greater than 0")
    @Column(nullable = false)
    private Double quantity; // in kg

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(nullable = false)
    private String status = "SCHEDULED"; // SCHEDULED, IN_TRANSIT, COMPLETED, CANCELLED

    public WasteTransport() {
    }

    public WasteTransport(WasteBin bin, Pipeline pipeline, TransportHub hub, String wasteType, Double quantity, String status) {
        this.bin = bin;
        this.pipeline = pipeline;
        this.hub = hub;
        this.wasteType = wasteType;
        this.quantity = quantity;
        this.status = status != null ? status : "SCHEDULED";
        this.startTime = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (startTime == null) {
            startTime = LocalDateTime.now();
        }
        if (status == null) {
            status = "SCHEDULED";
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WasteBin getBin() {
        return bin;
    }

    public void setBin(WasteBin bin) {
        this.bin = bin;
    }

    public Pipeline getPipeline() {
        return pipeline;
    }

    public void setPipeline(Pipeline pipeline) {
        this.pipeline = pipeline;
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
