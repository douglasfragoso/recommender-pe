package com.recommendersystempe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.recommendersystempe.models.POI;

public interface POIRepository extends JpaRepository<POI, Long> {
    
    @Transactional
    @Modifying(clearAutomatically = true) // Limpa o cache do EntityManager - Clears the EntityManager cache
    @Query(value = "UPDATE tb_pois SET poi_name = :name, poi_description = :description WHERE id = :id", nativeQuery = true)
    void update(
            @Param("id") Long id,
            @Param("name") String name,
            @Param("description") String description
           );
}
