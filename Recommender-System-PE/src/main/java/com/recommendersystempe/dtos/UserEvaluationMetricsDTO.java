package com.recommendersystempe.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for evaluation metrics of a user's recommendations")
public class UserEvaluationMetricsDTO {

    @Schema(description = "Precision@K", example = "0.85")
    private double precisionAtK;

    @Schema(description = "HitRate@K", example = "0.90")
    private double hitRateAtK;

    @Schema(description = "Intra List", example = "0.70")
    private double intraListSimilarity;
}