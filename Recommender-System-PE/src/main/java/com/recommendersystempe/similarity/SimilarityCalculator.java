package com.recommendersystempe.similarity;

import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

public class SimilarityCalculator {

    public static RealVector normalize(RealVector vector) {
        double norm = vector.getNorm();
        return norm == 0 ? vector : vector.mapDivide(norm);
    }

    public static double combinedSimilarity(RealVector v1, RealVector v2) {
        double cosine = v1.dotProduct(v2);
        double euclidean = 1 / (1 + new EuclideanDistance().compute(v1.toArray(), v2.toArray()));
        double pearson = (new PearsonsCorrelation().correlation(v1.toArray(), v2.toArray()) + 1) / 2;
        return (cosine + euclidean + pearson) / 3;
    }
}
