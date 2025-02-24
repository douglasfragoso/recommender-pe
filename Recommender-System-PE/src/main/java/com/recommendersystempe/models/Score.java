package com.recommendersystempe.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
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
    @ManyToOne
    @JoinColumn(name = "recommendation_id")
    private Recommendation recommendation;

}
