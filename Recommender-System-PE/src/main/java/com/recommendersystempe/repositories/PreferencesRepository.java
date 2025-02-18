package com.recommendersystempe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recommendersystempe.models.Preferences;

public interface PreferencesRepository extends JpaRepository<Preferences, Long> {
    
}
