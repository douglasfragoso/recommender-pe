package com.recommendersystempe.evaluation;

import java.util.List;
import java.util.Set;

import com.recommendersystempe.models.POI;

public class F1Score {
    
    // Combina Precision@k e Recall@k em uma única métrica, refletindo o equilíbrio entre qualidade e cobertura
    // F1-Score@k = 2*(Precision@k * Recall@k)/(Precision@k+ Recall@k) 
    public static double f1ScoreAtK(List<POI> recommended, Set<POI> relevant, int k) {;
        double precisionAtK = Precision.precisionAtK(recommended, relevant, k);
        double recallAtK = Recall.recallAtK(recommended, relevant, k);
        return (precisionAtK + recallAtK == 0) ? 0 : 2 * (precisionAtK * recallAtK) / (precisionAtK + recallAtK);
    }
}
