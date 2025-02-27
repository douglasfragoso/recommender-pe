package com.recommendersystempe.recommendation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.linear.RealVector;

public class ContentBasedRecommender {
    
      public static void main(String[] args) {
        // Exemplo de dados
        List<String> userPreferences = Arrays.asList("CULTURE", "ADVENTURE", "ART");
        List<List<String>> poiCharacteristics = Arrays.asList(
                Arrays.asList("CULTURE", "ADVENTURE"),
                Arrays.asList("ENTERTAINMENT", "ART"),
                Arrays.asList("CULTURE", "ART")
        );

        // Termos únicos
        List<String> terms = Arrays.asList("CULTURE", "ENTERTAINMENT", "ADVENTURE", "ART");

        // Vetor TF-IDF para o usuário
        RealVector userVector = TFIDF.toTFIDFVector(userPreferences, poiCharacteristics, terms);

        // Vetores TF-IDF para os POIs
        List<RealVector> poiVectors = new ArrayList<>();
        for (List<String> poi : poiCharacteristics) {
            poiVectors.add(TFIDF.toTFIDFVector(poi, poiCharacteristics, terms));
        }

        // Calcular similaridade de cosseno
        for (int i = 0; i < poiVectors.size(); i++) {
            double cosine = CosineSimilarity.cosineSimilarity(userVector, poiVectors.get(i));
            double euclidean = EuclideanSimilarity.euclideanSimilarity(userVector, poiVectors.get(i));
            double pearson = PearsonSimilarity.pearsonSimilarity(userVector, poiVectors.get(i));
            double similarity = (cosine + euclidean + pearson) / 3;
            System.out.println("Similaridade com POI " + i + ": " + similarity);
        }
    }
}
