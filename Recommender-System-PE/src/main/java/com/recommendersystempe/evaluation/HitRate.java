package com.recommendersystempe.evaluation;

import java.util.List;
import java.util.Set;

import com.recommendersystempe.models.POI;

public class HitRate {
    
    // Foco em garantir que cada usuário receba algo relevante no top-k, verificando a proporção dos usuários atendidos
    // Hit Rate@k = (Usuários com ao menos 1 item relevante no Top-k)/(Total de Usuários)
    public static double hitRateAtK(List<List<POI>> allRecommendations, 
                                   List<Set<POI>> allRelevantItems, 
                                   int k) {
        int usersWithHit = 0;
        for (int i = 0; i < allRecommendations.size(); i++) {
            List<POI> recommendations = allRecommendations.get(i);
            Set<POI> relevant = allRelevantItems.get(i);
            
            boolean hasHit = recommendations.stream()
                    .limit(k)
                    .anyMatch(relevant::contains);
            
            if (hasHit) usersWithHit++;
        }
        return (double) usersWithHit / allRecommendations.size();
    }
}
