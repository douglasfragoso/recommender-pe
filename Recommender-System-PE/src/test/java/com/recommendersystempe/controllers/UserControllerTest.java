package com.recommendersystempe.controllers;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.recommendersystempe.configs.SecurityConfig;
import com.recommendersystempe.dtos.UserDTO;
import com.recommendersystempe.models.Address;
import com.recommendersystempe.repositories.UserRepository;
import com.recommendersystempe.service.TokenService;
import com.recommendersystempe.service.UserService;

@WebMvcTest(UserController.class) // Habilita o contexto do Spring MVC para testes
@Import(SecurityConfig.class) // Importa a configuração real
public class UserControllerTest {
    
    // MockMvc é uma classe do Spring Test que permite simular requisições HTTP
    @Autowired
    private MockMvc mockMvc;
    
    // ObjectMapper é uma classe do Jackson que permite converter objetos Java em JSON e vice-versa
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean//anotação do Spring Test que cria um mock de um bean, precisa de contexto
    private UserService userService;

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
        ResultActions response = mockMvc.perform(post("/user")
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
}
