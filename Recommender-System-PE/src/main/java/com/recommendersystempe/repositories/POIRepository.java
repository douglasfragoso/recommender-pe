package com.recommendersystempe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recommendersystempe.models.POI;

public interface POIRepository extends JpaRepository<POI, Long> {
    
}
