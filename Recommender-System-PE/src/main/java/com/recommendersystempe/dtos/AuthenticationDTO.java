package com.recommendersystempe.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Represents the authenticationDTO in the system")
public class AuthenticationDTO {

    @Schema(description = "Email address of a user", example = "johndoe@example.com", required = true)
    @NotBlank(message = "The field email is required")
    @Size(max = 50, message = "The field email must be up to 50 characters")
    @Email(message = "The field email must be a valid email")
    private String email;

    @Schema(description = "Password of the client", example = "password123", required = true, accessMode = AccessMode.WRITE_ONLY)
    @NotBlank(message = "The field password is required")
    @Size(min = 6, max = 50, message = "The field password must be between 6 and 50 characters")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String userPassword;

}
