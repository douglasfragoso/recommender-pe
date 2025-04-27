package com.recommendersystempe.evaluation;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.recommendersystempe.models.POI;

@SpringBootTest
public class ItemCoverageTest {

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
    public void testConstructor() {
        new ItemCoverage();
    }

    @Test
    public void testItemCoverage_EmptyRecommendations() {
        List<List<POI>> recommendations = List.of();
        double result = ItemCoverage.itemCoverage(recommendations, 5);
        assertEquals(0.0, result, "Lista vazia deve retornar cobertura 0");
    }

    @Test
    public void testItemCoverage_AllItemsCovered() {
        POI poi1 = createPOIWithId(1L);
        POI poi2 = createPOIWithId(2L);
        List<List<POI>> recommendations = List.of(
            List.of(poi1, poi2),
            List.of(poi1, poi2)
        );
        
        double result = ItemCoverage.itemCoverage(recommendations, 2);
        assertEquals(1.0, result, "Todos os itens recomendados devem ter cobertura 1.0");
    }

    @Test
    public void testItemCoverage_PartialCoverage() {
        POI poi1 = createPOIWithId(1L);
        List<List<POI>> recommendations = List.of(
            List.of(poi1),
            List.of(poi1)
        );
        
        double result = ItemCoverage.itemCoverage(recommendations, 5);
        assertEquals(0.2, result, "1 de 5 itens deve retornar cobertura 0.2");
    }

    @Test
    public void testItemCoverage_DuplicateItems() {
        POI poi1 = createPOIWithId(1L);
        List<List<POI>> recommendations = List.of(
            List.of(poi1, poi1, poi1),
            List.of(poi1)
        );
        
        double result = ItemCoverage.itemCoverage(recommendations, 3);
        assertEquals(0.33, result, 0.01, "1 item único de 3 deve retornar ~0.33");
    }

}