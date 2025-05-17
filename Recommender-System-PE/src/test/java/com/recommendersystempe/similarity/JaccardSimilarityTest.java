package com.recommendersystempe.similarity;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JaccardSimilarityTest {

    List<String> userPreferences = Arrays.asList("CULTURE", "ADVENTURE", "ART");
    List<List<String>> poiCharacteristics = Arrays.asList(
            Arrays.asList("CULTURE", "ADVENTURE"),
            Arrays.asList("ENTERTAINMENT", "ART"),
            Arrays.asList("CULTURE", "ART"));
    List<String> terms = Arrays.asList("CULTURE", "ENTERTAINMENT", "ADVENTURE", "ART");
    RealVector userVector = TFIDF.toTFIDFVector(userPreferences, poiCharacteristics, terms);

    @Test
    public void testJaccardSimilarity() {
        List<RealVector> poiVectors = new ArrayList<>();
        for (List<String> poi : poiCharacteristics) {
            poiVectors.add(TFIDF.toTFIDFVector(poi, poiCharacteristics, terms));
        }

        assertAll(
                () -> assertEquals(0.5576296443369585,
                        JaccardSimilarity.jaccardSimilarity(userVector, poiVectors.get(0)), 1e-9),
                () -> assertEquals(0.11732197895543049,
                        JaccardSimilarity.jaccardSimilarity(userVector, poiVectors.get(1)), 1e-9),
                () -> assertEquals(0.3697253475527737,
                        JaccardSimilarity.jaccardSimilarity(userVector, poiVectors.get(2)), 1e-9));
    }

    @Test
    public void testConstructor() {
        JaccardSimilarity jaccardSimilarity = new JaccardSimilarity();
        assertNotNull(jaccardSimilarity);
    }

    @Test
    public void testPerfectSimilarity() {
        RealVector v1 = new ArrayRealVector(new double[] { 1.0, 2.0, 3.0 });
        RealVector v2 = new ArrayRealVector(new double[] { 1.0, 2.0, 3.0 });
        double similarity = JaccardSimilarity.jaccardSimilarity(v1, v2);
        assertEquals(1.0, similarity, 1e-9);
    }

    @Test
    public void testZeroVectorHandling() {
        RealVector zeroVector = new ArrayRealVector(new double[] { 0.0, 0.0, 0.0 });
        RealVector validVector = new ArrayRealVector(new double[] { 1.0, 2.0, 3.0 });

        assertEquals(0.0, JaccardSimilarity.jaccardSimilarity(zeroVector, validVector), 1e-9);

        assertEquals(0.0, JaccardSimilarity.jaccardSimilarity(zeroVector, zeroVector), 1e-9);
    }

    @Test
    public void testDifferentDimensions() {
        RealVector v1 = new ArrayRealVector(new double[] { 1.0, 2.0, 3.0 });
        RealVector v2 = new ArrayRealVector(new double[] { 1.0, 2.0 });

        assertThrows(IllegalArgumentException.class, () -> {
            JaccardSimilarity.jaccardSimilarity(v1, v2);
        });
    }

    @Test
    public void testPartialSimilarity() {
        RealVector v1 = new ArrayRealVector(new double[] { 1.0, 2.0, 3.0, 4.0 });
        RealVector v2 = new ArrayRealVector(new double[] { 1.0, 0.0, 3.0, 5.0 });

        double similarity = JaccardSimilarity.jaccardSimilarity(v1, v2);
        assertEquals(0.7272727272727273, similarity, 1e-9);
    }

    @Test
    public void testNegativeValues() {
        RealVector v1 = new ArrayRealVector(new double[] { -1.0, 2.0, -3.0 });
        RealVector v2 = new ArrayRealVector(new double[] { 1.0, -2.0, 3.0 });

        double similarity = JaccardSimilarity.jaccardSimilarity(v1, v2);
        assertEquals(-1.0, similarity, 1e-9);
    }
}
