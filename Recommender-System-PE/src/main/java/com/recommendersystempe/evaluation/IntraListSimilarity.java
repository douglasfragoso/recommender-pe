package com.recommendersystempe.evaluation;

import com.recommendersystempe.models.POI;
import com.recommendersystempe.similarity.SimilarityCalculator;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IntraListSimilarity {

    private static List<String> allFeatures = new ArrayList<>();

    // Inicializa as features globais com prefixos consistentes
    public static void initializeGlobalFeatures(List<POI> allPOIs) {
        allFeatures = allPOIs.stream()
                .flatMap(poi -> Stream.concat(
                        poi.getHobbies().stream().map(h -> "HOBBIE_" + h.name()),
                        Stream.concat(
                                poi.getMotivations().stream().map(m -> "MOTIVATION_" + m.name()), // Prefixo fixo
                                poi.getThemes().stream().map(t -> "THEME_" + t.name()))))
                .distinct()
                .collect(Collectors.toList());
    }

    public static double calculate(List<POI> recommendations) {
        if (recommendations.isEmpty() || allFeatures.isEmpty())
            return 1.0;
        if (recommendations.size() == 1)
            return 1.0;

        // Normaliza os vetores antes do cálculo
        List<RealVector> vectors = recommendations.stream()
                .map(poi -> SimilarityCalculator.normalize(convertToFeatureVector(poi)))
                .collect(Collectors.toList());

        double totalSimilarity = 0.0;
        int pairs = 0;

        for (int i = 0; i < vectors.size(); i++) {
            for (int j = i + 1; j < vectors.size(); j++) {
                RealVector v1 = vectors.get(i);
                RealVector v2 = vectors.get(j);
                double similarity = SimilarityCalculator.combinedSimilarity(v1, v2); // Usa a similaridade combinada
                totalSimilarity += similarity;
                pairs++;
            }
        }

        double result = pairs > 0 ? 1 - (totalSimilarity / pairs) : 1.0;
        result = Math.round(result * 100.0) / 100.0; 
        
        return result;
    }

    private static RealVector convertToFeatureVector(POI poi) {
        double[] vector = new double[allFeatures.size()];
        Set<String> poiFeatures = new HashSet<>();

        // Prefixos consistentes com initializeGlobalFeatures
        poi.getHobbies().forEach(h -> poiFeatures.add("HOBBIE_" + h.name()));
        poi.getMotivations().forEach(m -> poiFeatures.add("MOTIVATION_" + m.name()));
        poi.getThemes().forEach(t -> poiFeatures.add("THEME_" + t.name()));

        for (int i = 0; i < allFeatures.size(); i++) {
            vector[i] = poiFeatures.contains(allFeatures.get(i)) ? 1.0 : 0.0;
        }

        return new ArrayRealVector(vector);
    }

    public static List<String> getAllFeatures() {
        return new ArrayList<>(allFeatures);
    }
}