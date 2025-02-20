package com.recommendersystempe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.recommendersystempe.dtos.POIDTO;
import com.recommendersystempe.service.POIService;

@RestController
@RequestMapping(value = "/poi", produces = "application/json")
public class POIController {

    @Autowired
    private POIService poiService;

    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
    @PostMapping("/register")
    public ResponseEntity<POIDTO> insert(@RequestBody POIDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(poiService.insert(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
    @GetMapping
    public ResponseEntity<Page<POIDTO>> findAll(
            @PageableDefault(size = 10, page = 0, sort = { "id" }, direction = Direction.ASC) Pageable peageable) {
        return ResponseEntity.status(HttpStatus.OK).body(poiService.findAll(peageable));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
    @GetMapping("/id/{id}")
    public ResponseEntity<POIDTO> findById(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(poiService.findById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER', 'USER')")
    @PutMapping
    public ResponseEntity<String> update(@RequestBody POIDTO dto) {
        poiService.update(dto);
        return ResponseEntity.status(HttpStatus.OK).body("POI updated successfully");
    }
    
    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
    @DeleteMapping("/id/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") Long id) {
        poiService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("POI deleted successfully");
    }
}
