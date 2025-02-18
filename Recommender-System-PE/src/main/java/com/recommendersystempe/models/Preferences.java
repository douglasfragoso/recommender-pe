package com.recommendersystempe.models;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    private Address currentLocation;
    
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

