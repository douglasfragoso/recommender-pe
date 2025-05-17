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
            Arrays.asList("CULTURE", "ART"));

    List<String> terms = Arrays.asList("CULTURE", "ENTERTAINMENT", "ADVENTURE", "ART");

    RealVector userVector = TFIDF.toTFIDFVector(userPreferences, poiCharacteristics, terms);

    @Test
    public void testCombinedSimilarity() {
        List<RealVector> poiVectors = new ArrayList<>();
        for (List<String> poi : poiCharacteristics) {
            poiVectors.add(TFIDF.toTFIDFVector(poi, poiCharacteristics, terms));
        }

        double cosine1 = CosineSimilarity.cosineSimilarity(userVector, poiVectors.get(0));
        double euclidean1 = EuclideanSimilarity.euclideanSimilarity(userVector, poiVectors.get(0));
        double pearson1 = PearsonSimilarity.pearsonSimilarity(userVector, poiVectors.get(0));
        double jaccard1 = JaccardSimilarity.jaccardSimilarity(userVector, poiVectors.get(0));
        double expected1 = (cosine1 + euclidean1 + pearson1 + jaccard1) / 4;

        double cosine2 = CosineSimilarity.cosineSimilarity(userVector, poiVectors.get(1));
        double euclidean2 = EuclideanSimilarity.euclideanSimilarity(userVector, poiVectors.get(1));
        double pearson2 = PearsonSimilarity.pearsonSimilarity(userVector, poiVectors.get(1));
        double jaccard2 = JaccardSimilarity.jaccardSimilarity(userVector, poiVectors.get(1));
        double expected2 = (cosine2 + euclidean2 + pearson2 + jaccard2) / 4;

        double cosine3 = CosineSimilarity.cosineSimilarity(userVector, poiVectors.get(2));
        double euclidean3 = EuclideanSimilarity.euclideanSimilarity(userVector, poiVectors.get(2));
        double pearson3 = PearsonSimilarity.pearsonSimilarity(userVector, poiVectors.get(2));
        double jaccard3 = JaccardSimilarity.jaccardSimilarity(userVector, poiVectors.get(2));
        double expected3 = (cosine3 + euclidean3 + pearson3 + jaccard3) / 4;

        assertAll(
                () -> assertEquals(expected1, SimilarityCalculator.combinedSimilarity(userVector, poiVectors.get(0)),
                        1e-9),
                () -> assertEquals(expected2, SimilarityCalculator.combinedSimilarity(userVector, poiVectors.get(1)),
                        1e-9),
                () -> assertEquals(expected3, SimilarityCalculator.combinedSimilarity(userVector, poiVectors.get(2)),
                        1e-9));
    }

    @Test
    public void testConstructor() {
        SimilarityCalculator calculator = new SimilarityCalculator();
        assertNotNull(calculator); 
    }

    @Test
    public void testNormalizeZeroVector() {
        RealVector zeroVector = new ArrayRealVector(new double[] { 0.0, 0.0, 0.0 });
        RealVector result = SimilarityCalculator.normalize(zeroVector);
        assertArrayEquals(new double[] { 0.0, 0.0, 0.0 }, result.toArray(), 1e-9); 
    }

    @Test
    public void testNormalizeNonZeroVector() {
        RealVector vector = new ArrayRealVector(new double[] { 3.0, 4.0 });
        RealVector normalized = SimilarityCalculator.normalize(vector);
        double expectedNorm = 5.0; 
        assertArrayEquals(new double[] { 3.0 / expectedNorm, 4.0 / expectedNorm }, normalized.toArray(), 1e-9);
    }

    @Test
    public void testCombinedSimilarityWithZeroVectors() {
        RealVector zeroVector = new ArrayRealVector(new double[] { 0.0, 0.0 });
        double result = SimilarityCalculator.combinedSimilarity(zeroVector, zeroVector);

        assertTrue(Double.isNaN(result));
    }

    @Test
    public void testCombinedSimilarityPerfectMatch() {
        RealVector v1 = new ArrayRealVector(new double[] { 1.0, 2.0, 3.0 });
        RealVector v2 = new ArrayRealVector(new double[] { 1.0, 2.0, 3.0 });

  
        double result = SimilarityCalculator.combinedSimilarity(v1, v2);
        assertEquals(1.0, result, 1e-9);
    }

    @Test
    public void testCombinedSimilarityOrthogonalVectors() {
        RealVector v1 = new ArrayRealVector(new double[] { 1.0, 0.0 });
        RealVector v2 = new ArrayRealVector(new double[] { 0.0, 1.0 });

        double cosine = CosineSimilarity.cosineSimilarity(v1, v2);
        double euclidean = EuclideanSimilarity.euclideanSimilarity(v1, v2);
        double pearson = PearsonSimilarity.pearsonSimilarity(v1, v2);
        double jaccard = JaccardSimilarity.jaccardSimilarity(v1, v2);
        double expected = (cosine + euclidean + pearson + jaccard) / 4;

        double result = SimilarityCalculator.combinedSimilarity(v1, v2);
        assertEquals(expected, result, 1e-9);
    }
}
