package com.recommendersystempe.controllers;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.recommendersystempe.configs.SecurityConfig;
import com.recommendersystempe.dtos.PreferencesDTO;
import com.recommendersystempe.dtos.UserDTO;
import com.recommendersystempe.enums.Hobbies;
import com.recommendersystempe.enums.Motivations;
import com.recommendersystempe.enums.Roles;
import com.recommendersystempe.enums.Themes;
import com.recommendersystempe.models.Address;
import com.recommendersystempe.models.Preferences;
import com.recommendersystempe.models.User;
import com.recommendersystempe.repositories.PreferencesRepository;
import com.recommendersystempe.repositories.UserRepository;
import com.recommendersystempe.service.AuthenticationService;
import com.recommendersystempe.service.PreferencesService;
import com.recommendersystempe.service.TokenService;
import com.recommendersystempe.service.UserService;

@SuppressWarnings("unused")
@WebMvcTest(PreferencesController.class) // Habilita o contexto do Spring MVC para testes - Enables the Spring MVC context for testing
@Import(SecurityConfig.class) // Importa a configuração real - Imports the real configuration
public class PreferencesControllerTest {

        // MockMvc é uma classe do Spring Test que permite simular requisições HTTP - MockMvc is a Spring Test class that allows you to simulate HTTP requests
        @Autowired
        private MockMvc mockMvc;

        // ObjectMapper é uma classe do Jackson que permite converter objetos Java em JSON e vice-versa - ObjectMapper is a Jackson class that allows you to convert Java objects to JSON and vice versa
        @Autowired
        private ObjectMapper objectMapper;

        @MockitoBean // Anotação do Spring Test que cria um mock de um bean, precisa de contexto - Spring Test annotation that creates a mock of a bean, needs context
        private UserService userService;

        @MockitoBean
        private PreferencesService preferencesService;

        @MockitoBean
        private AuthenticationService authenticationService;

        @MockitoBean
        private TokenService tokenService;

        @MockitoBean // Apenas se o UserRepository for usado indiretamente - Only if UserRepository is used indirectly
        private UserRepository userRepository;

        @MockitoBean
        private PreferencesRepository preferencesRepository;

        private User user;
        private Address address;
        private PreferencesDTO preferencesDTO;
        private List<Motivations> motivations;
        private List<Hobbies> hobbies;
        private List<Themes> themes;
        private Address currentLocation;

        @BeforeEach
        public void setUp() {
                // given / arrange
                address = new Address(
                                "Rua Exemplo", 100, "Apto 202", "Boa Viagem", "Recife",
                                "PE", "Brasil", "50000000");

                user = new User(
                                "Douglas",
                                "Fragoso",
                                30,
                                "Masculino",
                                "12345678909",
                                "81-98765-4321",
                                "douglas@example.com",
                                "Senha123*",
                                address,
                                Roles.MASTER);
                ReflectionTestUtils.setField(user, "id", 1L); // ID definido via reflection - ID defined via reflection

                // Configurar UserDetailsService mockado - Configure mocked UserDetailsService
                UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                                user.getEmail(),
                                user.getPassword(),
                                Collections.singletonList(new SimpleGrantedAuthority("ROLE_MASTER")));

                // Mockar UserDetailsService - Mock UserDetailsService
                given(authenticationService.loadUserByUsername(user.getEmail())).willReturn(userDetails);
        }

        @Test
        void testListPreferencesObject_whenFindAll_ThenReturnListPreferences()
                        throws JsonProcessingException, Exception {
                // given / arrange
                motivations = List.of(Motivations.CULTURE, Motivations.STUDY);
                hobbies = List.of(Hobbies.PHOTOGRAPHY, Hobbies.MUSIC);
                themes = List.of(Themes.HISTORY, Themes.ADVENTURE);
                currentLocation = new Address("Rua Exemplo", 100, "Apto 202", "Boa Viagem", "Recife","PE", "Brasil", "50000000");

                Preferences preferences = new Preferences(user, Instant.now(), motivations, hobbies, themes,
                                currentLocation);

                ReflectionTestUtils.setField(preferences, "id", 1L); // ID definido via reflection - ID defined via reflection
                ReflectionTestUtils.setField(preferences, "user", user); // User definido via reflection - User defined via reflection

                List<Motivations> motivations1 = List.of(Motivations.CULTURE, Motivations.STUDY);
                List<Hobbies> hobbies1 = List.of(Hobbies.PHOTOGRAPHY, Hobbies.MUSIC);
                List<Themes> themes1 = List.of(Themes.HISTORY, Themes.ADVENTURE);
                Address currentLocation1 = new Address("Rua Exemplo", 100, "Apto 202", "Boa Viagem", "Recife","PE", "Brasil",
                                "50000000");

                Preferences preferences1 = new Preferences(user, Instant.now(), motivations1, hobbies1, themes1,
                                currentLocation1);
                ReflectionTestUtils.setField(preferences1, "id", 2L); // ID definido via reflection - ID defined via reflection
                ReflectionTestUtils.setField(preferences1, "user", user); // User definido via reflection - User defined via reflection

                Pageable pageable = PageRequest.of(0, 10);

                List<PreferencesDTO> preferencesDTOList = List.of(
                                new PreferencesDTO(preferences.getId(), preferences.getUser().getId(),
                                                preferences.getDate(),
                                                preferences.getMotivations(), preferences.getHobbies(),
                                                preferences.getThemes(), preferences.getCurrentLocation()), 
                                new PreferencesDTO(preferences1.getId(), preferences1.getUser().getId(),
                                                preferences1.getDate(),
                                                preferences1.getMotivations(), preferences1.getHobbies(),
                                                preferences1.getThemes(), preferences1.getCurrentLocation()));

                Page<PreferencesDTO> preferencesPage = new PageImpl<>(preferencesDTOList, pageable, 2);

                given(preferencesService.findAll(any(Pageable.class))).willReturn(preferencesPage);

                // when / act
                ResultActions response = mockMvc.perform(get("/preferences")
                                .param("page", "0")
                                .param("size", "10")
                                .with(user("douglas@example.com").password("senha123").roles("MASTER")) // Autenticação - Authentication
                                .contentType("application/json"));

                // then / assert
                response.andExpect(status().isOk())
                                .andDo(print())
                                .andExpect(jsonPath("$.totalElements").value(preferencesPage.getTotalElements()));
        }

        @Test
        void testGivenPreferencesId_whenFindbyId_ThenReturnPreferencesDTO() throws JsonProcessingException, Exception {
                // given / arrange
                motivations = List.of(Motivations.CULTURE, Motivations.STUDY);
                hobbies = List.of(Hobbies.PHOTOGRAPHY, Hobbies.MUSIC);
                themes = List.of(Themes.HISTORY, Themes.ADVENTURE);
                currentLocation = new Address("Rua Exemplo", 100, "Apto 202", "Boa Viagem", "Recife","PE", "Brasil", "50000000");

                Preferences preferences = new Preferences(user, Instant.now(), motivations, hobbies, themes,
                                currentLocation);

                ReflectionTestUtils.setField(preferences, "id", 1L); // ID definido via reflection - ID defined via reflection
                ReflectionTestUtils.setField(preferences, "user", user); // User definido via reflection - User defined via reflection

                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);

                Long id = 1L;
                given(preferencesService.findById(id)).willReturn(new PreferencesDTO(preferences.getId(),
                                preferences.getUser().getId(),
                                preferences.getDate(), preferences.getMotivations(), preferences.getHobbies(),
                                preferences.getThemes(), preferences.getCurrentLocation()));

                // when / act
                ResultActions response = mockMvc.perform(get("/preferences/id/{id}", id)
                                .with(user("douglas@example.com").password("senha123").roles("MASTER")) // Autenticação - Authentication
                                .contentType("application/json"));

                // then / assert
                response.andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.motivations").value(preferences.getMotivations().stream()
                                                .map(Enum::name).collect(Collectors.toList())))
                                .andExpect(jsonPath("$.hobbies").value(preferences.getHobbies().stream()
                                                .map(Enum::name).collect(Collectors.toList())))
                                .andExpect(jsonPath("$.themes").value(preferences.getThemes().stream()
                                                .map(Enum::name).collect(Collectors.toList())));
        }

}