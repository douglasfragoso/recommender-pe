package com.recommendersystempe.evaluation;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.recommendersystempe.dtos.GlobalEvaluationMetricsDTO;
import com.recommendersystempe.dtos.UserEvaluationMetricsDTO;
import com.recommendersystempe.enums.Hobbies;
import com.recommendersystempe.enums.Motivations;
import com.recommendersystempe.enums.Roles;
import com.recommendersystempe.enums.Themes;
import com.recommendersystempe.models.Address;
import com.recommendersystempe.models.POI;
import com.recommendersystempe.models.Recommendation;
import com.recommendersystempe.models.Score;
import com.recommendersystempe.models.User;
import com.recommendersystempe.repositories.POIRepository;
import com.recommendersystempe.repositories.RecommendationRepository;
import com.recommendersystempe.repositories.ScoreRepository;
import com.recommendersystempe.repositories.UserRepository;

@SpringBootTest
public class EvaluationCalculatorTest {

        @Autowired
        private ScoreRepository scoreRepository;

        @Autowired
        private POIRepository poiRepository;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private RecommendationRepository recommendationRepository;

        private static final Address ADDRESS = new Address(
                        "Rua Exemplo", 100, "Apto 202", "Boa Viagem", "Recife",
                        "PE", "Brasil", "50000000");

        private static final List<Motivations> MOTIVATIONS = List.of(
                        Motivations.CULTURE, Motivations.EDUCATION, Motivations.ARTISTIC_VALUE,
                        Motivations.RELAXATION, Motivations.SOCIAL);

        private static final List<Hobbies> HOBBIES = List.of(
                        Hobbies.PHOTOGRAPHY, Hobbies.MUSIC, Hobbies.ADVENTURE,
                        Hobbies.ART, Hobbies.READING);

        private static final List<Themes> THEMES = List.of(
                        Themes.HISTORY, Themes.ADVENTURE, Themes.NATURE,
                        Themes.CULTURAL, Themes.AFRO_BRAZILIAN);

        private static final Address CURRENT_LOCATION = ADDRESS;

        private POI createPoi(String nome, String descricao) {
                POI poi = new POI(nome, descricao, MOTIVATIONS, HOBBIES, THEMES, CURRENT_LOCATION);
                return poi;
        }

        @Test
        @Transactional
        public void testCalculateUserMetrics() {
                // given / arrange
                User user = new User(
                                "Mariana", "Silva", 28, "Feminino", "98765432100",
                                "11-99876-5432", "mariana@example.com", "Segura456*",
                                ADDRESS, Roles.USER);
                user = userRepository.save(user);

                List<POI> poiList = List.of(
                                createPoi("Parque da Cidade", "Descrição 1"),
                                createPoi("Parque da Cidade 2", "Descrição 2"),
                                createPoi("Parque da Cidade 3", "Descrição 3"),
                                createPoi("Parque da Cidade 4", "Descrição 4"),
                                createPoi("Parque da Cidade 5", "Descrição 5"));
                poiList = poiRepository.saveAll(poiList);

                Recommendation recommendation = new Recommendation();
                recommendation.setUser(user);
                recommendation.addPOI(poiList.get(0));
                recommendation.addPOI(poiList.get(1));
                recommendation.addPOI(poiList.get(2));
                recommendation.addPOI(poiList.get(3));
                recommendation.addPOI(poiList.get(4));
                recommendation = recommendationRepository.save(recommendation);

                List<Score> scores = new ArrayList<>(List.of(
                                new Score(poiList.get(0), 1, recommendation),
                                new Score(poiList.get(1), 0, recommendation),
                                new Score(poiList.get(2), 0, recommendation),
                                new Score(poiList.get(3), 1, recommendation),
                                new Score(poiList.get(4), 0, recommendation)));
                scoreRepository.saveAll(scores);

                // when / act
                Set<POI> relevantItems = scoreRepository.findByUser(user.getId()).stream()
                                .filter(score -> score.getScore() == 1)
                                .map(Score::getPoi)
                                .collect(Collectors.toSet());

                UserEvaluationMetricsDTO dto = EvaluationCalculator.calculateUserMetrics(poiList, relevantItems, 5);

                // then / assert
                assertEquals(0.4, dto.getPrecisionAtK(), 0.01);
        }

        @Test
        @Transactional
        public void testCalculateGlobalMetrics() {
                // given / arrange
                User user = userRepository.save(new User(
                                "Mariana", "Silva", 28, "Feminino", "98765432100",
                                "11-99876-5432", "mariana@example.com", "Segura456*",
                                ADDRESS, Roles.USER));

                User user2 = userRepository.save(new User(
                                "Douglas", "Fragoso", 30, "Masculino", "11783576430",
                                "81-98765-4325", "douglas2@example.com", "Senha123*",
                                ADDRESS, Roles.USER));

                List<POI> poiList = poiRepository.saveAll(List.of(
                                createPoi("Parque da Cidade", "Descrição 1"),
                                createPoi("Parque da Cidade 2", "Descrição 2"),
                                createPoi("Parque da Cidade 3", "Descrição 3"),
                                createPoi("Parque da Cidade 4", "Descrição 4"),
                                createPoi("Parque da Cidade 5", "Descrição 5")));

                Recommendation recommendation = new Recommendation();
                recommendation.setUser(user);
                recommendation.addPOI(poiList.get(0));
                recommendation.addPOI(poiList.get(1));
                recommendation.addPOI(poiList.get(2));
                recommendation.addPOI(poiList.get(3));
                recommendation.addPOI(poiList.get(4));
                recommendation = recommendationRepository.save(recommendation);

                Recommendation recommendation2 = new Recommendation();
                recommendation2.setUser(user2);
                recommendation2.addPOI(poiList.get(0));
                recommendation2.addPOI(poiList.get(1));
                recommendation2.addPOI(poiList.get(2));
                recommendation2.addPOI(poiList.get(3));
                recommendation2.addPOI(poiList.get(4));
                recommendation2 = recommendationRepository.save(recommendation2);

                // Scores (POIs relevantes: 0 e 3)
                List<Score> scoresUser1 = List.of(
                                new Score(poiList.get(0), 1, recommendation),
                                new Score(poiList.get(1), 0, recommendation),
                                new Score(poiList.get(2), 0, recommendation),
                                new Score(poiList.get(3), 1, recommendation),
                                new Score(poiList.get(4), 0, recommendation));

                List<Score> scoresUser2 = List.of(
                                new Score(poiList.get(0), 1, recommendation2),
                                new Score(poiList.get(1), 0, recommendation2),
                                new Score(poiList.get(2), 0, recommendation2),
                                new Score(poiList.get(3), 1, recommendation2),
                                new Score(poiList.get(4), 0, recommendation2));

                scoreRepository.saveAll(scoresUser1);
                scoreRepository.saveAll(scoresUser2);

                List<User> users = List.of(user, user2);
                List<List<POI>> allRecommendations = new ArrayList<>();
                List<Set<POI>> allRelevantItems = new ArrayList<>();

                // when / act
                for (User currentUser : users) {
                        List<Recommendation> recommendations = recommendationRepository.findByUser(currentUser.getId());
                        List<POI> recommendedPois = recommendations.stream()
                                        .flatMap(r -> r.getPois().stream())
                                        .collect(Collectors.toList());
                        allRecommendations.add(recommendedPois);

                        Set<POI> relevantItems = scoreRepository.findByUser(currentUser.getId()).stream()
                                        .filter(score -> score.getScore() == 1)
                                        .map(Score::getPoi)
                                        .collect(Collectors.toSet());
                        allRelevantItems.add(relevantItems);
                }

                int totalItems = poiList.size();
                List<POI> allPois = poiList; // Usa a lista local de POIs
                List<String> allSystemFeatures = IntraListSimilarity.getAllFeatures(); // Coleta todas as features

                GlobalEvaluationMetricsDTO dto = EvaluationCalculator.calculateGlobalMetrics(
                                allRecommendations,
                                allRelevantItems,
                                totalItems,
                                5,
                                allSystemFeatures,
                                allPois);

                // then / assert
                assertAll(
                                () -> assertEquals(0.4, dto.getAveragePrecisionAtK(), 0.01),
                                () -> assertEquals(0.4, dto.getPrecisionConfidenceLower(), 0.01),
                                () -> assertEquals(0.4, dto.getPrecisionConfidenceUpper(), 0.01),
                                () -> assertEquals(1.0, dto.getHitRateAtK(), 0.01),
                                () -> assertEquals(1.0, dto.getItemCoverage(), 0.01),
                                () -> assertEquals(1.0, dto.getIntraListSimilarity(), 0.01,
                                                "Similaridade deve ser 1.0 com features idênticas"),
                                () -> assertFalse(dto.getGlobalFeatureCoverage().isEmpty(),
                                                "Feature coverage não deve estar vazio"),
                                () -> assertEquals(5, dto.getPoiFrequency().size(),
                                                "Deve haver 5 POIs no mapa de frequência"));

        }

        @Test
        void testCalculateAverageWithEmptyList() throws Exception {
                Method method = EvaluationCalculator.class.getDeclaredMethod("calculateAverage", List.class);
                method.setAccessible(true);
                double result = (double) method.invoke(null, Collections.emptyList());
                assertEquals(0.0, result, 0.001);
        }

        @Test
        void testCalculateAverageWithNaN() throws Exception {
                Method method = EvaluationCalculator.class.getDeclaredMethod("calculateAverage", List.class);
                method.setAccessible(true);
                List<Double> values = Arrays.asList(Double.NaN, 2.0, Double.NaN, 4.0);
                double result = (double) method.invoke(null, values);
                assertEquals(3.0, result, 0.01);
        }

        @Test
        void testCalculateStandardDeviationWithSizeOne() throws Exception {
                Method method = EvaluationCalculator.class.getDeclaredMethod("calculateStandardDeviation", List.class,
                                double.class);
                method.setAccessible(true);
                List<Double> values = List.of(2.0);
                double result = (double) method.invoke(null, values, 2.0);
                assertEquals(0.0, result, 0.001);
        }

        @Test
        void testComputeMarginOfErrorWithSmallSample() throws Exception {
                Method method = EvaluationCalculator.class.getDeclaredMethod("computeMarginOfError", double.class,
                                int.class, double.class);
                method.setAccessible(true);
                double result = (double) method.invoke(null, 1.0, 1, 0.95);
                assertEquals(0.0, result, 0.001);
        }

        @Test
        void testComputeMarginOfErrorWithInvalidConfidence() throws Exception {
                Method method = EvaluationCalculator.class.getDeclaredMethod("computeMarginOfError", double.class,
                                int.class, double.class);
                method.setAccessible(true);
                double result = (double) method.invoke(null, 1.0, 10, 1.1);
                assertEquals(0.0, result, 0.001);
        }

        @Test
        @Transactional
        void testCalculateGlobalMetricsWithEmptyInput() {
                GlobalEvaluationMetricsDTO dto = EvaluationCalculator.calculateGlobalMetrics(
                                new ArrayList<>(),
                                new ArrayList<>(),
                                0,
                                5,
                                new ArrayList<>(),
                                new ArrayList<>());

                assertAll(
                                () -> assertEquals(0.0, dto.getAveragePrecisionAtK(), 0.01),
                                () -> assertEquals(0.0, dto.getPrecisionConfidenceLower(), 0.01),
                                () -> assertEquals(0.0, dto.getPrecisionConfidenceUpper(), 0.01),
                                () -> assertEquals(0.0, dto.getHitRateAtK(), 0.01),
                                () -> assertEquals(0.0, dto.getItemCoverage(), 0.01),
                                () -> assertEquals(0.0, dto.getIntraListSimilarity(), 0.01));
        }

        @Test
        @Transactional
        void testCalculateGlobalMetricsWithSingleUser() {
                // given / arrange
                User user = userRepository.save(new User(
                                "Single", "User", 25, "Feminino", "98765432100", // CPF válido
                                "11-91234-5678", "single@example.com", "Password123*",
                                ADDRESS, Roles.USER));

                List<POI> poiList = poiRepository.saveAll(List.of(
                                createPoi("POI 1", "Desc 1"),
                                createPoi("POI 2", "Desc 2")));

                Recommendation recommendation = new Recommendation();
                recommendation.setUser(user);
                recommendation.addPOI(poiList.get(0));
                recommendation.addPOI(poiList.get(1));
                recommendation = recommendationRepository.save(recommendation);

                List<Score> scores = List.of(
                                new Score(poiList.get(0), 1, recommendation),
                                new Score(poiList.get(1), 0, recommendation));
                scoreRepository.saveAll(scores);
                // when / act
                Set<POI> relevantItems = scoreRepository.findByUser(user.getId()).stream()
                                .filter(score -> score.getScore() == 1)
                                .map(Score::getPoi)
                                .collect(Collectors.toSet());

                GlobalEvaluationMetricsDTO dto = EvaluationCalculator.calculateGlobalMetrics(
                                List.of(poiList),
                                List.of(relevantItems),
                                poiList.size(),
                                2,
                                IntraListSimilarity.getAllFeatures(),
                                poiList);
                // then / assert
                assertAll(
                                () -> assertEquals(0.5, dto.getAveragePrecisionAtK(), 0.01),
                                () -> assertEquals(0.5, dto.getPrecisionConfidenceLower(), 0.01),
                                () -> assertEquals(0.5, dto.getPrecisionConfidenceUpper(), 0.01),
                                () -> assertEquals(1.0, dto.getItemCoverage(), 0.01));
        }

}
