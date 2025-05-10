package com.recommendersystempe.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for evaluation metrics of a user's recommendations")
public class UserEvaluationMetricsDTO {

    @Schema(description = "Precision@K", example = "0.85")
    @Size(min = 0, max = 1, message = "The field precisionAtK must be between 0 and 1")
    private double precisionAtK;

    @Schema(description = "HitRate@K", example = "0.90")
    @Size(min = 0, max = 1, message = "The field hitRateAtK must be between 0 and 1")
    private double hitRateAtK;

    @Schema(description = "Intra List", example = "0.70")
    @Size(min = 0, max = 1, message = "The field intraList must be between 0 and 1")
    private double intraListSimilarity;
}