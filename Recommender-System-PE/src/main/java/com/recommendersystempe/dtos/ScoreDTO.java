package com.recommendersystempe.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ScoreDTO {

    private Long recommendationId;
    private Long poiId;
    private Integer scoreValue;

    public ScoreDTO(Long poiId, Integer scoreValue) {
        this.poiId = poiId;
        this.scoreValue = scoreValue;
    }
}
