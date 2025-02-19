package com.recommendersystempe.dtos;

import java.util.ArrayList;
import java.util.List;

import com.recommendersystempe.enums.Hobbies;
import com.recommendersystempe.enums.Motivation;
import com.recommendersystempe.enums.Themes;
import com.recommendersystempe.models.Address;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class POIDTO {

    @Getter
    private Long id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String description;

    @Getter
    private List<Motivation> motivations = new ArrayList<>();

    @Getter
    private List<Hobbies> hobbies = new ArrayList<>();

    @Getter
    private List<Themes> themes = new ArrayList<>();

    @Getter
    @Setter
    private Address address;

    public POIDTO(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
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
