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
public class PearsonSimilarityTest {

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
    public void testPearsonSimilarity() {
        // Vetores TF-IDF para os POIs - TF-IDF vectors for POIs
        List<RealVector> poiVectors = new ArrayList<>();
        for (List<String> poi : poiCharacteristics) {
            poiVectors.add(TFIDF.toTFIDFVector(poi, poiCharacteristics, terms));
        }

        assertAll(
                () -> assertEquals(PearsonSimilarity.pearsonSimilarity(userVector, poiVectors.get(0)), 0.949602276159548),
                () -> assertEquals(PearsonSimilarity.pearsonSimilarity(userVector, poiVectors.get(1)), 0.09297213632971896),
                () -> assertEquals(PearsonSimilarity.pearsonSimilarity(userVector, poiVectors.get(2)), 0.44035147702253435)
        );
    }

     @Test
    public void testConstructor() {
        PearsonSimilarity pearsonSimilarity = new PearsonSimilarity();
        assertNotNull(pearsonSimilarity);
    }

    @Test
    public void testPerfectPositiveCorrelation() {
        RealVector v1 = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
        RealVector v2 = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
        double similarity = PearsonSimilarity.pearsonSimilarity(v1, v2);
        assertEquals(1.0, similarity, 1e-9);
    }

    @Test
    public void testPerfectNegativeCorrelation() {
        RealVector v1 = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
        RealVector v2 = new ArrayRealVector(new double[]{3.0, 2.0, 1.0});
        double similarity = PearsonSimilarity.pearsonSimilarity(v1, v2);
        assertEquals(0.0, similarity, 1e-9);
    }

    @Test
    public void testZeroCorrelation() {
        RealVector v1 = new ArrayRealVector(new double[]{1.0, -1.0, 0.0});
        RealVector v2 = new ArrayRealVector(new double[]{0.0, 1.0, -1.0});
        double similarity = PearsonSimilarity.pearsonSimilarity(v1, v2);
        // Expected Pearson correlation: -0.5, mapped to ( -0.5 + 1 ) / 2 = 0.25
        assertEquals(0.25, similarity, 1e-9);
    }

}
