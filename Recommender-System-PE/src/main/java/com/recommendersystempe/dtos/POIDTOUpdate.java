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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Represents the POIDTO in the system")
public class POIDTOUpdate {

    @Getter
    @Schema(description = "POI Id", example = "1", required = false)
    private Long id;

    @Getter
    @Setter
    @Schema(description = "Name of a POI", example = "Lake", required = false)
    private String name;

    @Getter
    @Setter
    @Schema(description = "Description of a POI", example = "Lake", required = false)
    private String description;

    @Getter
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(description = "List of motivations associated with the preferences", example = "[\"CULTURE\", \"ENTERTAINMENT\"]", required = false)
    private List<Motivations> motivations = new ArrayList<>();

    @Getter
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(description = "List of hobbies associated with the preferences", example = "[\"ADVENTURE\", \"ART\"]", required = false)
    private List<Hobbies> hobbies = new ArrayList<>();

    @Getter
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(description = "List of themes associated with the preferences", example = "[\"ADVENTURE\", \"CULTURAL\"]", required = false)
    private List<Themes> themes = new ArrayList<>();

    @Getter
    @Setter
    @Schema(description = "Address of a POI", required = false)
    private Address address;

    @Getter @Setter
    @Schema(description = "Status of a POI", required = false)
    private Status status;

}
