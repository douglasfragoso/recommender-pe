package com.recommendersystempe.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;


import java.time.Instant;
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
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import com.recommendersystempe.dtos.PreferencesDTO;
import com.recommendersystempe.enums.Hobbies;
import com.recommendersystempe.enums.Motivations;
import com.recommendersystempe.enums.Roles;
import com.recommendersystempe.enums.Themes;
import com.recommendersystempe.models.Address;
import com.recommendersystempe.models.Preferences;
import com.recommendersystempe.models.User;
import com.recommendersystempe.repositories.PreferencesRepository;
import com.recommendersystempe.repositories.UserRepository;
import com.recommendersystempe.service.PreferencesService;

@ExtendWith(MockitoExtension.class) // Anotação que estende o MockitoExtension - Annotation that extends the MockitoExtension 
public class PreferencesServiceTest {

        @Mock // Anotação do Mockito que cria um mock - Mockito annotation that creates a mock
        private UserRepository userRepository;

        @Mock
        private PreferencesRepository preferencesRepository;

        @InjectMocks // Anotação do Mockito que injeta os mocks criados - Mockito annotation that injects the created mocks
        private PreferencesService preferencesService;

        private User user;
        private Address address;
        // private PreferencesDTO preferencesDTO;
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

                user = new User("Douglas", "Fragoso", 30, "Masculino", "12345678900", "81-98765-4321",
                                "douglas@example.com", "senha123", address, Roles.USER);
        }

        // @Test
        // void testGivenValidUserDTO_whenInsert_ThenReturnUserDTO() {
        //         // given / arrange
        //         userRepository.save(user);
        //         ReflectionTestUtils.setField(user, "id", 1L);

        //         given(userRepository.findById(1L)).willReturn(Optional.of(user));

        //         motivations = List.of(Motivation.CULTURE, Motivation.STUDY);
        //         hobbies = List.of(Hobbies.PHOTOGRAPHY, Hobbies.MUSIC);
        //         themes = List.of(Themes.HISTORY, Themes.ADVENTURE);
        //         currentLocation = new Address("Rua Exemplo", 100, "Apto 202", "Boa Viagem", "Recife", "PE", "Brasil", "50000000");
        //         preferencesDTO = new PreferencesDTO(motivations, hobbies, themes, currentLocation);

        //         // Mockar autenticação
        //         Authentication auth = mock(Authentication.class);
        //         when(auth.getName()).thenReturn("douglas@example.com");
        //         SecurityContextHolder.getContext().setAuthentication(auth);

        //         // Mockar repositório
        //         given(userRepository.findByEmail("douglas@example.com")).willReturn(user);

        //         Preferences preferences = new Preferences();
        //         preferences.setUser(userRepository.findById(1L).get());
        //         preferences.setDate(Instant.now());
        //         preferences.addMotivation(motivations);
        //         preferences.addHobbie(hobbies);
        //         preferences.addTheme(themes);
        //         preferences.setCurrentLocation(preferencesDTO.getCurrentLocation());

        //         // when / act
        //         PreferencesDTO result = preferencesService.insert(preferencesDTO);

        //         // then / assert
        //         assertNotNull(result);
        //         assertEquals(preferencesDTO.getMotivations(), result.getMotivations());
        //         assertEquals(preferencesDTO.getCurrentLocation(), result.getCurrentLocation());
        // }

        @Test
        void testGivenPreferencesList_whenFindAll_ThenReturnPreferencesPage() {
                // given / arrange
                motivations = List.of(Motivations.CULTURE, Motivations.STUDY);
                hobbies = List.of(Hobbies.PHOTOGRAPHY, Hobbies.MUSIC);
                themes = List.of(Themes.HISTORY, Themes.ADVENTURE);
                currentLocation = new Address("Rua Exemplo", 100, "Apto 202", "Boa Viagem", "Recife", "PE", "Brasil", "50000000");

                preferences = new Preferences(user, Instant.now(), motivations, hobbies, themes, currentLocation);

                List<Motivations> motivations1 = List.of(Motivations.CULTURE, Motivations.STUDY);
                List<Hobbies> hobbies1 = List.of(Hobbies.PHOTOGRAPHY, Hobbies.MUSIC);
                List<Themes> themes1 = List.of(Themes.HISTORY, Themes.ADVENTURE);
                Address currentLocation1 = new Address("Rua Exemplo", 100, "Apto 202", "Boa Viagem", "Recife", "PE", "Brasil",
                                "50000000");

                Preferences preferences1 = new Preferences(user, Instant.now(), motivations1, hobbies1, themes1,
                                currentLocation1);

                Pageable pageable = PageRequest.of(0, 10);
                Page<Preferences> preferencesPage = new PageImpl<>(List.of(preferences, preferences1), pageable, 2);

                given(preferencesRepository.findAll(pageable)).willReturn(preferencesPage);

                // when / act
                Page<PreferencesDTO> preferencesList = preferencesService.findAll(pageable);

                // then / assert
                assertNotNull(preferencesList);
                assertEquals(2, preferencesList.getTotalElements());
        }

        @Test
        void testGivenId_whenFindById_ThenReturUserDTO() {
                // given / arrange
                userRepository.save(user);
                ReflectionTestUtils.setField(user, "id", 1L);

                motivations = List.of(Motivations.CULTURE, Motivations.STUDY);
                hobbies = List.of(Hobbies.PHOTOGRAPHY, Hobbies.MUSIC);
                themes = List.of(Themes.HISTORY, Themes.ADVENTURE);
                currentLocation = new Address("Rua Exemplo", 100, "Apto 202", "Boa Viagem", "Recife", "PE", "Brasil", "50000000");

                preferences = new Preferences(user, Instant.now(), motivations, hobbies, themes, currentLocation);

                given(preferencesRepository.findById(anyLong())).willReturn(Optional.of(preferences));

                // when / act
                PreferencesDTO savedPreferences = preferencesService.findById(1L);

                // then / assert
                assertNotNull(savedPreferences);
                assertEquals(1, savedPreferences.getUser());
        }

}
