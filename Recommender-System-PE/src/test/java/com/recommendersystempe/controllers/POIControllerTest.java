package com.recommendersystempe.controllers;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.recommendersystempe.configs.SecurityConfig;
import com.recommendersystempe.dtos.POIDTO;
import com.recommendersystempe.dtos.UserDTO;
import com.recommendersystempe.enums.Hobbies;
import com.recommendersystempe.enums.Motivations;
import com.recommendersystempe.enums.Roles;
import com.recommendersystempe.enums.Themes;
import com.recommendersystempe.models.Address;
import com.recommendersystempe.models.POI;
import com.recommendersystempe.models.User;
import com.recommendersystempe.repositories.POIRepository;
import com.recommendersystempe.repositories.UserRepository;
import com.recommendersystempe.service.AuthenticationService;
import com.recommendersystempe.service.POIService;
import com.recommendersystempe.service.TokenService;
import com.recommendersystempe.service.UserService;

@SuppressWarnings("unused")
@WebMvcTest(POIController.class) // Habilita o contexto do Spring MVC para testes - Enables the Spring MVC context for testing
@Import(SecurityConfig.class) // Importa a configuração real - Imports the real configuration - Importa a configuração real
public class POIControllerTest {

        // MockMvc é uma classe do Spring Test que permite simular requisições HTTP - MockMvc is a Spring Test class that allows you to simulate HTTP requests - 
        @Autowired
        private MockMvc mockMvc;

        // ObjectMapper é uma classe do Jackson que permite converter objetos Java em JSON e vice-versa - ObjectMapper is a Jackson class that allows you to convert Java objects to JSON and vice versa
        @Autowired
        private ObjectMapper objectMapper;

        @MockitoBean // Anotação do Spring Test que cria um mock de um bean, precisa de contexto - Spring Test annotation that creates a mock of a bean, needs context
        private UserService userService;

        @MockitoBean
        private POIService poiService;

        @MockitoBean
        private AuthenticationService authenticationService;

        @MockitoBean
        private TokenService tokenService;

        @MockitoBean // Apenas se o UserRepository for usado indiretamente - Only if UserRepository is used indirectly
        private UserRepository userRepository;

        @MockitoBean
        private POIRepository poiRepository;

        private User user;
        private Address address;
        private POIDTO poiDTO;
        private POI poi;
        private List<Motivations> motivations;
        private List<Hobbies> hobbies;
        private List<Themes> themes;
        private Address poiAddress;

        @BeforeEach
        public void setUp() {
                // given / arrange
                address = new Address(
                                "Rua Exemplo", 100, "Apto 202", "Boa Viagem","Recife",
                                "PE", "Brasil", "50000000");

                user = new User("Douglas", "Fragoso", 30, "Masculino", "12345678900", "81-98765-4321",
                                "douglas@example.com", "senha123", address, Roles.MASTER);
                ReflectionTestUtils.setField(user, "id", 1L); // ID definido via reflection - ID defined via reflection

                // Configurar UserDetailsService mockado - Configure mocked UserDetailsService
                UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                                user.getEmail(),
                                user.getPassword(),
                                Collections.singletonList(new SimpleGrantedAuthority("ROLE_MASTER")));

                // Mockar UserDetailsService - Mock UserDetailsService
                given(authenticationService.loadUserByUsername(user.getEmail())).willReturn(userDetails);
        }

        @Test
        void testGivenPOIDTO_whenSave_ThenReturnPOIDTO() throws JsonProcessingException, Exception {
                // given / arrange
                motivations = List.of(Motivations.CULTURE, Motivations.STUDY);
                hobbies = List.of(Hobbies.PHOTOGRAPHY, Hobbies.MUSIC);
                themes = List.of(Themes.HISTORY, Themes.ADVENTURE);
                poiAddress = new Address("Rua Exemplo", 100, "Apto 202", "Boa Viagem", "Recife","PE", "Brasil", "50000000");

                poiDTO = new POIDTO("Parque da Cidade", "Um grande parque urbano com áreas verdes, trilhas e lagos.",
                                motivations, hobbies, themes, poiAddress);

                given(poiService.insert(any(POIDTO.class)))
                                .willAnswer((invocation) -> invocation.getArgument(0));

                // when / act
                ResultActions response = mockMvc.perform(post("/poi/register")
                                .contentType("application/json")
                                .with(user("douglas@example.com").password("senha123").roles("MASTER")) // Autenticação - Authentication
                                .content(objectMapper.writeValueAsString(poiDTO)));

                // then / assert
                response.andDo(print())
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.motivations").value(poiDTO.getMotivations().stream()
                                                .map(Enum::name).collect(Collectors.toList())))
                                .andExpect(jsonPath("$.hobbies").value(poiDTO.getHobbies().stream()
                                                .map(Enum::name).collect(Collectors.toList())))
                                .andExpect(jsonPath("$.themes").value(poiDTO.getThemes().stream()
                                                .map(Enum::name).collect(Collectors.toList())));
        }

        @Test
        void testListPreferencesObject_whenFindAll_ThenReturnListPreferences()
                        throws JsonProcessingException, Exception {
                // given / arrange
                motivations = List.of(Motivations.CULTURE, Motivations.STUDY);
                hobbies = List.of(Hobbies.PHOTOGRAPHY, Hobbies.MUSIC);
                themes = List.of(Themes.HISTORY, Themes.ADVENTURE);
                poiAddress = new Address("Rua Exemplo", 100, "Apto 202", "Boa Viagem", "Recife","PE", "Brasil", "50000000");

                poi = new POI("Parque da Cidade", "Um grande parque urbano com áreas verdes, trilhas e lagos.",
                                motivations, hobbies, themes, poiAddress);

                ReflectionTestUtils.setField(poi, "id", 1L); // ID definido via reflection - ID defined via reflection

                List<Motivations> motivations1 = List.of(Motivations.CULTURE, Motivations.STUDY);
                List<Hobbies> hobbies1 = List.of(Hobbies.PHOTOGRAPHY, Hobbies.MUSIC);
                List<Themes> themes1 = List.of(Themes.HISTORY, Themes.ADVENTURE);
                Address poiAddress1 = new Address("Rua Exemplo", 100, "Apto 202", "Boa Viagem", "Recife","PE", "Brasil",
                                "50000000");

                POI poi1 = new POI("Parque da Cidade", "Um grande parque urbano com áreas verdes, trilhas e lagos.",
                                motivations1, hobbies1, themes1, poiAddress1);

                ReflectionTestUtils.setField(poi1, "id", 2L); // ID definido via reflection - ID defined via reflection

                Pageable pageable = PageRequest.of(0, 10);

                List<POIDTO> poiDTOList = List.of(
                                new POIDTO(poi.getId(), poi.getName(), poi.getDescription(), poi.getMotivations(), 
                                poi.getHobbies(),poi.getThemes(), poi.getAddress()),
                                new POIDTO(poi1.getId(), poi1.getName(), poi1.getDescription(), poi1.getMotivations(),
                                poi1.getHobbies(), poi1.getThemes(), poi1.getAddress()));

                Page<POIDTO> poiPage = new PageImpl<>(poiDTOList, pageable, 2);

                given(poiService.findAll(any(Pageable.class))).willReturn(poiPage);

                // when / act
                ResultActions response = mockMvc.perform(get("/poi")
                                .param("page", "0")
                                .param("size", "10")
                                .with(user("douglas@example.com").password("senha123").roles("MASTER")) // Autenticação - Authentication
                                .contentType("application/json"));

                // then / assert
                response.andExpect(status().isOk())
                                .andDo(print())
                                .andExpect(jsonPath("$.totalElements").value(poiPage.getTotalElements()));
        }

        @Test
        void testGivenPOIId_whenFindbyId_ThenReturnPOIDTO() throws JsonProcessingException, Exception {
                // given / arrange
                motivations = List.of(Motivations.CULTURE, Motivations.STUDY);
                hobbies = List.of(Hobbies.PHOTOGRAPHY, Hobbies.MUSIC);
                themes = List.of(Themes.HISTORY, Themes.ADVENTURE);
                poiAddress = new Address("Rua Exemplo", 100, "Apto 202", "Boa Viagem", "Recife","PE", "Brasil", "50000000");

                poi = new POI("Parque da Cidade", "Um grande parque urbano com áreas verdes, trilhas e lagos.",
                                motivations, hobbies, themes, poiAddress);

                ReflectionTestUtils.setField(poi, "id", 1L); // ID definido via reflection - ID defined via reflection

                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);

                Long id = 1L;
                given(poiService.findById(id)).willReturn(new POIDTO(poi.getId(), poi.getName(), poi.getDescription(),
                                poi.getMotivations(), poi.getHobbies(), poi.getThemes(), poi.getAddress()));

                // when / act
                ResultActions response = mockMvc.perform(get("/poi/id/{id}", id)
                                .with(user("douglas@example.com").password("senha123").roles("MASTER")) // Autenticação - Authentication
                                .contentType("application/json"));

                // then / assert
                response.andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.motivations").value(poi.getMotivations().stream()
                                                .map(Enum::name).collect(Collectors.toList())))
                                .andExpect(jsonPath("$.hobbies").value(poi.getHobbies().stream()
                                                .map(Enum::name).collect(Collectors.toList())))
                                .andExpect(jsonPath("$.themes").value(poi.getThemes().stream()
                                                .map(Enum::name).collect(Collectors.toList())));
        }

         @Test
        void testGivenPOIId_whenDeleteById_ThenReturnNoContent() throws JsonProcessingException, Exception {
                // given / arrange
                motivations = List.of(Motivations.CULTURE, Motivations.STUDY);
                hobbies = List.of(Hobbies.PHOTOGRAPHY, Hobbies.MUSIC);
                themes = List.of(Themes.HISTORY, Themes.ADVENTURE);
                poiAddress = new Address("Rua Exemplo", 100, "Apto 202", "Boa Viagem","Recife", "PE", "Brasil", "50000000");

                poi = new POI("Parque da Cidade", "Um grande parque urbano com áreas verdes, trilhas e lagos.",
                                motivations, hobbies, themes, poiAddress);

                ReflectionTestUtils.setField(poi, "id", 1L); // ID definido via reflection - ID defined via reflection

                Long id = 1L;
                willDoNothing().given(poiService).deleteById(id);

                // when / act

                ResultActions response = mockMvc.perform(delete("/poi/id/{id}", id)
                                .with(user("douglas@example.com").password("senha123").roles("MASTER")) // Autenticação - Authentication
                                .contentType("application/json"));

                // then / assert
                response.andDo(print())
                                .andExpect(status().isNoContent());
        }

        @Test
        void testGivenPOIDTO_whenUpdate_ThenReturnString() throws JsonProcessingException, Exception {
                // given / arrange
                motivations = List.of(Motivations.CULTURE, Motivations.STUDY);
                hobbies = List.of(Hobbies.PHOTOGRAPHY, Hobbies.MUSIC);
                themes = List.of(Themes.HISTORY, Themes.ADVENTURE);
                poiAddress = new Address("Rua Exemplo", 100, "Apto 202", "Boa Viagem", "Recife","PE", "Brasil", "50000000");

                poi = new POI("Parque da Cidade", "Um grande parque urbano com áreas verdes, trilhas e lagos.",
                                motivations, hobbies, themes, poiAddress);

                ReflectionTestUtils.setField(poi, "id", 1L); // ID definido via reflection - ID defined via reflection

                POIDTO poiDTO = new POIDTO(1L, "Parque da Cidade2",
                "Um grande parque urbano com áreas verdes, trilhas e lagos.");

                willDoNothing().given(poiService).update(any(POIDTO.class));

                // when / act

                ResultActions response = mockMvc.perform(put("/poi")
                                .with(user("douglas@example.com").password("senha123").roles("MASTER")) // Autenticação - Authentication
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(poiDTO))); // Enviando JSON - Sending JSON

                // then / assert
                response.andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(content().string("POI updated successfully"));
        }

}