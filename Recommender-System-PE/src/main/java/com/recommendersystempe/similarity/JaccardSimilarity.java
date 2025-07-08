package com.recommendersystempe.similarity;

import org.apache.commons.math3.linear.RealVector;

public class JaccardSimilarity {
    // Similaridade de Jaccard (índice de similaridade entre conjuntos) - Jaccard Similarity (set similarity index)
    // Calculado pela razão entre a interseção (soma dos mínimos) e a união (soma dos máximos) dos vetores - Calculated by the ratio between intersection (sum of mins) and union (sum of maxs) of vectors
    // J(Q, D) = sum(min(Q_i, D_i)) / sum(max(Q_i, D_i)) - J(Q, D) = sum(min(Q_i, D_i)) / sum(max(Q_i, D_i))
    public static double jaccardSimilarity(RealVector v1, RealVector v2) {
        double sumMin = 0.0;
        double sumMax = 0.0;
        
        for (int i = 0; i < v1.getDimension(); i++) {
            double x = v1.getEntry(i);
            double y = v2.getEntry(i);
            sumMin += Math.min(x, y);
            sumMax += Math.max(x, y);
        }
        
        return sumMax == 0 ? 0.0 : sumMin / sumMax; 
    }
}