package com.recommendersystempe.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import com.recommendersystempe.dtos.POIDTO;
import com.recommendersystempe.enums.Hobbies;
import com.recommendersystempe.enums.Motivations;
import com.recommendersystempe.enums.Themes;
import com.recommendersystempe.models.Address;
import com.recommendersystempe.models.POI;
import com.recommendersystempe.repositories.POIRepository;
import com.recommendersystempe.repositories.UserRepository;
import com.recommendersystempe.service.POIService;

@ExtendWith(MockitoExtension.class)
public class POIServiceTest {

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
    private static final POIDTO POI_DTO = new POIDTO(
            "Parque da Cidade", "Um grande parque urbano com áreas verdes, trilhas e lagos.",
            MOTIVATIONS, HOBBIES, THEMES, ADDRESS);

    @Mock
    private UserRepository userRepository;

    @Mock
    private POIRepository poiRepository;

    @InjectMocks
    private POIService poiService;

    @Test
    void testInsertPOI_ShouldReturnPOIDTO() {
        // given / arrange
        given(poiRepository.save(any(POI.class))).willAnswer(invocation -> {
            POI poi = invocation.getArgument(0);
            ReflectionTestUtils.setField(poi, "id", 1L);
            return poi;
        });

        // when / act
        POIDTO result = poiService.insert(POI_DTO);

        // then / assert
        assertAll(
                () -> assertNotNull(result, "POI must not be null"),
                () -> assertEquals(POI_DTO.getName(), result.getName(), "POI name must match"),
                () -> assertEquals(POI_DTO.getDescription(), result.getDescription(), "POI description must match"),
                () -> assertEquals(POI_DTO.getThemes(), result.getThemes(), "POI themes must match"),
                () -> assertEquals(POI_DTO.getHobbies(), result.getHobbies(), "POI hobbies must match"),
                () -> assertEquals(POI_DTO.getMotivations(), result.getMotivations(), "POI motivations must match"),
                () -> assertEquals(POI_DTO.getAddress(), result.getAddress(), "POI address must match")
        );
    }

    @Test
    void testFindAllPOIs_ShouldReturnPage() {
        // given / arrange
        POI poi = new POI( "Parque da Cidade", "Um grande parque urbano com áreas verdes, trilhas e lagos.", MOTIVATIONS, HOBBIES, THEMES, ADDRESS);
        POI poi1 = new POI("Parque da Cidade", "Um grande parque urbano com áreas verdes, trilhas e lagos.", MOTIVATIONS, HOBBIES, THEMES, ADDRESS);
        ReflectionTestUtils.setField(poi, "id", 1L);
        ReflectionTestUtils.setField(poi1, "id", 2L);

        Pageable pageable = PageRequest.of(0, 10);
        Page<POI> poiPage = new PageImpl<>(List.of(poi, poi1), pageable, 2);

        given(poiRepository.findAll(pageable)).willReturn(poiPage);

        // when / act
        Page<POIDTO> result = poiService.findAll(pageable);

        // then / assert
        assertAll(
                () -> assertNotNull(result, "POI list must not be null"),
                () -> assertEquals(2, result.getTotalElements(), "Total elements must match")
        );
    }

    @Test
    void testFindPOIById_ShouldReturnPOIDTO() {
        // given / arrange
        POI poi = new POI("Parque da Cidade", "Um grande parque urbano com áreas verdes, trilhas e lagos.", MOTIVATIONS, HOBBIES, THEMES, ADDRESS);
        ReflectionTestUtils.setField(poi, "id", 1L);
        given(poiRepository.findById(anyLong())).willReturn(Optional.of(poi));

        // when / act
        POIDTO result = poiService.findById(1L);

        // then / assert
        assertAll(
                () -> assertNotNull(result, "POI must not be null"),
                () -> assertEquals(1L, result.getId(), "POI ID must match")
        );
    }

    @Test
    void testUpdatePOI_ShouldUpdatePOI() {
        // given / arrange
        POI poi = new POI("Parque da Cidade", "Um grande parque urbano com áreas verdes, trilhas e lagos.", MOTIVATIONS, HOBBIES, THEMES, ADDRESS);
        ReflectionTestUtils.setField(poi, "id", 1L);
        POIDTO poiDTO = new POIDTO(1L, "Parque da Cidade2", "Um grande parque urbano com áreas verdes, trilhas e lagos.", MOTIVATIONS, HOBBIES, THEMES, ADDRESS);

        given(poiRepository.findById(1L)).willReturn(Optional.of(poi));

        // when / act
        poiService.update(poiDTO);

        // then / assert
        verify(poiRepository, times(1)).update(
                1L,
                "Parque da Cidade2",
                "Um grande parque urbano com áreas verdes, trilhas e lagos."
        );
    }

    @Test
    void testDeletePOIById_ShouldDeletePOI() {
        // given / arrange
        Long poiId = 1L;

        // when / act
        poiService.deleteById(poiId);

        // then / assert
        verify(poiRepository, times(1)).deleteById(poiId);
    }
}
