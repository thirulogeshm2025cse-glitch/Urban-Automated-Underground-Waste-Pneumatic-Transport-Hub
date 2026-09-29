package com.urbanwastehub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "pipeline")
public class Pipeline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Pipeline code is required")
    @Column(name = "pipeline_code", nullable = false, unique = true)
    private String pipelineCode;

    @NotBlank(message = "Source point is required")
    @Column(name = "source_point", nullable = false)
    private String sourcePoint;

    @NotBlank(message = "Destination hub is required")
    @Column(name = "destination_hub", nullable = false)
    private String destinationHub;

    @NotNull(message = "Length is required")
    @DecimalMin(value = "0.1", message = "Pipeline length must be greater than 0")
    @Column(nullable = false)
    private Double length; // in meters

    @NotNull(message = "Diameter is required")
    @DecimalMin(value = "0.1", message = "Diameter must be greater than 0")
    @Column(nullable = false)
    private Double diameter; // in centimeters

    @Column(name = "air_pressure", nullable = false)
    private Double airPressure = 2.5; // in bar (normal: 2.0 - 3.5 bar)

    @Column(name = "air_velocity", nullable = false)
    private Double airVelocity = 25.0; // in m/s (normal: 18 - 30 m/s)

    @Column(nullable = false)
    private String status = "ACTIVE"; // ACTIVE, INACTIVE, BLOCKED, MAINTENANCE

    @Column(name = "last_inspection_date")
    private LocalDate lastInspectionDate;

    public Pipeline() {
    }

    public Pipeline(String pipelineCode, String sourcePoint, String destinationHub, Double length, Double diameter, Double airPressure, Double airVelocity, String status, LocalDate lastInspectionDate) {
        this.pipelineCode = pipelineCode;
        this.sourcePoint = sourcePoint;
        this.destinationHub = destinationHub;
        this.length = length;
        this.diameter = diameter;
        this.airPressure = airPressure != null ? airPressure : 2.5;
        this.airVelocity = airVelocity != null ? airVelocity : 25.0;
        this.status = status != null ? status : "ACTIVE";
        this.lastInspectionDate = lastInspectionDate != null ? lastInspectionDate : LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPipelineCode() {
        return pipelineCode;
    }

    public void setPipelineCode(String pipelineCode) {
        this.pipelineCode = pipelineCode;
    }

    public String getSourcePoint() {
        return sourcePoint;
    }

    public void setSourcePoint(String sourcePoint) {
        this.sourcePoint = sourcePoint;
    }

    public String getDestinationHub() {
        return destinationHub;
    }

    public void setDestinationHub(String destinationHub) {
        this.destinationHub = destinationHub;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getDiameter() {
        return diameter;
    }

    public void setDiameter(Double diameter) {
        this.diameter = diameter;
    }

    public Double getAirPressure() {
        return airPressure;
    }

    public void setAirPressure(Double airPressure) {
        this.airPressure = airPressure;
    }

    public Double getAirVelocity() {
        return airVelocity;
    }

    public void setAirVelocity(Double airVelocity) {
        this.airVelocity = airVelocity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getLastInspectionDate() {
        return lastInspectionDate;
    }

    public void setLastInspectionDate(LocalDate lastInspectionDate) {
        this.lastInspectionDate = lastInspectionDate;
    }
}
