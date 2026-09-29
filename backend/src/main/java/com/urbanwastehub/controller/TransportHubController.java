package com.urbanwastehub.controller;

import com.urbanwastehub.dto.TransportHubDTO;
import com.urbanwastehub.entity.TransportHub;
import com.urbanwastehub.service.TransportHubService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hubs")
@CrossOrigin(origins = "*")
public class TransportHubController {

    private final TransportHubService transportHubService;

    public TransportHubController(TransportHubService transportHubService) {
        this.transportHubService = transportHubService;
    }

    @PostMapping
    public ResponseEntity<TransportHub> createHub(@Valid @RequestBody TransportHubDTO dto) {
        TransportHub created = transportHubService.createHub(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TransportHub>> getAllHubs() {
        List<TransportHub> hubs = transportHubService.getAllHubs();
        return ResponseEntity.ok(hubs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransportHub> getHubById(@PathVariable Long id) {
        TransportHub hub = transportHubService.getHubById(id);
        return ResponseEntity.ok(hub);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransportHub> updateHub(@PathVariable Long id, @Valid @RequestBody TransportHubDTO dto) {
        TransportHub updated = transportHubService.updateHub(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHub(@PathVariable Long id) {
        transportHubService.deleteHub(id);
        return ResponseEntity.ok().build();
    }
}
