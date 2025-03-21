package com.recommendersystempe.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.recommendersystempe.enums.Hobbies;
import com.recommendersystempe.enums.Motivations;
import com.recommendersystempe.enums.Themes;
import com.recommendersystempe.models.Address;
import com.recommendersystempe.models.POI;

@DataJpaTest
public class POIRepositoryTest {

    private static final List<Motivations> MOTIVATIONS = List.of(
            Motivations.CULTURE, Motivations.STUDY, Motivations.APPRECIATION,
            Motivations.RELAXATION, Motivations.SOCIAL);
    private static final List<Hobbies> HOBBIES = List.of(
            Hobbies.PHOTOGRAPHY, Hobbies.MUSIC, Hobbies.ADVENTURE,
            Hobbies.ART, Hobbies.READING);
    private static final List<Themes> THEMES = List.of(
            Themes.HISTORY, Themes.ADVENTURE, Themes.NATURE,
            Themes.CULTURAL, Themes.AFRO_BRAZILIAN);

    @Autowired
    private POIRepository poiRepository;

    private POI poi;
    private List<Motivations> motivations;
    private List<Hobbies> hobbies;
    private List<Themes> themes;
    private Address poiAddress;

    private POI createPOI(String name, String description, List<Motivations> motivations, List<Hobbies> hobbies,
            List<Themes> themes, Address address) {
        return new POI(name, description, MOTIVATIONS, HOBBIES, THEMES, address);
    }

    private Address createAddress(String street, int number, String complement, String neighborhood, String city,
            String state, String country, String zipCode) {
        return new Address(street, number, complement, neighborhood, city, state, country, zipCode);
    }

    @BeforeEach
    public void setUp() {
        // Clear repositories before each test
        poiRepository.deleteAll();

        // Create common test data
        motivations = MOTIVATIONS;
        hobbies = HOBBIES;
        themes = THEMES;
        poiAddress = createAddress("Rua Exemplo", 100, "Apto 202", "Boa Viagem", "Recife", "PE", "Brasil", "50000000");
        poi = createPOI("Parque da Cidade", "Um grande parque urbano com áreas verdes, trilhas e lagos.", motivations,
                hobbies, themes, poiAddress);
    }

    @Test
    void testSavePOI_ShouldReturnSavedPOI() {
        // when / act
        POI savedPOI = poiRepository.save(poi);
        Optional<POI> foundPOI = poiRepository.findById(savedPOI.getId());

        // then / assert
        assertTrue(foundPOI.isPresent(), "POI must be present in the database");
        assertEquals(savedPOI.getId(), foundPOI.get().getId(), "POI id must match");
    }

    @Test
    void testFindAllPOIs_ShouldReturnListOfPOIs() {
        // given / arrange
        POI poi1 = createPOI("Parque da Cidade1", "Descrição do Parque 1", motivations, hobbies, themes, poiAddress);
        POI poi2 = createPOI("Parque da Cidade2", "Descrição do Parque 2", motivations, hobbies, themes, poiAddress);

        poiRepository.save(poi1);
        poiRepository.save(poi2);

        // when / act
        List<POI> poiList = poiRepository.findAll();

        // then / assert
        assertEquals(2, poiList.size(), "POI list must have 2 POIs");
    }

    @Test
    void testFindPOIById_ShouldReturnPOI() {
        // given / arrange
        POI savedPOI = poiRepository.save(poi);

        // when / act
        Optional<POI> foundPOI = poiRepository.findById(savedPOI.getId());

        // then / assert
        assertTrue(foundPOI.isPresent(), "POI must be present in the database");
        assertEquals(savedPOI.getId(), foundPOI.get().getId(), "POI id must match");
    }

    @Test
    void testDeletePOIById_ShouldRemovePOI() {
        // given / arrange
        POI savedPOI = poiRepository.save(poi);

        // when / act
        poiRepository.deleteById(savedPOI.getId());
        Optional<POI> foundPOI = poiRepository.findById(savedPOI.getId());

        // then / assert
        assertTrue(foundPOI.isEmpty(), "POI must be deleted from the database");
    }

    @Test
    void testUpdatePOI_ShouldUpdateNameAndDescription() {
        // given / arrange
        POI savedPOI = poiRepository.save(poi);

        // when / act
        poiRepository.update(savedPOI.getId(), "Outro POI", "Outra Descricao");
        POI updatedPOI = poiRepository.findById(savedPOI.getId()).orElseThrow();

        // then / assert
        assertEquals("Outro POI", updatedPOI.getName(), "POI name must be updated");
        assertEquals("Outra Descricao", updatedPOI.getDescription(), "POI description must be updated");
    }

}