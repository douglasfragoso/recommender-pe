package com.recommendersystempe.evaluation;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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
public class HitRateTest {

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
        public void testHitRate() {
                // given / arrange
                User user = userRepository.save(new User(
                                "Mariana", "Silva", 28, "Feminino", "98765432100",
                                "11-99876-5432", "mariana@example.com", "Segura456*",
                                ADDRESS, Roles.USER));

                User user2 = userRepository.save(new User(
                                "Richard", "Fragoso", 30, "Masculino", "11783576430",
                                "81-98765-4328", "richard@example.com", "Senha123*",
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
                                        .map(Score::getPoi)
                                        .collect(Collectors.toSet());
                        allRelevantItems.add(relevantItems);
                }

                int totalItems = (int) poiRepository.count();

                // then / assert
                assertEquals(1, HitRate.hitRateAtK(allRecommendations, allRelevantItems, totalItems), 0.01);
        }

        @Test
        public void testConstructor() {
                new HitRate();
        }

        @Test
        public void testHitRateAtKWithEmptyRecommendations() {
                List<List<POI>> allRecommendations = Collections.emptyList();
                List<Set<POI>> allRelevantItems = List.of(Set.of(new POI()));

                double result = HitRate.hitRateAtK(allRecommendations, allRelevantItems, 3);
                assertEquals(0.0, result, 0.001, "Recomendações vazias devem retornar 0");
        }

        @Test
        public void testHitRateWhenRelevantItemsAreEmpty() {
                POI dummyPoi = new POI("Test", "Desc", List.of(), List.of(), List.of(), new Address());
                List<List<POI>> allRecommendations = List.of(List.of(dummyPoi));
                List<Set<POI>> allRelevantItems = List.of(Collections.emptySet());

                double result = HitRate.hitRateAtK(allRecommendations, allRelevantItems, 1);
                assertEquals(1.0, result, 0.001); 
        }

        @Test
        public void testHitRateRounding() {
                // given / arrange
                POI relevantPoi = new POI("Relevant", "Desc", List.of(), List.of(), List.of(), new Address());
                POI irrelevantPoi = new POI("Irrelevant", "Desc", List.of(), List.of(), List.of(), new Address());

                List<List<POI>> allRecommendations = List.of(
                                List.of(relevantPoi), 
                                List.of(irrelevantPoi), 
                                List.of(irrelevantPoi) 
                );

                List<Set<POI>> allRelevantItems = List.of(
                                Set.of(relevantPoi),
                                Set.of(relevantPoi),
                                Set.of(relevantPoi));
                // when / act
                double result = HitRate.hitRateAtK(allRecommendations, allRelevantItems, 1);

                // then / assert
                assertEquals(0.33, result, 0.001, "Arredondamento para duas casas decimais");
        }

        @Test
        public void testKGreaterThanRecommendationsSize() {
                // given / arrange
                POI relevantPoi = new POI("Relevant", "Desc", List.of(), List.of(), List.of(), new Address());
                List<List<POI>> allRecommendations = List.of(List.of(relevantPoi));
                List<Set<POI>> allRelevantItems = List.of(Set.of(relevantPoi));
                // when / act
                double result = HitRate.hitRateAtK(allRecommendations, allRelevantItems, 10);
                // then / assert
                assertEquals(1.0, result, 0.001, "k maior que recomendações deve considerar todos os itens");
        }
}
