package com.recommendersystempe.evaluation;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.recommendersystempe.models.POI;

public class POIFrequencyTest {

    private POI createPOIWithId(Long id) {
        POI poi = new POI();
        try {
            Field idField = POI.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(poi, id);
        } catch (Exception e) {
            throw new RuntimeException("Falha ao definir ID do POI via reflection", e);
        }
        return poi;
    }

    @Test
    public void testCalculatePoiFrequency_EmptyRecommendations() {
        // Cenário
        List<List<POI>> allRecommendations = new ArrayList<>();
        List<POI> allPois = Arrays.asList(
                createPOIWithId(1L),
                createPOIWithId(2L));

        // Ação
        Map<Long, Double> result = POIFrequency.calculatePoiFrequency(allRecommendations, allPois);

        // Verificação
        assertEquals(0.0, result.get(1L), "POI 1 deve ter frequência 0");
        assertEquals(0.0, result.get(2L), "POI 2 deve ter frequência 0");
        assertEquals(2, result.size(), "Mapa deve conter todos os POIs");
    }

    @Test
    public void testCalculatePoiFrequency_MixedRecommendations() {
        // Cenário
        POI poi1 = createPOIWithId(1L);
        POI poi2 = createPOIWithId(2L);
        POI poi3 = createPOIWithId(3L);
        POI poi4 = createPOIWithId(4L);

        List<List<POI>> allRecommendations = Arrays.asList(
                Arrays.asList(poi1, poi2, poi2), 
                Arrays.asList(poi2, poi3) 
        );

        List<POI> allPois = Arrays.asList(poi1, poi2, poi3, poi4);

        // Ação
        Map<Long, Double> result = POIFrequency.calculatePoiFrequency(allRecommendations, allPois);

        // Verificações
        assertEquals(0.5, result.get(1L), "POI 1 deve ter frequência 0.5");
        assertEquals(1.0, result.get(2L), "POI 2 deve ter frequência 1.0");
        assertEquals(0.5, result.get(3L), "POI 3 deve ter frequência 0.5");
        assertEquals(0.0, result.get(4L), "POI 4 deve ter frequência 0.0");
    }

    @Test
    public void testCalculatePoiFrequency_Rounding() {
        // Cenário
        POI poi1 = createPOIWithId(1L);
        List<List<POI>> allRecommendations = Arrays.asList(
                Arrays.asList(poi1), 
                new ArrayList<>(), 
                new ArrayList<>() 
        );

        // Ação
        Map<Long, Double> result = POIFrequency.calculatePoiFrequency(allRecommendations, Arrays.asList(poi1));

        // Verificação
        assertEquals(0.33, result.get(1L), 0.01, "Frequência deve ser arredondada para 0.33");
    }

    @Test
    public void testCalculatePoiFrequency_Ordering() {
        // Cenário
        POI poi3 = createPOIWithId(3L);
        POI poi1 = createPOIWithId(1L);
        POI poi2 = createPOIWithId(2L);

        List<List<POI>> allRecommendations = Arrays.asList(
                Arrays.asList(poi1, poi2, poi3));

        // Ação
        Map<Long, Double> result = POIFrequency.calculatePoiFrequency(
                allRecommendations,
                Arrays.asList(poi3, poi1, poi2) 
        );

        // Verificação
        List<Long> actualOrder = new ArrayList<>(result.keySet());
        List<Long> expectedOrder = Arrays.asList(1L, 2L, 3L);

        assertEquals(expectedOrder, actualOrder, "IDs devem estar em ordem crescente");
    }

    @Test
    public void testCalculatePoiFrequency_DuplicateInSameUser() {
        // Cenário
        POI poi1 = createPOIWithId(1L);
        List<List<POI>> allRecommendations = Arrays.asList(
                Arrays.asList(poi1, poi1, poi1) 
        );

        // Ação
        Map<Long, Double> result = POIFrequency.calculatePoiFrequency(allRecommendations, Arrays.asList(poi1));

        // Verificação
        assertEquals(1.0, result.get(1L), "Deve contar como 1 ocorrência por usuário");
    }
}