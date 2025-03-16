package com.recommendersystempe.evaluation;

import java.util.List;
import java.util.Set;

import com.recommendersystempe.models.POI;

public class HitRate {
    
    // Foco em garantir que cada usuário receba algo relevante no top-k, verificando a proporção dos usuários atendidos - Focus on ensuring that each user receives something relevant in the top-k, checking the proportion of users served
    // Hit Rate@k = (Usuários com ao menos 1 item relevante no Top-k)/(Total de Usuários) - Hit Rate@k = (Users with at least 1 relevant item in the Top-k) / (Total Users)
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
