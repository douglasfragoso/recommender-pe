package com.recommendersystempe.controllers;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recommendersystempe.configs.SecurityConfig;
import com.recommendersystempe.dtos.PreferencesDTO;
import com.recommendersystempe.dtos.RecommendationDTO;
import com.recommendersystempe.dtos.ScoreDTO;
import com.recommendersystempe.enums.Hobbies;
import com.recommendersystempe.enums.Motivations;
import com.recommendersystempe.enums.Roles;
import com.recommendersystempe.enums.Themes;
import com.recommendersystempe.models.Address;
import com.recommendersystempe.models.POI;
import com.recommendersystempe.models.Recommendation;
import com.recommendersystempe.models.User;
import com.recommendersystempe.repositories.POIRepository;
import com.recommendersystempe.repositories.PreferencesRepository;
import com.recommendersystempe.repositories.RecommendationRepository;
import com.recommendersystempe.repositories.ScoreRepository;
import com.recommendersystempe.repositories.UserRepository;
import com.recommendersystempe.service.AuthenticationService;
import com.recommendersystempe.service.PreferencesService;
import com.recommendersystempe.service.RecommendationService;
import com.recommendersystempe.service.TokenService;
import com.recommendersystempe.service.UserService;

@WebMvcTest(RecommendationController.class)
@Import(SecurityConfig.class)
public class RecommendationControllerTest {

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
                        Roles.MASTER);
        private static final List<Motivations> MOTIVATIONS = List.of(
                        Motivations.CULTURE, Motivations.EDUCATION, Motivations.ARTISTIC_VALUE,
                        Motivations.RELAXATION, Motivations.SOCIAL);
        private static final List<Hobbies> HOBBIES = List.of(
                        Hobbies.PHOTOGRAPHY, Hobbies.MUSIC, Hobbies.ADVENTURE,
                        Hobbies.ART, Hobbies.READING);
        private static final List<Themes> THEMES = List.of(
                        Themes.HISTORY, Themes.ADVENTURE, Themes.NATURE,
                        Themes.CULTURAL, Themes.AFRO_BRAZILIAN);
        private static final Address CURRENT_LOCATION = new Address(
                        "Rua Exemplo", 100, "Apto 202", "Boa Viagem", "Recife",
                        "PE", "Brasil", "50000000");

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockitoBean
        private AuthenticationService authenticationService;

        @MockitoBean
        private TokenService tokenService;

        @MockitoBean
        private UserRepository userRepository;

        @MockitoBean
        private RecommendationRepository recommendationRepository;

        @MockitoBean
        private PreferencesRepository preferencesRepository;

        @MockitoBean
        private ScoreRepository scoreRepository;

        @MockitoBean
        private POIRepository poiRepository;

        @MockitoBean
        private RecommendationService recommendationService;

        @MockitoBean
        private UserService userService;

        @MockitoBean
        private PreferencesService preferencesService;

        private List<POI> poiList;

        @BeforeEach
        public void setUp() {
                // given / arrange
                poiList = List.of(
                                createPoi("Parque da Cidade", "Descrição 1"),
                                createPoi("Parque da Cidade 2", "Descrição 2"),
                                createPoi("Parque da Cidade 3", "Descrição 3"),
                                createPoi("Parque da Cidade 4", "Descrição 4"),
                                createPoi("Parque da Cidade 5", "Descrição 5"));

                ReflectionTestUtils.setField(USER, "id", 1L);
                UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                                USER.getEmail(),
                                USER.getPassword(),
                                Collections.singletonList(new SimpleGrantedAuthority("MASTER")));
                given(authenticationService.loadUserByUsername(USER.getEmail())).willReturn(userDetails);
        }

        private POI createPoi(String nome, String descricao) {
                POI poi = new POI(nome, descricao, MOTIVATIONS, HOBBIES, THEMES, CURRENT_LOCATION);
                ReflectionTestUtils.setField(poi, "id", System.nanoTime());
                return poi;
        }

        @Test
        void testGivenValidPreferencesDTO_whenInsertShouldReturnPOIList() throws Exception {
                // given / arrange
                PreferencesDTO preferencesDTO = new PreferencesDTO(
                                MOTIVATIONS, HOBBIES, THEMES, CURRENT_LOCATION);

                RecommendationDTO recommendationDTO = new RecommendationDTO();
                recommendationDTO.addPOI(poiList);

                given(preferencesService.insert(ArgumentMatchers.any(PreferencesDTO.class)))
                                .willReturn(recommendationDTO);

                // when / act
                ResultActions response = mockMvc.perform(post("/recommendation")
                                .contentType("application/json")
                                .with(user(USER.getEmail()).roles("MASTER"))
                                .content(objectMapper.writeValueAsString(preferencesDTO)));

                // then / assert
                response.andDo(print())
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.pois", hasSize(5))) // Note the change to "$.pois"
                                .andExpect(jsonPath("$.pois[0].name").value("Parque da Cidade"))
                                .andExpect(jsonPath("$.pois[4].name").value("Parque da Cidade 5"));

                verify(preferencesService, times(1)).insert(ArgumentMatchers.any());
        }

        @Test
        void testGivenValidScore_whenAddScoresShouldReturnSuccess() throws Exception {
                // given / arrange
                Recommendation recommendation = new Recommendation();
                recommendation.setUser(USER);
                poiList.forEach(recommendation::addPOI);
                ReflectionTestUtils.setField(recommendation, "id", 1L);

                List<ScoreDTO> scoreList = new ArrayList<>();
                int scoreValue = 1;
                for (POI poi : poiList) {
                        Long poiId = (Long) ReflectionTestUtils.getField(poi, "id");
                        scoreList.add(new ScoreDTO(recommendation.getId(), poiId, scoreValue));
                        scoreValue = (scoreValue == 1) ? 0 : 1;
                }

                willDoNothing().given(recommendationService).score(anyLong(), anyList());

                // when / act
                ResultActions response = mockMvc
                                .perform(post("/recommendation/{recommendationId}/score", recommendation.getId())
                                                .contentType("application/json")
                                                .with(user(USER.getEmail()).roles("MASTER"))
                                                .content(objectMapper.writeValueAsString(scoreList)));

                // then / assert
                response.andExpect(status().isCreated())
                                .andExpect(content().string("Scored successfully, thank you!"));

                verify(recommendationService, times(1)).score(recommendation.getId(), scoreList);
        }

        @Test
        void testFindAllRecommendations_shouldReturnPageOfRecommendation() throws Exception {
                // given / arrange
                Page<RecommendationDTO> mockPage = new PageImpl<>(
                                List.of(new RecommendationDTO(1L, List.of())),
                                PageRequest.of(0, 10), 1);

                given(recommendationService.findAll(ArgumentMatchers.any(Pageable.class))).willReturn(mockPage);

                // when / act
                ResultActions response = mockMvc.perform(get("/recommendation")
                                .param("page", "0")
                                .param("size", "10")
                                .with(user(USER.getEmail()).roles("ADMIN")));

                // then / assert
                response.andExpect(status().isOk())
                                .andExpect(jsonPath("$.content", hasSize(1)))
                                .andExpect(jsonPath("$.totalElements").value(1));
        }

        @Test
        void testFindByIdWithValidId_shouldReturnRecommendation() throws Exception {
                // given / arrange
                Long id = 1L;
                RecommendationDTO mockDTO = new RecommendationDTO(id, List.of());

                given(recommendationService.findById(id)).willReturn(mockDTO);

                // when / act
                ResultActions response = mockMvc.perform(get("/recommendation/id/{id}", id)
                                .with(user(USER.getEmail()).roles("MASTER")));

                // then / assert
                response.andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(id));
                verify(recommendationService, times(1)).findById(id);
        }

        @Test
        void testFindAllByUser_shouldReturnUserRecommendations() throws Exception {
                // Arrange
                Page<RecommendationDTO> mockPage = new PageImpl<>(List.of(
                                new RecommendationDTO(1L, List.of())));

                given(recommendationService.findAllByUserId(ArgumentMatchers.any(Pageable.class))).willReturn(mockPage);
                given(userRepository.findByEmail(USER.getEmail())).willReturn(USER);

                // Act
                ResultActions response = mockMvc.perform(get("/recommendation/user")
                                .param("page", "0")
                                .param("size", "10")
                                .with(user(USER.getEmail()).roles("MASTER")));

                // Assert
                response.andExpect(status().isOk())
                                .andExpect(jsonPath("$.content[0].id").value(1L))
                                .andExpect(jsonPath("$.content[0].pois").exists());

        }
}
