package com.recommendersystempe.dtos;

import com.recommendersystempe.models.POI;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SimilarityMetricDTO {
    private POI poi;
    private double cosine;
    private double euclidean;
    private double pearson;
    private double jaccard;
    private double average;
}