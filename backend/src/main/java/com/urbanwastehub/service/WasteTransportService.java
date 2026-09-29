package com.urbanwastehub.service;

import com.urbanwastehub.dto.WasteTransportRequest;
import com.urbanwastehub.entity.Pipeline;
import com.urbanwastehub.entity.TransportHub;
import com.urbanwastehub.entity.WasteBin;
import com.urbanwastehub.entity.WasteTransport;
import com.urbanwastehub.exception.BadRequestException;
import com.urbanwastehub.exception.ResourceNotFoundException;
import com.urbanwastehub.repository.PipelineRepository;
import com.urbanwastehub.repository.TransportHubRepository;
import com.urbanwastehub.repository.WasteBinRepository;
import com.urbanwastehub.repository.WasteTransportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WasteTransportService {

    private final WasteTransportRepository wasteTransportRepository;
    private final WasteBinRepository wasteBinRepository;
    private final PipelineRepository pipelineRepository;
    private final TransportHubRepository transportHubRepository;
    private final AlertService alertService;

    public WasteTransportService(WasteTransportRepository wasteTransportRepository,
                                 WasteBinRepository wasteBinRepository,
                                 PipelineRepository pipelineRepository,
                                 TransportHubRepository transportHubRepository,
                                 AlertService alertService) {
        this.wasteTransportRepository = wasteTransportRepository;
        this.wasteBinRepository = wasteBinRepository;
        this.pipelineRepository = pipelineRepository;
        this.transportHubRepository = transportHubRepository;
        this.alertService = alertService;
    }

    public List<WasteTransport> getAllTransports() {
        return wasteTransportRepository.findAllByOrderByStartTimeDesc();
    }

    public WasteTransport getTransportById(Long id) {
        return wasteTransportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Waste Transport record not found with id: " + id));
    }

    @Transactional
    public WasteTransport createTransport(WasteTransportRequest request) {
        // 1. Check whether bin exists and contains waste
        WasteBin bin = wasteBinRepository.findById(request.getBinId())
                .orElseThrow(() -> new ResourceNotFoundException("Waste Bin not found with id: " + request.getBinId()));

        if (bin.getCurrentWaste() == null || bin.getCurrentWaste() <= 0) {
            throw new BadRequestException("Waste Bin " + bin.getBinCode() + " is already empty (0 kg). No waste to transport.");
        }

        double quantityToTransport = (request.getQuantity() != null && request.getQuantity() > 0)
                ? request.getQuantity()
                : bin.getCurrentWaste();

        if (quantityToTransport > bin.getCurrentWaste()) {
            throw new BadRequestException("Requested transport quantity (" + quantityToTransport +
                    " kg) exceeds available bin waste (" + bin.getCurrentWaste() + " kg).");
        }

        // 2. Check whether pipeline is ACTIVE
        Pipeline pipeline = pipelineRepository.findById(request.getPipelineId())
                .orElseThrow(() -> new ResourceNotFoundException("Pipeline not found with id: " + request.getPipelineId()));

        if (!"ACTIVE".equalsIgnoreCase(pipeline.getStatus())) {
            throw new BadRequestException("Pipeline " + pipeline.getPipelineCode() + " is currently '" +
                    pipeline.getStatus() + "'. Pneumatic transport can only route through ACTIVE pipelines.");
        }

        // 3. Check whether destination hub has available capacity
        TransportHub hub = transportHubRepository.findById(request.getHubId())
                .orElseThrow(() -> new ResourceNotFoundException("Transport Hub not found with id: " + request.getHubId()));

        double availableHubCapacity = hub.getMaximumCapacity() - hub.getCurrentWaste();
        if (quantityToTransport > availableHubCapacity) {
            throw new BadRequestException("Destination Hub " + hub.getHubCode() + " has insufficient capacity. Available: "
                    + Math.round(availableHubCapacity * 10.0) / 10.0 + " kg, Required: " + quantityToTransport + " kg.");
        }

        // Determine waste type
        String wasteType = request.getWasteType() != null && !request.getWasteType().isBlank()
                ? request.getWasteType().toUpperCase()
                : bin.getWasteType();

        // 4. Create the transport record
        WasteTransport transport = new WasteTransport();
        transport.setBin(bin);
        transport.setPipeline(pipeline);
        transport.setHub(hub);
        transport.setWasteType(wasteType);
        transport.setQuantity(Math.round(quantityToTransport * 10.0) / 10.0);
        transport.setStartTime(LocalDateTime.now().minusSeconds(15));
        transport.setStatus("IN_TRANSIT");

        // 5. Reduce waste from bin
        double newBinWaste = Math.max(0.0, bin.getCurrentWaste() - quantityToTransport);
        bin.setCurrentWaste(Math.round(newBinWaste * 10.0) / 10.0);
        if (bin.getCurrentWaste() == 0.0) {
            bin.setLastEmptied(LocalDateTime.now());
        }
        bin.updateMetrics();
        wasteBinRepository.save(bin);

        // 6. Increase waste in destination hub
        double newHubWaste = hub.getCurrentWaste() + quantityToTransport;
        hub.setCurrentWaste(Math.round(newHubWaste * 10.0) / 10.0);
        hub.updateUtilization();
        transportHubRepository.save(hub);

        // 7. Complete transport record
        transport.setEndTime(LocalDateTime.now());
        transport.setStatus("COMPLETED");
        WasteTransport savedTransport = wasteTransportRepository.save(transport);

        // 8. Generate alerts if capacity limits are reached or updated
        alertService.checkAndGenerateBinAlerts(bin);
        alertService.checkAndGenerateHubAlerts(hub);

        return savedTransport;
    }

    @Transactional
    public WasteTransport updateTransportStatus(Long id, String status) {
        WasteTransport transport = getTransportById(id);
        transport.setStatus(status.toUpperCase());
        if ("COMPLETED".equalsIgnoreCase(status) && transport.getEndTime() == null) {
            transport.setEndTime(LocalDateTime.now());
        }
        return wasteTransportRepository.save(transport);
    }
}
