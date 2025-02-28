package com.recommendersystempe.dtos;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.recommendersystempe.models.POI;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class RecommendationDTO {

    @Getter
    private Long id;

    @Getter
    @Setter
    private Long user;

    @Getter
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
