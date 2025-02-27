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
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.recommendersystempe.dtos.POIDTO;
import com.recommendersystempe.models.POI;
import com.recommendersystempe.models.Preferences;
import com.recommendersystempe.models.Recommendation;
import com.recommendersystempe.models.User;
import com.recommendersystempe.repositories.POIRepository;
import com.recommendersystempe.repositories.RecommendationRepository;
import com.recommendersystempe.repositories.UserRepository;
import com.recommendersystempe.service.exception.GeneralException;
import com.recommendersystempe.similarity.SimilarityCalculator;
import com.recommendersystempe.similarity.TFIDF;

@Service
public class RecommendationService {

    @Autowired
    private RecommendationRepository recommendationRepository;

    @Autowired
    private POIRepository poiRepository;

    @Autowired
    private UserRepository userRepository;

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

        // 8. Salvar recomendação no banco de dados
        Recommendation recommendation = new Recommendation();
        recommendation.setUser(user);
        recommendation.addPOI(sortedPois);
        recommendationRepository.save(recommendation);

        // 9. Converter para DTO e retornar top N
        return sortedPois.stream()
                .map(this::convertToDTO)
                .limit(6) // Top 6 recomendações
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