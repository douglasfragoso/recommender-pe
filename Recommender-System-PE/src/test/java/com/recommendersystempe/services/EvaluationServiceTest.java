package com.recommendersystempe.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.*;
import java.util.stream.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.recommendersystempe.dtos.*;
import com.recommendersystempe.enums.*;
import com.recommendersystempe.models.*;
import com.recommendersystempe.repositories.*;
import com.recommendersystempe.service.EvaluationService;
import com.recommendersystempe.service.exception.GeneralException;

@ExtendWith(MockitoExtension.class)
class EvaluationServiceTest {

        private static final Address ADDRESS = new Address(
                        "Rua Exemplo", 100, "Apto 202", "Boa Viagem", "Recife",
                        "PE", "Brasil", "50000000");

        @Mock
        private RecommendationRepository recommendationRepository;
        @Mock
        private POIRepository poiRepository;
        @Mock
        private UserRepository userRepository;
        @Mock
        private ScoreRepository scoreRepository;

        @InjectMocks
        private EvaluationService evaluationService;

        private User user;
        private List<POI> poiList;
        private List<Score> scoreEntities;

        @BeforeEach
        void setUp() {
                user = createUser(1L, "unique.email@test.com");
                poiList = createPOIsWithFeatures(5);
                scoreEntities = createValidScores();
        }

        private User createUser(Long id, String email) {
                User user = new User(
                                "TestUser", "Silva", 30, "Masculino",
                                "12345678900", "81-98765-4321", email,
                                "Password123*", ADDRESS, Roles.USER);
                ReflectionTestUtils.setField(user, "id", id);
                return user;
        }

        private List<POI> createPOIsWithFeatures(int count) {
                List<POI> pois = new ArrayList<>();
                for (long i = 1; i <= count; i++) {
                        POI poi = new POI(
                                        "POI " + i, "Descrição " + i,
                                        List.of(Motivations.CULTURE),
                                        List.of(Hobbies.HIKING),
                                        List.of(Themes.ADVENTURE),
                                        ADDRESS);
                        ReflectionTestUtils.setField(poi, "id", i);
                        pois.add(poi);
                }
                return pois;
        }

        private List<Score> createValidScores() {
                return IntStream.range(0, poiList.size())
                                .mapToObj(i -> {
                                        Score score = new Score();
                                        score.setPoi(poiList.get(i));
                                        score.setScore(i % 2); // Alterna entre 0 e 1
                                        ReflectionTestUtils.setField(score, "id", (long) (i + 1));
                                        return score;
                                })
                                .collect(Collectors.toList());
        }

        @Test
        void testEvaluateUserRecommendations_ShouldReturnUserMetrics() {
                // given / arrange
                given(userRepository.findById(1L)).willReturn(Optional.of(user));
                given(recommendationRepository.findByUser(1L))
                                .willReturn(List.of(createRecommendation(user, poiList)));
                given(scoreRepository.findByUser(1L)).willReturn(scoreEntities);

                // when / act
                UserEvaluationMetricsDTO result = evaluationService.evaluateUserRecommendations(1L, 5);

                // then / assert
                assertAll(
                                () -> assertNotNull(result, "DTO não deve ser nulo"),
                                () -> assertEquals(0.4, result.getPrecisionAtK(), 0.1, "Precisão esperada"));
        }

        private Recommendation createRecommendation(User user, List<POI> pois) {
                Recommendation rec = new Recommendation();
                rec.setUser(user);
                pois.forEach(rec::addPOI);
                return rec;
        }

        // @Test
        // void testEvaluateGlobalMetrics_ShouldReturnGlobalMetrics() {
        //         // given / arrange
        //         int k = 5;
        //         User user2 = createUser(2L, "another.email@test.com");

        //         given(poiRepository.findAll()).willReturn(poiList);
        //         given(poiRepository.count()).willReturn((long) poiList.size());
        //         given(userRepository.findAll()).willReturn(List.of(user, user2));

        //         given(recommendationRepository.findByUser(anyLong()))
        //                         .willReturn(List.of(createRecommendation(user, poiList)));

        //         given(scoreRepository.findByUser(anyLong())).willReturn(scoreEntities);

        //         // when / act
        //         GlobalEvaluationMetricsDTO metrics = evaluationService.evaluateGlobalMetrics(k);

        //         // then / assert
        //         assertNotNull(metrics, "Métricas não devem ser nulas");

        //         assertAll(
        //                         () -> assertEquals(1.0, metrics.getHitRateAtK(), 0.1, "HitRate esperado"),
        //                         () -> assertEquals(1.0, metrics.getItemCoverage(), 0.1, "Cobertura de itens"),
        //                         () -> assertTrue(metrics.getIntraListSimilarity() >= 0, "Similaridade inválida"));

        //         Map<String, Map<String, Double>> featureCoverage = metrics.getGlobalFeatureCoverage();
        //         assertAll(
        //                         () -> assertTrue(featureCoverage.containsKey("hobbies")),
        //                         () -> assertTrue(featureCoverage.get("hobbies").containsKey("HIKING")));
        // }

        @Test
        void testEvaluateUserRecommendations_UserNotFound_ShouldThrowException() {
                given(userRepository.findById(anyLong())).willReturn(Optional.empty());
                assertThrows(GeneralException.class,
                                () -> evaluationService.evaluateUserRecommendations(99L, 5));
        }
}