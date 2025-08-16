package com.recommendersystempe.dtos;

import java.time.LocalDate;

import org.hibernate.validator.constraints.br.CPF;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.recommendersystempe.enums.Roles;
import com.recommendersystempe.enums.Status;
import com.recommendersystempe.models.Address;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Schema(description = "Represents the UserDTOUpdate in the system")
public class UserDTOUpdate {

    @Getter
    @Schema(description = "User Id", example = "1", required = true)
    private Long id;

    @Getter
    @Setter
    @Schema(description = "Firstname of a user", example = "Jhon", required = true)
    @Size(min = 3, max = 20, message = "The field firstname must be between 3 and 20 characters")
    private String firstName;

    @Getter
    @Setter
    @Schema(description = "Lastname of a user", example = "Doe", required = true)
    @Size(min = 3, max = 20, message = "The field lastname must be between 3 and 20 characters")
    private String lastName;

    @Getter
    @Setter
    @Schema(description = "Date of birth of a user", example = "1998-05-20", required = true)
    @Past(message = "Birth date must be in the past")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @Getter
    @Setter
    @Schema(description = "Gender of a user", example = "Male", required = true)
    @Size(min = 5, max = 9, message = "The field gender must be between 5 and 9 characters")
    @Pattern(regexp = "^(Masculino|Feminino|Outro)$", message = "Gender must be Male, Female, or Other")
    private String gender;

    @Getter
    @Setter
    @Schema(description = "CPF of a user", example = "12345678901", required = true)
    @Size(min = 11, max = 11, message = "CPF must be exactly 11 digits")
    @CPF(message = "CPF should be valid")
    private String cpf;

    @Getter
    @Setter
    @Schema(description = "Phone number of a user", example = "11-98765-4321", required = true)
    @Pattern(regexp = "^\\d{10,11}$", message = "Phone number must be between 10 and 11 characters")
    private String phone;

    @Getter
    @Setter
    @Schema(description = "Email of a user", example = "jhon.doe@example.com", required = true)
    @Size(max = 50, message = "The field email must be up to 50 characters")
    @Email(message = "Email should be valid")
    private String email;

    @Setter
    @Getter
    @Schema(description = "Role of a user", required = false)
    private Roles role;

    @Getter
    @Setter
    @Schema(description = "Address of a user. Can be partially updated.", required = false)
    @Valid 
    private Address address;

    @Getter @Setter
    @Schema(description = "Status of a user", required = true)
    private Status status;

}