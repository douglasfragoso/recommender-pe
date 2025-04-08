package com.recommendersystempe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.recommendersystempe.controllers.exception.StandardError;
import com.recommendersystempe.dtos.GlobalEvaluationMetricsDTO;
import com.recommendersystempe.dtos.UserEvaluationMetricsDTO;
import com.recommendersystempe.service.EvaluationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/evaluation", produces = "application/json")
@Tag(name = "Evaluation", description = "API to management Evaluation")
public class EvaluationController {

    @Autowired
    private EvaluationService evaluationService;

    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
    @GetMapping("/user/{userId}")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Métricas de avaliação do usuário recuperadas com sucesso", content = @Content(schema = @Schema(implementation = UserEvaluationMetricsDTO.class))),
        @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content(schema = @Schema(implementation = StandardError.class))),
        @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content(schema = @Schema(implementation = StandardError.class))),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content(schema = @Schema(implementation = StandardError.class)))
    })
    @Operation(summary = "Avaliar recomendações de um usuário", description = "Calcula métricas de avaliação para as recomendações de um usuário. Apenas acessível por ADMIN e MASTER.", tags = {
        "Evaluation" })
    public ResponseEntity<UserEvaluationMetricsDTO> evaluateUserRecommendations(
            @PathVariable("userId") Long userId,
            @RequestParam(defaultValue = "5") int k) {
        UserEvaluationMetricsDTO dto = evaluationService.evaluateUserRecommendations(userId, k);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
    @GetMapping("/global")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Métricas globais de avaliação recuperadas com sucesso", content = @Content(schema = @Schema(implementation = GlobalEvaluationMetricsDTO.class))),
        @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content(schema = @Schema(implementation = StandardError.class))),
        @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content(schema = @Schema(implementation = StandardError.class)))
    })
    @Operation(summary = "Avaliar métricas globais", description = "Calcula métricas globais de avaliação para todas as recomendações. Apenas acessível por ADMIN e MASTER.", tags = {
        "Evaluation" })
    public ResponseEntity<GlobalEvaluationMetricsDTO> evaluateGlobalMetrics(
            @RequestParam(defaultValue = "5") int k) {
        GlobalEvaluationMetricsDTO dto = evaluationService.evaluateGlobalMetrics(k);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }
}