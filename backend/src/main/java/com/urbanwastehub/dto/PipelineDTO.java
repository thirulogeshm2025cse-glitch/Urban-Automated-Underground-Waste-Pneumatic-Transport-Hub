package com.urbanwastehub.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class PipelineDTO {

    @NotBlank(message = "Pipeline code is required")
    private String pipelineCode;

    @NotBlank(message = "Source point is required")
    private String sourcePoint;

    @NotBlank(message = "Destination hub is required")
    private String destinationHub;

    @NotNull(message = "Length is required")
    @DecimalMin(value = "0.1", message = "Length must be greater than 0")
    private Double length;

    @NotNull(message = "Diameter is required")
    @DecimalMin(value = "0.1", message = "Diameter must be greater than 0")
    private Double diameter;

    private Double airPressure;
    private Double airVelocity;
    private String status;
    private LocalDate lastInspectionDate;

    public PipelineDTO() {
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
