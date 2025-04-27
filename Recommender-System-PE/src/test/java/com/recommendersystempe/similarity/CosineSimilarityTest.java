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
public class CosineSimilarityTest {

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
                () -> assertEquals(CosineSimilarity.cosineSimilarity(userVector, poiVectors.get(0)), 0.933746255972236),
                () -> assertEquals(CosineSimilarity.cosineSimilarity(userVector, poiVectors.get(1)), 0.13720850674194446),
                () -> assertEquals(CosineSimilarity.cosineSimilarity(userVector, poiVectors.get(2)), 0.5061974505226827)
        );
    }

     @Test
    public void testConstructor() {
        CosineSimilarity cosineSimilarity = new CosineSimilarity();
        assertNotNull(cosineSimilarity); // Cobre o construtor não testado
    }

    @Test
    public void testPerfectSimilarity() {
        RealVector v1 = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
        RealVector v2 = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
        double similarity = CosineSimilarity.cosineSimilarity(v1, v2);
        assertEquals(1.0, similarity, 1e-9); // Verifica similaridade máxima
    }

    @Test
    public void testOrthogonalVectors() {
        RealVector v1 = new ArrayRealVector(new double[]{1.0, 0.0});
        RealVector v2 = new ArrayRealVector(new double[]{0.0, 1.0});
        double similarity = CosineSimilarity.cosineSimilarity(v1, v2);
        assertEquals(0.0, similarity, 1e-9); // Vetores ortogonais = similaridade zero
    }

    @Test
    public void testZeroVectorHandling() {
        RealVector zeroVector = new ArrayRealVector(new double[]{0.0, 0.0, 0.0});
        RealVector validVector = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
        
        // Verifica comportamento com vetor nulo
        assertTrue(Double.isNaN(CosineSimilarity.cosineSimilarity(zeroVector, validVector)));
        assertTrue(Double.isNaN(CosineSimilarity.cosineSimilarity(zeroVector, zeroVector)));
    }

}
