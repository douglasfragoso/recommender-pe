package com.recommendersystempe.similarity;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.linear.RealVector;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TFIDFTest {

    List<String> userPreferences = Arrays.asList("CULTURE", "ADVENTURE", "ART");
    List<List<String>> poiCharacteristics = Arrays.asList(
            Arrays.asList("CULTURE", "ADVENTURE"),
            Arrays.asList("ENTERTAINMENT", "ART"),
            Arrays.asList("CULTURE", "ART"));
    List<String> terms = Arrays.asList("CULTURE", "ENTERTAINMENT", "ADVENTURE", "ART");

    @Test
    public void testTF() {
        double tf = TFIDF.tf(userPreferences, "CULTURE");

        assertEquals(tf, 0.3333333333333333);
    }

    @Test
    public void testIDF() {
        double idf = TFIDF.idf(poiCharacteristics, "CULTURE");

        assertEquals(idf, 0.28768207245178085);
    }

    @Test
    public void testTFIDF() {
        double tfidf = TFIDF.tfIdf(userPreferences, poiCharacteristics, "CULTURE");

        assertEquals(tfidf, 0.09589402415059362);
    }

    @Test
    public void testToTFIDFVector() {
        RealVector vector = TFIDF.toTFIDFVector(userPreferences, poiCharacteristics, terms);

        assertAll(
                () -> assertEquals(vector.getDimension(), 4),
                () -> assertEquals(vector.getEntry(0), 0.09589402415059362),
                () -> assertEquals(vector.getEntry(1), 0.0),
                () -> assertEquals(vector.getEntry(2), 0.23104906018664842),
                () -> assertEquals(vector.getEntry(3), 0.09589402415059362)
        );
       
    }

    @Test
    public void testConstructor() {
        TFIDF tfidf = new TFIDF();
        assertNotNull(tfidf);
    }
}