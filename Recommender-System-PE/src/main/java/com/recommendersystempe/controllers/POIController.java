package com.recommendersystempe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
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

}
