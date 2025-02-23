package com.recommendersystempe.dtos;

import java.util.ArrayList;
import java.util.List;

import com.recommendersystempe.models.POI;
import com.recommendersystempe.models.Score;
import com.recommendersystempe.models.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class RecommendationDTO {
    
    @Getter
    private Long id;

    @Getter @Setter
    private User user;

    @Getter
    List<POI> pois = new ArrayList<>();

    @Getter
    private List<Score> scores;

    public void addPOI(List<POI> pois) {
        this.pois.addAll(pois);
    }
    
    public void addScore(List<Score> scores) {
        this.scores.addAll(scores);
    }

}
