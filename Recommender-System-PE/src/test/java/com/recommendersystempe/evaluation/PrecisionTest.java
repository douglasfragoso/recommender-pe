package com.recommendersystempe.evaluation;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.recommendersystempe.models.POI;

@SpringBootTest
public class PrecisionTest {

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
        new Precision();
    }

    @Test
    public void testPrecisionAtK_KGreaterThanListSize() {
        POI relevantPoi = createPOIWithId(1L);
        List<POI> recommended = List.of(relevantPoi, createPOIWithId(2L));
        Set<POI> relevant = Set.of(relevantPoi);
        
        double result = Precision.precisionAtK(recommended, relevant, 5);
        assertEquals(0.5, result, "k=5 com 2 itens (1 relevante) → 0.5");
    }

    @Test
    public void testPrecisionAtK_AllRelevant() {
        POI poi1 = createPOIWithId(1L);
        POI poi2 = createPOIWithId(2L);
        List<POI> recommended = List.of(poi1, poi2);
        Set<POI> relevant = Set.of(poi1, poi2);
        
        double result = Precision.precisionAtK(recommended, relevant, 2);
        assertEquals(1.0, result, "Todos os itens relevantes → precisão 1.0");
    }

    @Test
    public void testPrecisionAtK_NoRelevantItems() {
        POI poi1 = createPOIWithId(1L);
        List<POI> recommended = List.of(poi1, createPOIWithId(2L));
        Set<POI> relevant = Set.of();
        
        double result = Precision.precisionAtK(recommended, relevant, 2);
        assertEquals(0.0, result, "Nenhum item relevante → precisão 0.0");
    }

    @Test
    public void testPrecisionAtK_DuplicateRelevantItems() {
        POI relevantPoi = createPOIWithId(1L);
        List<POI> recommended = List.of(relevantPoi, relevantPoi, createPOIWithId(2L));
        Set<POI> relevant = Set.of(relevantPoi);
        
        double result = Precision.precisionAtK(recommended, relevant, 3);
        assertEquals(0.666, result, 0.001, "Duas ocorrências do mesmo relevante → 2/3");
    }
}