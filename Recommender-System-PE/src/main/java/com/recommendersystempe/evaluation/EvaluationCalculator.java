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

    public static UserEvaluationMetricsDTO calculateUserMetrics(
        List<POI> recommendedPois, 
        Set<POI> relevantItems, 
        int k
    ) {
        double precisionAtK = calculatePrecisionAtK(recommendedPois, relevantItems, k);
        double intraListSimilarity = IntraListSimilarity.calculate(recommendedPois);
        
        return new UserEvaluationMetricsDTO(
            precisionAtK,
            0, // HitRate será calculado separadamente
            intraListSimilarity
        );
    }

    private static double calculatePrecisionAtK(List<POI> recommended, Set<POI> relevant, int k) {
        if (recommended.isEmpty() || k <= 0) {
            return 0.0;
        }

        int topK = Math.min(k, recommended.size());
        int relevantCount = 0;

        for (int i = 0; i < topK; i++) {
            if (relevant.contains(recommended.get(i))) {
                relevantCount++;
            }
        }

        double precision = (double) relevantCount / topK;
        return Math.round(precision * 100.0) / 100.0; // Arredonda para 2 casas decimais
    }

    public static GlobalEvaluationMetricsDTO calculateGlobalMetrics(
        List<List<POI>> allRecommendations,
        List<Set<POI>> allRelevantItems,
        int totalItemsAvailable,
        int k,
        List<String> allSystemFeatures,
        List<POI> allPois 
    ) {
        // Verificação de consistência dos dados
        if (allRecommendations.size() != allRelevantItems.size()) {
            throw new IllegalArgumentException("As listas de recomendações e itens relevantes devem ter o mesmo tamanho");
        }

        List<Double> precisions = new ArrayList<>();
        List<Double> intraListSimilarities = new ArrayList<>();

        for (int i = 0; i < allRecommendations.size(); i++) {
            List<POI> recommendations = allRecommendations.get(i);
            Set<POI> relevantItems = allRelevantItems.get(i);
            
            // Verificação adicional para garantir que os POIs relevantes estão corretos
            if (recommendations == null || relevantItems == null) {
                continue;
            }

            double precision = calculatePrecisionAtK(recommendations, relevantItems, k);
            precisions.add(precision);
            
            double ils = IntraListSimilarity.calculate(recommendations);
            intraListSimilarities.add(ils);
        }

        // Cálculo das demais métricas
        double hitRateAtK = HitRate.hitRateAtK(allRecommendations, allRelevantItems, k);
        Map<String, Map<String, Double>> globalFeatureCoverage = 
            FeatureCoverageCalculator.calculateGlobalFeatureCoverage(allRecommendations, allSystemFeatures);
        Map<Long, Double> poiFrequency = 
            POIFrequency.calculatePoiFrequency(allRecommendations, allPois);

        // Cálculo estatístico
        double avgPrecision = calculateAverage(precisions);
        double stdDevPrecision = calculateStandardDeviation(precisions, avgPrecision);
        int sampleSize = precisions.size();
        double confidenceLevel = 0.95;
        double margin = computeMarginOfError(stdDevPrecision, sampleSize, confidenceLevel);
        
        margin = Math.round(margin * 100.0) / 100.0;
        double lowerBound = Math.max(0, Math.round((avgPrecision - margin) * 100.0) / 100.0);
        double upperBound = Math.min(1, Math.round((avgPrecision + margin) * 100.0) / 100.0);

        return new GlobalEvaluationMetricsDTO(
            avgPrecision,
            lowerBound,
            upperBound,
            hitRateAtK,
            ItemCoverage.itemCoverage(allRecommendations, totalItemsAvailable),
            calculateAverage(intraListSimilarities),
            globalFeatureCoverage,
            poiFrequency
        );
    }

    private static double calculateAverage(List<Double> values) {
        if (values.isEmpty()) return 0.0;
        double sum = 0.0;
        int count = 0;
        
        for (Double value : values) {
            if (value != null && !Double.isNaN(value)) {
                sum += value;
                count++;
            }
        }
        
        return count > 0 ? Math.round((sum / count) * 100.0) / 100.0 : 0.0;
    }

    private static double calculateStandardDeviation(List<Double> values, double mean) {
        if (values.size() <= 1) return 0.0;
        double sumSquares = 0.0;
        int count = 0;
        
        for (Double value : values) {
            if (value != null && !Double.isNaN(value)) {
                sumSquares += Math.pow(value - mean, 2);
                count++;
            }
        }
        
        double variance = sumSquares / (count - 1);
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