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

        // Count unique features per user
        for (List<POI> userRecs : allRecommendations) {
            Map<String, Boolean> userFeatures = new HashMap<>();
            
            userRecs.forEach(poi -> {
                poi.getHobbies().forEach(h -> userFeatures.putIfAbsent("HOBBIE_" + h.name(), true));
                poi.getMotivations().forEach(m -> userFeatures.putIfAbsent("MOTIVATION_" + m.name(), true));
                poi.getThemes().forEach(t -> userFeatures.putIfAbsent("THEME_" + t.name(), true));
            });
            
            // Increment counts for each unique feature this user received
            userFeatures.keySet().forEach(f -> featureCounts.merge(f, 1, Integer::sum));
        }

        // Organize by category - now using user count as denominator
        return organizeByCategory(featureCounts, allRecommendations.size());
    }

    private static Map<String, Map<String, Double>> organizeByCategory(
        Map<String, Integer> featureCounts,
        double totalUsers
    ) {
        Map<String, Map<String, Double>> coverage = new HashMap<>();
        coverage.put("themes", new HashMap<>());
        coverage.put("hobbies", new HashMap<>());
        coverage.put("motivations", new HashMap<>());
    
        featureCounts.forEach((feature, userCount) -> {
            String[] parts = feature.split("_", 2);
            String category = switch (parts[0]) {
                case "THEME" -> "themes";
                case "HOBBIE" -> "hobbies";
                case "MOTIVATION" -> "motivations";
                default -> throw new IllegalArgumentException("Categoria desconhecida: " + parts[0]);
            };
            String featureName = formatFeatureName(parts[1]);
            double coverageValue = Math.round((userCount / totalUsers) * 100.0) / 100.0; 
    
            coverage.get(category).put(featureName, coverageValue);
        });
    
        return coverage;
    }

    private static String formatFeatureName(String rawName) {
        return rawName.replace('_', ' ');
    }
}