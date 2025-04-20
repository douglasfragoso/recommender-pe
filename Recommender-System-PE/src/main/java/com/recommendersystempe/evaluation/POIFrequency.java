package com.recommendersystempe.evaluation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.recommendersystempe.models.POI;

public class POIFrequency {

    public static Map<Long, Double> calculatePoiFrequency(List<List<POI>> allRecommendations, List<POI> allPois) {
        Map<Long, Integer> poiCounts = new HashMap<>();
        int totalUsers = allRecommendations.size();

        for (List<POI> userRecommendations : allRecommendations) {
            Set<Long> uniquePois = userRecommendations.stream()
                .map(POI::getId)
                .collect(Collectors.toSet());

            uniquePois.forEach(poiId ->
                poiCounts.put(poiId, poiCounts.getOrDefault(poiId, 0) + 1)
            );
        }

        // Usar TreeMap para garantir ordenação natural (crescente) por ID
        Map<Long, Double> frequencyMap = new TreeMap<>();

        for (POI poi : allPois) {
            long poiId = poi.getId();
            int count = poiCounts.getOrDefault(poiId, 0);
            double frequency = Math.round((count / (double) totalUsers) * 100.0) / 100.0;
            frequencyMap.put(poiId, frequency);
        }

        return frequencyMap;
    }
}
