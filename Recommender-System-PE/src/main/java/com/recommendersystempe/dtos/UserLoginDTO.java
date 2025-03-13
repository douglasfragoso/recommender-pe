package com.recommendersystempe.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Represents the UserLoginDTO in the system")
public class UserLoginDTO {

    @Schema(description = "Firstname of a user", example = "Jhon", required = true)
    @Size(min = 3, max = 20, message = "The field firstname must be between 3 and 20 characters")
    private String firstName;

    @Schema(description = "Lastname of a user", example = "Doe", required = true)
    @Size(min = 3, max = 20, message = "The field lastname must be between 3 and 20 characters")
    private String lastName;

    @Schema(description = "Token of a authentication", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", required = true)
    @Size(min = 10, max = 1000, message = "The field token must be between 10 and 1000 characters")
    private String token;
}
