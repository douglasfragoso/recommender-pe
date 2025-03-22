package com.recommendersystempe.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.recommendersystempe.enums.Hobbies;
import com.recommendersystempe.enums.Motivations;
import com.recommendersystempe.enums.Roles;
import com.recommendersystempe.enums.Themes;
import com.recommendersystempe.models.Address;
import com.recommendersystempe.models.Preferences;
import com.recommendersystempe.models.User;

@DataJpaTest 
public class PreferencesRepositoryTest {

    private static final List<Motivations> MOTIVATIONS = List.of(
            Motivations.CULTURE, Motivations.STUDY, Motivations.APPRECIATION,
            Motivations.RELAXATION, Motivations.SOCIAL);
    private static final List<Hobbies> HOBBIES = List.of(
            Hobbies.PHOTOGRAPHY, Hobbies.MUSIC, Hobbies.ADVENTURE,
            Hobbies.ART, Hobbies.READING);
    private static final List<Themes> THEMES = List.of(
            Themes.HISTORY, Themes.ADVENTURE, Themes.NATURE,
            Themes.CULTURAL, Themes.AFRO_BRAZILIAN);
    private static final Address ADDRESS = new Address(
            "Avenida Central", 250, "Casa 5", "Boa Viagem", "Recife",
            "PE", "Brasil", "01000000");
    private static final Address CURRENT_LOCATION = new Address(
            "Rua Exemplo", 100, "Apto 202", "Boa Viagem", "Recife",
            "PE", "Brasil", "50000000");

    @Autowired
    private PreferencesRepository preferencesRepository;

    @Autowired
    private UserRepository userRepository;

    
    private User user;
    private Preferences preferences;

    @BeforeEach
    public void setUp() {
        // given / arrange
        userRepository.deleteAll();
        preferencesRepository.deleteAll();

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
                Roles.USER 
        );
        
        preferences = new Preferences(user, Instant.now(), MOTIVATIONS, HOBBIES, THEMES, CURRENT_LOCATION);
    }

    @Test
    void testGivenPreferences_whenSave_ThenReturPreferences() {
        // when / act
        userRepository.save(user);
        Preferences savedPreferences = preferencesRepository.save(preferences);
        Optional<Preferences> foundPreferences = preferencesRepository.findById(savedPreferences.getId());

        // then / assert
        assertNotNull(savedPreferences, "Preferences must not be null");
        assertTrue(savedPreferences.getId() > 0, "Preferences id must be greater than 0");
        assertNotNull(foundPreferences, "Preferences must be present in the database");
    }

    @Test
    void testGivenPreferencesList_whenFindAll_ThenReturnPreferencesList() {
        // given / arrange

        Preferences preferences1 = new Preferences(user, Instant.now(), MOTIVATIONS, HOBBIES, THEMES, CURRENT_LOCATION); 

        userRepository.save(user);
        preferencesRepository.save(preferences);
        preferencesRepository.save(preferences1);
        // when / act
        List<Preferences> preferencesList = preferencesRepository.findAll();

        // then / assert
        assertNotNull(preferencesList, "Preferences list must not be null");
        assertEquals(2, preferencesList.size(), "Preferences list must have 2 preferences");
    }

    @Test
    void testGivenSavePreferences_whenFindById_ThenReturnPreferences() {
        // given / arrange
        userRepository.save(user);
        preferencesRepository.save(preferences);

        // when / act
        Optional<Preferences> foundPreferences = preferencesRepository.findById(preferences.getId());

        // then / assert
        assertNotNull(foundPreferences, "Preferences must not be null");
        assertTrue(foundPreferences.isPresent(), "Preferences must be present in the database");
        assertEquals(foundPreferences.get().getId(), preferences.getId(), "Preferences id must be the same");
    }

}
