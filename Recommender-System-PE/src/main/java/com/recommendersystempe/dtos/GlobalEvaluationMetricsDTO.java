package com.recommendersystempe.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para métricas globais de avaliação de recomendações")
public class GlobalEvaluationMetricsDTO {

    @Schema(description = "HitRate@K", example = "0.90")
    @Size(min = 0, max = 1, message = "The field hitRateAtK must be between 0 and 1")
    private double hitRateAtK;

    @Schema(description = "Item Coverage", example = "0.60")
    @Size(min = 0, max = 1, message = "The field itemCoverage must be between 0 and 1")
    private double itemCoverage;

    @Schema(description = "Average of PrecisionAtK", example = "0.60")
    @Size(min = 0, max = 1, message = "The field averagePrecisionAtk must be between 0 and 1")
    private double averagePrecisionAtK;

    @Schema(description = "Average of RecallAtK", example = "0.60")
    @Size(min = 0, max = 1, message = "The field averageRecallAtK must be between 0 and 1")
    private double averageRecallAtK;

    @Schema(description = "Average of F1ScoreAtK", example = "0.60")
    @Size(min = 0, max = 1, message = "The field averageF1ScoreAtK must be between 0 and 1")
    private double averageF1ScoreAtK;
}