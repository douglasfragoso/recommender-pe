package com.recommendersystempe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recommendersystempe.models.Score;

public interface ScoreRepository extends JpaRepository<Score, Long> {
    
}
