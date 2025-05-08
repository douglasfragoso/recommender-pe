package com.recommendersystempe.similarity;

import org.apache.commons.math3.linear.RealVector;

public class JaccardSimilarity {
    
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