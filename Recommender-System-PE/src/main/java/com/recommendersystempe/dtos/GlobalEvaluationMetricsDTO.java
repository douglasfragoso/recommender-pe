package com.recommendersystempe.dtos;

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para métricas globais de avaliação de recomendações")
public class GlobalEvaluationMetricsDTO {

    @Schema(description = "Average of PrecisionAtK", example = "0.60")
    private double averagePrecisionAtK;

    @Schema(description = "Precision Confidence Lower", example = "0.56")
    private double precisionConfidenceLower;

    @Schema(description = "Precision Confidence Upper", example = "0.064")
    private double precisionConfidenceUpper;

    @Schema(description = "HitRate@K", example = "0.90")
    private double hitRateAtK;

    @Schema(description = "Item Coverage", example = "0.60")
    private double itemCoverage;

    @Schema(description = "Intra List", example = "0.70")
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