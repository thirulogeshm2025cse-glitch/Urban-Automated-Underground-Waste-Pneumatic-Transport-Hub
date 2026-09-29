package com.urbanwastehub.controller;

import com.urbanwastehub.dto.WasteTransportRequest;
import com.urbanwastehub.entity.WasteTransport;
import com.urbanwastehub.service.WasteTransportService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transports")
@CrossOrigin(origins = "*")
public class WasteTransportController {

    private final WasteTransportService wasteTransportService;

    public WasteTransportController(WasteTransportService wasteTransportService) {
        this.wasteTransportService = wasteTransportService;
    }

    @PostMapping
    public ResponseEntity<WasteTransport> createTransport(@Valid @RequestBody WasteTransportRequest request) {
        WasteTransport created = wasteTransportService.createTransport(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<WasteTransport>> getAllTransports() {
        List<WasteTransport> transports = wasteTransportService.getAllTransports();
        return ResponseEntity.ok(transports);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WasteTransport> getTransportById(@PathVariable Long id) {
        WasteTransport transport = wasteTransportService.getTransportById(id);
        return ResponseEntity.ok(transport);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WasteTransport> updateTransportStatus(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String status = payload.getOrDefault("status", "COMPLETED");
        WasteTransport updated = wasteTransportService.updateTransportStatus(id, status);
        return ResponseEntity.ok(updated);
    }
}
