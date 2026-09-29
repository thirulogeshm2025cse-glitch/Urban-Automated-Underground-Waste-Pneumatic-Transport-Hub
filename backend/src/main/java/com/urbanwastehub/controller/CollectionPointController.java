package com.urbanwastehub.controller;

import com.urbanwastehub.dto.CollectionPointDTO;
import com.urbanwastehub.entity.CollectionPoint;
import com.urbanwastehub.service.CollectionPointService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/collection-points")
@CrossOrigin(origins = "*")
public class CollectionPointController {

    private final CollectionPointService collectionPointService;

    public CollectionPointController(CollectionPointService collectionPointService) {
        this.collectionPointService = collectionPointService;
    }

    @PostMapping
    public ResponseEntity<CollectionPoint> createCollectionPoint(@Valid @RequestBody CollectionPointDTO dto) {
        CollectionPoint created = collectionPointService.createCollectionPoint(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CollectionPoint>> getAllCollectionPoints() {
        List<CollectionPoint> points = collectionPointService.getAllCollectionPoints();
        return ResponseEntity.ok(points);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CollectionPoint> getCollectionPointById(@PathVariable Long id) {
        CollectionPoint point = collectionPointService.getCollectionPointById(id);
        return ResponseEntity.ok(point);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CollectionPoint> updateCollectionPoint(@PathVariable Long id, @Valid @RequestBody CollectionPointDTO dto) {
        CollectionPoint updated = collectionPointService.updateCollectionPoint(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCollectionPoint(@PathVariable Long id) {
        collectionPointService.deleteCollectionPoint(id);
        return ResponseEntity.ok().build();
    }
}
