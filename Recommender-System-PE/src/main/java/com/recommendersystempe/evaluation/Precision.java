package com.recommendersystempe.evaluation;

import java.util.List;
import java.util.Set;

import com.recommendersystempe.models.POI;

public class Precision {

    // Tem foco na qualidade das recomendações através da proporção de itens
    // relevantes no top-k dos itens recomendados - Focuses on the quality of
    // recommendations through the proportion of relevant items in the top-k of
    // recommended items
    // Precision@k = (Itens Relevantes nos Top-k)/k - Precision@k = (Relevant Items
    // in Top-k) / k
    public static double precisionAtK(List<POI> recommended, Set<POI> relevant, int k) {
        if (recommended.isEmpty() || k <= 0) return 0.0;
        
        int topK = Math.min(k, recommended.size());
        long relevantCount = 0;
        
        for (int i = 0; i < topK; i++) {
            POI poi = recommended.get(i);
            if (relevant.contains(poi)) {
                relevantCount++;
            }
        }
        
        return (double) relevantCount / topK;
    }
}
