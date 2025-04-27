package com.recommendersystempe.similarity;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SimilarityCalculatorTest {

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
    public void testCosineSimilarity() {
        // Vetores TF-IDF para os POIs - TF-IDF vectors for POIs
        List<RealVector> poiVectors = new ArrayList<>();
        for (List<String> poi : poiCharacteristics) {
            poiVectors.add(TFIDF.toTFIDFVector(poi, poiCharacteristics, terms));
        }

        assertAll(
                () -> assertEquals(SimilarityCalculator.combinedSimilarity(userVector, poiVectors.get(0)), 0.6357736830479804),
                () -> assertEquals(SimilarityCalculator.combinedSimilarity(userVector, poiVectors.get(1)), 0.2686714370864634),
                () -> assertEquals(SimilarityCalculator.combinedSimilarity(userVector, poiVectors.get(2)), 0.4246247867893258)
        );
    }

    @Test
    public void testConstructor() {
        SimilarityCalculator calculator = new SimilarityCalculator();
        assertNotNull(calculator); // Cobre o construtor não testado
    }

    @Test
    public void testNormalizeZeroVector() {
        RealVector zeroVector = new ArrayRealVector(new double[]{0.0, 0.0, 0.0});
        RealVector result = SimilarityCalculator.normalize(zeroVector);
        assertArrayEquals(new double[]{0.0, 0.0, 0.0}, result.toArray(), 1e-9); // Norma zero → vetor inalterado
    }

    @Test
    public void testNormalizeNonZeroVector() {
        RealVector vector = new ArrayRealVector(new double[]{3.0, 4.0});
        RealVector normalized = SimilarityCalculator.normalize(vector);
        double expectedNorm = 5.0; // sqrt(3² + 4²) = 5
        assertArrayEquals(new double[]{3.0/expectedNorm, 4.0/expectedNorm}, normalized.toArray(), 1e-9);
    }

    @Test
    public void testCombinedSimilarityWithZeroVectors() {
        RealVector zeroVector = new ArrayRealVector(new double[]{0.0, 0.0});
        double result = SimilarityCalculator.combinedSimilarity(zeroVector, zeroVector);

        assertTrue(Double.isNaN(result)); // Média com NaN resulta em NaN
    }

    @Test
    public void testCombinedSimilarityPerfectMatch() {
        RealVector v1 = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
        RealVector v2 = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
        double result = SimilarityCalculator.combinedSimilarity(v1, v2);
  
        assertEquals(16.0/3.0, result, 1e-9);
    }

    @Test
    public void testCombinedSimilarityOrthogonalVectors() {
        RealVector v1 = new ArrayRealVector(new double[]{1.0, 0.0});
        RealVector v2 = new ArrayRealVector(new double[]{0.0, 1.0});
        double result = SimilarityCalculator.combinedSimilarity(v1, v2);
    
        double expected = (0.0 + 1/(1 + Math.sqrt(2)) + 0.0) / 3;
        assertEquals(expected, result, 1e-9);
    }
}
