package com.recommendersystempe.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import com.recommendersystempe.enums.Hobbies;
import com.recommendersystempe.enums.Motivations;
import com.recommendersystempe.enums.Roles;
import com.recommendersystempe.enums.Themes;
import com.recommendersystempe.models.Address;
import com.recommendersystempe.models.Preferences;
import com.recommendersystempe.models.User;

@DataJpaTest // annotation that configures the test to use an in-memory database
public class PreferencesRepositoryTest {

    @Autowired
    private PreferencesRepository preferencesRepository;

    @Autowired
    private UserRepository userRepository;

    
    private User user;
    private Address address;
    private Preferences preferences;
    private List<Motivations> motivations;
    private List<Hobbies> hobbies;
    private List<Themes> themes;
    private Address currentLocation;

    @BeforeEach
    public void setUp() {
        // given / arrange
        userRepository.deleteAll();
        preferencesRepository.deleteAll();

        address = new Address(
            "Rua Exemplo", 100, "Apto 202", "Boa Viagem", "Recife",
            "PE", "Brasil", "50000000");

        user = new User("Douglas", "Fragoso", 30, "Masculino", "12345678900", "81-98765-4321", "douglas@example.com", "senha123", address, Roles.USER); 

        motivations = List.of(Motivations.CULTURE, Motivations.STUDY);
        hobbies = List.of(Hobbies.PHOTOGRAPHY, Hobbies.MUSIC);   
        themes = List.of(Themes.HISTORY, Themes.ADVENTURE);
        currentLocation = new Address("Rua Exemplo", 100, "Apto 202", "Boa Viagem", "Recife","PE", "Brasil", "50000000");
        
        preferences = new Preferences(user, Instant.now(), motivations, hobbies, themes, currentLocation);
    }

    @Test
    @Transactional
    void testGivenPreferences_whenSave_ThenReturPreferences() {
        // when / act
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

        List<Motivations> motivations1 = List.of(Motivations.CULTURE, Motivations.STUDY);
        List<Hobbies> hobbies1 = List.of(Hobbies.PHOTOGRAPHY, Hobbies.MUSIC);   
        List<Themes> themes1 = List.of(Themes.HISTORY, Themes.ADVENTURE);
        Address currentLocation1 = new Address("Rua Exemplo", 100, "Apto 202", "Boa Viagem","Recife", "PE", "Brasil", "50000000");
        
        Preferences preferences1 = new Preferences(user, Instant.now(), motivations1, hobbies1, themes1, currentLocation1); 

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
