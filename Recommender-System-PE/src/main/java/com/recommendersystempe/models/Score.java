package com.recommendersystempe.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Entity(name = "tb_scores")
public class Score {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Getter @Setter
    @ManyToOne
    @JoinColumn(name = "poi_id")
    private POI poi;

    @Getter @Setter
    @Column(name = "score")
    private Integer score;

    @Getter @Setter
    @ManyToOne(cascade = CascadeType.ALL) 
    @JoinColumn(name = "recommendation_id")
    private Recommendation recommendation;

    public Score(POI poi, int i, Recommendation recommendation) {
        this.poi = poi;
        this.score = i;
        this.recommendation = recommendation;
    }

}
