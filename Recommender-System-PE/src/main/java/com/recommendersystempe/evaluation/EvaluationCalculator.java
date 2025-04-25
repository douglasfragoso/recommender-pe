package com.recommendersystempe.evaluation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.distribution.TDistribution;

import com.recommendersystempe.dtos.GlobalEvaluationMetricsDTO;
import com.recommendersystempe.dtos.UserEvaluationMetricsDTO;
import com.recommendersystempe.models.POI;

public class EvaluationCalculator {

    public double positiveInterval;
    public double negativeInterval;

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
            POIFrequency.calculatePoiFrequency(allRecommendations, allPois); 

        double avgPrecision = calculateAverage(precisions);
        double stdDevPrecision = calculateStandardDeviation(precisions, avgPrecision);
        int sampleSize = precisions.size();
        double confidenceLevel = 0.95;
        double margin = computeMarginOfError(stdDevPrecision, sampleSize, confidenceLevel);
        
        margin = Math.round(margin * 100.0) / 100.0;
        double lowerBound = Math.max(0, Math.round((avgPrecision - margin) * 100.0) / 100.0);
        double upperBound = Math.min(1, Math.round((avgPrecision + margin) * 100.0) / 100.0);

        return new GlobalEvaluationMetricsDTO(
            calculateAverage(precisions),
            lowerBound,
            upperBound,
            calculateAverage(hitRates),
            ItemCoverage.itemCoverage(allRecommendations, totalItemsAvailable),
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

    private static double calculateStandardDeviation(List<Double> values, double mean) {
        if (values.size() <= 1) return 0.0;
        double sumSquares = 0.0;
        for (Double value : values) {
            sumSquares += Math.pow(value - mean, 2);
        }
        double variance = sumSquares / (values.size() - 1);
        return Math.sqrt(variance);
    }

    private static double computeMarginOfError(double stdDev, int sampleSize, double confidenceLevel) {
        if (sampleSize <= 1) return 0.0;
        int degreesOfFreedom = sampleSize - 1;
        try {
            TDistribution tDistribution = new TDistribution(degreesOfFreedom);
            double alpha = (1 - confidenceLevel);
            double tValue = tDistribution.inverseCumulativeProbability(1 - alpha / 2);
            return tValue * (stdDev / Math.sqrt(sampleSize));
        } catch (Exception e) {
            return 0.0;
        }
    }
}
