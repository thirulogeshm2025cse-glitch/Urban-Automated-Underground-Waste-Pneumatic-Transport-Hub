package com.urbanwastehub.repository;

import com.urbanwastehub.entity.Pipeline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PipelineRepository extends JpaRepository<Pipeline, Long> {
    Optional<Pipeline> findByPipelineCode(String pipelineCode);
    boolean existsByPipelineCode(String pipelineCode);
    long countByStatus(String status);
}
