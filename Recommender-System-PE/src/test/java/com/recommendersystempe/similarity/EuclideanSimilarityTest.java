package com.recommendersystempe.similarity;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.linear.RealVector;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EuclideanSimilarityTest {

    List<String> userPreferences = Arrays.asList("CULTURE", "ADVENTURE", "ART");
    List<List<String>> poiCharacteristics = Arrays.asList(
            Arrays.asList("CULTURE", "ADVENTURE"),
            Arrays.asList("ENTERTAINMENT", "ART"),
            Arrays.asList("CULTURE", "ART")
    );

    // Termos únicos - Unique terms
    List<String> terms = Arrays.asList("CULTURE", "ENTERTAINMENT", "ADVENTURE", "ART");

    // Vetor TF-IDF para o usuário - TF-IDF vector for user
    RealVector userVector = TFIDF.toTFIDFVector(userPreferences, poiCharacteristics, terms);

    @Test
    public void testEuclideanSimilarity() {
        // Vetores TF-IDF para os POIs - TF-IDF vectors for POIs
        List<RealVector> poiVectors = new ArrayList<>();
        for (List<String> poi : poiCharacteristics) {
            poiVectors.add(TFIDF.toTFIDFVector(poi, poiCharacteristics, terms));
        }

        assertAll(
                () -> assertEquals(EuclideanSimilarity.euclideanSimilarity(userVector, poiVectors.get(0)), 0.8638497748630014),
                () -> assertEquals(EuclideanSimilarity.euclideanSimilarity(userVector, poiVectors.get(1)), 0.6992486791279793),
                () -> assertEquals(EuclideanSimilarity.euclideanSimilarity(userVector, poiVectors.get(2)), 0.8059358917420593)
        );
    }

}
