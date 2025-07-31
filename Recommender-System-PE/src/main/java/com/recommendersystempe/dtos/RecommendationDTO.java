package com.recommendersystempe.dtos;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.recommendersystempe.models.POI;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Represents the RecommendationDTO in the system")
public class RecommendationDTO {

    @Getter
    @Schema(description = "Recommendation Id", example = "1", required = true)
    private Long id;

    @Getter
    @Schema(description = "List of POIs associated with the recommendation", required = true)
    private List<POIDTO> pois = new ArrayList<>();

    public void addPOI(List<POI> pois) {
        this.pois.addAll(pois.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList()));
    }

    private POIDTO convertToDTO(POI poi) {
        return new POIDTO(
                poi.getId(),
                poi.getName(),
                poi.getDescription(),
                poi.getAddress());
    }
}