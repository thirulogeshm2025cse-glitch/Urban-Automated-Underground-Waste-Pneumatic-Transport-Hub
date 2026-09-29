package com.urbanwastehub.service;

import com.urbanwastehub.dto.MaintenanceDTO;
import com.urbanwastehub.entity.Maintenance;
import com.urbanwastehub.exception.ResourceNotFoundException;
import com.urbanwastehub.repository.MaintenanceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;

    public MaintenanceService(MaintenanceRepository maintenanceRepository) {
        this.maintenanceRepository = maintenanceRepository;
    }

    public List<Maintenance> getAllMaintenanceRecords() {
        return maintenanceRepository.findAllByOrderByMaintenanceDateDesc();
    }

    public Maintenance getMaintenanceById(Long id) {
        return maintenanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance record not found with id: " + id));
    }

    @Transactional
    public Maintenance createMaintenance(MaintenanceDTO dto) {
        Maintenance m = new Maintenance(
                dto.getEquipmentType().toUpperCase(),
                dto.getEquipmentId(),
                dto.getDescription(),
                dto.getMaintenanceDate() != null ? dto.getMaintenanceDate() : LocalDate.now(),
                dto.getTechnician(),
                dto.getCost(),
                dto.getStatus() != null ? dto.getStatus().toUpperCase() : "PENDING"
        );
        return maintenanceRepository.save(m);
    }

    @Transactional
    public Maintenance updateMaintenance(Long id, MaintenanceDTO dto) {
        Maintenance m = getMaintenanceById(id);
        m.setEquipmentType(dto.getEquipmentType().toUpperCase());
        m.setEquipmentId(dto.getEquipmentId());
        m.setDescription(dto.getDescription());
        if (dto.getMaintenanceDate() != null) {
            m.setMaintenanceDate(dto.getMaintenanceDate());
        }
        m.setTechnician(dto.getTechnician());
        if (dto.getCost() != null) {
            m.setCost(dto.getCost());
        }
        if (dto.getStatus() != null) {
            m.setStatus(dto.getStatus().toUpperCase());
        }
        return maintenanceRepository.save(m);
    }

    @Transactional
    public void deleteMaintenance(Long id) {
        Maintenance m = getMaintenanceById(id);
        maintenanceRepository.delete(m);
    }
}
