package com.recommendersystempe.evaluation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.recommendersystempe.models.POI;

public class FeatureCoverageCalculator {

    public static Map<String, Map<String, Double>> calculateGlobalFeatureCoverage(
        List<List<POI>> allRecommendations,
        List<String> allSystemFeatures
    ) {
        Map<String, Integer> featureCounts = new HashMap<>();
        allSystemFeatures.forEach(feature -> featureCounts.put(feature, 0));

        // Contabiliza ocorrências
        for (List<POI> userRecs : allRecommendations) {
            userRecs.forEach(poi -> {
                poi.getHobbies().forEach(h -> featureCounts.merge("HOBBIE_" + h.name(), 1, Integer::sum));
                poi.getMotivations().forEach(m -> featureCounts.merge("MOTIVATION_" + m.name(), 1, Integer::sum));
                poi.getThemes().forEach(t -> featureCounts.merge("THEME_" + t.name(), 1, Integer::sum));
            });
        }

        // Organiza em categorias
        return organizeByCategory(featureCounts, allSystemFeatures.size());
    }

    private static Map<String, Map<String, Double>> organizeByCategory(
        Map<String, Integer> featureCounts,
        int totalFeatures
    ) {
        Map<String, Map<String, Double>> coverage = new HashMap<>();
        coverage.put("themes", new HashMap<>());
        coverage.put("hobbies", new HashMap<>());
        coverage.put("motivations", new HashMap<>());
    
        featureCounts.forEach((feature, count) -> {
            String[] parts = feature.split("_", 2);
            String category = switch (parts[0]) {
                case "THEME" -> "themes";
                case "HOBBIE" -> "hobbies";
                case "MOTIVATION" -> "motivations";
                default -> throw new IllegalArgumentException("Categoria desconhecida: " + parts[0]);
            };
            String featureName = formatFeatureName(parts[1]);
            double value = (double) count / totalFeatures;
    
            coverage.get(category).put(featureName, value);
        });
    
        return coverage;
    }

    private static String formatFeatureName(String rawName) {
        return rawName.replace('_', ' '); // Ex: "URBAN_ART" → "URBAN ART"
    }
}