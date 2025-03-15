package com.recommendersystempe.evaluation;

import java.util.List;
import java.util.Set;

import com.recommendersystempe.dtos.GlobalEvaluationMetricsDTO;
import com.recommendersystempe.dtos.UserEvaluationMetricsDTO;
import com.recommendersystempe.models.POI;

public class EvaluationCalculator {

    // Método para calcular as métricas de avaliação de um usuário específico
    public static UserEvaluationMetricsDTO calculateUserMetrics(List<POI> recommendedPois, Set<POI> relevantItems, int k) {
        double precisionAtK = Precision.precisionAtK(recommendedPois, relevantItems, k);
        double recallAtK = Recall.recallAtK(recommendedPois, relevantItems, k);
        double f1ScoreAtK = F1Score.f1ScoreAtK(recommendedPois, relevantItems, k);

        return new UserEvaluationMetricsDTO(precisionAtK, recallAtK, f1ScoreAtK);
    }

    // Método para calcular as métricas globais de avaliação
    public static GlobalEvaluationMetricsDTO calculateGlobalMetrics(List<List<POI>> allRecommendations, 
                                                                   List<Set<POI>> allRelevantItems, 
                                                                   int totalItemsAvailable, 
                                                                   int k) {
        double hitRateAtK = HitRate.hitRateAtK(allRecommendations, allRelevantItems, k);
        double itemCoverage = ItemCovarage.itemCoverage(allRecommendations, totalItemsAvailable);

        return new GlobalEvaluationMetricsDTO(hitRateAtK, itemCoverage);
    }
}