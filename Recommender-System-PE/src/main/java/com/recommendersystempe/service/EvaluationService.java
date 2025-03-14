package com.recommendersystempe.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.recommendersystempe.dtos.GlobalEvaluationMetricsDTO;
import com.recommendersystempe.dtos.UserEvaluationMetricsDTO;
import com.recommendersystempe.evaluation.F1Score;
import com.recommendersystempe.evaluation.HitRate;
import com.recommendersystempe.evaluation.ItemCovarage;
import com.recommendersystempe.evaluation.Precision;
import com.recommendersystempe.evaluation.Recall;
import com.recommendersystempe.models.POI;
import com.recommendersystempe.models.Recommendation;
import com.recommendersystempe.models.Score;
import com.recommendersystempe.models.User;
import com.recommendersystempe.repositories.POIRepository;
import com.recommendersystempe.repositories.RecommendationRepository;
import com.recommendersystempe.repositories.ScoreRepository;
import com.recommendersystempe.repositories.UserRepository;
import com.recommendersystempe.service.exception.GeneralException;

@Service
public class EvaluationService {

    @Autowired
    private RecommendationRepository recommendationRepository;

    @Autowired
    private POIRepository poiRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    @Transactional(readOnly = true)
    public UserEvaluationMetricsDTO evaluateUserRecommendations(Long userId, int k) {
        // Busca o usuário
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException("User not found with ID: " + userId));

        // Busca as recomendações para o usuário
        List<Recommendation> recommendations = recommendationRepository.findByUser(user.getId());

        // Extrai os POIs recomendados
        List<POI> recommendedPois = recommendations.stream()
                .flatMap(r -> r.getPois().stream())
                .collect(Collectors.toList());

        // Busca os POIs relevantes para o usuário (ex.: POIs que o usuário pontuou)
        Set<POI> relevantItems = scoreRepository.findByUser(user.getId()).stream()
                .map(Score::getPoi)
                .collect(Collectors.toSet());

        // Calcula as métricas
        double precisionAtK = Precision.precisionAtK(recommendedPois, relevantItems, k);
        double recallAtK = Recall.recallAtK(recommendedPois, relevantItems, k);
        double f1ScoreAtK = F1Score.f1ScoreAtK(recommendedPois, relevantItems, k);

        return new UserEvaluationMetricsDTO(precisionAtK, recallAtK, f1ScoreAtK);
    }

    @Transactional(readOnly = true)
    public GlobalEvaluationMetricsDTO evaluateGlobalMetrics(int k) {
        // Busca todos os usuários
        List<User> users = userRepository.findAll();

        // Lista de recomendações e itens relevantes para cada usuário
        List<List<POI>> allRecommendations = new ArrayList<>();
        List<Set<POI>> allRelevantItems = new ArrayList<>();

        for (User user : users) {
            // Busca as recomendações para o usuário
            List<Recommendation> recommendations = recommendationRepository.findByUser(user.getId());
            List<POI> recommendedPois = recommendations.stream()
                    .flatMap(r -> r.getPois().stream())
                    .collect(Collectors.toList());
            allRecommendations.add(recommendedPois);

            // Busca os POIs relevantes para o usuário
            Set<POI> relevantItems = scoreRepository.findByUser(user.getId()).stream()
                    .map(Score::getPoi)
                    .collect(Collectors.toSet());
            allRelevantItems.add(relevantItems);
        }

        // Busca o número total de POIs disponíveis
        int totalItemsAvailable = (int) poiRepository.count();

        // Calcula as métricas globais
        double hitRateAtK = HitRate.hitRateAtK(allRecommendations, allRelevantItems, k);
        double itemCoverage = ItemCovarage.itemCoverage(allRecommendations, totalItemsAvailable);

        return new GlobalEvaluationMetricsDTO(hitRateAtK, itemCoverage);
    }
}