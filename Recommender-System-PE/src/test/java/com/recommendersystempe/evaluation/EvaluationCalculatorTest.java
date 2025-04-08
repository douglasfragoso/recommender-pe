package com.recommendersystempe.evaluation;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
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
                        Motivations.CULTURE, Motivations.STUDY, Motivations.APPRECIATION,
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

                Set<POI> relevantItems = scoreRepository.findByUser(user.getId()).stream()
                                .filter(score -> score.getScore() == 1)
                                .map(Score::getPoi)
                                .collect(Collectors.toSet());

                UserEvaluationMetricsDTO dto = EvaluationCalculator.calculateUserMetrics(poiList, relevantItems, 5);

                assertAll(
                                () -> assertEquals(0.4, dto.getPrecisionAtK(), 0.01),
                                () -> assertEquals(1, dto.getRecallAtK(), 0.01),
                                () -> assertEquals(0.5714285714285715, dto.getF1ScoreAtK(), 0.01));
        }

        @Test
        @Transactional
        public void testCalculateGlobalMetrics() {
                User user = userRepository.save(new User(
                                "Mariana", "Silva", 28, "Feminino", "98765432100",
                                "11-99876-5432", "mariana@example.com", "Segura456*",
                                ADDRESS, Roles.USER));

                User user2 = userRepository.save(new User(
                                "Douglas", "Fragoso", 30, "Masculino", "11783576430",
                                "81-98765-4321", "douglas@example.com", "Senha123*",
                                ADDRESS, Roles.USER));

                List<POI> poiList = poiRepository.saveAll(List.of(
                                createPoi("Parque da Cidade", "Descrição 1"),
                                createPoi("Parque da Cidade 2", "Descrição 2"), 
                                createPoi("Parque da Cidade 3", "Descrição 3"), 
                                createPoi("Parque da Cidade 4", "Descrição 4"), 
                                createPoi("Parque da Cidade 5", "Descrição 5") 
                ));

            
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

                int totalItems = (int) poiRepository.count();

                GlobalEvaluationMetricsDTO dto = EvaluationCalculator.calculateGlobalMetrics(
                                allRecommendations,
                                allRelevantItems,
                                totalItems,
                                5);

                assertAll( 
                        
                                () -> assertEquals(0.4, dto.getAveragePrecisionAtK(), 0.01),      
                                () -> assertEquals(1.0, dto.getAverageRecallAtK(), 0.01),          
                                () -> assertEquals(0.5714285714285715, dto.getAverageF1ScoreAtK(), 0.01), 
                                () -> assertEquals(1.0, dto.getHitRateAtK(), 0.01),               
                                () -> assertEquals(1.0, dto.getItemCoverage(), 0.01)               
                        );
        }
}
