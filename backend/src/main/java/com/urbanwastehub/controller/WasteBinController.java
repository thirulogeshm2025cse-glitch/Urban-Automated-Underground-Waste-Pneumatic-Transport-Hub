package com.urbanwastehub.controller;

import com.urbanwastehub.dto.WasteBinDTO;
import com.urbanwastehub.entity.WasteBin;
import com.urbanwastehub.service.WasteBinService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bins")
@CrossOrigin(origins = "*")
public class WasteBinController {

    private final WasteBinService wasteBinService;

    public WasteBinController(WasteBinService wasteBinService) {
        this.wasteBinService = wasteBinService;
    }

    @PostMapping
    public ResponseEntity<WasteBin> createBin(@Valid @RequestBody WasteBinDTO dto) {
        WasteBin created = wasteBinService.createBin(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<WasteBin>> getAllBins() {
        List<WasteBin> bins = wasteBinService.getAllBins();
        return ResponseEntity.ok(bins);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WasteBin> getBinById(@PathVariable Long id) {
        WasteBin bin = wasteBinService.getBinById(id);
        return ResponseEntity.ok(bin);
    }

    @GetMapping("/by-point/{pointId}")
    public ResponseEntity<List<WasteBin>> getBinsByCollectionPoint(@PathVariable Long pointId) {
        List<WasteBin> bins = wasteBinService.getBinsByCollectionPoint(pointId);
        return ResponseEntity.ok(bins);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WasteBin> updateBin(@PathVariable Long id, @Valid @RequestBody WasteBinDTO dto) {
        WasteBin updated = wasteBinService.updateBin(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBin(@PathVariable Long id) {
        wasteBinService.deleteBin(id);
        return ResponseEntity.ok().build();
    }
}
