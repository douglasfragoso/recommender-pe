package com.recommendersystempe.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.time.Instant;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import com.recommendersystempe.dtos.POIDTO;
import com.recommendersystempe.dtos.PreferencesDTO;
import com.recommendersystempe.dtos.RecommendationDTO;
import com.recommendersystempe.enums.Hobbies;
import com.recommendersystempe.enums.Motivations;
import com.recommendersystempe.enums.Roles;
import com.recommendersystempe.enums.Themes;
import com.recommendersystempe.models.Address;
import com.recommendersystempe.models.POI;
import com.recommendersystempe.models.Preferences;
import com.recommendersystempe.models.User;
import com.recommendersystempe.repositories.POIRepository;
import com.recommendersystempe.repositories.PreferencesRepository;
import com.recommendersystempe.repositories.UserRepository;
import com.recommendersystempe.service.PreferencesService;
import com.recommendersystempe.service.RecommendationService;

@ExtendWith(MockitoExtension.class)
public class PreferencesServiceTest {

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
        private static final Address CURRENT_LOCATION = new Address(
                        "Rua Exemplo", 100, "Apto 202", "Boa Viagem", "Recife",
                        "PE", "Brasil", "50000000");
        private static final User USER = new User(
                        "Richard", "Fragoso", 30, "Masculino", "12345678900", "81-98765-4321",
                        "richard@example.com", "Senha123*", ADDRESS, Roles.USER);

        @Mock
        private UserRepository userRepository;

        @Mock
        private PreferencesRepository preferencesRepository;

        @Mock
        private POIRepository poiRepository;

        @Mock
        private RecommendationService recommendationService;

        @InjectMocks
        private PreferencesService preferencesService;

        private Preferences preferences;

        private void mockAuthenticatedUser(User user) {
                Authentication auth = mock(Authentication.class);
                when(auth.getName()).thenReturn(user.getEmail());
                SecurityContextHolder.getContext().setAuthentication(auth);
                // Marcar o stub como lenient para evitar UnnecessaryStubbingException
                lenient().when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        }

        private POI createPoi(String nome, String descricao) {
                POI poi = new POI(nome, descricao, MOTIVATIONS, HOBBIES, THEMES, CURRENT_LOCATION);
                ReflectionTestUtils.setField(poi, "id", System.nanoTime());
                return poi;
        }

        @Test
        void testInsertPreferences_ShouldReturnPOIDTOList() {
                // given / arrange
                ReflectionTestUtils.setField(USER, "id", 1L);
                mockAuthenticatedUser(USER);

                List<POI> poiList = List.of(
                                createPoi("Parque da Cidade", "Descrição 1"),
                                createPoi("Parque da Cidade 2", "Descrição 2"),
                                createPoi("Parque da Cidade 3", "Descrição 3"),
                                createPoi("Parque da Cidade 4", "Descrição 4"),
                                createPoi("Parque da Cidade 5", "Descrição 5"));

                PreferencesDTO dto = new PreferencesDTO(
                                MOTIVATIONS,
                                HOBBIES,
                                THEMES,
                                CURRENT_LOCATION);

                List<POIDTO> poiDTOList = poiList.stream()
                                .map(poi -> new POIDTO(poi.getId(), poi.getName(), poi.getDescription()))
                                .toList();

                // Create a proper RecommendationDTO instead of casting
                RecommendationDTO recommendationDTO = new RecommendationDTO();
                recommendationDTO.addPOI(poiList);

                given(recommendationService.recommendation(any(Preferences.class))).willReturn(recommendationDTO);

                // when / act
                RecommendationDTO result = preferencesService.insert(dto);

                // then / assert
                assertNotNull(result);
                assertEquals(poiDTOList.size(), result.getPois().size());
                verify(preferencesRepository, times(2)).save(any(Preferences.class));
        }

        @Test
        void testFindAllPreferences_ShouldReturnPage() {
                // given / arrange
                preferences = new Preferences(USER, Instant.now(), MOTIVATIONS, HOBBIES, THEMES, CURRENT_LOCATION);

                Preferences preferences1 = new Preferences(USER, Instant.now(), MOTIVATIONS, HOBBIES, THEMES,
                                CURRENT_LOCATION);

                Pageable pageable = PageRequest.of(0, 10);
                Page<Preferences> preferencesPage = new PageImpl<>(List.of(preferences, preferences1), pageable, 2);

                given(preferencesRepository.findAll(pageable)).willReturn(preferencesPage);

                // when / act
                Page<PreferencesDTO> preferencesList = preferencesService.findAll(pageable);

                // then / assert
                assertAll(
                                () -> assertNotNull(preferencesList, "PreferencesPage must not be null"),
                                () -> assertEquals(2, preferencesList.getTotalElements(),
                                                "PreferencesPage must have 2 elements"));
        }

        @Test
        void testFindPreferencesById_ShouldReturnPreferencesDTO() {
                // given / arrange
                userRepository.save(USER);
                ReflectionTestUtils.setField(USER, "id", 1L);

                preferences = new Preferences(USER, Instant.now(), MOTIVATIONS, HOBBIES, THEMES, CURRENT_LOCATION);

                given(preferencesRepository.findById(anyLong())).willReturn(Optional.of(preferences));

                // when / act
                PreferencesDTO savedPreferences = preferencesService.findById(1L);

                // then / assert
                assertAll(
                                () -> assertNotNull(savedPreferences, "PreferencesDTO must not be null"),
                                () -> assertEquals(preferences.getId(), savedPreferences.getId(),
                                                "Id must be the same"),
                                () -> assertEquals(USER.getId(), savedPreferences.getUser(),
                                                "User id must be the same"));
        }

}