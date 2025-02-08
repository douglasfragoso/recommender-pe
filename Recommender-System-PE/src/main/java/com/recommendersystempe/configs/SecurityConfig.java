package com.recommendersystempe.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // Habilita a segurança baseada em web
@EnableMethodSecurity(securedEnabled = true) // Habilita a segurança baseada em anotações
public class SecurityConfig {

    @Autowired
    private SecurityFilterConfig securityFilter;

    // Configuração de segurança
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(customizer -> {
                    customizer.requestMatchers(HttpMethod.POST, "/auth/v1/login").permitAll();
                    customizer.requestMatchers(HttpMethod.POST, "/user").permitAll();
                    customizer.requestMatchers(HttpMethod.GET, "/h2-console/**").permitAll();
                    customizer.requestMatchers(HttpMethod.POST, "/h2-console/**").permitAll();
                    // customizer.requestMatchers(AUTH_WHITELIST).permitAll();
                    customizer.anyRequest().authenticated();
                })
                .headers(headers -> headers.frameOptions(options -> options.sameOrigin())) // Permite o uso do H2 - iframe
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    // private static final String[] AUTH_WHITELIST = {
    //         "/v2/api-docs",
    //         "/swagger-resources",
    //         "/swagger-resources/**",
    //         "/configuration/ui",
    //         "/configuration/security",
    //         "/swagger-ui.html",
    //         "/webjars/**",
    //         "/v3/api-docs/**",
    //         "/swagger-ui/**"
    // };

    //faz o AuthenticationManager ser injetado no SecurityFilter - usado no AuthenticationController	
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    //Bean para criptografar a senha - usado no UserService 
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
