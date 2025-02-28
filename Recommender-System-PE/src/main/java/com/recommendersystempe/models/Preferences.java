package com.recommendersystempe.models;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.recommendersystempe.enums.Hobbies;
import com.recommendersystempe.enums.Motivation;
import com.recommendersystempe.enums.Themes;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode(of = "id")
@ToString
@NoArgsConstructor
@Entity(name = "tb_preferences")
public class Preferences {

    @Getter 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
   
    @Getter @Setter
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Getter @Setter
    @Column(name = "date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "America/Sao_Paulo")
    private Instant date;

    @Getter
    @ElementCollection(targetClass = Motivation.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "preferences_motivations", joinColumns = @JoinColumn(name = "preference_id"))
    @Column(name = "motivation")
    private List<Motivation> motivations = new ArrayList<>();

    @Getter
    @ElementCollection(targetClass = Hobbies.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "preferences_hobbies", joinColumns = @JoinColumn(name = "preference_id"))
    @Column(name = "hobbie")
    private List<Hobbies> hobbies = new ArrayList<>();

    @Getter
    @ElementCollection(targetClass = Themes.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "preferences_themes", joinColumns = @JoinColumn(name = "preference_id"))
    @Column(name = "theme")
    private List<Themes> themes = new ArrayList<>();

    @Getter @Setter
    @Embedded
    private Address currentLocation;

    public Preferences(User user, Instant date, List<Motivation> motivations, List<Hobbies> hobbies, List<Themes> themes, Address currentLocation) {
        this.user = user;
        this.date = date;
        this.motivations = motivations;
        this.hobbies = hobbies;
        this.themes = themes;
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

