package com.recommendersystempe.dtos;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.recommendersystempe.enums.Hobbies;
import com.recommendersystempe.enums.Motivation;
import com.recommendersystempe.enums.Themes;
import com.recommendersystempe.models.Address;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class PreferencesDTO {

    @Getter
    private Long id;

    @Getter @Setter
    private Long user;

    @Getter @Setter
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "America/Sao_Paulo")
    private Instant date;

    @Getter
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private List<Motivation> motivations = new ArrayList<>();

    @Getter
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private List<Hobbies> hobbies = new ArrayList<>();

    @Getter
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private List<Themes> themes = new ArrayList<>();

    @Getter @Setter
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
