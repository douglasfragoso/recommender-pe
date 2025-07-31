package com.recommendersystempe.controllers;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.List;

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
import com.recommendersystempe.configs.SecurityConfig;
import com.recommendersystempe.dtos.UserDTO;
import com.recommendersystempe.enums.Roles;
import com.recommendersystempe.models.Address;
import com.recommendersystempe.models.User;
import com.recommendersystempe.repositories.UserRepository;
import com.recommendersystempe.service.AuthenticationService;
import com.recommendersystempe.service.TokenService;
import com.recommendersystempe.service.UserService;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
public class UserControllerTest {

        private static final Address ADDRESS = new Address(
                        "Rua Exemplo", 100, "Apto 202", "Boa Viagem", "Recife",
                        "PE", "Brasil", "50000000");
        private static final User USER = new User(
                        "Richard",
                        "Fragoso",
                        30,
                        "Masculino",
                        "12345678909",
                        "81-98765-4321",
                        "richard@example.com",
                        "Senha123*",
                        ADDRESS,
                        Roles.MASTER);

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockitoBean
        private UserService userService;

        @MockitoBean
        private AuthenticationService authenticationService;

        @MockitoBean
        private TokenService tokenService;

        @MockitoBean
        private UserRepository userRepository;

        @BeforeEach
        public void setUp() {
                ReflectionTestUtils.setField(USER, "id", 1L);
                UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                                USER.getEmail(),
                                USER.getPassword(),
                                Collections.singletonList(new SimpleGrantedAuthority("ROLE_MASTER")));
                given(authenticationService.loadUserByUsername(USER.getEmail())).willReturn(userDetails);
        }

        @Test
        void testGivenUserDTO_whenSaveReturnUserDTO() throws JsonProcessingException, Exception {
                // given / arrange
                UserDTO userDTO = new UserDTO("Richard", "Fragoso", 30, "Masculino", "12345678900", "81-98765-4321",
                                "richard@example.com", "Senha123*", ADDRESS);

                given(userService.insert(any(UserDTO.class))).willAnswer((invocation) -> invocation.getArgument(0));

                // when / act
                ResultActions response = mockMvc.perform(post("/user/register")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(userDTO)));

                // then / assert
                response.andDo(print())
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.firstName").value(userDTO.getFirstName()))
                                .andExpect(jsonPath("$.lastName").value(userDTO.getLastName()))
                                .andExpect(jsonPath("$.address.street").value(userDTO.getAddress().getStreet()))
                                .andExpect(jsonPath("$.address.number").value(userDTO.getAddress().getNumber()))
                                .andExpect(jsonPath("$.gender").value(userDTO.getGender()))
                                .andExpect(jsonPath("$.cpf").value(userDTO.getCpf()))
                                .andExpect(jsonPath("$.email").value(userDTO.getEmail()))
                                .andExpect(jsonPath("$.phone").value(userDTO.getPhone()));
        }

        @Test
        void testListUserObject_whenFindAllReturnListUser() throws JsonProcessingException, Exception {
                // given / arrange
                Address address1 = new Address("Rua Exemplo1", 101, "Apto 203", "Boa Viagem", "Recife", "PE", "Brasil",
                                "50000003");

                UserDTO userDTO1 = new UserDTO("Richard", "Fragoso", 30, "Masculino", "12345678900", "81-98765-4321",
                                "richard@example.com", "Senha123*", ADDRESS);
                UserDTO userDTO2 = new UserDTO("Lucas", "Fragoso", 30, "Masculino", "12345678901", "81-98765-4322",
                                "lucas@example.com", "Senha123*", address1);

                Pageable pageable = PageRequest.of(0, 10);
                List<UserDTO> userDTOList = List.of(
                                new UserDTO(userDTO1.getFirstName(), userDTO1.getLastName(), userDTO1.getAge(),
                                                userDTO1.getGender(),
                                                userDTO1.getCpf(), userDTO1.getPhone(), userDTO1.getEmail(),
                                                userDTO1.getUserPassword(),
                                                userDTO1.getAddress()),
                                new UserDTO(userDTO2.getFirstName(), userDTO2.getLastName(), userDTO2.getAge(),
                                                userDTO2.getGender(), userDTO2.getCpf(), userDTO2.getPhone(),
                                                userDTO2.getEmail(),
                                                userDTO2.getUserPassword(), userDTO2.getAddress()));
                Page<UserDTO> userPage = new PageImpl<>(userDTOList, pageable, 2);

                given(userService.findAll(any(Pageable.class))).willReturn(userPage);

                // when / act
                ResultActions response = mockMvc.perform(get("/user")
                                .param("page", "0")
                                .param("size", "10")
                                .with(user(USER.getEmail()).password(USER.getPassword()).roles("MASTER"))
                                .contentType("application/json"));

                // then / assert
                response.andExpect(status().isOk())
                                .andDo(print())
                                .andExpect(jsonPath("$.totalElements").value(userPage.getTotalElements()));
        }

        @Test
        void testGivenUserId_whenFindbyIdReturnUser() throws JsonProcessingException, Exception {
                // given / arrange
                UserDTO userDTO = new UserDTO("Richard", "Fragoso", 30, "Masculino", "12345678900", "81-98765-4321",
                                "richard@example.com", "Senha123*", ADDRESS);

                Long id = 1L;
                given(userService.findById(id)).willReturn(userDTO);

                // when / act
                ResultActions response = mockMvc.perform(get("/user/id/{id}", id)
                                .with(user(USER.getEmail()).password(USER.getPassword()).roles("MASTER"))
                                .contentType("application/json"));

                // then / assert
                response.andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.firstName").value(userDTO.getFirstName()))
                                .andExpect(jsonPath("$.lastName").value(userDTO.getLastName()))
                                .andExpect(jsonPath("$.address").value(userDTO.getAddress()))
                                .andExpect(jsonPath("$.gender").value(userDTO.getGender()))
                                .andExpect(jsonPath("$.email").value(userDTO.getEmail()));
        }

        @Test
        void testGivenUserId_whenDeleteByIdReturnNoContent() throws JsonProcessingException, Exception {
                // given / arrange
                Long id = 1L;
                willDoNothing().given(userService).deleteById(id);

                // when / act
                ResultActions response = mockMvc.perform(delete("/user/id/{id}", id)
                                .with(user(USER.getEmail()).password(USER.getPassword()).roles("MASTER"))
                                .contentType("application/json"));

                // then / assert
                response.andDo(print())
                                .andExpect(status().isNoContent());
        }

        @Test
        void testGivenUserDTO_whenUpdateReturnString() throws JsonProcessingException, Exception {
                // given / arrange
                UserDTO userDTO = new UserDTO("John", "Doe", 32, "Feminino", "12345678900", "81-98765-4322",
                                "richard@example.com", "Senha123*", ADDRESS);

                willDoNothing().given(userService).update(null, any(UserDTO.class));

                // when / act
                ResultActions response = mockMvc.perform(put("/user")
                                .with(user(USER.getEmail()).password(USER.getPassword()).roles("MASTER"))
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(userDTO)));

                // then / assert
                response.andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(content().string("Profile updated successfully"));
        }

        @Test
        void testGivenUserId_whenUpdateRoleReturnString() throws JsonProcessingException, Exception {
                // given / arrange
                Long id = 2L;
                willDoNothing().given(userService).updateRole(id);

                // when / act
                ResultActions response = mockMvc.perform(put("/user/roles/id/{id}", id)
                                .with(user(USER.getEmail()).password(USER.getPassword()).roles("MASTER"))
                                .contentType("application/json"));

                // then / assert
                response.andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(content().string("Role updated successfully"));
        }
}