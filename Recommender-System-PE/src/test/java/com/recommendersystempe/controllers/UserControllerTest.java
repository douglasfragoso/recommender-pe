package com.recommendersystempe.controllers;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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

@SuppressWarnings("unused")
@WebMvcTest(UserController.class) // Habilita o contexto do Spring MVC para testes
@Import(SecurityConfig.class) // Importa a configuração real
public class UserControllerTest {

        // MockMvc é uma classe do Spring Test que permite simular requisições HTTP
        @Autowired
        private MockMvc mockMvc;

        // ObjectMapper é uma classe do Jackson que permite converter objetos Java em
        // JSON e vice-versa
        @Autowired
        private ObjectMapper objectMapper;

        @MockitoBean // anotação do Spring Test que cria um mock de um bean, precisa de contexto
        private UserService userService;

        @MockitoBean // anotação do Spring Test que cria um mock de um bean, precisa de contexto
        private AuthenticationService authenticationService;

        @MockitoBean
        private TokenService tokenService;

        @MockitoBean // Apenas se o UserRepository for usado indiretamente
        private UserRepository userRepository;

        private UserDTO userDTO;
        private Address address;

        @BeforeEach
        public void setUp() {
                // given / arrange
                address = new Address(
                                "Rua Exemplo", 100, "Apto 202", "Boa Viagem",
                                "PE", "Brasil", "50000000");

                userDTO = new UserDTO("Douglas", "Fragoso", 30, "Masculino", "12345678900", "81-98765-4321",
                                "douglas@example.com",
                                "senha123", address);
        }

        @Test
        void testGivenUserDTO_whenSave_ThenReturnUserDTO() throws JsonProcessingException, Exception {
                // given / arrange
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
        void testListUserObject_whenFindAll_ThenReturnListUser() throws JsonProcessingException, Exception {
                // given / arrange
                Address address = new Address(
                                "Rua Exemplo", 100, "Apto 202", "Boa Viagem",
                                "PE", "Brasil", "50000000");

                User user = new User("Douglas", "Fragoso", 30, "Masculino", "12345678900", "81-98765-4321",
                                "douglas@example.com", "senha123", address, Roles.MASTER);
                ReflectionTestUtils.setField(user, "id", 1L); // ID definido via reflection

                Address address1 = new Address(
                                "Rua Exemplo1", 101, "Apto 203", "Boa Viagem",
                                "PE", "Brasil", "50000003");

                User user1 = new User("Lucas", "Fragoso", 30, "Masculino", "12345678901", "81-98765-4322",
                                "lucas@example.com",
                                "senha123", address1, Roles.USER);
                ReflectionTestUtils.setField(user, "id", 2L); // ID definido via reflection

                Pageable pageable = PageRequest.of(0, 10);

                // Configurar UserDetailsService mockado
                UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                                user.getEmail(),
                                user.getPassword(),
                                Collections.singletonList(new SimpleGrantedAuthority("ROLE_MASTER")) // 👈 Prefixo
                                                                                                     // obrigatório
                );

                // Mockar UserDetailsService
                given(authenticationService.loadUserByUsername(user.getEmail())).willReturn(userDetails);

                List<UserDTO> userDTOList = List.of(
                                new UserDTO(user.getFirstName(), user.getLastName(), user.getAge(), user.getGender(),
                                                user.getCpf(), user.getPhone(), user.getEmail(), user.getPassword(),
                                                user.getAddress()),
                                new UserDTO(user1.getFirstName(), user1.getLastName(), user1.getAge(),
                                                user1.getGender(), user1.getCpf(), user1.getPhone(), user1.getEmail(),
                                                user1.getPassword(), user1.getAddress()));

                Page<UserDTO> userPage = new PageImpl<>(userDTOList, pageable, 2);

                given(userService.findAll(any(Pageable.class))).willReturn(userPage);

                // when / act
                ResultActions response = mockMvc.perform(get("/user")
                                .param("page", "0")
                                .param("size", "10")
                                .with(user("douglas@example.com").password("senha123").roles("MASTER")) // Autenticação
                                .contentType("application/json"));

                // then / assert
                response.andExpect(status().isOk())
                                .andDo(print())
                                .andExpect(jsonPath("$.totalElements").value(userPage.getTotalElements()));
        }

        @Test
        void testGivenUserId_whenFindbyId_ThenReturnUser() throws JsonProcessingException, Exception {
                // given / arrange
                Address address = new Address(
                                "Rua Exemplo", 100, "Apto 202", "Boa Viagem",
                                "PE", "Brasil", "50000000");

                User user = new User("Douglas", "Fragoso", 30, "Masculino", "12345678900", "81-98765-4321",
                                "douglas@example.com", "senha123", address, Roles.MASTER);
                ReflectionTestUtils.setField(user, "id", 1L); // ID definido via reflection

                // Configurar UserDetailsService mockado
                UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                                user.getEmail(),
                                user.getPassword(),
                                Collections.singletonList(new SimpleGrantedAuthority("ROLE_MASTER")) // 👈 Prefixo
                                                                                                     // obrigatório
                );
                // Mockar UserDetailsService
                given(authenticationService.loadUserByUsername(user.getEmail())).willReturn(userDetails);
                Long id = 1L;
                given(userService.findById(id)).willReturn(new UserDTO(user.getId(), user.getFirstName(),
                                user.getLastName(), user.getAge(), user.getGender(), user.getCpf(), user.getPhone(),
                                user.getEmail(), user.getAddress(), user.getRole()));

                // when / act
                ResultActions response = mockMvc.perform(get("/user/id/{id}", id)
                                .with(user("douglas@example.com").password("senha123").roles("MASTER")) // Autenticação
                                .contentType("application/json"));

                // then / assert
                response.andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
                                .andExpect(jsonPath("$.lastName").value(user.getLastName()))
                                .andExpect(jsonPath("$.address").value(user.getAddress()))
                                .andExpect(jsonPath("$.gender").value(user.getGender()))
                                .andExpect(jsonPath("$.email").value(user.getEmail()));
        }

        @Test
        void testGivenUserId_whenDeleteById_ThenReturnNoContent() throws JsonProcessingException, Exception {
                // given / arrange
                Address address = new Address(
                                "Rua Exemplo", 100, "Apto 202", "Boa Viagem",
                                "PE", "Brasil", "50000000");

                User user = new User("Douglas", "Fragoso", 30, "Masculino", "12345678900", "81-98765-4321",
                                "douglas@example.com", "senha123", address, Roles.MASTER);
                ReflectionTestUtils.setField(user, "id", 1L); // ID definido via reflection

                Address address1 = new Address(
                                "Rua Exemplo1", 101, "Apto 203", "Boa Viagem",
                                "PE", "Brasil", "50000003");

                User user1 = new User("Lucas", "Fragoso", 30, "Masculino", "12345678901", "81-98765-4322",
                                "lucas@example.com", "senha123", address1, Roles.USER);

                ReflectionTestUtils.setField(user, "id", 2L); // ID definido via reflection

                // Configurar UserDetailsService mockado
                UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                                user.getEmail(),
                                user.getPassword(),
                                Collections.singletonList(new SimpleGrantedAuthority("ROLE_MASTER")) // 👈 Prefixo
                                                                                                     // obrigatório
                );
                // Mockar UserDetailsService
                given(authenticationService.loadUserByUsername(user.getEmail())).willReturn(userDetails);

                Long id = 2L;
                willDoNothing().given(userService).deleteById(id);

                // when / act

                ResultActions response = mockMvc.perform(delete("/user/id/{id}", id)
                                .with(user("douglas@example.com").password("senha123").roles("MASTER")) // Autenticação
                                .contentType("application/json"));

                // then / assert
                response.andDo(print())
                                .andExpect(status().isNoContent());
        }

        @Test
        void testGivenUserDTO_whenUpdate_ThenReturnString() throws JsonProcessingException, Exception {
                // given / arrange
                Address address = new Address(
                                "Rua Exemplo", 100, "Apto 202", "Boa Viagem",
                                "PE", "Brasil", "50000000");

                User user = new User("Douglas", "Fragoso", 30, "Masculino", "12345678900", "81-98765-4321",
                                "douglas@example.com", "senha123", address, Roles.MASTER);
                ReflectionTestUtils.setField(user, "id", 1L); // ID definido via reflection

                Address address1 = new Address(
                                "Rua Exemplo1", 101, "Apto 203", "Boa Viagem",
                                "PE", "Brasil", "50000003");

                User user1 = new User("Lucas", "Fragoso", 30, "Masculino", "12345678901", "81-98765-4322",
                                "lucas@example.com", "senha123", address1, Roles.USER);

                ReflectionTestUtils.setField(user, "id", 2L); // ID definido via reflection

                // Configurar UserDetailsService mockado
                UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                                user.getEmail(),
                                user.getPassword(),
                                Collections.singletonList(new SimpleGrantedAuthority("ROLE_MASTER")) // 👈 Prefixo
                                                                                                     // obrigatório
                );
                // Mockar UserDetailsService
                given(authenticationService.loadUserByUsername(user.getEmail())).willReturn(userDetails);

                UserDTO userDTO = new UserDTO();
                userDTO.setFirstName("John");
                userDTO.setLastName("Doe");
                userDTO.setAge(32);
                userDTO.setGender("Feminino");
                userDTO.setPhone("81-98765-4322");

                willDoNothing().given(userService).update(any(UserDTO.class));

                // when / act

                ResultActions response = mockMvc.perform(put("/user")
                                .with(user("douglas@example.com").password("senha123").roles("MASTER")) // Autenticação
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(userDTO))); // Enviando JSON

                // then / assert
                response.andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(content().string("Profile updated successfully"));
        }

        @Test
        void testGivenUserId_whenUpdateRole_ThenReturnString() throws JsonProcessingException, Exception {
                // given / arrange
                Address address = new Address(
                                "Rua Exemplo", 100, "Apto 202", "Boa Viagem",
                                "PE", "Brasil", "50000000");

                User user = new User("Douglas", "Fragoso", 30, "Masculino", "12345678900", "81-98765-4321",
                                "douglas@example.com", "senha123", address, Roles.MASTER);
                ReflectionTestUtils.setField(user, "id", 1L); // ID definido via reflection

                Address address1 = new Address(
                                "Rua Exemplo1", 101, "Apto 203", "Boa Viagem",
                                "PE", "Brasil", "50000003");

                User user1 = new User("Lucas", "Fragoso", 30, "Masculino", "12345678901", "81-98765-4322",
                                "lucas@example.com", "senha123", address1, Roles.USER);

                ReflectionTestUtils.setField(user1, "id", 2L); // ID definido via reflection

                // Configurar UserDetailsService mockado
                UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                                user.getEmail(),
                                user.getPassword(),
                                Collections.singletonList(new SimpleGrantedAuthority("ROLE_MASTER")) // 👈 Prefixo
                                                                                                     // obrigatório
                );
                // Mockar UserDetailsService
                given(authenticationService.loadUserByUsername(user.getEmail())).willReturn(userDetails);

                Long id = 2L;

                willDoNothing().given(userService).updateRole(id);

                // when / act
                ResultActions response = mockMvc.perform(put("/user/roles/id/{id}", id)
                                .with(user("douglas@example.com").password("senha123").roles("MASTER"))
                                .contentType("application/json"))
                                .andDo(print()); // Print details for debugging

                // then / assert
                response.andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(content().string("Role updated successfully"));
        }
}
