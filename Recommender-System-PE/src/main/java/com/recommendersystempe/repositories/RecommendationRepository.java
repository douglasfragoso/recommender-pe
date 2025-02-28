package com.recommendersystempe.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.recommendersystempe.models.Recommendation;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {

    @SuppressWarnings("null")
    @EntityGraph(attributePaths = { "user", "pois" })
    Page<Recommendation> findAll(Pageable pageable);

    @SuppressWarnings("null")
    @EntityGraph(attributePaths = { "user", "pois" })
    Optional<Recommendation> findById(Long id);

    @EntityGraph(attributePaths = { "user", "pois" })
    Page<Recommendation> findAllByUserId(Long userId, Pageable pageable);
}
