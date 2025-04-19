package com.recommendersystempe.evaluation;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.recommendersystempe.models.POI;

public class ItemCovarage {

    // Foco na diversidade das recomendações, avaliando a proporção de itens
    // potencialmente recomendáveis que foram de fato recomendados a todos os
    // usuários. - Focus on the diversity of recommendations, evaluating the
    // proportion of potentially recommendable items that were actually recommended
    // to all users.
    // Item Covarage = (N° de Itens Únicos Recomendados)/(Total de Itens
    // Disponíveis) - Item Covarage = (Number of Unique Recommended Items) / (Total
    // Available Items)
    public static double itemCoverage(List<List<POI>> allRecommendations, int totalItems) {
        Set<POI> uniqueRecommended = allRecommendations.stream()
                .flatMap(List::stream)
                .collect(Collectors.toSet());
        double coverage = (double) uniqueRecommended.size() / totalItems;
        return Math.round(coverage * 100.0) / 100.0;
    }
}
