package com.recommendersystempe.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para métricas de avaliação de recomendações de um usuário")
public class UserEvaluationMetricsDTO {

    @Schema(description = "Precision@K", example = "0.85")
    @Size(min = 0, max = 1, message = "The field precisionAtK must be between 0 and 1")
    private double precisionAtK;

    @Schema(description = "Recall@K", example = "0.75")
    @Size(min = 0, max = 1, message = "The field recallAtK must be between 0 and 1")
    private double recallAtK;

    @Schema(description = "F1-Score@K", example = "0.80")
    @Size(min = 0, max = 1, message = "The field f1ScoreAtK must be between 0 and 1")
    private double f1ScoreAtK;
}