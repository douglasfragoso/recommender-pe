package com.recommendersystempe.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.recommendersystempe.dtos.POIDTO;
import com.recommendersystempe.dtos.PreferencesDTO;
import com.recommendersystempe.service.PreferencesService;

@RestController
@RequestMapping(value = "/recommendation", produces = "application/json")
public class RecommendationController {

    @Autowired
    private PreferencesService preferencesService;

    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER', 'USER')")
    @PostMapping()
    public ResponseEntity<List<POIDTO>> insert(@RequestBody PreferencesDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(preferencesService.insert(dto));
    }
}
