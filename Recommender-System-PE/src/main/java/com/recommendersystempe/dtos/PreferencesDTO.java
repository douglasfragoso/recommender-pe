package com.recommendersystempe.dtos;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.recommendersystempe.enums.Hobbies;
import com.recommendersystempe.enums.Motivation;
import com.recommendersystempe.enums.Themes;
import com.recommendersystempe.models.Address;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Schema(description = "Represents the PreferencesDTO in the system")
public class PreferencesDTO {

    @Getter
    @Schema(description = "Preferences Id", example = "1", required = true)
    @Min(value = 1, message = "Preferences Id must be at least 1")
    private Long id;

    @Getter
    @Setter
    @Schema(description = "User Id", example = "1", required = true)
    @NotNull(message = "The field userId is required")
    @Min(value = 1, message = "User Id must be at least 1")
    private Long user;

    @Getter
    @Setter
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "America/Sao_Paulo")
    @Schema(description = "Date of the preferences", example = "2021-10-01T00:00:00Z", required = true)
    @NotNull(message = "The field date is required")
    private Instant date;

    @Getter
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(description = "List of motivations associated with the preferences", example = "[\"CULTURE\", \"ENTERTAINMENT\"]", required = true)
    @NotNull(message = "The field motivations is required")
    @Size(min = 1, max = 5, message = "The field motivation must have 1 to 5 elements")
    private List<Motivation> motivations = new ArrayList<>();

    @Getter
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(description = "List of hobbies associated with the preferences", example = "[\"ADVENTURE\", \"ART\"]", required = true)
    @NotNull(message = "The field hobbies is required")
    @Size(min = 1, max = 5, message = "The field hobbies must have 1 to 5 elements")
    private List<Hobbies> hobbies = new ArrayList<>();

    @Getter
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(description = "List of themes associated with the preferences", example = "[\"ADVENTURE\", \"CULTURAL\"]", required = true)
    @NotNull(message = "The field themes is required")
    @Size(min = 1, max = 5, message = "The field themes must have 1 to 5 elements")
    private List<Themes> themes = new ArrayList<>();

    @Getter
    @Setter
    @Schema(description = "Current location of the user", required = true)
    @NotNull(message = "The field address is required")
    private Address currentLocation;

    public PreferencesDTO(Long id, Long user, Instant date, List<Motivation> motivations, List<Hobbies> hobbies,
            List<Themes> themes, Address currentLocation) {
        this.id = id;
        this.user = user;
        this.date = date;
        this.motivations.addAll(motivations);
        this.hobbies.addAll(hobbies);
        this.themes.addAll(themes);
        this.currentLocation = currentLocation;
    }

    public PreferencesDTO(List<Motivation> motivations, List<Hobbies> hobbies, List<Themes> themes,
            Address currentLocation) {
        this.motivations.addAll(motivations);
        this.hobbies.addAll(hobbies);
        this.themes.addAll(themes);
        this.currentLocation = currentLocation;
    }

    public void addMotivation(List<Motivation> motivations) {
        this.motivations.addAll(motivations);
    }

    public void addHobbie(List<Hobbies> hobbies) {
        this.hobbies.addAll(hobbies);
    }

    public void addTheme(List<Themes> themes) {
        this.themes.addAll(themes);
    }

}
