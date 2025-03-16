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
import com.recommendersystempe.evaluation.EvaluationCalculator;
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
        // Busca o usuário - Find the user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException("User not found with ID: " + userId));

        // Busca as recomendações para o usuário - Find the recommendations for the user
        List<Recommendation> recommendations = recommendationRepository.findByUser(user.getId());

        // Extrai os POIs recomendados - Extract the recommended POIs
        List<POI> recommendedPois = recommendations.stream()
                .flatMap(r -> r.getPois().stream())
                .collect(Collectors.toList());

        // Busca os POIs relevantes para o usuário (ex.: POIs que o usuário pontuou) - Find the relevant POIs for the user (e.g.: POIs that the user scored)
        Set<POI> relevantItems = scoreRepository.findByUser(user.getId()).stream()
                .map(Score::getPoi)
                .collect(Collectors.toSet());

        // Calcula as métricas usando a classe EvaluationCalculator - Calculate the metrics using the EvaluationCalculator class
        return EvaluationCalculator.calculateUserMetrics(recommendedPois, relevantItems, k);
    }

    @Transactional(readOnly = true)
    public GlobalEvaluationMetricsDTO evaluateGlobalMetrics(int k) {
        // Busca todos os usuários - Find all users
        List<User> users = userRepository.findAll();

        // Lista de recomendações e itens relevantes para cada usuário - List of recommendations and relevant items for each user
        List<List<POI>> allRecommendations = new ArrayList<>();
        List<Set<POI>> allRelevantItems = new ArrayList<>();

        for (User user : users) {
            // Busca as recomendações para o usuário - Find the recommendations for the user
            List<Recommendation> recommendations = recommendationRepository.findByUser(user.getId());
            List<POI> recommendedPois = recommendations.stream()
                    .flatMap(r -> r.getPois().stream())
                    .collect(Collectors.toList());
            allRecommendations.add(recommendedPois);

            // Busca os POIs relevantes para o usuário - Find the relevant POIs for the user
            Set<POI> relevantItems = scoreRepository.findByUser(user.getId()).stream()
                    .map(Score::getPoi)
                    .collect(Collectors.toSet());
            allRelevantItems.add(relevantItems);
        }

        // Busca o número total de POIs disponíveis - Find the total number of available POIs
        int totalItemsAvailable = (int) poiRepository.count();

        // Calcula as métricas globais usando a classe EvaluationCalculator - Calculate the global metrics using the EvaluationCalculator class
        return EvaluationCalculator.calculateGlobalMetrics(allRecommendations, allRelevantItems, totalItemsAvailable, k);
    }
}