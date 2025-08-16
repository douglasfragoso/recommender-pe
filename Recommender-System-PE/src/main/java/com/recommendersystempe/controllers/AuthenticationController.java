package com.recommendersystempe.controllers;

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

import com.recommendersystempe.controllers.exception.StandardError;
import com.recommendersystempe.controllers.exception.ValidationError;
import com.recommendersystempe.dtos.AuthenticationDTO;
import com.recommendersystempe.dtos.UserLoginDTO;
import com.recommendersystempe.enums.Status;
import com.recommendersystempe.models.User;
import com.recommendersystempe.service.TokenService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/auth", produces = "application/json")
@Tag(name = "Auth", description = "Authentication API")
public class AuthenticationController {

        @Autowired
        private AuthenticationManager authenticationManager;

        @Autowired
        private TokenService tokenService;

        @PostMapping("/v1/login")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Successfully authenticated", content = @Content(schema = @Schema(implementation = UserLoginDTO.class), examples = @ExampleObject(value = "{ \"firstName\": \"John\", \"lastName\": \"Doe\", \"token\": \"jwt.token.here\" }"))),
                        @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = ValidationError.class))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = StandardError.class))) })
        @Operation(summary = "Authenticate user", description = "Authenticates a user by email and password, returning a JWT token", tags = {
                        "Auth" })
        ResponseEntity<UserLoginDTO> clientLogin(@Valid @RequestBody AuthenticationDTO authenticationDTO) {
                // Autenticando o usuário
                Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                authenticationDTO.getEmail(),
                                                authenticationDTO.getUserPassword()));

                // Gerando o token
                User loggedUser = (User) authentication.getPrincipal();

                if(loggedUser.getStatus() != Status.ACTIVE) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
                }

                String token = tokenService.generateToken(loggedUser);

                UserLoginDTO userDTO = new UserLoginDTO(loggedUser.getFirstName(), loggedUser.getLastName(), token);

                return ResponseEntity.status(HttpStatus.OK).body(userDTO);
        }

}
