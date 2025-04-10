package com.recommendersystempe.evaluation;

import java.util.List;
import java.util.Set;

import com.recommendersystempe.models.POI;

public class Recall {
   
    // Mede a proporção entre itens relevantes que foram recomendados no top-k em relação ao total de itens relevantes disponíveis - Measures the proportion of relevant items that were recommended in the top-k in relation to the total available relevant items
    // Recall@k = (Itens Relevantes nos Top-k)/Total de Itens Relevantes - Recall@k = (Relevant Items in Top-k) / Total Relevant Items
    public static double recallAtK(List<POI> recommended, Set<POI> relevant, int k) {
        if (relevant.isEmpty()) return 0.0;
        int topK = Math.min(k, recommended.size());
        long relevantInTopK = recommended.subList(0, topK).stream()
                .filter(relevant::contains)
                .count();
        return (double) relevantInTopK / relevant.size();
    }
}
