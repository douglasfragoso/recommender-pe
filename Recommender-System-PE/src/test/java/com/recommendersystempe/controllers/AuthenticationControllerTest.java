package com.recommendersystempe.controllers;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;

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
import com.recommendersystempe.enums.Status;
import com.recommendersystempe.models.Address;
import com.recommendersystempe.models.User;
import com.recommendersystempe.repositories.UserRepository;
import com.recommendersystempe.service.TokenService;
import com.recommendersystempe.service.UserService;

@WebMvcTest(AuthenticationController.class)
@Import(SecurityConfig.class)
public class AuthenticationControllerTest {

    private static final String USER_EMAIL = "richard@example.com";
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
                "Richard",
                "Fragoso",
                LocalDate.of(1990, 12, 5),
                "Masculino",
                "12345678909",
                "81-98765-4321",
                email,
                password,
                address,
                Roles.MASTER);
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
        // given / arrange
        String jsonRequest = createLoginRequest(USER_EMAIL, USER_PASSWORD);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
        // when / act
        when(tokenService.generateToken(any(User.class))).thenReturn("mocked-jwt-token");
        // then / assert
        mockMvc.perform(post("/auth/v1/login")
                .contentType("application/json")
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Richard"))
                .andExpect(jsonPath("$.lastName").value("Fragoso"))
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenService).generateToken(any(User.class));
    }

    @Test
    void testClientLoginFailure() throws Exception {
        // given / arrange
        String jsonRequest = createLoginRequest("wrong@example.com", "WrongPassword");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));
        // then / assert
        mockMvc.perform(post("/auth/v1/login")
                .contentType("application/json")
                .content(jsonRequest))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testClientLoginWithInactiveStatus() throws Exception {
        // given / arrange
        String jsonRequest = createLoginRequest(USER_EMAIL, USER_PASSWORD);

        User inactiveUser = createUser(USER_EMAIL, USER_PASSWORD);
        inactiveUser.setStatus(Status.INACTIVE);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("User is not active"));

        // when / act + then / assert
        mockMvc.perform(post("/auth/v1/login")
                .contentType("application/json")
                .content(jsonRequest))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid credentials"))
                .andExpect(jsonPath("$.message").value("User is not active"));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenService, never()).generateToken(any(User.class));
    }
}
