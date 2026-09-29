package com.urbanwastehub.service;

import com.urbanwastehub.entity.Alert;
import com.urbanwastehub.entity.Pipeline;
import com.urbanwastehub.entity.TransportHub;
import com.urbanwastehub.entity.WasteBin;
import com.urbanwastehub.exception.ResourceNotFoundException;
import com.urbanwastehub.repository.AlertRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlertService {

    private final AlertRepository alertRepository;

    public AlertService(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    public List<Alert> getAllAlerts() {
        return alertRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Alert> getActiveAlerts() {
        return alertRepository.findByResolvedFalseOrderByCreatedAtDesc();
    }

    public long getActiveAlertCount() {
        return alertRepository.countByResolvedFalse();
    }

    @Transactional
    public Alert createAlertIfNotExists(String alertType, String equipmentType, String equipmentId, String message, String severity) {
        boolean exists = alertRepository.existsByEquipmentIdAndAlertTypeAndResolvedFalse(equipmentId, alertType);
        if (!exists) {
            Alert alert = new Alert(alertType, equipmentType, equipmentId, message, severity);
            return alertRepository.save(alert);
        }
        return null;
    }

    @Transactional
    public Alert resolveAlert(Long id) {
        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found with id: " + id));
        alert.setResolved(true);
        alert.setResolvedAt(LocalDateTime.now());
        return alertRepository.save(alert);
    }

    @Transactional
    public void checkAndGenerateBinAlerts(WasteBin bin) {
        double fill = bin.getFillPercentage() != null ? bin.getFillPercentage() : 0.0;
        if (fill >= 100.0) {
            createAlertIfNotExists("BIN_FULL", "WASTE_BIN", bin.getBinCode(),
                    "Bin " + bin.getBinCode() + " has reached 100% capacity (" + bin.getCurrentWaste() + " / " + bin.getCapacity() + " kg)! Immediate pneumatic evacuation required.",
                    "CRITICAL");
        } else if (fill >= 80.0) {
            createAlertIfNotExists("BIN_WARNING", "WASTE_BIN", bin.getBinCode(),
                    "Bin " + bin.getBinCode() + " fill level is at " + fill + "% (Threshold >= 80%). Dispatch transport soon.",
                    "WARNING");
        }
    }

    @Transactional
    public void checkAndGeneratePipelineAlerts(Pipeline pipeline) {
        if ("BLOCKED".equalsIgnoreCase(pipeline.getStatus())) {
            createAlertIfNotExists("PIPELINE_BLOCKED", "PIPELINE", pipeline.getPipelineCode(),
                    "Underground pipeline " + pipeline.getPipelineCode() + " (" + pipeline.getSourcePoint() + " -> " + pipeline.getDestinationHub() + ") is BLOCKED! Air flow stopped.",
                    "CRITICAL");
        }

        // Pressure check: Normal 2.0 to 3.5 bar
        if (pipeline.getAirPressure() != null) {
            if (pipeline.getAirPressure() < 1.8) {
                createAlertIfNotExists("LOW_PRESSURE", "PIPELINE", pipeline.getPipelineCode(),
                        "Underground pipeline " + pipeline.getPipelineCode() + " air pressure is abnormally LOW (" + pipeline.getAirPressure() + " bar < 1.8 bar min threshold).",
                        "WARNING");
            } else if (pipeline.getAirPressure() > 3.8) {
                createAlertIfNotExists("HIGH_PRESSURE", "PIPELINE", pipeline.getPipelineCode(),
                        "Underground pipeline " + pipeline.getPipelineCode() + " air pressure is abnormally HIGH (" + pipeline.getAirPressure() + " bar > 3.8 bar max threshold). Risk of pneumatic seal rupture!",
                        "CRITICAL");
            }
        }
    }

    @Transactional
    public void checkAndGenerateHubAlerts(TransportHub hub) {
        double util = hub.getUtilization() != null ? hub.getUtilization() : 0.0;
        if (util >= 95.0) {
            createAlertIfNotExists("HUB_CAPACITY_CRITICAL", "TRANSPORT_HUB", hub.getHubCode(),
                    "Transport Hub " + hub.getHubCode() + " (" + hub.getHubName() + ") is critically full at " + util + "% capacity (" + hub.getCurrentWaste() + " / " + hub.getMaximumCapacity() + " kg).",
                    "CRITICAL");
        } else if (util >= 80.0) {
            createAlertIfNotExists("HUB_CAPACITY_WARNING", "TRANSPORT_HUB", hub.getHubCode(),
                    "Transport Hub " + hub.getHubCode() + " (" + hub.getHubName() + ") utilization reached " + util + "% (Threshold >= 80%). Initiate waste processing.",
                    "WARNING");
        }
    }
}
