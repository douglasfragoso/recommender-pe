package com.recommendersystempe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.recommendersystempe.dto.AuthenticationDTO;
import com.recommendersystempe.dto.UserLoginDTO;
import com.recommendersystempe.models.User;
import com.recommendersystempe.service.TokenService;

@RestController
@RequestMapping(value = "/auth", produces = "application/json")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/v1/login")
    ResponseEntity<UserLoginDTO> clientLogin(@RequestBody AuthenticationDTO authenticationDTO) {
            // Autenticando o usuário
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationDTO.getEmail(),
                            authenticationDTO.getUserPassword()));

            // Gerando o token                
            User loggedUser = (User) authentication.getPrincipal();

            String token = tokenService.generateToken(loggedUser);

            UserLoginDTO userDTO = new UserLoginDTO(loggedUser.getId(), loggedUser.getFirstName(), token);

            return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }


    
}
