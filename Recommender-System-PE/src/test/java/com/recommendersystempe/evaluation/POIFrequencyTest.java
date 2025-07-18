package com.recommendersystempe.evaluation;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.recommendersystempe.models.POI;

@SpringBootTest
public class POIFrequencyTest {

    private POI createPOIWithId(Long id) {
        POI poi = new POI();
        try {
            java.lang.reflect.Field idField = POI.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(poi, id);
        } catch (Exception e) {
            throw new RuntimeException("Falha ao definir ID do POI via reflection", e);
        }
        return poi;
    }

    @Test
    public void testConstructor() {
        new POIFrequency();
    }

    @Test
    public void testCalculatePoiFrequency_EmptyRecommendations() {
        List<List<POI>> allRecommendations = new ArrayList<>();
        List<POI> allPois = Arrays.asList(createPOIWithId(1L), createPOIWithId(2L));

        Map<Long, Double> result = POIFrequency.calculatePoiFrequency(allRecommendations, allPois);

        assertEquals(0.0, result.get(1L), "POI 1 deve ter frequência 0");
        assertEquals(0.0, result.get(2L), "POI 2 deve ter frequência 0");
    }

    @Test
    public void testCalculatePoiFrequency_MixedRecommendations() {
        POI poi1 = createPOIWithId(1L);
        POI poi2 = createPOIWithId(2L);
        POI poi3 = createPOIWithId(3L);
        POI poi4 = createPOIWithId(4L);

        
        List<List<POI>> recommendations = Arrays.asList(
                Arrays.asList(poi1, poi2), 
                Arrays.asList(poi2, poi3) 
        );

        Map<Long, Double> result = POIFrequency.calculatePoiFrequency(
                recommendations,
                Arrays.asList(poi1, poi2, poi3, poi4));

        assertEquals(0.5, result.get(1L), "POI1 aparece em 1 de 2 listas");
        assertEquals(1.0, result.get(2L), "POI2 aparece em 2 de 2 listas");
        assertEquals(0.5, result.get(3L), "POI3 aparece em 1 de 2 listas");
        assertEquals(0.0, result.get(4L), "POI4 não aparece em nenhuma lista");
    }

    @Test
    public void testCalculatePoiFrequency_EmptyAllPois() {
        Map<Long, Double> result = POIFrequency.calculatePoiFrequency(
                List.of(List.of(createPOIWithId(1L))),
                new ArrayList<>());

        assertTrue(result.isEmpty(), "Mapa deve estar vazio quando allPois é vazio");
    }

    @Test
    public void testCalculatePoiFrequency_AllUsersRecommendSamePOI() {
        POI poi = createPOIWithId(1L);
        List<List<POI>> recommendations = List.of(
                List.of(poi),
                List.of(poi),
                List.of(poi));

        Map<Long, Double> result = POIFrequency.calculatePoiFrequency(recommendations, List.of(poi));

        assertEquals(1.0, result.get(1L), "Frequência deve ser 1.0 para 3/3 usuários");
    }

    @Test
    public void testCalculatePoiFrequency_OrderWithMixedIds() {
        POI poi3 = createPOIWithId(3L);
        POI poi1 = createPOIWithId(1L);
        POI poi2 = createPOIWithId(2L);

        List<List<POI>> recommendations = List.of(List.of(poi1, poi2, poi3));
        Map<Long, Double> result = POIFrequency.calculatePoiFrequency(
                recommendations,
                List.of(poi3, poi2, poi1));

        assertEquals(List.of(1L, 2L, 3L), new ArrayList<>(result.keySet()), "Ordenação por ID crescente");
    }
}