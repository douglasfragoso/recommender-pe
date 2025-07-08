package com.recommendersystempe.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.recommendersystempe.enums.Hobbies;
import com.recommendersystempe.enums.Motivations;
import com.recommendersystempe.enums.Roles;
import com.recommendersystempe.enums.Themes;
import com.recommendersystempe.models.Address;
import com.recommendersystempe.models.POI;
import com.recommendersystempe.models.Recommendation;
import com.recommendersystempe.models.Score;
import com.recommendersystempe.models.User;

@DataJpaTest
public class ScoreRepositoryTest {

    private static final Address ADDRESS = new Address(
            "Rua Exemplo", 100, "Apto 202", "Boa Viagem", "Recife",
            "PE", "Brasil", "50000000");
    private static final List<Motivations> MOTIVATIONS = List.of(
            Motivations.CULTURE, Motivations.EDUCATION, Motivations.ARTISTIC_VALUE,
            Motivations.RELAXATION, Motivations.SOCIAL);
    private static final List<Hobbies> HOBBIES = List.of(
            Hobbies.PHOTOGRAPHY, Hobbies.MUSIC, Hobbies.ADVENTURE,
            Hobbies.ART, Hobbies.READING);
    private static final List<Themes> THEMES = List.of(
            Themes.HISTORY, Themes.ADVENTURE, Themes.NATURE,
            Themes.CULTURAL, Themes.AFRO_BRAZILIAN);
    private static final Address CURRENT_LOCATION = new Address(
            "Rua Exemplo", 100, "Apto 202", "Boa Viagem", "Recife",
            "PE", "Brasil", "50000000");

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private POIRepository poiRepository;

    @Autowired
    private RecommendationRepository recommendationRepository;

    private User user;
    private List<POI> poiList;
    private Recommendation recommendation;
    private List<Score> scoreList;

    @BeforeEach
    public void setUp() {
        scoreRepository.deleteAll();
        recommendationRepository.deleteAll();
        userRepository.deleteAll();
        poiRepository.deleteAll();

        user = new User(
                "João",
                "Silva",
                30,
                "Masculino",
                "12345678909",
                "11-98765-4321",
                "joao@example.com",
                "Senha123*",
                ADDRESS,
                Roles.USER);

        poiList = new ArrayList<>(List.of(
                new POI(
                        "Parque da Cidade",
                        "Um grande parque urbano com áreas verdes, trilhas e lagos.",
                        MOTIVATIONS, HOBBIES, THEMES, CURRENT_LOCATION),
                new POI(
                        "Parque da Cidade2",
                        "Um grande parque urbano com áreas verdes, trilhas e lagos.",
                        MOTIVATIONS, HOBBIES, THEMES, CURRENT_LOCATION),
                new POI(
                        "Parque da Cidade3",
                        "Um grande parque urbano com áreas verdes, trilhas e lagos.",
                        MOTIVATIONS, HOBBIES, THEMES, CURRENT_LOCATION),
                new POI(
                        "Parque da Cidade4",
                        "Um grande parque urbano com áreas verdes, trilhas e lagos.",
                        MOTIVATIONS, HOBBIES, THEMES, CURRENT_LOCATION),
                new POI(
                        "Parque da Cidade5",
                        "Um grande parque urbano com áreas verdes, trilhas e lagos.",
                        MOTIVATIONS, HOBBIES, THEMES, CURRENT_LOCATION)));
       

        recommendation = new Recommendation();
        recommendation.setUser(user);
        poiList.forEach(recommendation::addPOI);
     
    }

    @Test
    void testGivenScores_whenSaveReturNothing() {
        // when / act
        userRepository.save(user);
        poiRepository.saveAll(poiList);
        recommendationRepository.save(recommendation);
        scoreList = new ArrayList<>(List.of(
                new Score(poiList.get(0), 1, recommendation),
                new Score(poiList.get(1), 0,  recommendation),
                new Score(poiList.get(2), 0, recommendation),
                new Score(poiList.get(3), 1, recommendation),
                new Score(poiList.get(4), 0, recommendation)));
        scoreRepository.saveAll(scoreList);

        List<Score> scores = scoreRepository.findAll();

        // then / assert
        assertAll(
                () -> assertNotNull(scores, "A lista de scores não deve ser nula"),
                () -> assertEquals(5, scores.size(), "A lista deve conter 5 scores"));
    }

    @Test
    void testFindByUserScore_ShouldReturListOfScores() {
        // when / act
        userRepository.save(user);
        poiRepository.saveAll(poiList);
        recommendationRepository.save(recommendation);
        scoreList = new ArrayList<>(List.of(
                new Score(poiList.get(0), 1, recommendation),
                new Score(poiList.get(1), 0,  recommendation),
                new Score(poiList.get(2), 0, recommendation),
                new Score(poiList.get(3), 1, recommendation),
                new Score(poiList.get(4), 0, recommendation)));
        scoreRepository.saveAllAndFlush(scoreList);

        List<Score> scores = scoreRepository.findByUser(user.getId());

        // then / assert
        assertAll(
                () -> assertNotNull(scores, "A lista de scores não deve ser nula"),
                () -> assertEquals(5, scores.size(), "A lista deve conter 5 scores"));
    }
    
}