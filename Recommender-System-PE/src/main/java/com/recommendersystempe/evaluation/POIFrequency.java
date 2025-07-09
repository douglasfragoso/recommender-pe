package com.recommendersystempe.evaluation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.recommendersystempe.models.POI;

public class POIFrequency {
    public static Map<Long, Double> calculatePoiFrequency(
            List<List<POI>> allRecommendations,
            List<POI> allPois) {
        Map<Long, Integer> poiCounts = new HashMap<>();
        int totalLists = allRecommendations.size();

        // Conta todas ocorrências em todas as listas
        for (List<POI> recommendations : allRecommendations) {
            for (POI poi : recommendations) {
                long poiId = poi.getId();
                poiCounts.put(poiId, poiCounts.getOrDefault(poiId, 0) + 1);
            }
        }

        Map<Long, Double> frequencyMap = new TreeMap<>();
        for (POI poi : allPois) {
            long poiId = poi.getId();
            int count = poiCounts.getOrDefault(poiId, 0);
            double frequency = (double) count / totalLists;
            frequencyMap.put(poiId, Math.round(frequency * 100.0) / 100.0);
        }

        return frequencyMap;
    }
}
