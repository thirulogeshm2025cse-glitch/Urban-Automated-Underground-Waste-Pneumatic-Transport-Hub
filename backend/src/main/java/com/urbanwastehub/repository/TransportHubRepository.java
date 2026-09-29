package com.urbanwastehub.repository;

import com.urbanwastehub.entity.TransportHub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TransportHubRepository extends JpaRepository<TransportHub, Long> {
    Optional<TransportHub> findByHubCode(String hubCode);
    boolean existsByHubCode(String hubCode);
    long countByStatus(String status);
}
