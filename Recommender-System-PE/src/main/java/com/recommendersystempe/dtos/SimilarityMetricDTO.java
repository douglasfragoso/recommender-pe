package com.recommendersystempe.dtos;

import com.recommendersystempe.models.SimilarityMetric;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Schema(description = "DTO for similarity metrics of a user's recommendations")
public class SimilarityMetricDTO {

    @Schema(description = "POI Id", example = "1", required = true)
    @Min(value = 1, message = "POI Id must be at least 1")
    private long poiId;

    @Schema(description = "Cosine", example = "0.85")
    @Size(min = 0, max = 1, message = "The field cosine must be between 0 and 1")
    private double cosine;

    @Schema(description = "Euclidean", example = "0.85")
    @Size(min = 0, max = 1, message = "The field euclidean must be between 0 and 1")
    private double euclidean;

    @Schema(description = "Pearson", example = "0.85")
    @Size(min = 0, max = 1, message = "The field pearson must be between 0 and 1")
    private double pearson;

    @Schema(description = "Jaccard", example = "0.85")
    @Size(min = 0, max = 1, message = "The field jaccard must be between 0 and 1")
    private double jaccard;

    @Schema(description = "Average", example = "0.85")
    @Size(min = 0, max = 1, message = "The field average must be between 0 and 1")
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