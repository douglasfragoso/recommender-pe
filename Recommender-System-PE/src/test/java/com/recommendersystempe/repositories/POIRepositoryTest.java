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
            Motivations.CULTURE, Motivations.STUDY, Motivations.ARTISTIC_VALUE,
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
            
    @Autowired
    private POIRepository poiRepository;

    private POI poi;

    @BeforeEach
    public void setUp() {
        poiRepository.deleteAll();

        poi = new POI("Parque da Cidade", "Um grande parque urbano com áreas verdes, trilhas e lagos.", MOTIVATIONS,
                HOBBIES, THEMES, ADDRESS);
    }

    @Test
    void testSavePOI_ShouldReturnSavedPOI() {
        // when / act
        POI savedPOI = poiRepository.save(poi);
        Optional<POI> foundPOI = poiRepository.findById(savedPOI.getId());

        // then / assert
        assertAll(
                () -> assertTrue(foundPOI.isPresent(), "POI must be present in the database"),
                () -> assertEquals(savedPOI.getId(), foundPOI.get().getId(), "POI id must match"));
    }

    @Test
    void testFindAllPOIs_ShouldReturnListOfPOIs() {
        // given / arrange
        POI poi1 = new POI("Parque da Cidade1", "Descrição do Parque 1", MOTIVATIONS, HOBBIES, THEMES, ADDRESS);
        POI poi2 = new POI("Parque da Cidade2", "Descrição do Parque 2", MOTIVATIONS, HOBBIES, THEMES, ADDRESS);

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
        assertAll(
                () -> assertTrue(foundPOI.isPresent(), "POI must be present in the database"),
                () -> assertEquals(savedPOI.getId(), foundPOI.get().getId(), "POI id must match"));
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
        assertAll(
                () -> assertEquals("Outro POI", updatedPOI.getName(), "POI name must be updated"),
                () -> assertEquals("Outra Descricao", updatedPOI.getDescription(), "POI description must be updated"));
    }

}