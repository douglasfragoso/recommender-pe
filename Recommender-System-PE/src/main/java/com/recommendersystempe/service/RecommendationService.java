package com.recommendersystempe.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.math3.linear.RealVector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.recommendersystempe.dtos.RecommendationDTO;
import com.recommendersystempe.dtos.RecommendationPOIDTO;
import com.recommendersystempe.dtos.ScoreDTO;
import com.recommendersystempe.dtos.SimilarityMetricDTO;
import com.recommendersystempe.enums.Status;
import com.recommendersystempe.models.POI;
import com.recommendersystempe.models.Preferences;
import com.recommendersystempe.models.Recommendation;
import com.recommendersystempe.models.Score;
import com.recommendersystempe.models.SimilarityMetric;
import com.recommendersystempe.models.User;
import com.recommendersystempe.repositories.POIRepository;
import com.recommendersystempe.repositories.RecommendationRepository;
import com.recommendersystempe.repositories.ScoreRepository;
import com.recommendersystempe.repositories.UserRepository;
import com.recommendersystempe.service.exception.GeneralException;
import com.recommendersystempe.similarity.SimilarityCalculator;
import com.recommendersystempe.similarity.TFIDF;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Validator;

@Service
public class RecommendationService {

    @Autowired
    private Validator validator;

    @Autowired
    private RecommendationRepository recommendationRepository;

    @Autowired
    private POIRepository poiRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    @Transactional
    public RecommendationDTO recommendation(Preferences userPreferences) {
        User user = searchUser();

        List<String> userFeatures = getFeaturesFromPreferences(userPreferences);
         List<POI> allPois = poiRepository.findByStatus(Status.ACTIVE);
        List<List<String>> allPoiFeatures = allPois.stream()
                .map(this::getFeaturesFromPOI)
                .collect(Collectors.toList());

        Set<String> allTerms = new HashSet<>(userFeatures);
        allPoiFeatures.forEach(allTerms::addAll);
        List<String> terms = new ArrayList<>(allTerms);

        RealVector userVector = TFIDF.toTFIDFVector(userFeatures, allPoiFeatures, terms);
        userVector = SimilarityCalculator.normalize(userVector);

        Map<POI, SimilarityMetric> metricsMap = new HashMap<>();

        Map<POI, Double> poiScores = new HashMap<>();
        for (int i = 0; i < allPois.size(); i++) {
            POI poi = allPois.get(i);
            RealVector poiVector = TFIDF.toTFIDFVector(allPoiFeatures.get(i), allPoiFeatures, terms);
            poiVector = SimilarityCalculator.normalize(poiVector);

            double cosine = SimilarityCalculator.cosineSimilarity(userVector, poiVector);
            double euclidean = SimilarityCalculator.euclideanSimilarity(userVector, poiVector);
            double pearson = SimilarityCalculator.pearsonSimilarity(userVector, poiVector);
            double jaccard = SimilarityCalculator.jaccardSimilarity(userVector, poiVector);
            double average = SimilarityCalculator.combinedSimilarity(userVector, poiVector);

            SimilarityMetric metric = new SimilarityMetric(null, poi, cosine, euclidean, pearson, jaccard);

            Set<jakarta.validation.ConstraintViolation<SimilarityMetric>> violations = validator.validate(metric);
            if (!violations.isEmpty()) {
                String messages = violations.stream()
                        .map(v -> v.getPropertyPath() + " " + v.getMessage())
                        .collect(Collectors.joining(", "));
                throw new jakarta.validation.ConstraintViolationException(
                        "Invalid similarity metric: " + messages, violations);
            }

            metricsMap.put(poi, metric);
            poiScores.put(poi, average);
        }

        List<POI> sortedPois = poiScores.entrySet().stream()
                .sorted(Map.Entry.<POI, Double>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        List<POI> top5Pois = sortedPois.stream().limit(5).collect(Collectors.toList());

        Recommendation recommendation = new Recommendation();
        recommendation.setUser(user);
        recommendation.getPois().addAll(top5Pois);

        top5Pois.forEach(poi -> {
            SimilarityMetric metric = metricsMap.get(poi);
            if (metric != null) {
                metric.setRecommendation(recommendation);
                recommendation.addSimilarityMetric(metric);
            }
        });

        Set<jakarta.validation.ConstraintViolation<Recommendation>> recommendationViolations = validator
                .validate(recommendation);
        if (!recommendationViolations.isEmpty()) {
            String messages = recommendationViolations.stream()
                    .map(v -> v.getPropertyPath() + " " + v.getMessage())
                    .collect(Collectors.joining(", "));
            throw new jakarta.validation.ConstraintViolationException(
                    "Invalid recommendation: " + messages, recommendationViolations);
        }
        recommendationRepository.save(recommendation);

        return convertToDTO(recommendation);
    }

    @Transactional
    public void score(Long recommendationId, List<ScoreDTO> scoreDTOs) {
        // Validações iniciais - Initial validations
        if (scoreDTOs == null || scoreDTOs.isEmpty()) {
            throw new IllegalArgumentException("Scores list is empty");
        }

        Recommendation recommendation = recommendationRepository.findById(recommendationId)
                .orElseThrow(() -> new EntityNotFoundException("Recommendation not found"));

        User user = searchUser();
        if (!recommendation.getUser().getId().equals(user.getId())) {
            throw new GeneralException("Unauthorized to score this recommendation");
        }

        scoreDTOs.forEach(dto -> {
            if (dto.getPoiId() == null || dto.getScoreValue() == null) {
                throw new IllegalArgumentException("Invalid score data");
            }
            if (dto.getScoreValue() != 0 && dto.getScoreValue() != 1) {
                throw new IllegalArgumentException("Score must be 0 or 1");
            }
        });

        // Processar scores - Process scores
        scoreDTOs.forEach(dto -> {
            POI poi = poiRepository.findById(dto.getPoiId())
                    .orElseThrow(() -> new EntityNotFoundException("POI not found"));

            if (recommendation.getScores().stream().anyMatch(s -> s.getPoi().equals(poi))) {
                throw new IllegalStateException("POI already scored");
            }

            Score score = new Score();
            score.setPoi(poi);
            score.setScore(dto.getScoreValue());
            score.setRecommendation(recommendation);

            scoreRepository.save(score);
            recommendation.getScores().add(score);
        });
    }

    @Transactional(readOnly = true)
    public Page<RecommendationDTO> findAll(Pageable pageable) {
        Page<Recommendation> list = recommendationRepository.findAll(pageable);
        return list.map(this::convertToDTO);
    }

    @Transactional(readOnly = true)
    public RecommendationDTO findById(Long id) {
        Recommendation recommendation = recommendationRepository.findById(id)
                .orElseThrow(() -> new GeneralException("Recommendation not found, id does not exist: " + id));
        return convertToDTO(recommendation);
    }

    @Transactional(readOnly = true)
    public Page<RecommendationDTO> findAllByUserId(Pageable pageable) {
        User user = searchUser();
        Page<Recommendation> recommendations = recommendationRepository.findAllByUserId(user.getId(), pageable);
        return recommendations.map(this::convertToDTO);
    }

    @Transactional(readOnly = true)
    public List<SimilarityMetricDTO> findSimilarityMetricsByRecommendationId(Long recommendationId) {
        Recommendation recommendation = recommendationRepository
                .findByIdWithFullMetrics(recommendationId)
                .orElseThrow(() -> new EntityNotFoundException("Recommendation not found"));

        return recommendation.getSimilarityMetrics().stream()
                .map(SimilarityMetricDTO::new)
                .collect(Collectors.toList());
    }

    private List<String> getFeaturesFromPreferences(Preferences preferences) {
        List<String> features = new ArrayList<>();
        features.addAll(preferences.getMotivations().stream().map(Enum::toString).toList());
        features.addAll(preferences.getHobbies().stream().map(Enum::toString).toList());
        features.addAll(preferences.getThemes().stream().map(Enum::toString).toList());
        return features;
    }

    private List<String> getFeaturesFromPOI(POI poi) {
        List<String> features = new ArrayList<>();
        features.addAll(poi.getMotivations().stream().map(Enum::toString).toList());
        features.addAll(poi.getHobbies().stream().map(Enum::toString).toList());
        features.addAll(poi.getThemes().stream().map(Enum::toString).toList());
        return features;
    }

    private RecommendationDTO convertToDTO(Recommendation recommendation) {
        List<RecommendationPOIDTO> poiDTOs = recommendation.getPois().stream()
                .map(this::convertPOIDTO)
                .collect(Collectors.toList());

        return new RecommendationDTO(
                recommendation.getId(),
                poiDTOs);
    }

    private RecommendationPOIDTO convertPOIDTO(POI poi) {
        return new RecommendationPOIDTO(
                poi.getId(),
                poi.getName(),
                poi.getDescription(),
                poi.getAddress());
    }

    private User searchUser() {
        Authentication autenticado = SecurityContextHolder.getContext().getAuthentication();
        if (!(autenticado instanceof AnonymousAuthenticationToken)) {
            String userEmail = autenticado.getName();
            User user = userRepository.findByEmail(userEmail);
            if (user == null) {
                throw new GeneralException("User not found in database");
            }
            return user;
        }
        throw new GeneralException("User not authenticated");
    }
}