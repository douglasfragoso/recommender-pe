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

import com.recommendersystempe.configs.SecurityConfig;
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
import com.recommendersystempe.service.AuthenticationService;
import com.recommendersystempe.service.PreferencesService;
import com.recommendersystempe.service.TokenService;
import com.recommendersystempe.service.UserService;

@WebMvcTest(PreferencesController.class)
@Import(SecurityConfig.class)
public class PreferencesControllerTest {

    private static final List<Motivations> MOTIVATIONS = List.of(Motivations.CULTURE, Motivations.EDUCATION);
    private static final List<Hobbies> HOBBIES = List.of(Hobbies.PHOTOGRAPHY, Hobbies.MUSIC);
    private static final List<Themes> THEMES = List.of(Themes.HISTORY, Themes.ADVENTURE);
    private static final Address CURRENT_LOCATION = new Address("Rua Exemplo 2", 44, "Apto 22", "Boa Viagem", "Recife", "PE", "Brasil", "5000000");
    private static final Address ADDRESS = new Address(
            "Rua Exemplo", 100, "Apto 202", "Boa Viagem", "Recife",
            "PE", "Brasil", "50000000");
    private static final User USER = new User(
            "Richard",
            "Fragoso",
            30,
            "Masculino",
            "12345678909",
            "81-98765-4321",
            "richard@example.com",
            "Senha123*",
            ADDRESS,
            Roles.MASTER
    );

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private PreferencesService preferencesService;

    @MockitoBean
    private AuthenticationService authenticationService;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private PreferencesRepository preferencesRepository;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(USER, "id", 1L); 
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                USER.getEmail(),
                USER.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_MASTER"))
        );
        given(authenticationService.loadUserByUsername(USER.getEmail())).willReturn(userDetails);
    }

    private Preferences createPreferences(User user, List<Motivations> motivations, List<Hobbies> hobbies, List<Themes> themes, Address currentLocation) {
        Preferences preferences = new Preferences(user, Instant.now(), motivations, hobbies, themes, currentLocation);
        ReflectionTestUtils.setField(preferences, "id", 1L);
        ReflectionTestUtils.setField(preferences, "user", user);
        return preferences;
    }

    @Test
    void testListPreferencesObject_whenFindAllReturnListPreferences() throws Exception {
        // given / arrange
        Preferences preferences = createPreferences(USER, MOTIVATIONS, HOBBIES, THEMES, CURRENT_LOCATION);
        Preferences preferences1 = createPreferences(USER, MOTIVATIONS, HOBBIES, THEMES, CURRENT_LOCATION);
        ReflectionTestUtils.setField(preferences1, "id", 2L);


        List<PreferencesDTO> preferencesDTOList = List.of(
                new PreferencesDTO(preferences.getId(), preferences.getUser().getId(), preferences.getDate(),
                        preferences.getMotivations(), preferences.getHobbies(), preferences.getThemes(), preferences.getCurrentLocation()),
                new PreferencesDTO(preferences1.getId(), preferences1.getUser().getId(), preferences1.getDate(),
                        preferences1.getMotivations(), preferences1.getHobbies(), preferences1.getThemes(), preferences1.getCurrentLocation())
        );

        Pageable pageable = PageRequest.of(0, 10);
        Page<PreferencesDTO> preferencesPage = new PageImpl<>(preferencesDTOList, pageable, 2);

        given(preferencesService.findAll(any(Pageable.class))).willReturn(preferencesPage);

        // when / act
        ResultActions response = mockMvc.perform(get("/preferences")
                .param("page", "0")
                .param("size", "10")
                .with(user(USER.getEmail()).password(USER.getPassword()).roles("MASTER"))
                .contentType("application/json"));

        // then / assert
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.totalElements").value(preferencesPage.getTotalElements()));
    }

    @Test
    void testGivenPreferencesId_whenFindbyIdReturnPreferencesDTO() throws Exception {
        // given / arrange
        Preferences preferences = createPreferences(USER, MOTIVATIONS, HOBBIES, THEMES, CURRENT_LOCATION);

        PreferencesDTO preferencesDTO = new PreferencesDTO(
                preferences.getId(),
                preferences.getUser().getId(),
                preferences.getDate(),
                preferences.getMotivations(),
                preferences.getHobbies(),
                preferences.getThemes(),
                preferences.getCurrentLocation()
        );

        Long id = 1L;
        given(preferencesService.findById(id)).willReturn(preferencesDTO);

        // when / act
        ResultActions response = mockMvc.perform(get("/preferences/id/{id}", id)
                .with(user(USER.getEmail()).password(USER.getPassword()).roles("MASTER"))
                .contentType("application/json"));

        // then / assert
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.motivations").value(MOTIVATIONS.stream()
                        .map(Enum::name).collect(Collectors.toList())))
                .andExpect(jsonPath("$.hobbies").value(HOBBIES.stream()
                        .map(Enum::name).collect(Collectors.toList())))
                .andExpect(jsonPath("$.themes").value(THEMES.stream()
                        .map(Enum::name).collect(Collectors.toList())));

        verify(preferencesService).findById(id);
    }
}