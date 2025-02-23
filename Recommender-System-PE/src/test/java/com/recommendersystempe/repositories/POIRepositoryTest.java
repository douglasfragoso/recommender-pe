package com.recommendersystempe.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import com.recommendersystempe.enums.Hobbies;
import com.recommendersystempe.enums.Motivation;
import com.recommendersystempe.enums.Roles;
import com.recommendersystempe.enums.Themes;
import com.recommendersystempe.models.Address;
import com.recommendersystempe.models.POI;
import com.recommendersystempe.models.User;

import jakarta.persistence.EntityManager;

@DataJpaTest // annotation that configures the test to use an in-memory database
public class POIRepositoryTest {

    @Autowired
    private POIRepository poiRepository;

    @Autowired
    private UserRepository userRepository;

    // EntityManager is used to interact with the persistence context
    @Autowired
    private EntityManager entityManager;

    private User user;
    private Address address;
    private POI poi;
    private List<Motivation> motivations;
    private List<Hobbies> hobbies;
    private List<Themes> themes;
    private Address poiAddress;

    @BeforeEach
    public void setUp() {
        // given / arrange
        userRepository.deleteAll();
        poiRepository.deleteAll();

        address = new Address(
                "Rua Exemplo", 100, "Apto 202", "Boa Viagem",
                "PE", "Brasil", "50000000");

        user = new User("Douglas", "Fragoso", 30, "Masculino", "12345678900", "81-98765-4321", "douglas@example.com",
                "senha123", address, Roles.USER);

        motivations = List.of(Motivation.CULTURE, Motivation.STUDY);
        hobbies = List.of(Hobbies.PHOTOGRAPHY, Hobbies.MUSIC);
        themes = List.of(Themes.HISTORY, Themes.ADVENTURE);
        poiAddress = new Address("Rua Exemplo", 100, "Apto 202", "Boa Viagem", "PE", "Brasil", "50000000");

        poi = new POI("Parque da Cidade", "Um grande parque urbano com áreas verdes, trilhas e lagos.", motivations,hobbies, themes, poiAddress);
    }

    @Test
    @Transactional
    void testGivenPOI_whenSave_ThenReturPOI() {
        // when / act
        POI savedPOI = poiRepository.save(poi);
        Optional<POI> foundPOI = poiRepository.findById(savedPOI.getId());

        // then / assert
        assertNotNull(savedPOI, "POI must not be null");
        assertTrue(savedPOI.getId() > 0, "POI id must be greater than 0");
        assertNotNull(foundPOI, "POI must be present in the database");
    }

    @Test
    void testGivenPOIList_whenFindAll_ThenReturnPOIList() {
        // given / arrange
        List<Motivation> motivations1 = List.of(Motivation.CULTURE, Motivation.STUDY);
        List<Hobbies> hobbies1 = List.of(Hobbies.PHOTOGRAPHY, Hobbies.MUSIC);
        List<Themes> themes1 = List.of(Themes.HISTORY, Themes.ADVENTURE);
        Address poiAddress1 = new Address("Rua Exemplo", 100, "Apto 202", "Boa Viagem", "PE", "Brasil", "50000000");

        POI poi1 = new POI("Parque da Cidade1", "Um grande parque urbano com áreas verdes, trilhas e lagos.",
                motivations1, hobbies1, themes1, poiAddress1);

        userRepository.save(user);
        poiRepository.save(poi);
        poiRepository.save(poi1);

        // when / act
        List<POI> poiList = poiRepository.findAll();

        // then / assert
        assertNotNull(poiList, "POI list must not be null");
        assertEquals(2, poiList.size(), "POI list must have 2 POI");
    }

    @Test
    void testGivenSavePOI_whenFindById_ThenReturnPOI() {
        // given / arrange
        userRepository.save(user);
        poiRepository.save(poi);

        // when / act
        Optional<POI> foundPOI = poiRepository.findById(poi.getId());

        // then / assert
        assertNotNull(foundPOI, "POI must not be null");
        assertTrue(foundPOI.isPresent(), "POI must be present in the database");
        assertEquals(foundPOI.get().getId(), poi.getId(), "POI id must be the same");
    }

    @Test
    @Transactional
    void testGivenPOI_whenDeleteById_ThenReturnNull() {
        // given / arrange
        poiRepository.save(poi);

        // when / act
        poiRepository.deleteById(poi.getId());
        Optional<POI> poi1 = poiRepository.findById(poi.getId());

        // then / assert
        assertTrue(poi1.isEmpty(), "User must be deleted");
    }

    @Test
    @Transactional
    void testGivenPOIList_whenUpdate_ThenReturnNothing() {
        // given / arrange
        poiRepository.save(poi);

        // when / act

        poiRepository.update(poi.getId(), "Outro POI", "Outra Descricao");

        // Clear the persistence context, causing all managed entities to become
        // detached
        entityManager.clear();

        POI updatedPOI = poiRepository.findById(poi.getId()).orElseThrow();

        // then / assert
        assertNotNull(updatedPOI, "POI must not be null");
        assertEquals("Outro POI", updatedPOI.getName(), "POI name name must be Outro 'POI'");
        assertEquals("Outra Descricao", updatedPOI.getDescription(), "POI gender must be 'Outra Descricao'");
    }

}
