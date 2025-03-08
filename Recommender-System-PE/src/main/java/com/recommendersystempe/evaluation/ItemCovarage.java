package com.recommendersystempe.evaluation;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.recommendersystempe.models.POI;

public class ItemCovarage {
    
     // Foco na diversidade das recomendações, avaliando a proporção de itens potencialmente recomendáveis que foram de fato recomendados a todos os usuários.
     // Item Covarage  = (N° de Itens Únicos Recomendados)/(Total de Itens Disponíveis)
    public static double itemCoverage(List<List<POI>> allRecommendations, int totalItems) {
        Set<POI> uniqueRecommended = allRecommendations.stream()
                .flatMap(List::stream)
                .collect(Collectors.toSet());
        return (double) uniqueRecommended.size() / totalItems;
    }
}
