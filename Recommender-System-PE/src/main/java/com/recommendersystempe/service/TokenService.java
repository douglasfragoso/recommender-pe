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
    
    // Gera um token JWT com o email do usuário
    public String generateToken(User user) {
        try {
            //algorithm usado para assinar o token
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

            return JWT.create()
                    .withIssuer(TOKEN_ISSUER)
                    .withSubject(user.getEmail())
                    .withExpiresAt(_expirationDate())
                    .withClaim("id", user.getId())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token JWT.", exception);
        }
    }

    // Verifica se o token é válido e retorna o email do usuário
    public String getSubject(String token) {
        try {
            //algorithm usado para verificar a assinatura do token
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

    private Instant _expirationDate() {
        // O token expira em 10 minutos de acordo com o fuso horário do Brasil
        return LocalDateTime.now().plusMinutes(10).toInstant(ZoneOffset.of("-03:00"));
    }

    // Propriedades definida no application.properties
    @Value("${api.security.token.secret.key}")
    private String SECRET_KEY;

    @Value("${api.security.token.issuer}")
    private String TOKEN_ISSUER;
}


