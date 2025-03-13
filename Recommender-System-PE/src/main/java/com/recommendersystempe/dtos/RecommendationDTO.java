package com.recommendersystempe.dtos;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.recommendersystempe.models.POI;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Represents the RecommendationDTO in the system")
public class RecommendationDTO {

    @Getter
    @Schema(description = "Recommendation Id", example = "1", required = true)
    @Min(value = 1, message = "Recommendation Id must be at least 1")
    private Long id;

    @Getter
    @Setter
    @Schema(description = "User Id", example = "1", required = true)
    @NotNull(message = "The field userId is required")
    @Min(value = 1, message = "User Id must be at least 1")
    private Long userId;

    @Getter
    @Schema(description = "List of POIs associated with the recommendation", required = true)
    @NotNull(message = "The field pois is required")
    @Size(min = 5, max = 5, message = "The field pois must have exactly 5 elements")
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