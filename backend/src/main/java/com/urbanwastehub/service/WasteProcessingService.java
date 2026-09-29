package com.urbanwastehub.service;

import com.urbanwastehub.dto.WasteProcessingRequest;
import com.urbanwastehub.entity.TransportHub;
import com.urbanwastehub.entity.WasteProcessing;
import com.urbanwastehub.exception.BadRequestException;
import com.urbanwastehub.exception.ResourceNotFoundException;
import com.urbanwastehub.repository.TransportHubRepository;
import com.urbanwastehub.repository.WasteProcessingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WasteProcessingService {

    private final WasteProcessingRepository wasteProcessingRepository;
    private final TransportHubRepository transportHubRepository;

    public WasteProcessingService(WasteProcessingRepository wasteProcessingRepository,
                                  TransportHubRepository transportHubRepository) {
        this.wasteProcessingRepository = wasteProcessingRepository;
        this.transportHubRepository = transportHubRepository;
    }

    public List<WasteProcessing> getAllProcessingRecords() {
        return wasteProcessingRepository.findAllByOrderByProcessingDateDesc();
    }

    public WasteProcessing getProcessingById(Long id) {
        return wasteProcessingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Waste Processing record not found with id: " + id));
    }

    @Transactional
    public WasteProcessing createProcessing(WasteProcessingRequest req) {
        TransportHub hub = transportHubRepository.findById(req.getHubId())
                .orElseThrow(() -> new ResourceNotFoundException("Transport Hub not found with id: " + req.getHubId()));

        if (req.getQuantity() > hub.getCurrentWaste()) {
            throw new BadRequestException("Processing quantity (" + req.getQuantity() +
                    " kg) exceeds current waste held at hub " + hub.getHubCode() + " (" + hub.getCurrentWaste() + " kg).");
        }

        // Deduct processed waste from hub
        double remainingWaste = Math.max(0.0, hub.getCurrentWaste() - req.getQuantity());
        hub.setCurrentWaste(Math.round(remainingWaste * 10.0) / 10.0);
        hub.updateUtilization();
        transportHubRepository.save(hub);

        String status = req.getStatus() != null && !req.getStatus().isBlank()
                ? req.getStatus().toUpperCase()
                : "COMPLETED";

        WasteProcessing processing = new WasteProcessing(
                hub,
                req.getWasteType().toUpperCase(),
                req.getQuantity(),
                req.getProcessingMethod(),
                status
        );
        processing.setProcessingDate(LocalDateTime.now());

        return wasteProcessingRepository.save(processing);
    }
}
