package com.recommendersystempe.config;

import java.io.IOException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.recommendersystempe.repositories.UserRepository;
import com.recommendersystempe.service.TokenService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilterConfig extends OncePerRequestFilter { 
    // OncePerRequestFilter é um filtro que garante que o filtro seja chamado apenas uma vez por requisição

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    // Método para filtrar as requisições
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,  FilterChain filterChain)
            throws ServletException, IOException {

        String token = _getTokenFromRequest(request);

        if (Objects.nonNull(token)) {
            String subject = tokenService.getSubject(token);

            UserDetails client = userRepository.findByEmail(subject);

            Authentication authentication = new UsernamePasswordAuthenticationToken(client, null,
                    client.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    // Método para obter o token do cabeçalho da requisição
    private String _getTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (Objects.nonNull(authHeader)) {
            return authHeader.replace("Bearer ", "");
        }
        return null;
    }
}
