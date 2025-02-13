package com.recommendersystempe.controllers;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recommendersystempe.configs.SecurityConfig;
import com.recommendersystempe.dtos.AuthenticationDTO;
import com.recommendersystempe.enums.Roles;
import com.recommendersystempe.models.Address;
import com.recommendersystempe.models.User;
import com.recommendersystempe.repositories.UserRepository;
import com.recommendersystempe.service.TokenService;
import com.recommendersystempe.service.UserService;

@WebMvcTest(AuthenticationController.class) // Habilita o contexto do Spring MVC para testes
@Import(SecurityConfig.class) // Importa a configuração real
public class AuthenticationControllerTest {
    
    // MockMvc é uma classe do Spring Test que permite simular requisições HTTP
    @Autowired
    private MockMvc mockMvc;
    
    // ObjectMapper é uma classe do Jackson que permite converter objetos Java em JSON e vice-versa
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean//anotação do Spring Test que cria um mock de um bean, precisa de contexto
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UserRepository userRepository;
    
    private User user;
    private Address address;

    @BeforeEach
    public void setUp() {
        // given / arrange
        address = new Address(
            "Rua Exemplo", 100, "Apto 202", "Boa Viagem", 
            "PE", "Brasil", "50000000");

        user = new User("Douglas", "Fragoso", 30, "Masculino", "12345678900", "81-98765-4321", "douglas@example.com", "senha123", address, Roles.USER); 
        userRepository.save(user);
    }

    @Test
    void testClientLoginSuccess() throws Exception {
        // given / arrange
        AuthenticationDTO authDTO = new AuthenticationDTO("douglas@example.com", "senha123");
        
        //when / act
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, "senha123", user.getAuthorities());
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(tokenService.generateToken(any(User.class))).thenReturn("mocked-jwt-token");

        // then / assert
        mockMvc.perform(post("/auth/v1/login")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(authDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Douglas"))
                .andExpect(jsonPath("$.lastName").value("Fragoso"))
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"));
    }

}
