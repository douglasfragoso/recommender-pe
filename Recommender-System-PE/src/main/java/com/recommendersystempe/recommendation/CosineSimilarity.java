package com.recommendersystempe.recommendation;

import org.apache.commons.math3.linear.RealVector;

public class CosineSimilarity {
   
    // Similaridade de Cosseno
    // v1.dotProduct(v2) calcula o produto escalar
    // v1.getNorm() e v2.getNorm() calculam a norma
    public static double cosineSimilarity(RealVector v1, RealVector v2) {
        return v1.dotProduct(v2) / (v1.getNorm() * v2.getNorm());
    }
}
