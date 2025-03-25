package com.recommendersystempe.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.recommendersystempe.dtos.GlobalEvaluationMetricsDTO;
import com.recommendersystempe.dtos.ScoreDTO;
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
import com.recommendersystempe.service.EvaluationService;
import com.recommendersystempe.service.exception.GeneralException;

@ExtendWith(MockitoExtension.class)
public class EvaluationServiceTest {

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
    private static final Address CURRENT_LOCATION = new Address(
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
    private Recommendation recommendation;
    private List<ScoreDTO> scoreList = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        user = new User(
                "Mariana",
                "Silva",
                28,
                "Feminino",
                "98765432100",
                "11-99876-5432",
                "mariana@example.com",
                "Segura456*",
                ADDRESS,
                Roles.USER);
        ReflectionTestUtils.setField(user, "id", 1L);

        poiList = List.of(
                createPoi("Parque da Cidade", "Descrição 1"),
                createPoi("Parque da Cidade 2", "Descrição 2"),
                createPoi("Parque da Cidade 3", "Descrição 3"),
                createPoi("Parque da Cidade 4", "Descrição 4"),
                createPoi("Parque da Cidade 5", "Descrição 5"));

        recommendation = new Recommendation();
        recommendation.setUser(user);
        poiList.forEach(recommendation::addPOI);
        ReflectionTestUtils.setField(recommendation, "id", 1L);

        int scoreValue = 1;
        for (POI poi : poiList) {
            Long poiId = (Long) ReflectionTestUtils.getField(poi, "id");
            scoreList.add(new ScoreDTO(poiId, scoreValue));
            scoreValue = (scoreValue == 1) ? 0 : 1;
        }
    }

    private POI createPoi(String nome, String descricao) {
        POI poi = new POI(nome, descricao, MOTIVATIONS, HOBBIES, THEMES, CURRENT_LOCATION);
        ReflectionTestUtils.setField(poi, "id", System.nanoTime());
        return poi;
    }

    @Test
    void testEvaluateUserRecommendations_ShouldReturnUserMetrics() {
        // given / arrange
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(recommendationRepository.findByUser(1L))
                .willReturn(Collections.singletonList(recommendation));
        given(scoreRepository.findByUser(1L))
                .willReturn(scoreList.stream()
                        .map(scoreDTO -> {
                            Score score = new Score();
                            // Buscar o POI correspondente na lista de recomendação
                            @SuppressWarnings("null")
                            POI poi = recommendation.getPois().stream()
                                    .filter(p -> ((Long) ReflectionTestUtils.getField(p, "id"))
                                            .equals(scoreDTO.getPoiId()))
                                    .findFirst().orElse(null);
                            score.setPoi(poi);
                            ReflectionTestUtils.setField(score, "id", scoreDTO.getPoiId());
                            return score;
                        })
                        .toList());

        // when / act
        UserEvaluationMetricsDTO result = evaluationService.evaluateUserRecommendations(1L, 1);

        // then / assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertNotNull(result.getF1ScoreAtK()),
                () -> assertNotNull(result.getPrecisionAtK()),
                () -> assertNotNull(result.getRecallAtK()),
                () -> assertEquals(0.2, result.getRecallAtK()),
                () -> assertEquals(1, result.getPrecisionAtK()),
                () -> assertEquals(0.33333333333333337, result.getF1ScoreAtK()));
    }

    @Test
    void testEvaluateUserRecommendations_UserNotFound_ShouldThrowException() {
        // given / arrange
        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        // when / act
        assertThrows(GeneralException.class, () -> evaluationService.evaluateUserRecommendations(99L, 1));
    }

    @SuppressWarnings("null")
    @Test
    void testEvaluateGlobalMetrics_ShouldReturnGlobalMetrics() {
        // given / arrange
        User user2 = new User(
                "Douglas", "Fragoso", 30, "Masculino", "12345678900", "81-98765-4321",
                "douglas@example.com", "senha123", ADDRESS, Roles.USER);
        ReflectionTestUtils.setField(user2, "id", 2L);

        Recommendation recommendation2 = new Recommendation();
        recommendation2.setUser(user2);
        poiList.forEach(recommendation2::addPOI);
        ReflectionTestUtils.setField(recommendation2, "id", 2L);

        List<ScoreDTO> scoreListUser1 = new ArrayList<>();
        List<ScoreDTO> scoreListUser2 = new ArrayList<>();

        int scoreValue = 1;
        for (POI poi : poiList) {
            Long poiId = (Long) ReflectionTestUtils.getField(poi, "id");
            scoreListUser1.add(new ScoreDTO(poiId, scoreValue));
            scoreValue = (scoreValue == 1) ? 0 : 1;
        }

        scoreValue = 0;
        for (POI poi : poiList) {
            Long poiId = (Long) ReflectionTestUtils.getField(poi, "id");
            scoreListUser2.add(new ScoreDTO(poiId, scoreValue));
            scoreValue = (scoreValue == 1) ? 0 : 1;
        }

        given(userRepository.findAll()).willReturn(Arrays.asList(user, user2));

        given(recommendationRepository.findByUser(1L))
                .willReturn(Collections.singletonList(recommendation));
        given(recommendationRepository.findByUser(2L))
                .willReturn(Collections.singletonList(recommendation2));

     
        given(scoreRepository.findByUser(1L))
                .willReturn(scoreListUser1.stream()
                        .map(scoreDTO -> {
                            Score score = new Score();
                            @SuppressWarnings("null")
                            POI poi = recommendation.getPois().stream()
                                    .filter(p -> ((Long) ReflectionTestUtils.getField(p, "id"))
                                            .equals(scoreDTO.getPoiId()))
                                    .findFirst().orElse(null);
                            score.setPoi(poi);
                            ReflectionTestUtils.setField(score, "id", scoreDTO.getPoiId());
                            ReflectionTestUtils.setField(score, "score", scoreDTO.getScoreValue());
                            return score;
                        })
                        .toList());

        given(scoreRepository.findByUser(2L))
                .willReturn(scoreListUser2.stream()
                        .map(scoreDTO -> {
                            Score score = new Score();
                            POI poi = recommendation2.getPois().stream()
                                    .filter(p -> ((Long) ReflectionTestUtils.getField(p, "id"))
                                            .equals(scoreDTO.getPoiId()))
                                    .findFirst().orElse(null);
                            score.setPoi(poi);
                            ReflectionTestUtils.setField(score, "id", scoreDTO.getPoiId());
                            ReflectionTestUtils.setField(score, "score", scoreDTO.getScoreValue());
                            return score;
                        })
                        .toList());

        given(poiRepository.count()).willReturn(10L);

        // when / act
        GlobalEvaluationMetricsDTO globalMetrics = evaluationService.evaluateGlobalMetrics(1);

        // then / assert
        assertNotNull(globalMetrics);
        assertEquals(1, globalMetrics.getHitRateAtK());
        assertEquals(0.5, globalMetrics.getItemCoverage());
    }

}
