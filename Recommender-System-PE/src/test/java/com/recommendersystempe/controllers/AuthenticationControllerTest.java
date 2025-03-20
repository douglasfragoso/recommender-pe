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
import org.springframework.security.authentication.BadCredentialsException;
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

@WebMvcTest(AuthenticationController.class)
@Import(SecurityConfig.class)
public class AuthenticationControllerTest {

    private static final String USER_EMAIL = "douglas@example.com";
    private static final String USER_PASSWORD = "Senha123*";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
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

        user = createUser(USER_EMAIL, USER_PASSWORD);
    }

    private User createUser(String email, String password) {
        return new User(
                "Douglas", 
                "Fragoso",
                30, 
                "Masculino",
                "12345678909", 
                "81-98765-4321",
                email, 
                password,
                address, 
                Roles.MASTER 
        );
    }

    private String createLoginRequest(String email, String password) {
        return String.format("""
            {
                "email": "%s",
                "userPassword": "%s"
            }
            """, email, password);
    }

    @Test
    void testClientLoginSuccess() throws Exception {
        String jsonRequest = createLoginRequest(USER_EMAIL, USER_PASSWORD);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));

        when(tokenService.generateToken(any(User.class))).thenReturn("mocked-jwt-token");
        
        mockMvc.perform(post("/auth/v1/login")
                .contentType("application/json")
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Douglas"))
                .andExpect(jsonPath("$.lastName").value("Fragoso"))
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenService).generateToken(any(User.class));
    }

    @Test
    void testClientLoginFailure() throws Exception {
        String jsonRequest = createLoginRequest("wrong@example.com", "WrongPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenThrow(new BadCredentialsException("Invalid credentials"));

        mockMvc.perform(post("/auth/v1/login")
                .contentType("application/json")
                .content(jsonRequest))
                .andExpect(status().isUnauthorized());
    }
}

    