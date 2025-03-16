package com.recommendersystempe.similarity;

import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

public class PearsonSimilarity {
    
    // Pearson Correlation (normalizado para [0, 1]) - Pearson Correlation (normalized to [0, 1])
    // Cálculado pela a média dos vetores Q e D, seguido da medição da covariância e desvio padrão entre os vetores - Calculated by the average of vectors Q and D, followed by the measurement of covariance and standard deviation between the vectors
    // PCC(Q, D) = (cov(Q, D) / (std(Q) * std(D)) + 1) - PCC(Q, D) = (cov(Q, D) / (std(Q) * std(D)) + 1)
    public static double pearsonSimilarity(RealVector v1, RealVector v2) {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        double pcc;
        try {
            pcc = pc.correlation(v1.toArray(), v2.toArray());
        } catch (Exception e) { // Evita NaN se um vetor for constante - Avoids NaN if a vector is constant
            pcc = 0.0;
        }
        return (pcc + 1) / 2; // Mapeia de [-1, 1] para [0, 1] - Maps from [-1, 1] to [0, 1]
    }

}
