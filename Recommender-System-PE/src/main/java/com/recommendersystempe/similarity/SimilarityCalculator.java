package com.recommendersystempe.similarity;

import org.apache.commons.math3.linear.RealVector;

public class SimilarityCalculator {

    public static RealVector normalize(RealVector vector) {
        double norm = vector.getNorm();
        return norm == 0 ? vector : vector.mapDivide(norm);
    }

    public static double combinedSimilarity(RealVector v1, RealVector v2) {
  
        double cosine = CosineSimilarity.cosineSimilarity(v1, v2);
        double euclidean = EuclideanSimilarity.euclideanSimilarity(v1, v2);
        double pearson = PearsonSimilarity.pearsonSimilarity(v1, v2);
        double jaccard = JaccardSimilarity.jaccardSimilarity(v1, v2);
        
        return (cosine + euclidean + pearson + jaccard) / 4;
    }
}