package com.recommendersystempe.dtos;

import java.util.Map;

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

    @Schema(description = "Average of PrecisionAtK", example = "0.60")
    @Size(min = 0, max = 1, message = "The field averagePrecisionAtk must be between 0 and 1")
    private double averagePrecisionAtK;

    @Schema(description = "HitRate@K", example = "0.90")
    @Size(min = 0, max = 1, message = "The field hitRateAtK must be between 0 and 1")
    private double hitRateAtK;

    @Schema(description = "Item Coverage", example = "0.60")
    @Size(min = 0, max = 1, message = "The field itemCoverage must be between 0 and 1")
    private double itemCoverage;

    @Schema(description = "Intra List", example = "0.70")
    @Size(min = 0, max = 1, message = "The field intraList must be between 0 and 1")
    private double intraListSimilarity;

    @Schema(description = "Feature Coverage", example = """
            {
                "themes": {
                    "CULTURAL": 0.57,
                    "MODERN": 0.55
                },
                "hobbies": {
                    "LEARNING": 0.57,
                    "PHOTOGRAPHY": 0.45
                },
                "motivations": {
                    "EXPLORATION": 0.50,
                    "CULTURE": 0.43
                }
            }""")
    private Map<String, Map<String, Double>> globalFeatureCoverage;

    @Schema(description = "Frequência de Recomendação por POI", example = """
            {
                "8": 0.8,
                "22": 0.6,
                "27": 0.5
            }""")
    private Map<Long, Double> poiFrequency;
}