package com.urbanwastehub.repository;

import com.urbanwastehub.entity.CollectionPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CollectionPointRepository extends JpaRepository<CollectionPoint, Long> {
    Optional<CollectionPoint> findByPointCode(String pointCode);
    boolean existsByPointCode(String pointCode);
}
