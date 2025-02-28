package com.recommendersystempe.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.recommendersystempe.dtos.POIDTO;
import com.recommendersystempe.dtos.PreferencesDTO;
import com.recommendersystempe.dtos.RecommendationDTO;
import com.recommendersystempe.dtos.ScoreDTO;
import com.recommendersystempe.service.PreferencesService;
import com.recommendersystempe.service.RecommendationService;

@RestController
@RequestMapping(value = "/recommendation", produces = "application/json")
public class RecommendationController {

    @Autowired
    private PreferencesService preferencesService;

    @Autowired
    private RecommendationService recommendationService;

    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER', 'USER')")
    @PostMapping()
    public ResponseEntity<List<POIDTO>> insert(@RequestBody PreferencesDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(preferencesService.insert(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER', 'USER')")
    @PostMapping("{recommendationId}/score")
    public ResponseEntity<String> score(@PathVariable("recommendationId") Long id, @RequestBody List<ScoreDTO> dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body("Scored successfully, thank you!");
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
    @GetMapping
    public ResponseEntity<Page<RecommendationDTO>> findAll(
            @PageableDefault(size = 10, page = 0, sort = { "id" }, direction = Direction.ASC) Pageable peageable) {
        return ResponseEntity.status(HttpStatus.OK).body(recommendationService.findAll(peageable));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
    @GetMapping("/id/{id}")
    public ResponseEntity<RecommendationDTO> findById(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(recommendationService.findById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER', 'USER')")
    @GetMapping("/user")
    public ResponseEntity<Page<RecommendationDTO>> findAllByUser(
            @PageableDefault(size = 10, page = 0, sort = { "id" }, direction = Direction.ASC) Pageable peageable) {
        return ResponseEntity.status(HttpStatus.OK).body(recommendationService.findAllByUserId(peageable));
    }

}
