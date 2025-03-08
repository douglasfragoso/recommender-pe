package com.recommendersystempe.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.recommendersystempe.models.Score;
import com.recommendersystempe.models.User;

public interface ScoreRepository extends JpaRepository<Score, Long> {

    // Método para buscar pontuações por usuário
    @Query("SELECT s FROM Score s WHERE s.recommendation.user = :user")
    List<Score> findByUser(@Param("user") User user);
    
}
