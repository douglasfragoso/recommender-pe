package com.recommendersystempe.dtos;

import org.hibernate.validator.constraints.br.CPF;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Represents the UserDTO in the system")
public class UserDTOUpdate {

    @Getter
    @Schema(description = "User Id", example = "1", required = true)
    private Long id;

    @Getter
    @Setter
    @Schema(description = "Firstname of a user", example = "Jhon", required = true)
    @NotBlank(message = "The field firstname is required")
    @Size(min = 3, max = 20, message = "The field firstname must be between 3 and 20 characters")
    private String firstName;

    @Getter
    @Setter
    @Schema(description = "Lastname of a user", example = "Doe", required = true)
    @NotBlank(message = "The field lastname is required")
    @Size(min = 3, max = 20, message = "The field lastname must be between 3 and 20 characters")
    private String lastName;

    @Getter
    @Setter
    @Schema(description = "Age of a user", example = "25", required = true)
    @NotNull(message = "The field age is required")
    @Min(value = 1, message = "Age must be at least 1")
    @Max(value = 120, message = "Age must be at most 120")
    private Integer age;

    @Getter
    @Setter
    @Schema(description = "Gender of a user", example = "Male", required = true)
    @NotBlank(message = "The field gender is required")
    @Size(min = 5, max = 9, message = "The field gender must be between 5 and 9 characters")
    @Pattern(regexp = "^(Masculino|Feminino|Outro)$", message = "Gender must be Male, Female, or Other")
    private String gender;

    @Getter
    @Setter
    @Schema(description = "CPF of a user", example = "12345678901", required = true)
    @NotBlank(message = "The field CPF is required")
    @Size(min = 11, max = 11, message = "CPF must be exactly 11 digits")
    @CPF(message = "CPF should be valid")
    private String cpf;

    @Getter
    @Setter
    @Schema(description = "Phone number of a user", example = "11-98765-4321", required = true)
    @NotBlank(message = "The field phone is required")
    @Pattern(regexp = "^\\d{10,11}$", message = "Phone number must be between 10 and 11 characters")
    private String phone;

    @Getter
    @Setter
    @Schema(description = "Email of a user", example = "jhon.doe@example.com", required = true)
    @NotBlank(message = "The field email is required")
    @Size(max = 50, message = "The field email must be up to 50 characters")
    @Email(message = "Email should be valid")
    private String email;

}