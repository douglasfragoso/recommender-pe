package com.recommendersystempe.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.recommendersystempe.service.EvaluationService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/evaluation", produces = "application/json")
@Tag(name = "Evaluation", description = "Evaluation API")
public class EvaluationController {

    @Autowired
    private EvaluationService evaluationService;

    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Double>> evaluateUserRecommendations(@PathVariable("userId") Long userId,          @RequestParam(defaultValue = "5") int k) {
        return ResponseEntity.status(HttpStatus.CREATED).body(evaluationService.evaluateUserRecommendations(userId, k));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
    @GetMapping("/global")
    public ResponseEntity<Map<String, Double>> evaluateGlobalMetrics(
            @RequestParam(defaultValue = "5") int k){
        return ResponseEntity.status(HttpStatus.CREATED).body(evaluationService.evaluateGlobalMetrics(k));
}

}
