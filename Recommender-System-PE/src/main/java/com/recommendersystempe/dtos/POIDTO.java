package com.recommendersystempe.dtos;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.recommendersystempe.enums.Hobbies;
import com.recommendersystempe.enums.Motivations;
import com.recommendersystempe.enums.Themes;
import com.recommendersystempe.models.Address;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_EMPTY) // Ignora listas vazias
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Represents the POIDTO in the system")
public class POIDTO {

    @Getter
    @Schema(description = "POI Id", example = "1", required = true)
    @Min(value = 1, message = "POI Id must be at least 1")
    private Long id;

    @Getter
    @Setter
    @Schema(description = "Name of a POI", example = "Lake", required = true)
    @NotBlank(message = "The field name is required")
    @Size(min = 3, max = 20, message = "The field name must be between 3 and 20 characters")
    private String name;

    @Getter
    @Setter
    @Schema(description = "Description of a POI", example = "Lake", required = true)
    @NotBlank(message = "The field description is required")
    @Size(min = 50, max = 1000, message = "The field description must be between 50 and 1000 characters")
    private String description;

    @Getter
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(description = "List of motivations associated with the preferences", example = "[\"CULTURE\", \"ENTERTAINMENT\"]", required = true)
    @NotNull(message = "The field motivations is required")
    @Size(min = 5, max = 5, message = "The field motivation must have 1 to 5 elements")
    private List<Motivations> motivations = new ArrayList<>();

    @Getter
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(description = "List of hobbies associated with the preferences", example = "[\"ADVENTURE\", \"ART\"]", required = true)
    @NotNull(message = "The field hobbies is required")
    @Size(min = 5, max = 5, message = "The field hobbies must have 1 to 5 elements")
    private List<Hobbies> hobbies = new ArrayList<>();

    @Getter
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(description = "List of themes associated with the preferences", example = "[\"ADVENTURE\", \"CULTURAL\"]", required = true)
    @NotNull(message = "The field themes is required")
    @Size(min = 5, max = 5, message = "The field themes must have 1 to 5 elements")
    private List<Themes> themes = new ArrayList<>();

    @Getter
    @Setter
    @Schema(description = "Address of a POI", required = true)
    @NotNull(message = "The field address is required")
    private Address address;

    public POIDTO(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public POIDTO(Long id, String name, String description, Address poiAddress) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = poiAddress;
    }

    public POIDTO(String name, String description, List<Motivations> motivations, List<Hobbies> hobbies,
            List<Themes> themes, Address poiAddress) {
        this.name = name;
        this.motivations.addAll(motivations);
        this.hobbies.addAll(hobbies);
        this.themes.addAll(themes);
        this.address = poiAddress;
    }

    public void addMotivation(List<Motivations> motivations) {
        this.motivations.addAll(motivations);
    }

    public void addHobbie(List<Hobbies> hobbies) {
        this.hobbies.addAll(hobbies);
    }

    public void addTheme(List<Themes> themes) {
        this.themes.addAll(themes);
    }
}
