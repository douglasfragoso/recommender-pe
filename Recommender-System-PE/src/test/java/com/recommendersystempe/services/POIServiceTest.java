package com.recommendersystempe.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
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

@ExtendWith(MockitoExtension.class) // annotation that extends the MockitoExtension
public class POIServiceTest {

        @Mock // anotação do Mockito que cria um mock
        private UserRepository userRepository;

        @Mock
        private POIRepository poiRepository;

        @InjectMocks // anotação do Mockito que injeta os mocks criados
        private POIService poiService;

        private POIDTO poiDTO;
        private POI poi;
        private List<Motivations> motivations;
        private List<Hobbies> hobbies;
        private List<Themes> themes;
        private Address poiAddress;

        @BeforeEach
        public void setUp() {
                // given / arrange
                poiRepository.deleteAll();
        }

        @Test
        void testGivenValidPOIDTO_whenInsert_ThenReturnPOIDTO() {
                // given / arrange
                motivations = List.of(Motivations.CULTURE, Motivations.STUDY);
                hobbies = List.of(Hobbies.PHOTOGRAPHY, Hobbies.MUSIC);
                themes = List.of(Themes.HISTORY, Themes.ADVENTURE);
                poiAddress = new Address("Rua Exemplo", 100, "Apto 202", "Boa Viagem", "Recife", "PE", "Brasil", "50000000");

                poiDTO = new POIDTO("Parque da Cidade", "Um grande parque urbano com áreas verdes, trilhas e lagos.",
                                motivations, hobbies, themes, poiAddress);
                                
                POI poi = new POI();
                poi.setName(poiDTO.getName());
                poi.setDescription(poiDTO.getDescription());
                poi.addHobbie(hobbies);
                poi.addTheme(themes);
                poi.setAddress(poiDTO.getAddress());

                // when / act
                POIDTO result = poiService.insert(poiDTO);

                // then / assert
                assertNotNull(result);
                assertEquals(poiDTO.getMotivations(), result.getMotivations());
                assertEquals(poiDTO.getAddress(), result.getAddress());
        }

        @Test
        void testGivenPOIList_whenFindAll_ThenReturnPOIPage() {
                // given / arrange
                motivations = List.of(Motivations.CULTURE, Motivations.STUDY);
                hobbies = List.of(Hobbies.PHOTOGRAPHY, Hobbies.MUSIC);
                themes = List.of(Themes.HISTORY, Themes.ADVENTURE);
                poiAddress = new Address("Rua Exemplo", 100, "Apto 202", "Boa Viagem", "Recife","PE", "Brasil", "50000000");

                poi = new POI("Parque da Cidade", "Um grande parque urbano com áreas verdes, trilhas e lagos.",
                                motivations, hobbies, themes, poiAddress);

                List<Motivations> motivations1 = List.of(Motivations.CULTURE, Motivations.STUDY);
                List<Hobbies> hobbies1 = List.of(Hobbies.PHOTOGRAPHY, Hobbies.MUSIC);
                List<Themes> themes1 = List.of(Themes.HISTORY, Themes.ADVENTURE);
                Address poiAddress1 = new Address("Rua Exemplo", 100, "Apto 202", "Boa Viagem","Recife", "PE", "Brasil",
                                "50000000");

                POI poi1 = new POI("Parque da Cidade", "Um grande parque urbano com áreas verdes, trilhas e lagos.",
                                motivations1, hobbies1, themes1, poiAddress1);

                Pageable pageable = PageRequest.of(0, 10);
                Page<POI> poiPage = new PageImpl<>(List.of(poi, poi1), pageable, 2);

                given(poiRepository.findAll(pageable)).willReturn(poiPage);

                // when / act
                Page<POIDTO> poiList = poiService.findAll(pageable);

                // then / assert
                assertNotNull(poiList);
                assertEquals(2, poiList.getTotalElements());
        }

        @Test
        void testGivenId_whenFindById_ThenReturUserDTO() {
                // given / arrange
                motivations = List.of(Motivations.CULTURE, Motivations.STUDY);
                hobbies = List.of(Hobbies.PHOTOGRAPHY, Hobbies.MUSIC);
                themes = List.of(Themes.HISTORY, Themes.ADVENTURE);
                poiAddress = new Address("Rua Exemplo", 100, "Apto 202", "Boa Viagem","Recife", "PE", "Brasil", "50000000");

                poi = new POI("Parque da Cidade", "Um grande parque urbano com áreas verdes, trilhas e lagos.",
                                motivations, hobbies, themes, poiAddress);
                ReflectionTestUtils.setField(poi, "id", 1L);

                given(poiRepository.findById(anyLong())).willReturn(Optional.of(poi));

                // when / act
                POIDTO savedPOI = poiService.findById(1L);

                // then / assert
                assertNotNull(savedPOI);
                assertEquals(1, savedPOI.getId());
        }

        @Test
        void testGivenPerson_whenUpdate_thenReturnNothing() {
                // given / arrange
                motivations = List.of(Motivations.CULTURE, Motivations.STUDY);
                hobbies = List.of(Hobbies.PHOTOGRAPHY, Hobbies.MUSIC);
                themes = List.of(Themes.HISTORY, Themes.ADVENTURE);
                poiAddress = new Address("Rua Exemplo", 100, "Apto 202", "Boa Viagem","Recife", "PE", "Brasil", "50000000");

                poi = new POI("Parque da Cidade", "Um grande parque urbano com áreas verdes, trilhas e lagos.",
                                motivations, hobbies, themes, poiAddress);
                ReflectionTestUtils.setField(poi, "id", 1L);

                POIDTO poiDTO = new POIDTO(1L, "Parque da Cidade2",
                                "Um grande parque urbano com áreas verdes, trilhas e lagos.");

                when(poiRepository.findById(1L)).thenReturn(Optional.of(poi));

                // when / act
                poiService.update(poiDTO);

                // then / assert
                verify(poiRepository, times(1)).update(
                                1L, // ID do usuário autenticado
                                "Parque da Cidade2", // Novo first name
                                "Um grande parque urbano com áreas verdes, trilhas e lagos.");
        }

        @Test
        void testGivenUserId_whenDeleteById_thenReturnNothing() {
                // given / arrange
                motivations = List.of(Motivations.CULTURE, Motivations.STUDY);
                hobbies = List.of(Hobbies.PHOTOGRAPHY, Hobbies.MUSIC);
                themes = List.of(Themes.HISTORY, Themes.ADVENTURE);
                poiAddress = new Address("Rua Exemplo", 100, "Apto 202", "Boa Viagem", "Recife","PE", "Brasil", "50000000");

                poi = new POI("Parque da Cidade", "Um grande parque urbano com áreas verdes, trilhas e lagos.",
                                motivations, hobbies, themes, poiAddress);
                ReflectionTestUtils.setField(poi, "id", 1L);

                Long poiId = 1L;

                // when / act
                poiService.deleteById(poiId);

                // then / assert
                verify(poiRepository, times(1)).deleteById(poiId);

        }

}
