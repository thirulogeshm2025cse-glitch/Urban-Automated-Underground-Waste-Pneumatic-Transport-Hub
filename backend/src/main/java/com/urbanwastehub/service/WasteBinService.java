package com.urbanwastehub.service;

import com.urbanwastehub.dto.WasteBinDTO;
import com.urbanwastehub.entity.CollectionPoint;
import com.urbanwastehub.entity.WasteBin;
import com.urbanwastehub.exception.BadRequestException;
import com.urbanwastehub.exception.ResourceNotFoundException;
import com.urbanwastehub.repository.CollectionPointRepository;
import com.urbanwastehub.repository.WasteBinRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WasteBinService {

    private final WasteBinRepository wasteBinRepository;
    private final CollectionPointRepository collectionPointRepository;
    private final AlertService alertService;

    public WasteBinService(WasteBinRepository wasteBinRepository,
                           CollectionPointRepository collectionPointRepository,
                           AlertService alertService) {
        this.wasteBinRepository = wasteBinRepository;
        this.collectionPointRepository = collectionPointRepository;
        this.alertService = alertService;
    }

    public List<WasteBin> getAllBins() {
        return wasteBinRepository.findAll();
    }

    public WasteBin getBinById(Long id) {
        return wasteBinRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Waste Bin not found with id: " + id));
    }

    public List<WasteBin> getBinsByCollectionPoint(Long collectionPointId) {
        return wasteBinRepository.findByCollectionPointId(collectionPointId);
    }

    @Transactional
    public WasteBin createBin(WasteBinDTO dto) {
        if (wasteBinRepository.existsByBinCode(dto.getBinCode())) {
            throw new BadRequestException("Waste Bin with code '" + dto.getBinCode() + "' already exists");
        }
        CollectionPoint cp = collectionPointRepository.findById(dto.getCollectionPointId())
                .orElseThrow(() -> new ResourceNotFoundException("Collection Point not found with id: " + dto.getCollectionPointId()));

        if (dto.getCurrentWaste() != null && dto.getCurrentWaste() > dto.getCapacity()) {
            throw new BadRequestException("Current waste cannot exceed bin maximum capacity");
        }

        WasteBin bin = new WasteBin(
                dto.getBinCode(),
                cp,
                dto.getWasteType().toUpperCase(),
                dto.getCapacity(),
                dto.getCurrentWaste()
        );

        WasteBin saved = wasteBinRepository.save(bin);
        alertService.checkAndGenerateBinAlerts(saved);
        return saved;
    }

    @Transactional
    public WasteBin updateBin(Long id, WasteBinDTO dto) {
        WasteBin bin = getBinById(id);

        if (!bin.getBinCode().equalsIgnoreCase(dto.getBinCode()) &&
                wasteBinRepository.existsByBinCode(dto.getBinCode())) {
            throw new BadRequestException("Waste Bin with code '" + dto.getBinCode() + "' already exists");
        }

        if (dto.getCollectionPointId() != null &&
                !bin.getCollectionPoint().getId().equals(dto.getCollectionPointId())) {
            CollectionPoint cp = collectionPointRepository.findById(dto.getCollectionPointId())
                    .orElseThrow(() -> new ResourceNotFoundException("Collection Point not found with id: " + dto.getCollectionPointId()));
            bin.setCollectionPoint(cp);
        }

        if (dto.getCapacity() != null) {
            bin.setCapacity(dto.getCapacity());
        }
        if (dto.getCurrentWaste() != null) {
            bin.setCurrentWaste(dto.getCurrentWaste());
        }
        if (dto.getWasteType() != null) {
            bin.setWasteType(dto.getWasteType().toUpperCase());
        }
        bin.setBinCode(dto.getBinCode());
        bin.updateMetrics();

        WasteBin saved = wasteBinRepository.save(bin);
        alertService.checkAndGenerateBinAlerts(saved);
        return saved;
    }

    @Transactional
    public void deleteBin(Long id) {
        WasteBin bin = getBinById(id);
        wasteBinRepository.delete(bin);
    }
}
