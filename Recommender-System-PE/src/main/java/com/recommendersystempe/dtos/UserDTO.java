package com.recommendersystempe.dtos;

import java.time.LocalDate;

import org.hibernate.validator.constraints.br.CPF;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.recommendersystempe.enums.Roles;
import com.recommendersystempe.enums.Status;
import com.recommendersystempe.models.Address;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Schema(description = "Represents the UserDTO in the system")
public class UserDTO {

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
    @Schema(description = "Date of birth of a user", example = "1998-05-20", required = true)
    @NotNull(message = "The field date of birth is required")
    @Past(message = "Birth date must be in the past")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

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

    @Getter
    @Setter
    @Schema(description = "Password of a user", example = "Password123!", required = true)
    @NotBlank(message = "The field password is required")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*+=])(?=\\S+$).{8,}$",
    message = "Password must contain at least one digit, one lowercase, one uppercase, one special character and no whitespace")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) 
    private String userPassword;

    @Getter
    @Setter
    @Schema(description = "Address of a user", required = true)
    @NotNull(message = "The field address is required")
    @Valid
    private Address address;

    @Setter
    @Getter
    @Schema(description = "Role of a user", required = false)
    private Roles role;

    @Getter @Setter
    @Schema(description = "Status of a user", required = true)
    private Status status;

    public UserDTO(String firstName, String lastName, LocalDate birthDate, String gender, String cpf, String phone,
            String email, String userPassword, Address address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.cpf = cpf;
        this.phone = phone;
        this.email = email;
        this.userPassword = userPassword;
        this.address = address;
    }

    public UserDTO(Long id, String firstName, String lastName, LocalDate birthDate, String gender, String cpf, String phone,
            String email, Address address, Roles role, Status status) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.cpf = cpf;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.role = role;
        this.status = status;
    }

    public UserDTO(Long id, String firstName, String lastName, LocalDate birthDate, String gender, String phone, Address address) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
       
    }
}