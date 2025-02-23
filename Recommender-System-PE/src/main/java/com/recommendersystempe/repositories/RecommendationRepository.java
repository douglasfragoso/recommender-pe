package com.recommendersystempe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recommendersystempe.models.Recommendation;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    
}
