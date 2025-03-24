package com.recommendersystempe.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.given;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import com.recommendersystempe.dtos.POIDTO;
import com.recommendersystempe.enums.Hobbies;
import com.recommendersystempe.enums.Motivations;
import com.recommendersystempe.enums.Roles;
import com.recommendersystempe.enums.Themes;
import com.recommendersystempe.models.Address;
import com.recommendersystempe.models.POI;
import com.recommendersystempe.models.Preferences;
import com.recommendersystempe.models.Recommendation;
import com.recommendersystempe.models.User;
import com.recommendersystempe.repositories.POIRepository;
import com.recommendersystempe.repositories.RecommendationRepository;
import com.recommendersystempe.repositories.ScoreRepository;
import com.recommendersystempe.repositories.UserRepository;
import com.recommendersystempe.service.RecommendationService;

@ExtendWith(MockitoExtension.class)
public class RecommendationServiceTest {

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
                createPoi("Parque da Cidade 5", "Descrição 5")
        );

        preferences = new Preferences(user, Instant.now(), MOTIVATIONS, HOBBIES, THEMES, CURRENT_LOCATION);

        // Configura o contexto de segurança para simular um usuário autenticado
        mockAuthenticatedUser(user);
    }

    private void mockAuthenticatedUser(User user) {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn(user.getEmail());
        SecurityContextHolder.getContext().setAuthentication(auth);

        given(userRepository.findByEmail(user.getEmail())).willReturn(user);
    }

    private POI createPoi(String nome, String descricao) {
        POI poi = new POI(nome, descricao, MOTIVATIONS, HOBBIES, THEMES, CURRENT_LOCATION);
        ReflectionTestUtils.setField(poi, "id", System.nanoTime());
        return poi;
    }

    @Test
    void testRecommendation_ShouldReturnTop5Pois() {
        // Arrange
        given(poiRepository.findAll()).willReturn(poiList);

        // Act
        List<POIDTO> result = recommendationService.recommendation(preferences);

        // Assert
        assertNotNull(result);
        assertEquals(5, result.size()); 
        verify(recommendationRepository, times(1)).save(any(Recommendation.class));
    }
}
