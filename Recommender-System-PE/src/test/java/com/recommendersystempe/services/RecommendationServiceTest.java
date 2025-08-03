package com.recommendersystempe.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import com.recommendersystempe.dtos.RecommendationDTO;
import com.recommendersystempe.dtos.ScoreDTO;
import com.recommendersystempe.enums.Hobbies;
import com.recommendersystempe.enums.Motivations;
import com.recommendersystempe.enums.Roles;
import com.recommendersystempe.enums.Themes;
import com.recommendersystempe.models.Address;
import com.recommendersystempe.models.POI;
import com.recommendersystempe.models.Preferences;
import com.recommendersystempe.models.Recommendation;
import com.recommendersystempe.models.Score;
import com.recommendersystempe.models.User;
import com.recommendersystempe.repositories.POIRepository;
import com.recommendersystempe.repositories.RecommendationRepository;
import com.recommendersystempe.repositories.ScoreRepository;
import com.recommendersystempe.repositories.UserRepository;
import com.recommendersystempe.service.RecommendationService;
import com.recommendersystempe.service.exception.GeneralException;

import jakarta.validation.Validator;

@ExtendWith(MockitoExtension.class)
public class RecommendationServiceTest {

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

        @Mock
        private Validator validator;

        @InjectMocks
        private RecommendationService recommendationService;

        private User user;
        private List<POI> poiList;
        private Preferences preferences;

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

                preferences = new Preferences(user, Instant.now(), MOTIVATIONS, HOBBIES, THEMES, CURRENT_LOCATION);
        }

        private void mockAuthenticatedUser(User user) {
                Authentication auth = mock(Authentication.class);
                when(auth.getName()).thenReturn(user.getEmail());
                SecurityContextHolder.getContext().setAuthentication(auth);
                // Marcar o stub como lenient para evitar UnnecessaryStubbingException
                lenient().when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        }

        private POI createPoi(String nome, String descricao) {
                POI poi = new POI(nome, descricao, MOTIVATIONS, HOBBIES, THEMES, CURRENT_LOCATION);
                ReflectionTestUtils.setField(poi, "id", System.nanoTime());
                return poi;
        }

        @Test
        void testRecommendation_ShouldReturnTop5Pois() {
                // given / arrange 
                mockAuthenticatedUser(user);
                given(poiRepository.findAll()).willReturn(poiList);

                // when / act
                RecommendationDTO result = recommendationService.recommendation(preferences);

                // then / assert
                assertNotNull(result);
                verify(recommendationRepository, times(1)).save(any(Recommendation.class));
                verify(recommendationRepository)
                                .save(argThat(recommendation -> recommendation.getSimilarityMetrics() != null &&
                                                recommendation.getSimilarityMetrics().size() == 5));
        }

        @SuppressWarnings("null")
        @Test
        void testScore_ShouldSaveScores() {
                // given / arrange
                mockAuthenticatedUser(user);
                for (POI poi : poiList) {
                        Long poiId = (Long) ReflectionTestUtils.getField(poi, "id");
                        given(poiRepository.findById(poiId)).willReturn(Optional.of(poi));
                }

                Recommendation recommendation = new Recommendation();
                recommendation.setUser(user);
                poiList.forEach(recommendation::addPOI);
                ReflectionTestUtils.setField(recommendation, "id", 1L);
                given(recommendationRepository.findById(1L)).willReturn(Optional.of(recommendation));

                List<ScoreDTO> scoreList = new ArrayList<>();
                int scoreValue = 1;
                for (POI poi : poiList) {
                        Long poiId = (Long) ReflectionTestUtils.getField(poi, "id");
                        scoreList.add(new ScoreDTO(poiId, scoreValue));
                        scoreValue = (scoreValue == 1) ? 0 : 1;
                }

                // when / act
                recommendationService.score(1L, scoreList);

                // then / assert
                verify(scoreRepository, times(5)).save(any(Score.class));
                assertEquals(5, recommendation.getScores().size());
        }

        @Test
        void testFindAll_ShouldReturnPageOfRecommendations() {
                // given / arrange
                Pageable pageable = PageRequest.of(0, 10);
                Recommendation recommendation = new Recommendation();
                recommendation.setUser(user);
                Page<Recommendation> recommendationPage = new PageImpl<>(List.of(recommendation));

                given(recommendationRepository.findAll(pageable)).willReturn(recommendationPage);

                // when / act
                Page<RecommendationDTO> result = recommendationService.findAll(pageable);

                // then / assert
                assertNotNull(result);
                assertEquals(1, result.getTotalElements());
        }

        @Test
        void testFindById_ShouldReturnRecommendationDTO() {
                // given / arrange
                Recommendation recommendation = new Recommendation();
                recommendation.setUser(user);
                poiList.forEach(recommendation::addPOI);

                given(recommendationRepository.findById(anyLong())).willReturn(Optional.of(recommendation));

                // when / act
                RecommendationDTO result = recommendationService.findById(1L);

                // then / assert
                assertNotNull(result);
                assertEquals(5, result.getPois().size());
        }

        @Test
        void testFindAllByUserId_ShouldReturnPageOfRecommendations() {
                // given / arrange
                mockAuthenticatedUser(user);
                Pageable pageable = PageRequest.of(0, 10);
                Recommendation recommendation = new Recommendation();
                recommendation.setUser(user);
                Page<Recommendation> recommendationPage = new PageImpl<>(List.of(recommendation));

                given(recommendationRepository.findAllByUserId(eq(user.getId()), eq(pageable)))
                                .willReturn(recommendationPage);

                // when / act
                Page<RecommendationDTO> result = recommendationService.findAllByUserId(pageable);

                // then / assert
                assertNotNull(result);
                assertEquals(1, result.getTotalElements());
        }

        @Test
        void testFindById_WhenRecommendationNotFound_ShouldThrowException() {
                // given / arrange
                given(recommendationRepository.findById(anyLong())).willReturn(Optional.empty());

                // when / act / then / assert
                assertThrows(GeneralException.class, () -> recommendationService.findById(1L));
        }

        @Test
        void testScore_WhenUserNotAuthenticated_ShouldThrowException() {
                // given / arrange
                SecurityContextHolder.clearContext();

                given(recommendationRepository.findById(anyLong())).willReturn(Optional.of(new Recommendation()));

                // when / act / then / assert
                assertThrows(NullPointerException.class,
                                () -> recommendationService.score(1L, List.of(new ScoreDTO(1L, 1))));
        }
}
