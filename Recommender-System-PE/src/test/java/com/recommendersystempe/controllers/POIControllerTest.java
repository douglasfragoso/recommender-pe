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

@WebMvcTest(POIController.class)
@Import(SecurityConfig.class)
public class POIControllerTest {

        private static final Address ADDRESS = new Address(
                        "Rua Exemplo", 100, "Apto 202", "Boa Viagem", "Recife",
                        "PE", "Brasil", "50000000");
        private static final User USER = new User(
                        "Douglas",
                        "Fragoso",
                        30,
                        "Masculino",
                        "12345678909",
                        "81-98765-4321",
                        "douglas@example.com",
                        "Senha123*",
                        ADDRESS,
                        Roles.MASTER);
        private static final List<Motivations> MOTIVATIONS = List.of(
                        Motivations.CULTURE, Motivations.STUDY, Motivations.ARTISTIC_VALUE,
                        Motivations.RELAXATION, Motivations.SOCIAL);
        private static final List<Hobbies> HOBBIES = List.of(
                        Hobbies.PHOTOGRAPHY, Hobbies.MUSIC, Hobbies.ADVENTURE,
                        Hobbies.ART, Hobbies.READING);
        private static final List<Themes> THEMES = List.of(
                        Themes.HISTORY, Themes.ADVENTURE, Themes.NATURE,
                        Themes.CULTURAL, Themes.AFRO_BRAZILIAN);

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockitoBean
        private UserService userService;

        @MockitoBean
        private POIService poiService;

        @MockitoBean
        private AuthenticationService authenticationService;

        @MockitoBean
        private TokenService tokenService;

        @MockitoBean
        private UserRepository userRepository;

        @MockitoBean
        private POIRepository poiRepository;

        private POIDTO poiDTO;
        private POI poi;
        private Address poiAddress;

        @BeforeEach
        public void setUp() {
                UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                        USER.getEmail(),
                        USER.getPassword(),
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_MASTER"))
                    );
                    given(authenticationService.loadUserByUsername(USER.getEmail())).willReturn(userDetails);
        }


        @Test
        void testGivenPOIDTO_whenSaveReturnPOIDTO() throws JsonProcessingException, Exception {
                // given / arrange
                poiAddress = new Address("Rua Exemplo", 100, "Apto 202", "Boa Viagem", "Recife", "PE", "Brasil",
                                "50000000");

                poiDTO = new POIDTO("Parque da Cidade", "Um grande parque urbano com áreas verdes, trilhas e lagos.",
                                MOTIVATIONS, HOBBIES, THEMES, poiAddress);

                given(poiService.insert(any(POIDTO.class)))
                                .willAnswer((invocation) -> invocation.getArgument(0));
                //when / act
                ResultActions response = mockMvc.perform(post("/poi/register")
                                .contentType("application/json")
                                .with(user(USER.getEmail()).password(USER.getPassword()).roles("MASTER"))
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

                verify(poiService).insert(any(POIDTO.class));
        }

        @Test
        void testListPOIObject_whenFindAllReturnListPOIDTO() throws JsonProcessingException, Exception {
                // given / arrange
                poiAddress = new Address("Rua Exemplo", 100, "Apto 202", "Boa Viagem", "Recife", "PE", "Brasil",
                                "50000000");

                poi = new POI("Parque da Cidade", "Um grande parque urbano com áreas verdes, trilhas e lagos.",
                                MOTIVATIONS, HOBBIES, THEMES, poiAddress);

                ReflectionTestUtils.setField(poi, "id", 1L);

                List<Motivations> motivations1 = List.of(Motivations.CULTURE, Motivations.STUDY);
                List<Hobbies> hobbies1 = List.of(Hobbies.PHOTOGRAPHY, Hobbies.MUSIC);
                List<Themes> themes1 = List.of(Themes.HISTORY, Themes.ADVENTURE);
                Address poiAddress1 = new Address("Rua Exemplo", 100, "Apto 202", "Boa Viagem", "Recife", "PE",
                                "Brasil", "50000000");

                POI poi1 = new POI("Parque da Cidade", "Um grande parque urbano com áreas verdes, trilhas e lagos.",
                                motivations1, hobbies1, themes1, poiAddress1);

                ReflectionTestUtils.setField(poi1, "id", 2L);

                Pageable pageable = PageRequest.of(0, 10);

                List<POIDTO> poiDTOList = List.of(
                                new POIDTO(poi.getId(), poi.getName(), poi.getDescription(), poi.getMotivations(),
                                                poi.getHobbies(), poi.getThemes(), poi.getAddress()),
                                new POIDTO(poi1.getId(), poi1.getName(), poi1.getDescription(), poi1.getMotivations(),
                                                poi1.getHobbies(), poi1.getThemes(), poi1.getAddress()));

                Page<POIDTO> poiPage = new PageImpl<>(poiDTOList, pageable, 2);

                given(poiService.findAll(any(Pageable.class))).willReturn(poiPage);
                // when / act
                ResultActions response = mockMvc.perform(get("/poi")
                                .param("page", "0")
                                .param("size", "10")
                                .with(user(USER.getEmail()).password(USER.getPassword()).roles("MASTER"))
                                .contentType("application/json"));
                // then / assert
                response.andExpect(status().isOk())
                                .andDo(print())
                                .andExpect(jsonPath("$.totalElements").value(poiPage.getTotalElements()));
        }

        @Test
        void testGivenPOIId_whenFindbyIdReturnPOIDTO() throws JsonProcessingException, Exception {
                // given / arrange
                poiAddress = new Address("Rua Exemplo", 100, "Apto 202", "Boa Viagem", "Recife", "PE", "Brasil",
                                "50000000");

                poi = new POI("Parque da Cidade", "Um grande parque urbano com áreas verdes, trilhas e lagos.",
                                MOTIVATIONS, HOBBIES, THEMES, poiAddress);

                ReflectionTestUtils.setField(poi, "id", 1L);

                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);

                Long id = 1L;
                given(poiService.findById(id)).willReturn(new POIDTO(poi.getId(), poi.getName(), poi.getDescription(),
                                poi.getMotivations(), poi.getHobbies(), poi.getThemes(), poi.getAddress()));
                // when / act
                ResultActions response = mockMvc.perform(get("/poi/id/{id}", id)
                                .with(user(USER.getEmail()).password(USER.getPassword()).roles("MASTER"))
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
                
                verify(poiService).findById(id);
        }

        @Test
        void testGivenPOIId_whenDeleteByIdReturnNoContent() throws JsonProcessingException, Exception {
                // given / arrange
                poiAddress = new Address("Rua Exemplo", 100, "Apto 202", "Boa Viagem", "Recife", "PE", "Brasil",
                                "50000000");

                poi = new POI("Parque da Cidade", "Um grande parque urbano com áreas verdes, trilhas e lagos.",
                                MOTIVATIONS, HOBBIES, THEMES, poiAddress);

                ReflectionTestUtils.setField(poi, "id", 1L);

                Long id = 1L;
                willDoNothing().given(poiService).deleteById(id);
                // when / act
                ResultActions response = mockMvc.perform(delete("/poi/id/{id}", id)
                                .with(user(USER.getEmail()).password(USER.getPassword()).roles("MASTER"))
                                .contentType("application/json"));
                // then / assert
                response.andDo(print())
                                .andExpect(status().isNoContent());

                verify(poiService).deleteById(id);
        }

        @Test
        void testGivenPOIDTO_whenUpdateReturnString() throws JsonProcessingException, Exception {
                // given / arrange
                poiAddress = new Address("Rua Exemplo", 100, "Apto 202", "Boa Viagem", "Recife", "PE", "Brasil",
                                "50000000");

                poi = new POI("Parque da Cidade", "Um grande parque urbano com áreas verdes, trilhas e lagos.",
                                MOTIVATIONS, HOBBIES, THEMES, poiAddress);

                ReflectionTestUtils.setField(poi, "id", 1L);

                POIDTO poiDTO = new POIDTO(1L, "Parque da Cidade2",
                                "Um grande parque urbano com áreas verdes, trilhas e lagos.",
                                MOTIVATIONS, HOBBIES, THEMES, poiAddress);

                willDoNothing().given(poiService).update(any(POIDTO.class));
                // when / act
                ResultActions response = mockMvc.perform(put("/poi")
                                .with(user(USER.getEmail()).password(USER.getPassword()).roles("MASTER"))
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(poiDTO)));
                // then / assert
                response.andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(content().string("POI updated successfully"));
                
                verify(poiService).update(any(POIDTO.class));
        }
}