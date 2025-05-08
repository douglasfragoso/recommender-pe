// Novo repositório SimilarityMetricRepository.java
package com.recommendersystempe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.recommendersystempe.models.SimilarityMetric;

public interface SimilarityMetricRepository extends JpaRepository<SimilarityMetric, Long> {
}