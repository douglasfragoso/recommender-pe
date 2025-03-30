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

import com.recommendersystempe.controllers.exception.StandardError;
import com.recommendersystempe.controllers.exception.ValidationError;
import com.recommendersystempe.dtos.POIDTO;
import com.recommendersystempe.dtos.PreferencesDTO;
import com.recommendersystempe.dtos.RecommendationDTO;
import com.recommendersystempe.dtos.ScoreDTO;
import com.recommendersystempe.service.PreferencesService;
import com.recommendersystempe.service.RecommendationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/recommendation", produces = "application/json")
@Tag(name = "Recommendation", description = "API to management Recommendation")
public class RecommendationController {

    @Autowired
    private PreferencesService preferencesService;

    @Autowired
    private RecommendationService recommendationService;

    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER', 'USER')")
    @PostMapping
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Successfully created", content = @Content(schema = @Schema(implementation = RecommendationDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = ValidationError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = StandardError.class))) })
    @Operation(summary = "Insert preferences", description = "Insert preferences and get recommendation, only for Admin and Master", tags = {
            "Auth" })
    public ResponseEntity<List<POIDTO>> insert(@RequestBody PreferencesDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(preferencesService.insert(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER', 'USER')")
    @PostMapping("{recommendationId}/score")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Successfully created", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = ValidationError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = StandardError.class))) })
    @Operation(summary = "Insert score", description = "Insert a new scores to a list of Point of Interest (POI). Only accessible by ADMIN and MASTER roles.", tags = {
            "Auth" })
    public ResponseEntity<String> score(@PathVariable("recommendationId") Long id, @RequestBody List<ScoreDTO> dto) {
        recommendationService.score(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Scored successfully, thank you!");
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
    @GetMapping
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully find all", content = @Content(schema = @Schema(implementation = RecommendationDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = StandardError.class)))
    })
    @Operation(summary = "Find all recommendation", description = "Retrieve a paginated list of all recommendations. Only accessible by ADMIN and MASTER roles.", tags = {
            "Auth" })
    public ResponseEntity<Page<RecommendationDTO>> findAll(
            @PageableDefault(size = 10, page = 0, sort = { "id" }, direction = Direction.ASC) Pageable peageable) {
        return ResponseEntity.status(HttpStatus.OK).body(recommendationService.findAll(peageable));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
    @GetMapping("/id/{id}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully find by id", content = @Content(schema = @Schema(implementation = RecommendationDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = StandardError.class)))
    })
    @Operation(summary = "Find by recommendation id", description = "Retrieve recommendation by their ID. Only accessible by ADMIN and MASTER roles.", tags = {
            "Auth" })
    public ResponseEntity<RecommendationDTO> findById(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(recommendationService.findById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER', 'USER')")
    @GetMapping("/user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully find all", content = @Content(schema = @Schema(implementation = POIDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = StandardError.class)))
    })
    @Operation(summary = "Find all recommendation", description = "Retrieve a paginated list of all user recommendation. Only accessible by ADMIN and MASTER roles.", tags = {
            "Auth" })
    public ResponseEntity<Page<RecommendationDTO>> findAllByUser(
            @PageableDefault(size = 10, page = 0, sort = { "id" }, direction = Direction.ASC) Pageable peageable) {
        return ResponseEntity.status(HttpStatus.OK).body(recommendationService.findAllByUserId(peageable));
    }

}
