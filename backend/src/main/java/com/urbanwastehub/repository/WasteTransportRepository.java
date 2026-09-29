package com.urbanwastehub.repository;

import com.urbanwastehub.entity.WasteTransport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WasteTransportRepository extends JpaRepository<WasteTransport, Long> {
    List<WasteTransport> findByStatus(String status);
    long countByStatus(String status);
    List<WasteTransport> findAllByOrderByStartTimeDesc();

    @Query("SELECT COALESCE(SUM(t.quantity), 0.0) FROM WasteTransport t WHERE t.status = 'COMPLETED'")
    Double sumCompletedTransportQuantity();

    @Query("SELECT COALESCE(SUM(t.quantity), 0.0) FROM WasteTransport t WHERE t.status = 'IN_TRANSIT'")
    Double sumInTransitTransportQuantity();
}
