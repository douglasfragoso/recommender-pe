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
                        Motivations.CULTURE, Motivations.EDUCATION, Motivations.ARTISTIC_VALUE,
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

                poi = new POI("Parque da Cidade", "Um grande parque urbano com áreas verdes, trilhas e lagos.",
                                MOTIVATIONS,
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
        void testGivenPOI_whenExistsByName_thenReturnTrue() {
                // given / arrange
                POI savedPOI = poiRepository.save(poi);

                // when / act
                boolean exists = poiRepository.existsByName(savedPOI.getName());

                // then / assert
                assertTrue(exists, "POI with given name should exist in the database");
        }

        @Test
        void testGivenNonExistentName_whenExistsByName_thenReturnFalse() {
                // when / act
                boolean exists = poiRepository.existsByName("Non-existent POI");

                // then / assert
                assertFalse(exists, "POI with given name should not exist in the database");
        }

        @Test
        void testGivenPOI_whenExistsByNameAndIdNot_thenReturnTrue() {
                // given / arrange
                POI savedPOI1 = poiRepository.save(poi);
                POI savedPOI2 = new POI("Museu de Arte", "Museu com diversas obras de arte", MOTIVATIONS,
                                HOBBIES, THEMES, ADDRESS);
                poiRepository.save(savedPOI2);

                // when / act
                boolean exists = poiRepository.existsByNameAndIdNot(savedPOI2.getName(), savedPOI1.getId());

                // then / assert
                assertTrue(exists, "POI with given name and different ID should exist in the database");
        }

        @Test
        void testGivenPOI_whenExistsByNameAndIdNotWithSameId_thenReturnFalse() {
                // given / arrange
                POI savedPOI = poiRepository.save(poi);

                // when / act
                boolean exists = poiRepository.existsByNameAndIdNot(savedPOI.getName(), savedPOI.getId());

                // then / assert
                assertFalse(exists, "POI with same name and ID should not be considered as existing");
        }

}