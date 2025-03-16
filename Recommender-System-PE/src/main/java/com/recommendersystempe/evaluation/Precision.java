package com.recommendersystempe.evaluation;

import java.util.List;
import java.util.Set;

import com.recommendersystempe.models.POI;

public class Precision {
    
    // Tem foco na qualidade das recomendações através da proporção de itens relevantes no top-k dos itens recomendados - Focuses on the quality of recommendations through the proportion of relevant items in the top-k of recommended items
    // Precision@k = (Itens Relevantes nos Top-k)/k - Precision@k = (Relevant Items in Top-k) / k
    public static double precisionAtK(List<POI> recommended, Set<POI> relevant, int k) {
        int topK = Math.min(k, recommended.size());
        long relevantInTopK = recommended.subList(0, topK).stream()
                .filter(relevant::contains)
                .count();
        return (double) relevantInTopK / topK;
    }
}
