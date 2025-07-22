package com.recommendersystempe.controllers;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import com.recommendersystempe.configs.SecurityConfig;
import com.recommendersystempe.dtos.GlobalEvaluationMetricsDTO;
import com.recommendersystempe.dtos.UserEvaluationMetricsDTO;
import com.recommendersystempe.enums.Roles;
import com.recommendersystempe.models.Address;
import com.recommendersystempe.models.User;
import com.recommendersystempe.repositories.POIRepository;
import com.recommendersystempe.repositories.PreferencesRepository;
import com.recommendersystempe.repositories.RecommendationRepository;
import com.recommendersystempe.repositories.ScoreRepository;
import com.recommendersystempe.repositories.UserRepository;
import com.recommendersystempe.service.AuthenticationService;
import com.recommendersystempe.service.EvaluationService;
import com.recommendersystempe.service.PreferencesService;
import com.recommendersystempe.service.RecommendationService;
import com.recommendersystempe.service.TokenService;
import com.recommendersystempe.service.UserService;

@WebMvcTest(EvaluationController.class)
@Import(SecurityConfig.class)
public class EvaluationControllerTest {

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

        @Autowired
        private MockMvc mockMvc;

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

        @MockitoBean
        private EvaluationService evaluationService;

        @BeforeEach
        public void setUp() {
                // given / arrange

                ReflectionTestUtils.setField(USER, "id", 1L);
                UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                                USER.getEmail(),
                                USER.getPassword(),
                                Collections.singletonList(new SimpleGrantedAuthority("MASTER")));
                given(authenticationService.loadUserByUsername(USER.getEmail())).willReturn(userDetails);
        }

        @Test
        void testEvaluateUserRecommendations_ValidRequestShouldReturnMetrics() throws Exception {
                // given / arrange
                Long userId = USER.getId();
                int k = 5;

                UserEvaluationMetricsDTO mockMetrics = new UserEvaluationMetricsDTO(0.2, 0.3, 0.4);
                // when / act
                given(evaluationService.evaluateUserRecommendations(userId, k))
                                .willReturn(mockMetrics);

                // Act & Assert
                mockMvc.perform(get("/evaluation/user/{userId}", userId)
                                .param("k", String.valueOf(k)) // Adicionar parâmetro
                                .with(user(USER.getEmail()).roles("MASTER")))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.precisionAtK").value(0.2));
        }

        @Test
        void testEvaluateGlobalMetrics_ValidRequestShouldReturnMetrics() throws Exception {
                // Arrange
                int k = 5;

                // Mock dos dados necessários
                Map<String, Map<String, Double>> featureCoverage = Map.of(
                                "themes", Map.of("ADVENTURE", 0.8),
                                "hobbies", Map.of("HIKING", 0.7),
                                "motivations", Map.of("CULTURE", 0.9));

                Map<Long, Double> poiFrequency = Map.of(
                                1L, 0.75,
                                2L, 0.65);

                GlobalEvaluationMetricsDTO mockMetrics = new GlobalEvaluationMetricsDTO(
                                0.75, // averagePrecisionAtK
                                0.68, // precisionConfidenceLower
                                0.82, // precisionConfidenceUpper
                                0.85, // hitRateAtK
                                0.90, // itemCoverage
                                0.95, // intraListSimilarity
                                featureCoverage,
                                poiFrequency);

                given(evaluationService.evaluateGlobalMetrics(k))
                                .willReturn(mockMetrics);

                // Act & Assert
                mockMvc.perform(get("/evaluation/global")
                                .param("k", String.valueOf(k))
                                .with(user(USER.getEmail()).roles("MASTER")))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.averagePrecisionAtK").value(0.75))
                                .andExpect(jsonPath("$.hitRateAtK").value(0.85))
                                .andExpect(jsonPath("$.itemCoverage").value(0.9))
                                .andExpect(jsonPath("$.intraListSimilarity").value(0.95))
                                .andExpect(jsonPath("$.globalFeatureCoverage.themes.ADVENTURE").value(0.8))
                                .andExpect(jsonPath("$.poiFrequency.1").value(0.75));
        }

}
