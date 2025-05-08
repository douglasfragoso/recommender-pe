package com.recommendersystempe.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_similarity_metric")
@AllArgsConstructor
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

}