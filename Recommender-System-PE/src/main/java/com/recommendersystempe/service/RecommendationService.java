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

import com.recommendersystempe.dtos.POIDTO;
import com.recommendersystempe.dtos.RecommendationDTO;
import com.recommendersystempe.dtos.ScoreDTO;
import com.recommendersystempe.models.POI;
import com.recommendersystempe.models.Preferences;
import com.recommendersystempe.models.Recommendation;
import com.recommendersystempe.models.Score;
import com.recommendersystempe.models.User;
import com.recommendersystempe.repositories.POIRepository;
import com.recommendersystempe.repositories.RecommendationRepository;
import com.recommendersystempe.repositories.ScoreRepository;
import com.recommendersystempe.repositories.UserRepository;
import com.recommendersystempe.service.exception.GeneralException;
import com.recommendersystempe.similarity.SimilarityCalculator;
import com.recommendersystempe.similarity.TFIDF;

import jakarta.persistence.EntityNotFoundException;

@Service
public class RecommendationService {

    @Autowired
    private RecommendationRepository recommendationRepository;

    @Autowired
    private POIRepository poiRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    @Transactional
    public List<POIDTO> recommendation(Preferences userPreferences) {
        User user = searchUser();

        // 1. Coletar todas as características do usuário
        List<String> userFeatures = getFeaturesFromPreferences(userPreferences);

        // 2. Coletar todos os POIs e suas características
        List<POI> allPois = poiRepository.findAll();
        List<List<String>> allPoiFeatures = allPois.stream()
                .map(this::getFeaturesFromPOI)
                .collect(Collectors.toList());

        // 3. Coletar todos os termos únicos
        Set<String> allTerms = new HashSet<>(userFeatures);
        allPoiFeatures.forEach(allTerms::addAll);
        List<String> terms = new ArrayList<>(allTerms);

        // 4. Gerar vetor TF-IDF do usuário
        RealVector userVector = TFIDF.toTFIDFVector(
                userFeatures,
                allPoiFeatures,
                terms);

        // 5. Normalizar vetor do usuário
        userVector = SimilarityCalculator.normalize(userVector);

        // 6. Calcular similaridade para cada POI
        Map<POI, Double> poiScores = new HashMap<>();
        for (int i = 0; i < allPois.size(); i++) {
            POI poi = allPois.get(i);

            // Gerar vetor TF-IDF do POI
            RealVector poiVector = TFIDF.toTFIDFVector(
                    allPoiFeatures.get(i),
                    allPoiFeatures,
                    terms);

            // Normalizar vetor do POI
            poiVector = SimilarityCalculator.normalize(poiVector);

            // Calcular similaridade combinada
            double similarity = SimilarityCalculator.combinedSimilarity(userVector, poiVector);
            poiScores.put(poi, similarity);
        }

        // 7. Ordenar POIs pela similaridade
        List<POI> sortedPois = poiScores.entrySet().stream()
                .sorted(Map.Entry.<POI, Double>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // 8. Limitar aos 5 primeiros POIs (para salvar e retornar)
        List<POI> top5Pois = sortedPois.stream()
                .limit(5) // ⬅️ Limite aqui
                .collect(Collectors.toList());

        // 9. Salvar recomendação (apenas os 5 POIs)
        Recommendation recommendation = new Recommendation();
        recommendation.setUser(user);
        recommendation.getPois().addAll(top5Pois); // ⬅️ Adiciona só os 5
        recommendationRepository.save(recommendation);

        // 10. Converter para DTO e retornar
        return top5Pois.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void score(Long recommendationId, List<ScoreDTO> scoreDTOs) {
        // Validações iniciais
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

        // Processar scores
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
        User user = searchUser(); // Obtém o usuário autenticado
        Page<Recommendation> recommendations = recommendationRepository.findAllByUserId(user.getId(), pageable);
        return recommendations.map(this::convertToDTO);
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
        // Mapear os POIs para POIDTO
        List<POIDTO> poiDTOs = recommendation.getPois().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new RecommendationDTO(
                recommendation.getId(),
                recommendation.getUser().getId(), // Apenas o ID do usuário
                poiDTOs // Lista de POIDTO
        );
    }

    private POIDTO convertToDTO(POI poi) {
        return new POIDTO(
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