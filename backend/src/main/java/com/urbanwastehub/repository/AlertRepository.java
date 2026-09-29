package com.urbanwastehub.repository;

import com.urbanwastehub.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findByResolvedFalseOrderByCreatedAtDesc();
    List<Alert> findAllByOrderByCreatedAtDesc();
    long countByResolvedFalse();
    boolean existsByEquipmentIdAndAlertTypeAndResolvedFalse(String equipmentId, String alertType);
}
