package com.urbanwastehub.controller;

import com.urbanwastehub.dto.WasteProcessingRequest;
import com.urbanwastehub.entity.WasteProcessing;
import com.urbanwastehub.service.WasteProcessingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/processing")
@CrossOrigin(origins = "*")
public class WasteProcessingController {

    private final WasteProcessingService wasteProcessingService;

    public WasteProcessingController(WasteProcessingService wasteProcessingService) {
        this.wasteProcessingService = wasteProcessingService;
    }

    @PostMapping
    public ResponseEntity<WasteProcessing> createProcessing(@Valid @RequestBody WasteProcessingRequest request) {
        WasteProcessing created = wasteProcessingService.createProcessing(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<WasteProcessing>> getAllProcessingRecords() {
        List<WasteProcessing> records = wasteProcessingService.getAllProcessingRecords();
        return ResponseEntity.ok(records);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WasteProcessing> getProcessingById(@PathVariable Long id) {
        WasteProcessing record = wasteProcessingService.getProcessingById(id);
        return ResponseEntity.ok(record);
    }
}
