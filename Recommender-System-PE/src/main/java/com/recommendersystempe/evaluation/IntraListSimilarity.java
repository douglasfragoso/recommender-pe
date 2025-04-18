package com.recommendersystempe.evaluation;

import com.recommendersystempe.models.POI;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IntraListSimilarity {
    
    private static List<String> allFeatures = new ArrayList<>();

    // Inicializa as features globais (chamar uma vez no startup do sistema)
    public static void initializeGlobalFeatures(List<POI> allPOIs) {
        allFeatures = allPOIs.stream()
            .flatMap(poi -> Stream.concat(
                poi.getHobbies().stream().map(h -> "HOBBIE_" + h.name()),
                Stream.concat(
                    poi.getMotivations().stream().map(m -> "MOTIV_" + m.name()),
                    poi.getThemes().stream().map(t -> "THEME_" + t.name())
                )
            ))
            .distinct()
            .collect(Collectors.toList());
    }

    public static double calculate(List<POI> recommendations) {
        if (recommendations.isEmpty() || allFeatures.isEmpty()) return 1.0;
        if (recommendations.size() == 1) return 1.0;

        List<RealVector> vectors = recommendations.stream()
            .map(IntraListSimilarity::convertToFeatureVector)
            .collect(Collectors.toList());

        double totalSimilarity = 0.0;
        int pairs = 0;

        for (int i = 0; i < vectors.size(); i++) {
            for (int j = i + 1; j < vectors.size(); j++) {
                RealVector v1 = vectors.get(i);
                RealVector v2 = vectors.get(j);
                double similarity = cosineSimilarity(v1, v2);
                totalSimilarity += similarity;
                pairs++;
            }
        }

        return pairs > 0 ? 1 - (totalSimilarity / pairs) : 1.0;
    }

    private static RealVector convertToFeatureVector(POI poi) {
        double[] vector = new double[allFeatures.size()];
        Set<String> poiFeatures = new HashSet<>();
        
        poi.getHobbies().forEach(h -> poiFeatures.add("HOBBIE_" + h.name()));
        poi.getMotivations().forEach(m -> poiFeatures.add("MOTIV_" + m.name()));
        poi.getThemes().forEach(t -> poiFeatures.add("THEME_" + t.name()));

        for (int i = 0; i < allFeatures.size(); i++) {
            vector[i] = poiFeatures.contains(allFeatures.get(i)) ? 1.0 : 0.0;
        }
        
        return new ArrayRealVector(vector);
    }

    private static double cosineSimilarity(RealVector v1, RealVector v2) {
        double dotProduct = v1.dotProduct(v2);
        double normProduct = v1.getNorm() * v2.getNorm();
        return normProduct == 0 ? 0 : dotProduct / normProduct;
    }
}