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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.recommendersystempe.controllers.exception.StandardError;
import com.recommendersystempe.dtos.PreferencesDTO;
import com.recommendersystempe.service.PreferencesService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/preferences", produces = "application/json")
@Tag(name = "Preferences", description = "API to management Preferences")
public class PreferencesController {

    @Autowired
    private PreferencesService preferencesService;

    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
    @GetMapping
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully find all", content = @Content(schema = @Schema(implementation = PreferencesDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = StandardError.class))) })
    @Operation(summary = "Find All Preferences", description = "Retrieve a paginated list of all user preferences. Only accessible by ADMIN and MASTER roles.", tags = {
        "Preferences" })
    public ResponseEntity<Page<PreferencesDTO>> findAll(
            @PageableDefault(size = 10, page = 0, sort = { "id" }, direction = Direction.ASC) Pageable peageable) {
        return ResponseEntity.status(HttpStatus.OK).body(preferencesService.findAll(peageable));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
    @GetMapping("/id/{id}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully find by id", content = @Content(schema = @Schema(implementation = PreferencesDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = StandardError.class)))
         })
    @Operation(summary = "Find by Preferences id", description = "Retrieve preferences by their ID. Only accessible by ADMIN and MASTER roles.", tags = {
        "Preferences" })
    public ResponseEntity<PreferencesDTO> findById(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(preferencesService.findById(id));
    }
}
