package com.recommendersystempe.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Schema(description = "Represents the ScoreDTO in the system")
public class ScoreDTO {

    @Schema(description = "Recommendation Id", example = "1", required = true)
    private Long recommendationId;

    @Schema(description = "POI Id", example = "1", required = true)
    @NotNull(message = "The field poiId is required")
    @Min(value = 1, message = "POI Id must be at least 1")
    private Long poiId;

    @Schema(description = "Score value", example = "1", required = true)
    @NotNull(message = "The field scoreValue is required")
    @Min(value = 0, message = "Score value must be at least 0")
    @Max(value = 1, message = "Score value must be at most 1")
    private Integer scoreValue;

    public ScoreDTO(Long poiId, Integer scoreValue) {
        this.poiId = poiId;
        this.scoreValue = scoreValue;
    }
}