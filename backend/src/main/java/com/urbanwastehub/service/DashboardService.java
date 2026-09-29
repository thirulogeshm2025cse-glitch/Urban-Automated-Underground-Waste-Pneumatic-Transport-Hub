package com.urbanwastehub.service;

import com.urbanwastehub.dto.DashboardStatsDTO;
import com.urbanwastehub.repository.*;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final CollectionPointRepository collectionPointRepository;
    private final WasteBinRepository wasteBinRepository;
    private final PipelineRepository pipelineRepository;
    private final TransportHubRepository transportHubRepository;
    private final WasteTransportRepository wasteTransportRepository;
    private final MaintenanceRepository maintenanceRepository;
    private final AlertRepository alertRepository;

    public DashboardService(CollectionPointRepository collectionPointRepository,
                            WasteBinRepository wasteBinRepository,
                            PipelineRepository pipelineRepository,
                            TransportHubRepository transportHubRepository,
                            WasteTransportRepository wasteTransportRepository,
                            MaintenanceRepository maintenanceRepository,
                            AlertRepository alertRepository) {
        this.collectionPointRepository = collectionPointRepository;
        this.wasteBinRepository = wasteBinRepository;
        this.pipelineRepository = pipelineRepository;
        this.transportHubRepository = transportHubRepository;
        this.wasteTransportRepository = wasteTransportRepository;
        this.maintenanceRepository = maintenanceRepository;
        this.alertRepository = alertRepository;
    }

    public DashboardStatsDTO getStats() {
        DashboardStatsDTO stats = new DashboardStatsDTO();
        stats.setTotalCollectionPoints(collectionPointRepository.count());
        stats.setTotalWasteBins(wasteBinRepository.count());
        stats.setFullBins(wasteBinRepository.countByStatus("FULL"));
        stats.setActivePipelines(pipelineRepository.countByStatus("ACTIVE"));
        stats.setActiveTransportHubs(transportHubRepository.countByStatus("ACTIVE"));

        Double currentBinWaste = wasteBinRepository.sumTotalCurrentWaste();
        Double transportedWaste = wasteTransportRepository.sumCompletedTransportQuantity();
        Double inTransitWaste = wasteTransportRepository.sumInTransitTransportQuantity();

        stats.setWasteCollectedToday(Math.round(((currentBinWaste != null ? currentBinWaste : 0.0) +
                (transportedWaste != null ? transportedWaste : 0.0)) * 10.0) / 10.0);
        stats.setWasteTransportedToday(Math.round((transportedWaste != null ? transportedWaste : 0.0) * 10.0) / 10.0);
        stats.setWasteInTransit(Math.round((inTransitWaste != null ? inTransitWaste : 0.0) * 10.0) / 10.0);
        stats.setPendingMaintenance(maintenanceRepository.countByStatus("PENDING"));
        stats.setActiveAlerts(alertRepository.countByResolvedFalse());

        return stats;
    }
}
