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

    public static double cosineSimilarity(RealVector v1, RealVector v2) {
        return CosineSimilarity.cosineSimilarity(v1, v2);
    }

    public static double euclideanSimilarity(RealVector v1, RealVector v2) {
        return EuclideanSimilarity.euclideanSimilarity(v1, v2);
    }

    public static double pearsonSimilarity(RealVector v1, RealVector v2) {
        return PearsonSimilarity.pearsonSimilarity(v1, v2);
    }

    public static double jaccardSimilarity(RealVector v1, RealVector v2) {
        return JaccardSimilarity.jaccardSimilarity(v1, v2);
    }
}