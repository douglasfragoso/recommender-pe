package com.recommendersystempe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.recommendersystempe.controllers.exception.StandardError;
import com.recommendersystempe.controllers.exception.ValidationError;
import com.recommendersystempe.dtos.POIDTO;
import com.recommendersystempe.dtos.POIDTOUpdate;
import com.recommendersystempe.service.POIService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/poi", produces = "application/json")
@Tag(name = "POI", description = "API to management Point of Interest")
public class POIController {

    @Autowired
    private POIService poiService;

    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
    @PostMapping("/register")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Successfully created", content = @Content(schema = @Schema(implementation = POIDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = ValidationError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = StandardError.class))) })
    @Operation(summary = "Insert POI", description = "Insert a new Point of Interest (POI). Only accessible by ADMIN and MASTER roles.", tags = {
        "POI" })
    public ResponseEntity<POIDTO> insert(@Valid @RequestBody POIDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(poiService.insert(dto));
    }

    @GetMapping
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully find all", content = @Content(schema = @Schema(implementation = POIDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = StandardError.class))) })
    @Operation(summary = "Find All POI", description = "Retrieve a paginated list of all POI. Accessible to everyone.", tags = {
            "POI" })
    public ResponseEntity<Page<POIDTO>> findAll(
            @PageableDefault(size = 10, page = 0, sort = { "id" }, direction = Direction.ASC) Pageable peageable) {
        return ResponseEntity.status(HttpStatus.OK).body(poiService.findAll(peageable));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
    @GetMapping("/id/{id}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully find by id", content = @Content(schema = @Schema(implementation = POIDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = ValidationError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = StandardError.class))) })
    @Operation(summary = "Find by POI id", description = "Find by POI id, only for Admin and Master", tags = {
        "POI" })
    public ResponseEntity<POIDTO> findById(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(poiService.findById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
    @PatchMapping("/id/{id}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully update", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = ValidationError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = StandardError.class))) })
    @Operation(summary = "Update POI", description = "Update POI, only for Admin and Master", tags = {
        "POI" })
    public ResponseEntity<String> update(@PathVariable("id") Long id, @Valid @RequestBody POIDTOUpdate dto) {
        poiService.update(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body("POI updated successfully");
    }

}
