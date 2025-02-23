package com.recommendersystempe.models;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Score {

    @ManyToOne
    @JoinColumn(name = "poi_id")
    private POI poi;

    private Integer score;

}
