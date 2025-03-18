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

import com.recommendersystempe.configs.SecurityConfig;
import com.recommendersystempe.enums.Roles;
import com.recommendersystempe.models.Address;
import com.recommendersystempe.models.User;
import com.recommendersystempe.repositories.UserRepository;
import com.recommendersystempe.service.TokenService;
import com.recommendersystempe.service.UserService;

@WebMvcTest(AuthenticationController.class) // Habilita o contexto do Spring MVC para testes - Enables the Spring MVC
                                            // context for testing
@Import(SecurityConfig.class) // Importa a configuração real - Imports the real configuration
public class AuthenticationControllerTest {

    // MockMvc é uma classe do Spring Test que permite simular requisições HTTP -
    // MockMvc is a Spring Test class that allows you to simulate HTTP requests
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean // anotação do Spring Test que cria um mock de um bean, precisa de contexto -
                 // Spring Test annotation that creates a mock of a bean, needs context
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

        address = new Address(
                "Rua Exemplo", 100, "Apto 202", "Boa Viagem", "Recife",
                "PE", "Brasil", "50000000");

        user = new User(
                "Douglas", 
                "Fragoso",
                30, 
                "Masculino",
                "12345678909", 
                "81-98765-4321",
                "douglas@example.com", 
                "Senha123*",
                address, 
                Roles.MASTER 
        );
    }

    @Test
    void testClientLoginSuccess() throws Exception {
        // given / arrange
        String jsonRequest = """
                {
                    "email": "douglas@example.com",
                    "userPassword": "Senha123*"
                }
                """;

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));

        when(tokenService.generateToken(any(User.class))).thenReturn("mocked-jwt-token");

        // then / assert
        mockMvc.perform(post("/auth/v1/login")
                .contentType("application/json")
                .content(jsonRequest)) // Usa o JSON manual
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Douglas"))
                .andExpect(jsonPath("$.lastName").value("Fragoso"))
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"));
    }
    
    
}
