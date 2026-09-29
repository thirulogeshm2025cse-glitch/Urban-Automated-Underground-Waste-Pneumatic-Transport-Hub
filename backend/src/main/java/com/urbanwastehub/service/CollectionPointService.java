package com.urbanwastehub.service;

import com.urbanwastehub.dto.CollectionPointDTO;
import com.urbanwastehub.entity.CollectionPoint;
import com.urbanwastehub.exception.BadRequestException;
import com.urbanwastehub.exception.ResourceNotFoundException;
import com.urbanwastehub.repository.CollectionPointRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CollectionPointService {

    private final CollectionPointRepository collectionPointRepository;

    public CollectionPointService(CollectionPointRepository collectionPointRepository) {
        this.collectionPointRepository = collectionPointRepository;
    }

    public List<CollectionPoint> getAllCollectionPoints() {
        return collectionPointRepository.findAll();
    }

    public CollectionPoint getCollectionPointById(Long id) {
        return collectionPointRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Collection Point not found with id: " + id));
    }

    @Transactional
    public CollectionPoint createCollectionPoint(CollectionPointDTO dto) {
        if (collectionPointRepository.existsByPointCode(dto.getPointCode())) {
            throw new BadRequestException("Collection point with code '" + dto.getPointCode() + "' already exists");
        }
        CollectionPoint cp = new CollectionPoint(
                dto.getPointCode(),
                dto.getLocation(),
                dto.getLatitude(),
                dto.getLongitude(),
                dto.getStatus()
        );
        return collectionPointRepository.save(cp);
    }

    @Transactional
    public CollectionPoint updateCollectionPoint(Long id, CollectionPointDTO dto) {
        CollectionPoint cp = getCollectionPointById(id);

        if (!cp.getPointCode().equalsIgnoreCase(dto.getPointCode()) &&
                collectionPointRepository.existsByPointCode(dto.getPointCode())) {
            throw new BadRequestException("Collection point with code '" + dto.getPointCode() + "' already exists");
        }

        cp.setPointCode(dto.getPointCode());
        cp.setLocation(dto.getLocation());
        cp.setLatitude(dto.getLatitude());
        cp.setLongitude(dto.getLongitude());
        if (dto.getStatus() != null) {
            cp.setStatus(dto.getStatus());
        }
        return collectionPointRepository.save(cp);
    }

    @Transactional
    public void deleteCollectionPoint(Long id) {
        CollectionPoint cp = getCollectionPointById(id);
        collectionPointRepository.delete(cp);
    }
}
