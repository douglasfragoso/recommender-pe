package com.recommendersystempe.configs;

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
    // OncePerRequestFilter é um filtro que garante a chamada apenas uma vez por requisição - OncePerRequestFilter is a filter that guarantees that it is called only once per request.

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    // Método para filtrar as requisições - Method to filter requests
    @Override
    protected void doFilterInternal(@SuppressWarnings("null") HttpServletRequest request, @SuppressWarnings("null") HttpServletResponse response,  @SuppressWarnings("null") FilterChain filterChain)
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

    // Método para obter o token do cabeçalho da requisição - Method to get the token from the request header
    private String _getTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (Objects.nonNull(authHeader)) {
            return authHeader.replace("Bearer ", "");
        }
        return null;
    }
}
