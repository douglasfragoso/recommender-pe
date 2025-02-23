package com.recommendersystempe.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @Getter @Setter
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Getter
    @ManyToOne
    @JoinColumn(name = "poi_id")
    List<POI> pois = new ArrayList<>();

    @Getter
    @Embedded
    @Column(name = "scores")
    private List<Score> scores;

    public void addPOI(List<POI> pois) {
        this.pois.addAll(pois);
    }
    
    public void addScore(List<Score> scores) {
        this.scores.addAll(scores);
    }

}
