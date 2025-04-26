package com.recommendersystempe.evaluation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.recommendersystempe.models.POI;
import com.recommendersystempe.models.Address;
import com.recommendersystempe.enums.Hobbies;
import com.recommendersystempe.enums.Motivations;
import com.recommendersystempe.enums.Themes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class IntraListSimilarityTest {

    private POI createPOI(List<Hobbies> hobbies, List<Motivations> motivations, List<Themes> themes) {
        return new POI("TestPOI", "Description", motivations, hobbies, themes, new Address());
    }

    @BeforeEach
    public void setUp() {
        // Reinicializa as features globais antes de cada teste
        IntraListSimilarity.initializeGlobalFeatures(new ArrayList<>());
    }

    @Test
    public void testCalculate_EmptyRecommendations_ReturnsOne() {
        List<POI> recommendations = new ArrayList<>();
        double result = IntraListSimilarity.calculate(recommendations);
        assertEquals(1.0, result, 0.001, "Lista vazia deve retornar 1.0");
    }

    @Test
    public void testCalculate_SinglePOI_ReturnsOne() {
        POI poi = createPOI(
                Collections.singletonList(Hobbies.HIKING),
                Collections.singletonList(Motivations.CULTURE),
                Collections.singletonList(Themes.ADVENTURE)
        );
        IntraListSimilarity.initializeGlobalFeatures(Collections.singletonList(poi));
        List<POI> recommendations = Collections.singletonList(poi);
        double result = IntraListSimilarity.calculate(recommendations);
        assertEquals(1.0, result, 0.001, "Lista com um POI deve retornar 1.0");
    }

    @Test
    public void testCalculate_TwoIdenticalPOIs_ReturnsZero() {
        List<Hobbies> hobbies = Collections.singletonList(Hobbies.HIKING);
        List<Motivations> motivations = Collections.singletonList(Motivations.CULTURE);
        List<Themes> themes = Collections.singletonList(Themes.ADVENTURE);
        POI poi1 = createPOI(hobbies, motivations, themes);
        POI poi2 = createPOI(hobbies, motivations, themes);
        List<POI> allPOIs = Arrays.asList(poi1, poi2);
        IntraListSimilarity.initializeGlobalFeatures(allPOIs);
        List<POI> recommendations = Arrays.asList(poi1, poi2);
        double result = IntraListSimilarity.calculate(recommendations);
        assertEquals(0.0, result, 0.001, "POIs idênticos devem ter similaridade 1, diversidade 0");
    }

    @Test
    public void testCalculate_TwoDifferentPOIs_ReturnsHighDiversity() {
        POI poi1 = createPOI(
                Collections.singletonList(Hobbies.HIKING),
                Collections.emptyList(),
                Collections.emptyList()
        );
        POI poi2 = createPOI(
                Collections.emptyList(),
                Collections.singletonList(Motivations.CULTURE),
                Collections.emptyList()
        );
        List<POI> allPOIs = Arrays.asList(poi1, poi2);
        IntraListSimilarity.initializeGlobalFeatures(allPOIs);
        List<POI> recommendations = Arrays.asList(poi1, poi2);
        double result = IntraListSimilarity.calculate(recommendations);
        assertEquals(0.86, result, 0.01, "POIs diferentes devem retornar diversidade ~0.86");
    }

    @Test
    public void testCalculate_ThreePOIs_ReturnsCorrectDiversity() {
        POI poi1 = createPOI(
                Arrays.asList(Hobbies.HIKING, Hobbies.BIRD_WATCHING),
                Collections.singletonList(Motivations.CULTURE),
                Collections.emptyList()
        );
        POI poi2 = createPOI(
                Collections.singletonList(Hobbies.HIKING),
                Collections.singletonList(Motivations.RELAXATION),
                Collections.singletonList(Themes.ADVENTURE)
        );
        POI poi3 = createPOI(
                Collections.emptyList(),
                Collections.singletonList(Motivations.CULTURE),
                Collections.singletonList(Themes.NATURE)
        );
        List<POI> allPOIs = Arrays.asList(poi1, poi2, poi3);
        IntraListSimilarity.initializeGlobalFeatures(allPOIs);
        List<POI> recommendations = Arrays.asList(poi1, poi2, poi3);
        double result = IntraListSimilarity.calculate(recommendations);
        
        assertEquals(0.66, result, 0.01, "Diversidade calculada para três POIs deve ser ~0.66");
    }
}