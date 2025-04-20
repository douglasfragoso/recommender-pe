package com.recommendersystempe.evaluation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.recommendersystempe.dtos.GlobalEvaluationMetricsDTO;
import com.recommendersystempe.dtos.UserEvaluationMetricsDTO;
import com.recommendersystempe.models.POI;

public class EvaluationCalculator {

    public static UserEvaluationMetricsDTO calculateUserMetrics(
        List<POI> recommendedPois, 
        Set<POI> relevantItems, 
        int k
    ) {
        double precisionAtK = Precision.precisionAtK(recommendedPois, relevantItems, k);
        double hitRateAtK = HitRate.hitRateAtK(List.of(recommendedPois), List.of(relevantItems), k);
        double intraListSimilarity = IntraListSimilarity.calculate(recommendedPois);
        
        return new UserEvaluationMetricsDTO(
            precisionAtK,
            hitRateAtK,
            intraListSimilarity
        );
    }

    public static GlobalEvaluationMetricsDTO calculateGlobalMetrics(
        List<List<POI>> allRecommendations,
        List<Set<POI>> allRelevantItems,
        int totalItemsAvailable,
        int k,
        List<String> allSystemFeatures,
        List<POI> allPois 
    ) {
        List<Double> precisions = new ArrayList<>();
        List<Double> hitRates = new ArrayList<>();
        List<Double> intraListSimilarities = new ArrayList<>();

        for (int i = 0; i < allRecommendations.size(); i++) {
            UserEvaluationMetricsDTO userMetrics = calculateUserMetrics(
                allRecommendations.get(i),
                allRelevantItems.get(i),
                k
            );
            precisions.add(userMetrics.getPrecisionAtK());
            hitRates.add(userMetrics.getHitRateAtK());
            intraListSimilarities.add(userMetrics.getIntraListSimilarity());
        }

        Map<String, Map<String, Double>> globalFeatureCoverage = 
            FeatureCoverageCalculator.calculateGlobalFeatureCoverage(allRecommendations, allSystemFeatures);
        
        Map<Long, Double> poiFrequency = 
            POIFrequency.calculatePoiFrequency(allRecommendations, allPois); // <- AJUSTADO AQUI

        return new GlobalEvaluationMetricsDTO(
            calculateAverage(precisions),
            calculateAverage(hitRates),
            ItemCovarage.itemCoverage(allRecommendations, totalItemsAvailable),
            calculateAverage(intraListSimilarities),
            globalFeatureCoverage,
            poiFrequency 
        );
    }

    private static double calculateAverage(List<Double> values) {
        if (values.isEmpty()) return 0.0;
        double average = values.stream()
                .filter(d -> !Double.isNaN(d))
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
        
        return Math.round(average * 100.0) / 100.0;
    }
}
