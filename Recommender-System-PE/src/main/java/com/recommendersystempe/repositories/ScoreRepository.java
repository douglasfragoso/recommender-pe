package com.recommendersystempe.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.recommendersystempe.models.Score;

public interface ScoreRepository extends JpaRepository<Score, Long> {

    @Query(value = "SELECT s.* FROM tb_scores s JOIN tb_recommendation r ON s.recommendation_id = r.id WHERE r.user_id = :userId", nativeQuery = true)
    List<Score> findByUser(@Param("userId") Long userId);

    List<Score> findByRecommendationId(Long recommendationId);
}

