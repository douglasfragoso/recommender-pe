package com.recommendersystempe.dtos;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.recommendersystempe.enums.Hobbies;
import com.recommendersystempe.enums.Motivations;
import com.recommendersystempe.enums.Status;
import com.recommendersystempe.enums.Themes;
import com.recommendersystempe.models.Address;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Represents the POIDTO in the system")
public class POIDTOUpdate {

    @Getter
    @Schema(description = "POI Id", example = "1", required = true)
    private Long id;

    @Getter
    @Setter
    @Schema(description = "Name of a POI", example = "Lake", required = true)
    @Size(min = 3, max = 50, message = "The field name must be between 3 and 50 characters")
    private String name;

    @Getter
    @Setter
    @Schema(description = "Description of a POI", example = "Lake", required = true)
    @Size(min = 50, max = 1000, message = "The field description must be between 50 and 1000 characters")
    private String description;

    @Getter
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(description = "List of motivations associated with the preferences", example = "[\"CULTURE\", \"ENTERTAINMENT\"]", required = true)
    @Size(min = 5, max = 5, message = "The field motivation must have exactly 5 elements")
    private List<Motivations> motivations = new ArrayList<>();

    @Getter
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(description = "List of hobbies associated with the preferences", example = "[\"ADVENTURE\", \"ART\"]", required = true)
    @Size(min = 5, max = 5, message = "The field hobbies must have exactly 5 elements")
    private List<Hobbies> hobbies = new ArrayList<>();

    @Getter
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(description = "List of themes associated with the preferences", example = "[\"ADVENTURE\", \"CULTURAL\"]", required = true)
    @Size(min = 5, max = 5, message = "The field themes must have exactly 5 elements")
    private List<Themes> themes = new ArrayList<>();

    @Getter
    @Setter
    @Schema(description = "Address of a POI", required = true)
    @Valid
    private Address address;

    @Getter @Setter
    @Schema(description = "Status of a POI", required = true)
    private Status status;

}
