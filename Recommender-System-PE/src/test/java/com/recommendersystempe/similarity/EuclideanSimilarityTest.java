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

    @Test
    public void testConstructor() {
        EuclideanSimilarity euclideanSimilarity = new EuclideanSimilarity();
        assertNotNull(euclideanSimilarity); // Cobre o construtor não testado
    }

    @Test
    public void testZeroDistance() {
        RealVector identicalVector = new ArrayRealVector(new double[]{2.5, 3.1, 4.7});
        double similarity = EuclideanSimilarity.euclideanSimilarity(identicalVector, identicalVector);
        assertEquals(1.0, similarity, 1e-9); // Distância zero = similaridade máxima (1.0)
    }

    @Test
    public void testHighDistance() {
        RealVector v1 = new ArrayRealVector(new double[]{0.0, 0.0});
        RealVector v2 = new ArrayRealVector(new double[]{3.0, 4.0}); // Distância Euclidiana = 5.0
        double similarity = EuclideanSimilarity.euclideanSimilarity(v1, v2);
        assertEquals(1.0 / (1.0 + 5.0), similarity, 1e-9); // 1/(1+5) ≈ 0.166666...
    }

    @Test
    public void testMixedZeroVector() {
        RealVector zeroVector = new ArrayRealVector(new double[]{0.0, 0.0, 0.0});
        RealVector nonZeroVector = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
        double similarity = EuclideanSimilarity.euclideanSimilarity(zeroVector, nonZeroVector);
        double expected = 1.0 / (1.0 + Math.sqrt(14)); // sqrt(1² + 2² + 3²) = sqrt(14)
        assertEquals(expected, similarity, 1e-9);
    }

    @Test
    public void testEmptyVectors() {
        RealVector emptyVector = new ArrayRealVector(new double[]{});
        RealVector otherEmptyVector = new ArrayRealVector(new double[]{});
        double similarity = EuclideanSimilarity.euclideanSimilarity(emptyVector, otherEmptyVector);
        assertEquals(1.0, similarity, 1e-9); // Vetores vazios têm distância zero
    }

}
