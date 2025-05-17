package com.recommendersystempe.evaluation;

import static org.junit.jupiter.api.Assertions.*;

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
                new Address());
    }

    @Test
    public void testEmptyRecommendations() {
        // given / arrange
        List<List<POI>> allRecommendations = Collections.emptyList();
        List<String> allFeatures = Arrays.asList(
                "THEME_ADVENTURE",
                "HOBBIE_HIKING",
                "MOTIVATION_CULTURE");

        // when / act
        Map<String, Map<String, Double>> result = FeatureCoverageCalculator
                .calculateGlobalFeatureCoverage(allRecommendations, allFeatures);

        // then / assert
        assertEquals(0.0, result.get("themes").get("ADVENTURE"), "Tema sem uso deve ter cobertura 0");
        assertEquals(0.0, result.get("hobbies").get("HIKING"), "Hobbie sem uso deve ter cobertura 0");
        assertEquals(0.0, result.get("motivations").get("CULTURE"), "Motivação sem uso deve ter cobertura 0");
    }

    @Test
    public void testSingleUserWithFullFeatures() {
        // given / arrange
        POI poi = createPOI(
                Arrays.asList(Hobbies.HIKING),
                Arrays.asList(Motivations.CULTURE),
                Arrays.asList(Themes.ADVENTURE));
        List<List<POI>> recommendations = Arrays.asList(Arrays.asList(poi));
        List<String> allFeatures = Arrays.asList(
                "THEME_ADVENTURE",
                "HOBBIE_HIKING",
                "MOTIVATION_CULTURE");

        // when / act
        Map<String, Map<String, Double>> result = FeatureCoverageCalculator
                .calculateGlobalFeatureCoverage(recommendations, allFeatures);

        // then / assert
        assertEquals(1.0, result.get("themes").get("ADVENTURE"), "Tema presente deve ter cobertura total");
        assertEquals(1.0, result.get("hobbies").get("HIKING"), "Hobbie presente deve ter cobertura total");
        assertEquals(1.0, result.get("motivations").get("CULTURE"), "Motivação presente deve ter cobertura total");
    }

    @Test
    public void testFeatureNameFormatting() {
        // given / arrange
        POI poi = createPOI(
                Arrays.asList(Hobbies.BIRD_WATCHING),
                Collections.emptyList(),
                Collections.emptyList());
        List<List<POI>> recommendations = Arrays.asList(Arrays.asList(poi));
        List<String> allFeatures = Arrays.asList("HOBBIE_BIRD_WATCHING");

        // when / act
        Map<String, Map<String, Double>> result = FeatureCoverageCalculator
                .calculateGlobalFeatureCoverage(recommendations, allFeatures);

        // then / assert
        assertTrue(
                result.get("hobbies").containsKey("BIRD WATCHING"),
                "Subtraço deve ser substituído por espaço");
    }

    @Test
    public void testRoundingBehavior() {
        // given / arrange
        POI poi = createPOI(
                Arrays.asList(Hobbies.HIKING),
                Collections.emptyList(),
                Collections.emptyList());
        List<List<POI>> recommendations = Arrays.asList(
                Arrays.asList(poi),
                Arrays.asList(poi),
                Collections.emptyList());
        List<String> allFeatures = Arrays.asList("HOBBIE_HIKING");

        // when / act
        Map<String, Map<String, Double>> result = FeatureCoverageCalculator
                .calculateGlobalFeatureCoverage(recommendations, allFeatures);

        // then / assert
        assertEquals(0.67, result.get("hobbies").get("HIKING"), 0.01, "Arredondamento para duas casas decimais");
    }

    @Test
    public void testCategoryOrdering() {
        // given / arrange
        Map<String, Map<String, Double>> result = FeatureCoverageCalculator
                .calculateGlobalFeatureCoverage(Collections.emptyList(), Collections.emptyList());

        // then / assert
        List<String> expectedOrder = Arrays.asList("themes", "hobbies", "motivations");
        assertEquals(
                expectedOrder,
                new ArrayList<>(result.keySet()),
                "Ordem das categorias deve ser mantida");
    }

    @Test
    public void testDuplicateFeaturesInSameUser() {
        // given / arrange
        POI poi1 = createPOI(
                Arrays.asList(Hobbies.HIKING),
                Collections.emptyList(),
                Collections.emptyList());
        POI poi2 = createPOI(
                Arrays.asList(Hobbies.HIKING),
                Collections.emptyList(),
                Collections.emptyList());
        List<List<POI>> recommendations = Arrays.asList(
                Arrays.asList(poi1, poi2) 
        );
        List<String> allFeatures = Arrays.asList("HOBBIE_HIKING");

        // when / act
        Map<String, Map<String, Double>> result = FeatureCoverageCalculator
                .calculateGlobalFeatureCoverage(recommendations, allFeatures);

        // then / assert
        assertEquals(1.0, result.get("hobbies").get("HIKING"), "Deduplicação por usuário");
    }

    @Test
    public void testConstructor() {
        // This test ensures that the constructor can be instantiated (if needed for coverage)
        new FeatureCoverageCalculator();
    }

    @Test
    public void testUnknownCategoryFeatureThrowsException() {
        List<String> allFeatures = Arrays.asList("INVALID_CATEGORY_FEATURE");
        List<List<POI>> recommendations = Collections.emptyList();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> FeatureCoverageCalculator.calculateGlobalFeatureCoverage(recommendations, allFeatures),
                "Deveria lançar exceção para categoria desconhecida");

        assertTrue(exception.getMessage().contains("Categoria desconhecida: INVALID"),
                "Mensagem de exceção deve indicar a categoria 'INVALID'");
    }
}