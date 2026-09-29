package com.urbanwastehub.repository;

import com.urbanwastehub.entity.WasteBin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface WasteBinRepository extends JpaRepository<WasteBin, Long> {
    Optional<WasteBin> findByBinCode(String binCode);
    boolean existsByBinCode(String binCode);
    List<WasteBin> findByCollectionPointId(Long collectionPointId);
    long countByStatus(String status);

    @Query("SELECT COALESCE(SUM(b.currentWaste), 0.0) FROM WasteBin b")
    Double sumTotalCurrentWaste();
}
