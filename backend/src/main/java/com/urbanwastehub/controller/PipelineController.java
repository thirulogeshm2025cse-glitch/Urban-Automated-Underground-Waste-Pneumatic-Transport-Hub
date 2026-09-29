package com.urbanwastehub.controller;

import com.urbanwastehub.dto.PipelineDTO;
import com.urbanwastehub.entity.Pipeline;
import com.urbanwastehub.service.PipelineService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pipelines")
@CrossOrigin(origins = "*")
public class PipelineController {

    private final PipelineService pipelineService;

    public PipelineController(PipelineService pipelineService) {
        this.pipelineService = pipelineService;
    }

    @PostMapping
    public ResponseEntity<Pipeline> createPipeline(@Valid @RequestBody PipelineDTO dto) {
        Pipeline created = pipelineService.createPipeline(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Pipeline>> getAllPipelines() {
        List<Pipeline> pipelines = pipelineService.getAllPipelines();
        return ResponseEntity.ok(pipelines);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pipeline> getPipelineById(@PathVariable Long id) {
        Pipeline pipeline = pipelineService.getPipelineById(id);
        return ResponseEntity.ok(pipeline);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pipeline> updatePipeline(@PathVariable Long id, @Valid @RequestBody PipelineDTO dto) {
        Pipeline updated = pipelineService.updatePipeline(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePipeline(@PathVariable Long id) {
        pipelineService.deletePipeline(id);
        return ResponseEntity.ok().build();
    }
}
