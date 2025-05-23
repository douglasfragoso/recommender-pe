package com.recommendersystempe.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_similarity_metric")
@NoArgsConstructor
public class SimilarityMetric {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @Getter @Setter
    @JoinColumn(name = "recommendation_id", nullable = false)
    private Recommendation recommendation;

    @ManyToOne
    @Getter @Setter
    @JoinColumn(name = "poi_id", nullable = false)
    private POI poi;

    @Getter @Setter
    @Column(name = "cosine", nullable = false)
    private double cosine;

    @Getter @Setter
    @Column(nullable = false)
    private double euclidean;

    @Getter @Setter
    @Column(nullable = false)
    private double pearson;

    @Getter @Setter
    @Column(nullable = false)
    private double jaccard;

    @Getter @Setter
    @Column(nullable = false)
    private double average;

    public SimilarityMetric(Recommendation recommendation, POI poi, 
                           double cosine, double euclidean, 
                           double pearson, double jaccard) {
        this.recommendation = recommendation;
        this.poi = poi;
        this.cosine = cosine;
        this.euclidean = euclidean;
        this.pearson = pearson;
        this.jaccard = jaccard;
        this.average = (cosine + euclidean + pearson + jaccard) / 4;
    }
}