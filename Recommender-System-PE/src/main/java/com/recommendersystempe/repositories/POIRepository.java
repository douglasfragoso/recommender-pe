package com.recommendersystempe.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recommendersystempe.enums.Status;
import com.recommendersystempe.models.POI;

public interface POIRepository extends JpaRepository<POI, Long> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    List<POI> findByStatus(Status status);
}
