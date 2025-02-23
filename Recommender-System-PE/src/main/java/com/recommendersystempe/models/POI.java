package com.recommendersystempe.models;

import java.util.ArrayList;
import java.util.List;

import com.recommendersystempe.enums.Hobbies;
import com.recommendersystempe.enums.Motivation;
import com.recommendersystempe.enums.Themes;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode(of = "id")
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tb_pois")
public class POI {

    @Getter 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Getter @Setter
    @Column(name = "name", length = 20)
    private String name;

    @Getter @Setter
    @Column(name = "description", length = 500)
    private String description;

    @Getter
    @Column(name = "motivation")
    @Enumerated(EnumType.ORDINAL)
    private List<Motivation> motivations = new ArrayList<>();

    @Getter
    @Column(name = "hobbies")
    @Enumerated(EnumType.ORDINAL)
    private List<Hobbies> hobbies = new ArrayList<>();

    @Getter
    @Column(name = "themes")
    @Enumerated(EnumType.ORDINAL)
    private List<Themes> themes = new ArrayList<>();

    @Getter @Setter
    @Embedded
    private Address address;

    public POI(String name, String descripition, List<Motivation> motivations, List<Hobbies> hobbies,
    List<Themes> themes, Address address) {
        this.name = name;
        this.description = descripition;
        this.motivations = motivations;
        this.hobbies = hobbies;
        this.themes = themes;
        this.address = address;
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
