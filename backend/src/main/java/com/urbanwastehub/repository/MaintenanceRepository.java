package com.urbanwastehub.repository;

import com.urbanwastehub.entity.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {
    List<Maintenance> findByStatus(String status);
    long countByStatus(String status);
    List<Maintenance> findAllByOrderByMaintenanceDateDesc();
}
