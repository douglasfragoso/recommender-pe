package com.recommendersystempe.dtos;

import com.recommendersystempe.models.SimilarityMetric;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Schema(description = "DTO for similarity metrics of a user's recommendations")
public class SimilarityMetricDTO {

    @Schema(description = "POI Id", example = "1", required = true)
    private long poiId;

    @Schema(description = "Cosine", example = "0.85")
    private double cosine;

    @Schema(description = "Euclidean", example = "0.85")
    private double euclidean;

    @Schema(description = "Pearson", example = "0.85")
    private double pearson;

    @Schema(description = "Jaccard", example = "0.85")
    private double jaccard;

    @Schema(description = "Average", example = "0.85")
    private double average;

     public SimilarityMetricDTO(SimilarityMetric metric) {
        this.poiId = metric.getPoi().getId();
        this.cosine = metric.getCosine();
        this.euclidean = metric.getEuclidean();
        this.pearson = metric.getPearson();
        this.jaccard = metric.getJaccard();
        this.average = metric.getAverage();
    }
}