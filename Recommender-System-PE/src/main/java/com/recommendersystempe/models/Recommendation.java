package com.recommendersystempe.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode(of = "id")
@ToString
@NoArgsConstructor
@Entity(name = "tb_recommendation")
public class Recommendation {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Getter
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "recommendation_poi", joinColumns = @JoinColumn(name = "recommendation_id"), inverseJoinColumns = @JoinColumn(name = "poi_id"))
    private List<POI> pois = new ArrayList<>();

    @Getter
    @OneToMany(mappedBy = "recommendation", cascade = CascadeType.ALL)
    private List<Score> scores = new ArrayList<>();

    @Getter
    @OneToMany(mappedBy = "recommendation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SimilarityMetric> similarityMetrics = new ArrayList<>();

    public void addSimilarityMetric(SimilarityMetric metric) {
        this.similarityMetrics.add(metric);
        metric.setRecommendation(this);
    }

    public void addPOI(POI poi) {
        this.pois.add(poi);
        poi.getRecommendations().add(this); // Sincroniza o lado inverso - Synchronizes the reverse side
    }

    public void addScore(List<Score> scores) {
        this.scores.addAll(scores);
    }

}
