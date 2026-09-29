package com.urbanwastehub.service;

import com.urbanwastehub.dto.PipelineDTO;
import com.urbanwastehub.entity.Pipeline;
import com.urbanwastehub.exception.BadRequestException;
import com.urbanwastehub.exception.ResourceNotFoundException;
import com.urbanwastehub.repository.PipelineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PipelineService {

    private final PipelineRepository pipelineRepository;
    private final AlertService alertService;

    public PipelineService(PipelineRepository pipelineRepository, AlertService alertService) {
        this.pipelineRepository = pipelineRepository;
        this.alertService = alertService;
    }

    public List<Pipeline> getAllPipelines() {
        return pipelineRepository.findAll();
    }

    public Pipeline getPipelineById(Long id) {
        return pipelineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pipeline not found with id: " + id));
    }

    @Transactional
    public Pipeline createPipeline(PipelineDTO dto) {
        if (pipelineRepository.existsByPipelineCode(dto.getPipelineCode())) {
            throw new BadRequestException("Pipeline with code '" + dto.getPipelineCode() + "' already exists");
        }

        Pipeline p = new Pipeline(
                dto.getPipelineCode(),
                dto.getSourcePoint(),
                dto.getDestinationHub(),
                dto.getLength(),
                dto.getDiameter(),
                dto.getAirPressure(),
                dto.getAirVelocity(),
                dto.getStatus(),
                dto.getLastInspectionDate()
        );

        Pipeline saved = pipelineRepository.save(p);
        alertService.checkAndGeneratePipelineAlerts(saved);
        return saved;
    }

    @Transactional
    public Pipeline updatePipeline(Long id, PipelineDTO dto) {
        Pipeline p = getPipelineById(id);

        if (!p.getPipelineCode().equalsIgnoreCase(dto.getPipelineCode()) &&
                pipelineRepository.existsByPipelineCode(dto.getPipelineCode())) {
            throw new BadRequestException("Pipeline with code '" + dto.getPipelineCode() + "' already exists");
        }

        p.setPipelineCode(dto.getPipelineCode());
        p.setSourcePoint(dto.getSourcePoint());
        p.setDestinationHub(dto.getDestinationHub());
        p.setLength(dto.getLength());
        p.setDiameter(dto.getDiameter());
        if (dto.getAirPressure() != null) {
            p.setAirPressure(dto.getAirPressure());
        }
        if (dto.getAirVelocity() != null) {
            p.setAirVelocity(dto.getAirVelocity());
        }
        if (dto.getStatus() != null) {
            p.setStatus(dto.getStatus().toUpperCase());
        }
        if (dto.getLastInspectionDate() != null) {
            p.setLastInspectionDate(dto.getLastInspectionDate());
        }

        Pipeline saved = pipelineRepository.save(p);
        alertService.checkAndGeneratePipelineAlerts(saved);
        return saved;
    }

    @Transactional
    public void deletePipeline(Long id) {
        Pipeline p = getPipelineById(id);
        pipelineRepository.delete(p);
    }
}
