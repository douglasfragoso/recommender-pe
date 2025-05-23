package com.recommendersystempe.similarity;

import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.ml.distance.EuclideanDistance;

public class EuclideanSimilarity {
    
    // Similaridade Euclidiana (1 / (1 + distância)) - Euclidean Similarity (1 / (1 + distance))
    // calculada através da raiz quadrada das somas dos quadrados das diferenças dos vetores Q = (q1, q2,...,qn) e D = (d1, d2,...,dn) - calculated through the square root of the sum of the squares of the differences of the vectors Q = (q1, q2,...,qn) and D = (d1, d2,...,dn)
    // dist(Q, D) = sqrt((q1 - d1)^2 + (q2 - d2)^2 + ... + (qn - dn)^2)
    public static double euclideanSimilarity(RealVector v1, RealVector v2) {
        EuclideanDistance ed = new EuclideanDistance();
        double distance = ed.compute(v1.toArray(), v2.toArray());
        return 1 / (1 + distance); // Mapeia para [0, 1] - Maps to [0, 1]
    }
}
