package com.recommendersystempe.evaluation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.recommendersystempe.dtos.GlobalEvaluationMetricsDTO;
import com.recommendersystempe.dtos.UserEvaluationMetricsDTO;
import com.recommendersystempe.models.POI;

public class EvaluationCalculator {

    // Método para calcular as métricas de avaliação de um usuário específico -
    // Method to calculate the evaluation metrics of a specific user
    public static UserEvaluationMetricsDTO calculateUserMetrics(List<POI> recommendedPois, Set<POI> relevantItems,
            int k) {
        double precisionAtK = Precision.precisionAtK(recommendedPois, relevantItems, k);

        return new UserEvaluationMetricsDTO(precisionAtK);
    }

    public static GlobalEvaluationMetricsDTO calculateGlobalMetrics(List<List<POI>> allRecommendations,
            List<Set<POI>> allRelevantItems,
            int totalItemsAvailable,
            int k) {
        // Listas para armazenar as métricas de cada usuário - Lists to store the
        // metrics of each user
        List<Double> precisions = new ArrayList<>();

        // Calcula métricas para cada usuário - Calculates metrics for each user
        for (int i = 0; i < allRecommendations.size(); i++) {
            UserEvaluationMetricsDTO userMetrics = calculateUserMetrics(
                    allRecommendations.get(i),
                    allRelevantItems.get(i),
                    k);
            precisions.add(userMetrics.getPrecisionAtK());
        }

        // Calcula as médias globais - Calculates the global averages
        double averagePrecision = calculateAverage(precisions);

        // Métricas existentes - Existing metrics
        double hitRateAtK = HitRate.hitRateAtK(allRecommendations, allRelevantItems, k);
        double itemCoverage = ItemCovarage.itemCoverage(allRecommendations, totalItemsAvailable);

        return new GlobalEvaluationMetricsDTO(
                averagePrecision,
                hitRateAtK,
                itemCoverage);
    }

    private static double calculateAverage(List<Double> values) {
        if (values.isEmpty()) return 0.0;
    
        double sum = 0.0;
        int count = 0;
    
        for (Double value : values) {
            if (!Double.isNaN(value)) {
                sum += value;
                count++;
            }
        }
    
        return (count == 0) ? 0.0 : sum / count;
    }
    
}