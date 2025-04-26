package com.recommendersystempe.evaluation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.recommendersystempe.models.POI;
import com.recommendersystempe.models.Address;
import com.recommendersystempe.enums.Hobbies;
import com.recommendersystempe.enums.Motivations;
import com.recommendersystempe.enums.Themes;

import org.junit.jupiter.api.Test;

public class FeatureCoverageCalculatorTest {

    private POI createPOI(List<Hobbies> hobbies, List<Motivations> motivations, List<Themes> themes) {
        return new POI(
            "TestPOI", 
            "Description", 
            motivations, 
            hobbies, 
            themes, 
            new Address()
        );
    }

    @Test
    public void testEmptyRecommendations() {
        // Cenário
        List<List<POI>> allRecommendations = Collections.emptyList();
        List<String> allFeatures = Arrays.asList(
            "THEME_ADVENTURE", 
            "HOBBIE_HIKING", 
            "MOTIVATION_CULTURE"
        );

        // Ação
        Map<String, Map<String, Double>> result = 
            FeatureCoverageCalculator.calculateGlobalFeatureCoverage(allRecommendations, allFeatures);

        // Verificações
        assertEquals(0.0, result.get("themes").get("ADVENTURE"), "Tema sem uso deve ter cobertura 0");
        assertEquals(0.0, result.get("hobbies").get("HIKING"), "Hobbie sem uso deve ter cobertura 0");
        assertEquals(0.0, result.get("motivations").get("CULTURE"), "Motivação sem uso deve ter cobertura 0");
    }

    @Test
    public void testSingleUserWithFullFeatures() {
        // Cenário
        POI poi = createPOI(
            Arrays.asList(Hobbies.HIKING),
            Arrays.asList(Motivations.CULTURE),
            Arrays.asList(Themes.ADVENTURE)
        );
        List<List<POI>> recommendations = Arrays.asList(Arrays.asList(poi));
        List<String> allFeatures = Arrays.asList(
            "THEME_ADVENTURE", 
            "HOBBIE_HIKING", 
            "MOTIVATION_CULTURE"
        );

        // Ação
        Map<String, Map<String, Double>> result = 
            FeatureCoverageCalculator.calculateGlobalFeatureCoverage(recommendations, allFeatures);

        // Verificações
        assertEquals(1.0, result.get("themes").get("ADVENTURE"), "Tema presente deve ter cobertura total");
        assertEquals(1.0, result.get("hobbies").get("HIKING"), "Hobbie presente deve ter cobertura total");
        assertEquals(1.0, result.get("motivations").get("CULTURE"), "Motivação presente deve ter cobertura total");
    }

    @Test
    public void testFeatureNameFormatting() {
        // Cenário
        POI poi = createPOI(
            Arrays.asList(Hobbies.BIRD_WATCHING),
            Collections.emptyList(),
            Collections.emptyList()
        );
        List<List<POI>> recommendations = Arrays.asList(Arrays.asList(poi));
        List<String> allFeatures = Arrays.asList("HOBBIE_BIRD_WATCHING");

        // Ação
        Map<String, Map<String, Double>> result = 
            FeatureCoverageCalculator.calculateGlobalFeatureCoverage(recommendations, allFeatures);

        // Verificação
        assertTrue(
            result.get("hobbies").containsKey("BIRD WATCHING"),
            "Subtraço deve ser substituído por espaço"
        );
    }

    @Test
    public void testRoundingBehavior() {
        // Cenário
        POI poi = createPOI(
            Arrays.asList(Hobbies.HIKING),
            Collections.emptyList(),
            Collections.emptyList()
        );
        List<List<POI>> recommendations = Arrays.asList(
            Arrays.asList(poi),
            Arrays.asList(poi),
            Collections.emptyList()
        );
        List<String> allFeatures = Arrays.asList("HOBBIE_HIKING");

        // Ação
        Map<String, Map<String, Double>> result = 
            FeatureCoverageCalculator.calculateGlobalFeatureCoverage(recommendations, allFeatures);

        // Verificação
        assertEquals(0.67, result.get("hobbies").get("HIKING"), 0.01, "Arredondamento para duas casas decimais");
    }

    @Test
    public void testCategoryOrdering() {
        // Cenário
        Map<String, Map<String, Double>> result = 
            FeatureCoverageCalculator.calculateGlobalFeatureCoverage(Collections.emptyList(), Collections.emptyList());

        // Verificação
        List<String> expectedOrder = Arrays.asList("themes", "hobbies", "motivations");
        assertEquals(
            expectedOrder,
            new ArrayList<>(result.keySet()),
            "Ordem das categorias deve ser mantida"
        );
    }

    @Test
    public void testDuplicateFeaturesInSameUser() {
        // Cenário
        POI poi1 = createPOI(
            Arrays.asList(Hobbies.HIKING),
            Collections.emptyList(),
            Collections.emptyList()
        );
        POI poi2 = createPOI(
            Arrays.asList(Hobbies.HIKING),
            Collections.emptyList(),
            Collections.emptyList()
        );
        List<List<POI>> recommendations = Arrays.asList(
            Arrays.asList(poi1, poi2) // Mesmo hobbie em dois POIs
        );
        List<String> allFeatures = Arrays.asList("HOBBIE_HIKING");

        // Ação
        Map<String, Map<String, Double>> result = 
            FeatureCoverageCalculator.calculateGlobalFeatureCoverage(recommendations, allFeatures);

        // Verificação
        assertEquals(1.0, result.get("hobbies").get("HIKING"), "Deduplicação por usuário");
    }
}