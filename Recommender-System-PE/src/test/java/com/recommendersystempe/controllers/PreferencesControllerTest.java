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
@WebMvcTest(PreferencesController.class) // Habilita o contexto do Spring MVC para testes
@Import(SecurityConfig.class) // Importa a configuração real
public class PreferencesControllerTest {

        // MockMvc é uma classe do Spring Test que permite simular requisições HTTP
        @Autowired
        private MockMvc mockMvc;

        // ObjectMapper é uma classe do Jackson que permite converter objetos Java em
        // JSON e vice-versa
        @Autowired
        private ObjectMapper objectMapper;

        @MockitoBean // anotação do Spring Test que cria um mock de um bean, precisa de contexto
        private UserService userService;

        @MockitoBean
        private PreferencesService preferencesService;

        @MockitoBean
        private AuthenticationService authenticationService;

        @MockitoBean
        private TokenService tokenService;

        @MockitoBean // Apenas se o UserRepository for usado indiretamente
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
                                "Rua Exemplo", 100, "Apto 202", "Boa Viagem",
                                "PE", "Brasil", "50000000");

                user = new User("Douglas", "Fragoso", 30, "Masculino", "12345678900", "81-98765-4321",
                                "douglas@example.com", "senha123", address, Roles.MASTER);
                ReflectionTestUtils.setField(user, "id", 1L); // ID definido via reflection

                // Configurar UserDetailsService mockado
                UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                                user.getEmail(),
                                user.getPassword(),
                                Collections.singletonList(new SimpleGrantedAuthority("ROLE_MASTER")));

                // Mockar UserDetailsService
                given(authenticationService.loadUserByUsername(user.getEmail())).willReturn(userDetails);
        }

        @Test
        void testGivenPreferencesDTO_whenSave_ThenReturnPreferencesDTO() throws JsonProcessingException, Exception {
                // given / arrange
                motivations = List.of(Motivations.CULTURE, Motivations.STUDY);
                hobbies = List.of(Hobbies.PHOTOGRAPHY, Hobbies.MUSIC);
                themes = List.of(Themes.HISTORY, Themes.ADVENTURE);
                currentLocation = new Address("Rua Exemplo", 100, "Apto 202", "Boa Viagem", "PE", "Brasil", "50000000");
                preferencesDTO = new PreferencesDTO(motivations, hobbies, themes, currentLocation);

                given(preferencesService.insert(any(PreferencesDTO.class)))
                                .willAnswer((invocation) -> invocation.getArgument(0));

                // when / act
                ResultActions response = mockMvc.perform(post("/preferences/register")
                                .contentType("application/json")
                                .with(user("douglas@example.com").password("senha123").roles("MASTER")) // Autenticação
                                .content(objectMapper.writeValueAsString(preferencesDTO)));

                // then / assert
                response.andDo(print())
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.motivations").value(preferencesDTO.getMotivations().stream()
                                                .map(Enum::name).collect(Collectors.toList())))
                                .andExpect(jsonPath("$.hobbies").value(preferencesDTO.getHobbies().stream()
                                                .map(Enum::name).collect(Collectors.toList())))
                                .andExpect(jsonPath("$.themes").value(preferencesDTO.getThemes().stream()
                                                .map(Enum::name).collect(Collectors.toList())));
        }

        @Test
        void testListPreferencesObject_whenFindAll_ThenReturnListPreferences()
                        throws JsonProcessingException, Exception {
                // given / arrange
                motivations = List.of(Motivations.CULTURE, Motivations.STUDY);
                hobbies = List.of(Hobbies.PHOTOGRAPHY, Hobbies.MUSIC);
                themes = List.of(Themes.HISTORY, Themes.ADVENTURE);
                currentLocation = new Address("Rua Exemplo", 100, "Apto 202", "Boa Viagem", "PE", "Brasil", "50000000");

                Preferences preferences = new Preferences(user, Instant.now(), motivations, hobbies, themes,
                                currentLocation);

                ReflectionTestUtils.setField(preferences, "id", 1L); // ID definido via reflection
                ReflectionTestUtils.setField(preferences, "user", user); // User definido via reflection

                List<Motivations> motivations1 = List.of(Motivations.CULTURE, Motivations.STUDY);
                List<Hobbies> hobbies1 = List.of(Hobbies.PHOTOGRAPHY, Hobbies.MUSIC);
                List<Themes> themes1 = List.of(Themes.HISTORY, Themes.ADVENTURE);
                Address currentLocation1 = new Address("Rua Exemplo", 100, "Apto 202", "Boa Viagem", "PE", "Brasil",
                                "50000000");

                Preferences preferences1 = new Preferences(user, Instant.now(), motivations1, hobbies1, themes1,
                                currentLocation1);
                ReflectionTestUtils.setField(preferences1, "id", 2L); // ID definido via reflection
                ReflectionTestUtils.setField(preferences1, "user", user); // User definido via reflection

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
                                .with(user("douglas@example.com").password("senha123").roles("MASTER")) // Autenticação
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
                currentLocation = new Address("Rua Exemplo", 100, "Apto 202", "Boa Viagem", "PE", "Brasil", "50000000");

                Preferences preferences = new Preferences(user, Instant.now(), motivations, hobbies, themes,
                                currentLocation);

                ReflectionTestUtils.setField(preferences, "id", 1L); // ID definido via reflection
                ReflectionTestUtils.setField(preferences, "user", user); // User definido via reflection

                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);

                Long id = 1L;
                given(preferencesService.findById(id)).willReturn(new PreferencesDTO(preferences.getId(),
                                preferences.getUser().getId(),
                                preferences.getDate(), preferences.getMotivations(), preferences.getHobbies(),
                                preferences.getThemes(), preferences.getCurrentLocation()));

                // when / act
                ResultActions response = mockMvc.perform(get("/preferences/id/{id}", id)
                                .with(user("douglas@example.com").password("senha123").roles("MASTER")) // Autenticação
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