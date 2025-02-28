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

    @Getter @Setter
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Getter
    @ManyToMany(mappedBy = "recommendations")
    private List<POI> pois = new ArrayList<>();

    @Getter
    @OneToMany(mappedBy = "recommendation",  cascade = CascadeType.ALL)
    private List<Score> scores = new ArrayList<>();
    
    public void addPOI(List<POI> pois) {
        this.pois.addAll(pois);
    }
    
    public void addScore(List<Score> scores) {
        this.scores.addAll(scores);
    }

}
