package com.recommendersystempe.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.recommendersystempe.enums.Hobbies;
import com.recommendersystempe.enums.Motivations;
import com.recommendersystempe.enums.Roles;
import com.recommendersystempe.enums.Themes;
import com.recommendersystempe.models.Address;
import com.recommendersystempe.models.POI;
import com.recommendersystempe.models.Recommendation;
import com.recommendersystempe.models.User;

@DataJpaTest
public class RecommendationRepositoryTest {

    private static final Address ADDRESS = new Address(
            "Rua Exemplo", 100, "Apto 202", "Boa Viagem", "Recife",
            "PE", "Brasil", "50000000");
    private static final List<Motivations> MOTIVATIONS = List.of(
            Motivations.CULTURE, Motivations.STUDY, Motivations.ARTISTIC_VALUE,
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

    private List<POI> poiList;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecommendationRepository recommendationRepository;

    @Autowired
    private POIRepository poiRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        recommendationRepository.deleteAll();
        poiRepository.deleteAll();

        user = new User(
                "Mariana",
                "Silva",
                28,
                "Feminino",
                "98765432100",
                "11-99876-5432",
                "mariana@example.com",
                "Segura456*",
                ADDRESS,
                Roles.USER);

        // Create new POI instances for each test
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
                        MOTIVATIONS, HOBBIES, THEMES, CURRENT_LOCATION)
        ));
    }

    @Test
    void testGivenPreferences_whenSaveReturRecommendation() {
        // when / act
        userRepository.save(user);
        poiRepository.saveAll(poiList);

        Recommendation recommendation = new Recommendation();
        recommendation.setUser(user);
        poiList.forEach(recommendation::addPOI);
        recommendationRepository.save(recommendation);

        Recommendation savedRecommendation = recommendationRepository.findById(recommendation.getId()).get();

        // then / assert
        assertAll(
                () -> assertNotNull(savedRecommendation, "Recommendation must not be null"),
                () -> assertTrue(savedRecommendation.getId() > 0,
                        "Recommendation id must be greater than 0"),
                () -> assertEquals(user, savedRecommendation.getUser(),
                        "Recommendation user must be equal to USER"),
                () -> assertEquals(poiList.size(), savedRecommendation.getPois().size(),
                        "Recommendation pois must be equal to poiList size"));
    }

    @Test
    void testFindAllRecommendation_ShouldReturnListOfRecommendation() {
        // given / arrange
        userRepository.save(user);
        poiRepository.saveAll(poiList);

        Recommendation recommendation = new Recommendation();
        recommendation.setUser(user);
        poiList.forEach(recommendation::addPOI);
        recommendationRepository.save(recommendation);

        Recommendation recommendation1 = new Recommendation();
        recommendation1.setUser(user);
        poiList.forEach(recommendation1::addPOI);
        recommendationRepository.save(recommendation1);

        // when / act
        List<Recommendation> recommendationsList = recommendationRepository.findAll();

        // then / assert
        assertAll(
                () -> assertNotNull(recommendationsList, "Recommendations list must not be null"),
                () -> assertEquals(2, recommendationsList.size(),
                        "Recommendations list must have 2 recommendations"));
    }

    @Test
    void testFindRecommendationById_ShouldReturnRecommendation() {
        // given / arrange
        userRepository.save(user);
        poiRepository.saveAll(poiList);

        Recommendation recommendation = new Recommendation();
        recommendation.setUser(user);
        poiList.forEach(recommendation::addPOI);
        recommendationRepository.save(recommendation);

        // when / act
        Recommendation foundRecommendation = recommendationRepository.findById(recommendation.getId()).get();

        // then / assert
        assertAll(
                () -> assertNotNull(foundRecommendation, "Preferences must not be null"),
                () -> assertNotNull(foundRecommendation,
                        "Preferences must be present in the database"),
                () -> assertEquals(foundRecommendation.getId(), recommendation.getId(),
                        "Preferences id must be the same"));
    }

     @Test
    void testFindAllByUserId_ShouldReturnPageOfRecommendations() {
        // given / arrange
        userRepository.save(user);
        poiRepository.saveAll(poiList);

        Recommendation recommendation = new Recommendation();
        recommendation.setUser(user);
        poiList.forEach(recommendation::addPOI);
        recommendationRepository.save(recommendation);

        Recommendation recommendation1 = new Recommendation();
        recommendation1.setUser(user);
        poiList.forEach(recommendation1::addPOI);
        recommendationRepository.save(recommendation1);

        Pageable pageable = PageRequest.of(0, 10);

        // when / act
        Page<Recommendation> recommendationsPage = recommendationRepository.findAllByUserId(user.getId(), pageable);

        // then / assert
        assertAll(
                () -> assertNotNull(recommendationsPage, "Recommendations page must not be null"),
                () -> assertEquals(2, recommendationsPage.getTotalElements(),
                        "Recommendations page must have 2 recommendations"),
                () -> assertEquals(user, recommendationsPage.getContent().get(0).getUser(),
                        "Recommendation user must be equal to USER"));
    }

    @Test
    void testFindByUser_ShouldReturnListOfRecommendations() {
        // given / arrange
        userRepository.save(user);
        poiRepository.saveAll(poiList);

        Recommendation recommendation = new Recommendation();
        recommendation.setUser(user);
        poiList.forEach(recommendation::addPOI);
        recommendationRepository.save(recommendation);

        Recommendation recommendation1 = new Recommendation();
        recommendation1.setUser(user);
        poiList.forEach(recommendation1::addPOI);
        recommendationRepository.save(recommendation1);

        // when / act
        List<Recommendation> recommendationsList = recommendationRepository.findByUser(user.getId());

        // then / assert
        assertAll(
                () -> assertNotNull(recommendationsList, "Recommendations list must not be null"),
                () -> assertEquals(2, recommendationsList.size(),
                        "Recommendations list must have 2 recommendations"),
                () -> assertEquals(user, recommendationsList.get(0).getUser(),
                        "Recommendation user must be equal to USER"));
    }
}
