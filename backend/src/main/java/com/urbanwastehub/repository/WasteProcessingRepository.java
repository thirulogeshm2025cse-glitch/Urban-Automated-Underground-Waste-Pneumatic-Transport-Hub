package com.urbanwastehub.repository;

import com.urbanwastehub.entity.WasteProcessing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WasteProcessingRepository extends JpaRepository<WasteProcessing, Long> {
    List<WasteProcessing> findByHubId(Long hubId);
    List<WasteProcessing> findAllByOrderByProcessingDateDesc();
    long countByStatus(String status);
}
