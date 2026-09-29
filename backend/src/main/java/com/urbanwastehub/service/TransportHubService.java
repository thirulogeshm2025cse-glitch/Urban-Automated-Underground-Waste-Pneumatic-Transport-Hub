package com.urbanwastehub.service;

import com.urbanwastehub.dto.TransportHubDTO;
import com.urbanwastehub.entity.TransportHub;
import com.urbanwastehub.exception.BadRequestException;
import com.urbanwastehub.exception.ResourceNotFoundException;
import com.urbanwastehub.repository.TransportHubRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransportHubService {

    private final TransportHubRepository transportHubRepository;
    private final AlertService alertService;

    public TransportHubService(TransportHubRepository transportHubRepository, AlertService alertService) {
        this.transportHubRepository = transportHubRepository;
        this.alertService = alertService;
    }

    public List<TransportHub> getAllHubs() {
        return transportHubRepository.findAll();
    }

    public TransportHub getHubById(Long id) {
        return transportHubRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transport Hub not found with id: " + id));
    }

    @Transactional
    public TransportHub createHub(TransportHubDTO dto) {
        if (transportHubRepository.existsByHubCode(dto.getHubCode())) {
            throw new BadRequestException("Transport Hub with code '" + dto.getHubCode() + "' already exists");
        }

        TransportHub hub = new TransportHub(
                dto.getHubCode(),
                dto.getHubName(),
                dto.getLocation(),
                dto.getMaximumCapacity(),
                dto.getCurrentWaste(),
                dto.getStatus()
        );

        TransportHub saved = transportHubRepository.save(hub);
        alertService.checkAndGenerateHubAlerts(saved);
        return saved;
    }

    @Transactional
    public TransportHub updateHub(Long id, TransportHubDTO dto) {
        TransportHub hub = getHubById(id);

        if (!hub.getHubCode().equalsIgnoreCase(dto.getHubCode()) &&
                transportHubRepository.existsByHubCode(dto.getHubCode())) {
            throw new BadRequestException("Transport Hub with code '" + dto.getHubCode() + "' already exists");
        }

        hub.setHubCode(dto.getHubCode());
        hub.setHubName(dto.getHubName());
        hub.setLocation(dto.getLocation());
        if (dto.getMaximumCapacity() != null) {
            hub.setMaximumCapacity(dto.getMaximumCapacity());
        }
        if (dto.getCurrentWaste() != null) {
            hub.setCurrentWaste(dto.getCurrentWaste());
        }
        if (dto.getStatus() != null) {
            hub.setStatus(dto.getStatus());
        }
        hub.updateUtilization();

        TransportHub saved = transportHubRepository.save(hub);
        alertService.checkAndGenerateHubAlerts(saved);
        return saved;
    }

    @Transactional
    public void deleteHub(Long id) {
        TransportHub hub = getHubById(id);
        transportHubRepository.delete(hub);
    }
}
