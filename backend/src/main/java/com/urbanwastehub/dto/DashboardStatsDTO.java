package com.urbanwastehub.dto;

public class DashboardStatsDTO {

    private long totalCollectionPoints;
    private long totalWasteBins;
    private long fullBins;
    private long activePipelines;
    private long activeTransportHubs;
    private double wasteCollectedToday;
    private double wasteTransportedToday;
    private double wasteInTransit;
    private long pendingMaintenance;
    private long activeAlerts;

    public DashboardStatsDTO() {
    }

    public long getTotalCollectionPoints() {
        return totalCollectionPoints;
    }

    public void setTotalCollectionPoints(long totalCollectionPoints) {
        this.totalCollectionPoints = totalCollectionPoints;
    }

    public long getTotalWasteBins() {
        return totalWasteBins;
    }

    public void setTotalWasteBins(long totalWasteBins) {
        this.totalWasteBins = totalWasteBins;
    }

    public long getFullBins() {
        return fullBins;
    }

    public void setFullBins(long fullBins) {
        this.fullBins = fullBins;
    }

    public long getActivePipelines() {
        return activePipelines;
    }

    public void setActivePipelines(long activePipelines) {
        this.activePipelines = activePipelines;
    }

    public long getActiveTransportHubs() {
        return activeTransportHubs;
    }

    public void setActiveTransportHubs(long activeTransportHubs) {
        this.activeTransportHubs = activeTransportHubs;
    }

    public double getWasteCollectedToday() {
        return wasteCollectedToday;
    }

    public void setWasteCollectedToday(double wasteCollectedToday) {
        this.wasteCollectedToday = wasteCollectedToday;
    }

    public double getWasteTransportedToday() {
        return wasteTransportedToday;
    }

    public void setWasteTransportedToday(double wasteTransportedToday) {
        this.wasteTransportedToday = wasteTransportedToday;
    }

    public double getWasteInTransit() {
        return wasteInTransit;
    }

    public void setWasteInTransit(double wasteInTransit) {
        this.wasteInTransit = wasteInTransit;
    }

    public long getPendingMaintenance() {
        return pendingMaintenance;
    }

    public void setPendingMaintenance(long pendingMaintenance) {
        this.pendingMaintenance = pendingMaintenance;
    }

    public long getActiveAlerts() {
        return activeAlerts;
    }

    public void setActiveAlerts(long activeAlerts) {
        this.activeAlerts = activeAlerts;
    }
}
