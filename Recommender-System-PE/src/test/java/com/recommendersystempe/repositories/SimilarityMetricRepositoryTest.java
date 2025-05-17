package com.recommendersystempe.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.recommendersystempe.enums.Hobbies;
import com.recommendersystempe.enums.Motivations;
import com.recommendersystempe.enums.Roles;
import com.recommendersystempe.enums.Themes;
import com.recommendersystempe.models.Address;
import com.recommendersystempe.models.POI;
import com.recommendersystempe.models.Recommendation;
import com.recommendersystempe.models.SimilarityMetric;
import com.recommendersystempe.models.User;

@DataJpaTest
public class SimilarityMetricRepositoryTest {

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

    @Autowired
    private SimilarityMetricRepository similarityMetricRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private POIRepository poiRepository;

    @Autowired
    private RecommendationRepository recommendationRepository;

    private User user;
    private List<POI> poiList;
    private Recommendation recommendation;
    private List<SimilarityMetric> similarityMetrics;

    @BeforeEach
    public void setUp() {
        similarityMetricRepository.deleteAll();
        recommendationRepository.deleteAll();
        userRepository.deleteAll();
        poiRepository.deleteAll();

        user = new User(
                "João",
                "Silva",
                30,
                "Masculino",
                "12345678909",
                "11-98765-4321",
                "joao@example.com",
                "Senha123*",
                ADDRESS,
                Roles.USER);

        poiList = new ArrayList<>(List.of(
                new POI(
                        "Parque da Cidade",
                        "Um grande parque urbano com áreas verdes, trilhas e lagos.",
                        MOTIVATIONS, HOBBIES, THEMES, CURRENT_LOCATION),
                new POI(
                        "Museu de Arte",
                        "Um museu com exposições de arte contemporânea.",
                        MOTIVATIONS, HOBBIES, THEMES, CURRENT_LOCATION),
                new POI(
                        "Teatro Municipal",
                        "Um teatro histórico com apresentações culturais.",
                        MOTIVATIONS, HOBBIES, THEMES, CURRENT_LOCATION)));

        recommendation = new Recommendation();
        recommendation.setUser(user);
        poiList.forEach(recommendation::addPOI);
    }

    @Test
    void testSaveSimilarityMetrics_ShouldReturnSavedMetrics() {
        // when / arrange
        userRepository.save(user);
        poiRepository.saveAll(poiList);
        recommendationRepository.save(recommendation);
        
        similarityMetrics = new ArrayList<>();
        for (POI poi : poiList) {
            SimilarityMetric metric = new SimilarityMetric(
                recommendation, 
                poi, 
                0.8, // cosine
                0.7, // euclidean
                0.6, // pearson
                0.5  // jaccard
            );
            similarityMetrics.add(metric);
        }
        
        // when / act
        List<SimilarityMetric> savedMetrics = similarityMetricRepository.saveAll(similarityMetrics);
        
        // then / assert
        assertAll(
            () -> assertNotNull(savedMetrics, "A lista de métricas salvas não deve ser nula"),
            () -> assertEquals(3, savedMetrics.size(), "A lista deve conter 3 métricas"),
            () -> assertTrue(savedMetrics.stream().allMatch(m -> m.getId() != null), "Todas as métricas devem ter IDs gerados"),
            () -> assertEquals(0.8, savedMetrics.get(0).getCosine(), "O valor de cosine deve ser preservado"),
            () -> assertEquals(0.7, savedMetrics.get(0).getEuclidean(), "O valor de euclidean deve ser preservado"),
            () -> assertEquals(0.6, savedMetrics.get(0).getPearson(), "O valor de pearson deve ser preservado"),
            () -> assertEquals(0.5, savedMetrics.get(0).getJaccard(), "O valor de jaccard deve ser preservado"),
            () -> assertEquals(0.65, savedMetrics.get(0).getAverage(), 0.001, "O valor médio deve ser calculado corretamente")
        );
    }
    
    @Test
    void testFindAllSimilarityMetrics_ShouldReturnAllMetrics() {
        // when / arrange
        userRepository.save(user);
        poiRepository.saveAll(poiList);
        recommendationRepository.save(recommendation);
        
        similarityMetrics = new ArrayList<>();
        for (POI poi : poiList) {
            SimilarityMetric metric = new SimilarityMetric(
                recommendation, 
                poi, 
                0.8, // cosine
                0.7, // euclidean
                0.6, // pearson
                0.5  // jaccard
            );
            similarityMetrics.add(metric);
        }
        similarityMetricRepository.saveAll(similarityMetrics);
        
        // when / act
        List<SimilarityMetric> foundMetrics = similarityMetricRepository.findAll();
        
        // then / assert
        assertAll(
            () -> assertNotNull(foundMetrics, "A lista de métricas encontradas não deve ser nula"),
            () -> assertEquals(3, foundMetrics.size(), "A lista deve conter 3 métricas")
        );
    }
    
    @Test
    void testFindSimilarityMetricById_ShouldReturnMetric() {
        // when / arrange
        userRepository.save(user);
        poiRepository.saveAll(poiList);
        recommendationRepository.save(recommendation);
        
        SimilarityMetric metric = new SimilarityMetric(
            recommendation, 
            poiList.get(0), 
            0.8, // cosine
            0.7, // euclidean
            0.6, // pearson
            0.5  // jaccard
        );
        SimilarityMetric savedMetric = similarityMetricRepository.save(metric);
        
        // when / act
        Optional<SimilarityMetric> foundMetric = similarityMetricRepository.findById(savedMetric.getId());
        
        // then / assert
        assertAll(
            () -> assertTrue(foundMetric.isPresent(), "A métrica deve ser encontrada"),
            () -> assertEquals(savedMetric.getId(), foundMetric.get().getId(), "Os IDs devem corresponder"),
            () -> assertEquals(0.8, foundMetric.get().getCosine(), "O valor de cosine deve ser preservado"),
            () -> assertEquals(0.7, foundMetric.get().getEuclidean(), "O valor de euclidean deve ser preservado"),
            () -> assertEquals(0.6, foundMetric.get().getPearson(), "O valor de pearson deve ser preservado"),
            () -> assertEquals(0.5, foundMetric.get().getJaccard(), "O valor de jaccard deve ser preservado")
        );
    }
    
    @Test
    void testDeleteSimilarityMetric_ShouldRemoveMetric() {
        // when / arrange
        userRepository.save(user);
        poiRepository.saveAll(poiList);
        recommendationRepository.save(recommendation);
        
        SimilarityMetric metric = new SimilarityMetric(
            recommendation, 
            poiList.get(0), 
            0.8, // cosine
            0.7, // euclidean
            0.6, // pearson
            0.5  // jaccard
        );
        SimilarityMetric savedMetric = similarityMetricRepository.save(metric);
        
        // when / act
        similarityMetricRepository.deleteById(savedMetric.getId());
        Optional<SimilarityMetric> foundMetric = similarityMetricRepository.findById(savedMetric.getId());
        
        // then / assert
        assertFalse(foundMetric.isPresent(), "A métrica deve ser removida");
    }
    
    @Test
    void testUpdateSimilarityMetric_ShouldUpdateValues() {
        // when / arrange
        userRepository.save(user);
        poiRepository.saveAll(poiList);
        recommendationRepository.save(recommendation);
        
        SimilarityMetric metric = new SimilarityMetric(
            recommendation, 
            poiList.get(0), 
            0.8, // cosine
            0.7, // euclidean
            0.6, // pearson
            0.5  // jaccard
        );
        SimilarityMetric savedMetric = similarityMetricRepository.save(metric);
        
        // when / act
        savedMetric.setCosine(0.9);
        savedMetric.setEuclidean(0.85);
        savedMetric.setPearson(0.75);
        savedMetric.setJaccard(0.65);
        savedMetric.setAverage((savedMetric.getCosine() + savedMetric.getEuclidean() + savedMetric.getPearson() + savedMetric.getJaccard())/4);
        similarityMetricRepository.save(savedMetric);
        
        Optional<SimilarityMetric> updatedMetric = similarityMetricRepository.findById(savedMetric.getId());
        
        // then / assert
        assertAll(
            () -> assertTrue(updatedMetric.isPresent(), "A métrica atualizada deve ser encontrada"),
            () -> assertEquals(0.9, updatedMetric.get().getCosine(), "O valor de cosine deve ser atualizado"),
            () -> assertEquals(0.85, updatedMetric.get().getEuclidean(), "O valor de euclidean deve ser atualizado"),
            () -> assertEquals(0.75, updatedMetric.get().getPearson(), "O valor de pearson deve ser atualizado"),
            () -> assertEquals(0.65, updatedMetric.get().getJaccard(), "O valor de jaccard deve ser atualizado"),
            () -> assertEquals(0.7875, updatedMetric.get().getAverage(), 0.001, "O valor médio deve ser recalculado")
        );
    }
}
