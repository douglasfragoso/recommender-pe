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
public class PrecisionTest {

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
    public void testPrecision() {
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

        assertEquals(0.4, Precision.precisionAtK(poiList, relevantItems, 5), 0.01);
    }
}
