package com.recommendersystempe.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.recommendersystempe.models.User;

@Service
public class TokenService {
    
    // Gera um token JWT com o email do usuário - Generates a JWT token with the user's email
    public String generateToken(User user) {
        try {
            //Algoritimo usado para assinar o token - Algorithm used to sign the token
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

            return JWT.create()
                    .withIssuer(TOKEN_ISSUER)
                    .withSubject(user.getEmail())
                    .withExpiresAt(_expirationDate())
                    .withClaim("id", user.getId())
                    .withClaim("role", user.getRole().toString())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token JWT.", exception);
        }
    }

    // Verifica se o token é válido e retorna o email do usuário - Checks if the token is valid and returns the user's email
    public String getSubject(String token) {
        try {
            //Algoritmo usado para verificar a assinartura do token - Algorithm used to verify the token signature
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

            return JWT.require(algorithm)
                    .withIssuer(TOKEN_ISSUER)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token JWT inválido ou expirado.", exception);
        }
    }

    public String getRole(String token) {
        try {
            //Algoritmo usado para verificar a assinartura do token - Algorithm used to verify the token signature
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

            return JWT.require(algorithm)
                    .withIssuer(TOKEN_ISSUER)
                    .build()
                    .verify(token)
                    .getClaim("role").asString();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token JWT inválido ou expirado.", exception);
        }
    }

    private Instant _expirationDate() {
        // O token expira em 10 minutos de acordo com o fuso horário do Brasil - The token expires in 10 minutes according to the Brazil time zone
        return LocalDateTime.now().plusMinutes(60).toInstant(ZoneOffset.of("-03:00"));
    }

    // Propriedades definida no application.properties - Properties defined in application.properties
    @Value("${api.security.token.secret.key}")
    private String SECRET_KEY;

    @Value("${api.security.token.issuer}")
    private String TOKEN_ISSUER;
}


