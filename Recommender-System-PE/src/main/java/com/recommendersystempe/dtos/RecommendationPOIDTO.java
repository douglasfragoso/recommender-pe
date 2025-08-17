package com.recommendersystempe.dtos;

import com.recommendersystempe.models.Address;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Represents the POIDTO in the system")
public class RecommendationPOIDTO {

    @Getter
    @Schema(description = "POI Id", example = "1", required = true)
    private Long id;

    @Getter
    @Setter
    @Schema(description = "Name of a POI", example = "Lake", required = true)
    @NotBlank(message = "The field name is required")
    @Size(min = 3, max = 50, message = "The field name must be between 3 and 50 characters")
    private String name;

    @Getter
    @Setter
    @Schema(description = "Description of a POI", example = "Lake", required = true)
    @NotBlank(message = "The field description is required")
    @Size(min = 50, max = 1000, message = "The field description must be between 50 and 1000 characters")
    private String description;

    @Getter
    @Setter
    @Schema(description = "Address of a POI", required = true)
    @NotNull(message = "The field address is required")
    @Valid
    private Address address;

}
