package com.recommendersystempe.evaluation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.recommendersystempe.models.POI;

public class FeatureCoverageCalculator {

    public static Map<String, Map<String, Double>> calculateGlobalFeatureCoverage(
        List<List<POI>> allRecommendations,
        List<String> allSystemFeatures
    ) {
        Map<String, Integer> featureListCounts = new HashMap<>();  // Contagem de listas que contêm a feature
        allSystemFeatures.forEach(feature -> featureListCounts.put(feature, 0));

        for (List<POI> recommendations : allRecommendations) {
            // Conjunto de features únicas nesta lista específica
            Set<String> featuresInThisList = new HashSet<>();
            
            for (POI poi : recommendations) {
                poi.getHobbies().forEach(h -> featuresInThisList.add("HOBBIE_" + h.name()));
                poi.getMotivations().forEach(m -> featuresInThisList.add("MOTIVATION_" + m.name()));
                poi.getThemes().forEach(t -> featuresInThisList.add("THEME_" + t.name()));
            }
            
            // Incrementar contador apenas UMA VEZ por lista para cada feature
            for (String feature : featuresInThisList) {
                featureListCounts.put(feature, featureListCounts.getOrDefault(feature, 0) + 1);
            }
        }

        return organizeByCategory(featureListCounts, allRecommendations.size());
    }

    private static Map<String, Map<String, Double>> organizeByCategory(
        Map<String, Integer> featureListCounts,
        int totalRecommendations
    ) {
        Map<String, Map<String, Double>> coverage = new LinkedHashMap<>();
        coverage.put("themes", new TreeMap<>());
        coverage.put("hobbies", new TreeMap<>());
        coverage.put("motivations", new TreeMap<>());

        featureListCounts.forEach((feature, count) -> {
            String[] parts = feature.split("_", 2);
            String category = switch (parts[0]) {
                case "THEME" -> "themes";
                case "HOBBIE" -> "hobbies";
                case "MOTIVATION" -> "motivations";
                default -> throw new IllegalArgumentException("Categoria desconhecida: " + parts[0]);
            };
            
            String featureName = formatFeatureName(parts[1]);
            double coverageValue = (double) count / totalRecommendations;
            coverageValue = Math.round(coverageValue * 100.0) / 100.0;  // Arredonda para 2 decimais
            
            coverage.get(category).put(featureName, coverageValue);
        });

        return coverage;
    }
    
    private static String formatFeatureName(String rawName) {
        return rawName.replace('_', ' ');
    }
}