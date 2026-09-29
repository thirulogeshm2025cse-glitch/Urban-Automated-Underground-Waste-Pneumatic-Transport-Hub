package com.urbanwastehub.service;

import com.urbanwastehub.entity.*;
import com.urbanwastehub.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataInitializationService implements CommandLineRunner {

    private final CollectionPointRepository collectionPointRepository;
    private final WasteBinRepository wasteBinRepository;
    private final PipelineRepository pipelineRepository;
    private final TransportHubRepository transportHubRepository;
    private final WasteTransportRepository wasteTransportRepository;
    private final WasteProcessingRepository wasteProcessingRepository;
    private final MaintenanceRepository maintenanceRepository;
    private final AlertService alertService;

    public DataInitializationService(CollectionPointRepository collectionPointRepository,
                                     WasteBinRepository wasteBinRepository,
                                     PipelineRepository pipelineRepository,
                                     TransportHubRepository transportHubRepository,
                                     WasteTransportRepository wasteTransportRepository,
                                     WasteProcessingRepository wasteProcessingRepository,
                                     MaintenanceRepository maintenanceRepository,
                                     AlertService alertService) {
        this.collectionPointRepository = collectionPointRepository;
        this.wasteBinRepository = wasteBinRepository;
        this.pipelineRepository = pipelineRepository;
        this.transportHubRepository = transportHubRepository;
        this.wasteTransportRepository = wasteTransportRepository;
        this.wasteProcessingRepository = wasteProcessingRepository;
        this.maintenanceRepository = maintenanceRepository;
        this.alertService = alertService;
    }

    @Override
    public void run(String... args) {
        if (collectionPointRepository.count() > 0) {
            return; // Data already exists
        }

        System.out.println(">>> Initializing Urban Waste Hub Sample Data in MySQL...");

        // 1. Five Collection Points
        CollectionPoint cp1 = collectionPointRepository.save(new CollectionPoint("CP001", "Central Plaza - North Gate", 11.0168, 76.9558, "ACTIVE"));
        CollectionPoint cp2 = collectionPointRepository.save(new CollectionPoint("CP002", "Tech Park Boulevard", 11.0285, 76.9632, "ACTIVE"));
        CollectionPoint cp3 = collectionPointRepository.save(new CollectionPoint("CP003", "Green Valley Residential District", 11.0094, 76.9421, "ACTIVE"));
        CollectionPoint cp4 = collectionPointRepository.save(new CollectionPoint("CP004", "Metro Commercial Complex", 11.0341, 76.9710, "ACTIVE"));
        CollectionPoint cp5 = collectionPointRepository.save(new CollectionPoint("CP005", "Riverside Medical & Research Quarter", 11.0412, 76.9805, "ACTIVE"));

        // 2. Ten Waste Bins
        WasteBin bin1 = wasteBinRepository.save(new WasteBin("BIN001", cp1, "ORGANIC", 500.0, 420.0));
        WasteBin bin2 = wasteBinRepository.save(new WasteBin("BIN002", cp1, "RECYCLABLE", 400.0, 210.0));
        WasteBin bin3 = wasteBinRepository.save(new WasteBin("BIN003", cp2, "PLASTIC", 350.0, 250.0));
        WasteBin bin4 = wasteBinRepository.save(new WasteBin("BIN004", cp2, "PAPER", 300.0, 110.0));
        WasteBin bin5 = wasteBinRepository.save(new WasteBin("BIN005", cp3, "ORGANIC", 600.0, 520.0));
        WasteBin bin6 = wasteBinRepository.save(new WasteBin("BIN006", cp3, "GENERAL", 450.0, 190.0));
        WasteBin bin7 = wasteBinRepository.save(new WasteBin("BIN007", cp4, "PLASTIC", 400.0, 385.0));
        WasteBin bin8 = wasteBinRepository.save(new WasteBin("BIN008", cp4, "RECYCLABLE", 350.0, 240.0));
        WasteBin bin9 = wasteBinRepository.save(new WasteBin("BIN009", cp5, "HAZARDOUS", 250.0, 140.0));
        WasteBin bin10 = wasteBinRepository.save(new WasteBin("BIN010", cp5, "GENERAL", 500.0, 150.0));

        // 3. Five Underground Pipelines
        Pipeline pl1 = pipelineRepository.save(new Pipeline("PL001", "CP001 - Central Plaza", "HUB001 - Central Alpha", 1200.0, 50.0, 2.6, 24.0, "ACTIVE", LocalDate.now().minusDays(12)));
        Pipeline pl2 = pipelineRepository.save(new Pipeline("PL002", "CP002 - Tech Park", "HUB001 - Central Alpha", 1850.0, 50.0, 2.8, 26.5, "ACTIVE", LocalDate.now().minusDays(25)));
        Pipeline pl3 = pipelineRepository.save(new Pipeline("PL003", "CP003 - Green Valley", "HUB001 - Central Alpha", 2400.0, 60.0, 1.5, 15.0, "ACTIVE", LocalDate.now().minusDays(45)));
        Pipeline pl4 = pipelineRepository.save(new Pipeline("PL004", "CP004 - Metro Complex", "HUB002 - Southside Beta", 980.0, 50.0, 2.5, 23.0, "ACTIVE", LocalDate.now().minusDays(8)));
        Pipeline pl5 = pipelineRepository.save(new Pipeline("PL005", "CP005 - Medical Quarter", "HUB002 - Southside Beta", 1650.0, 45.0, 4.2, 31.0, "BLOCKED", LocalDate.now().minusDays(3)));

        // 4. Two Transport Hubs
        TransportHub hub1 = transportHubRepository.save(new TransportHub("HUB001", "Central Pneumatic Terminal Alpha", "Sector 4 Underground Deep Vault", 10000.0, 6850.0, "ACTIVE"));
        TransportHub hub2 = transportHubRepository.save(new TransportHub("HUB002", "Southside Automated Sorting Hub Beta", "Sub-level 2 Industrial Logistics Zone", 8000.0, 6720.0, "ACTIVE"));

        // 5. Ten+ Waste Transport Records
        createHistoricalTransport(bin1, pl1, hub1, "ORGANIC", 380.0, LocalDateTime.now().minusHours(14), LocalDateTime.now().minusHours(14).plusMinutes(6));
        createHistoricalTransport(bin2, pl1, hub1, "RECYCLABLE", 290.0, LocalDateTime.now().minusHours(13), LocalDateTime.now().minusHours(13).plusMinutes(4));
        createHistoricalTransport(bin3, pl2, hub1, "PLASTIC", 280.0, LocalDateTime.now().minusHours(11), LocalDateTime.now().minusHours(11).plusMinutes(5));
        createHistoricalTransport(bin4, pl2, hub1, "PAPER", 220.0, LocalDateTime.now().minusHours(10), LocalDateTime.now().minusHours(10).plusMinutes(3));
        createHistoricalTransport(bin5, pl3, hub1, "ORGANIC", 450.0, LocalDateTime.now().minusHours(9), LocalDateTime.now().minusHours(9).plusMinutes(8));
        createHistoricalTransport(bin6, pl3, hub1, "GENERAL", 310.0, LocalDateTime.now().minusHours(8), LocalDateTime.now().minusHours(8).plusMinutes(7));
        createHistoricalTransport(bin7, pl4, hub2, "PLASTIC", 340.0, LocalDateTime.now().minusHours(6), LocalDateTime.now().minusHours(6).plusMinutes(4));
        createHistoricalTransport(bin8, pl4, hub2, "RECYCLABLE", 270.0, LocalDateTime.now().minusHours(5), LocalDateTime.now().minusHours(5).plusMinutes(4));
        createHistoricalTransport(bin9, pl4, hub2, "HAZARDOUS", 180.0, LocalDateTime.now().minusHours(3), LocalDateTime.now().minusHours(3).plusMinutes(3));
        createHistoricalTransport(bin10, pl4, hub2, "GENERAL", 390.0, LocalDateTime.now().minusHours(2), LocalDateTime.now().minusHours(2).plusMinutes(5));
        createHistoricalTransport(bin1, pl1, hub1, "ORGANIC", 220.0, LocalDateTime.now().minusMinutes(45), LocalDateTime.now().minusMinutes(40));

        // 6. Waste Processing Records
        wasteProcessingRepository.save(new WasteProcessing(hub1, "ORGANIC", 1200.0, "ANAEROBIC_BIO_DIGESTION", "COMPLETED"));
        wasteProcessingRepository.save(new WasteProcessing(hub1, "RECYCLABLE", 850.0, "AUTOMATED_OPTICAL_SORTING", "COMPLETED"));
        wasteProcessingRepository.save(new WasteProcessing(hub1, "PAPER", 600.0, "HYDRAULIC_BALING", "COMPLETED"));
        wasteProcessingRepository.save(new WasteProcessing(hub2, "PLASTIC", 950.0, "PELLETIZING_RECOVERY", "COMPLETED"));
        wasteProcessingRepository.save(new WasteProcessing(hub2, "HAZARDOUS", 300.0, "HIGH_TEMP_PLASMA_GASIFICATION", "COMPLETED"));
        wasteProcessingRepository.save(new WasteProcessing(hub2, "GENERAL", 1100.0, "REFUSE_DERIVED_FUEL_CONVERSION", "IN_PROGRESS"));

        // 7. Maintenance Records
        maintenanceRepository.save(new Maintenance("PIPELINE", "PL005", "Emergency pneumatic clearing due to solid blockage at junction 4B", LocalDate.now(), "Marcus Vance", 450.0, "IN_PROGRESS"));
        maintenanceRepository.save(new Maintenance("PIPELINE", "PL003", "Exhauster turbine vacuum seal calibration and sensor check", LocalDate.now().minusDays(1), "David Chen", 180.0, "PENDING"));
        maintenanceRepository.save(new Maintenance("TRANSPORT_HUB", "HUB002", "Automated compactor ram lubrication and load cell inspection", LocalDate.now().minusDays(4), "Elena Rostova", 320.0, "COMPLETED"));
        maintenanceRepository.save(new Maintenance("COLLECTION_POINT", "CP004", "Inlet chute airlock valve replacement and proximity sensor test", LocalDate.now().minusDays(6), "Marcus Vance", 140.0, "COMPLETED"));
        maintenanceRepository.save(new Maintenance("WASTE_BIN", "BIN007", "Ultrasonic fill level sensor cleaning and recalibration", LocalDate.now().minusDays(9), "Sarah Jenkins", 75.0, "COMPLETED"));

        // 8. Generate Alerts based on data
        alertService.checkAndGenerateBinAlerts(bin1); // >= 80% WARNING
        alertService.checkAndGenerateBinAlerts(bin5); // >= 80% WARNING
        alertService.checkAndGenerateBinAlerts(bin7); // >= 80% WARNING
        alertService.checkAndGeneratePipelineAlerts(pl3); // LOW_PRESSURE
        alertService.checkAndGeneratePipelineAlerts(pl5); // HIGH_PRESSURE + BLOCKED
        alertService.checkAndGenerateHubAlerts(hub2); // utilization > 80% WARNING

        System.out.println(">>> Urban Waste Hub Sample Data Successfully Seeded!");
    }

    private void createHistoricalTransport(WasteBin bin, Pipeline pipeline, TransportHub hub,
                                           String wasteType, Double quantity,
                                           LocalDateTime start, LocalDateTime end) {
        WasteTransport t = new WasteTransport();
        t.setBin(bin);
        t.setPipeline(pipeline);
        t.setHub(hub);
        t.setWasteType(wasteType);
        t.setQuantity(quantity);
        t.setStartTime(start);
        t.setEndTime(end);
        t.setStatus("COMPLETED");
        wasteTransportRepository.save(t);
    }
}
