package com.recommendersystempe.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.recommendersystempe.enums.Roles;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode(of = "id")
@ToString
@NoArgsConstructor
@Entity(name = "tb_users")
public class User implements UserDetails {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Getter @Setter
    @Column(name = "first_name", length = 20)
    @NotBlank(message = "The field firstname is required")
    @Size(min = 3, max = 20, message = "The field firstname must be between 3 and 20 characters")
    private String firstName;

    @Getter @Setter
    @Column(name = "last_name", length = 20)
    @NotBlank(message = "The field lastname is required")
    @Size(min = 3, max = 20, message = "The field lastname must be between 3 and 20 characters")
    private String lastName;
   
    @Getter @Setter
    @Column(name = "age")
    @Min(value = 1, message = "Age must be at least 1")
    @Max(value = 120, message = "Age must be at most 120")
    private Integer age;

    @Getter @Setter
    @Column(name = "gender", length = 9)
    @NotBlank(message = "The field gender is required")
    @Size(min = 6, max = 9, message = "The field gender must be between 6 and 9 characters")
    @Pattern(regexp = "^(Masculino|Feminino|Outro)$", message = "Gender must be Male, Female, or Other")
    private String gender;

    @Getter @Setter
    @Column(name = "cpf", length = 11, unique = true, updatable = false)
    @NotBlank(message = "The field CPF is required")
    @Size(min = 11, max = 11, message = "CPF must be exactly 11 digits")
    @Pattern(regexp = "\\d{11}", message = "CPF must be exactly 11 digits")
    private String cpf;

    @Getter @Setter
    @JsonFormat(pattern = "^(\\d{2})-(\\d{4,5})-(\\d{4})$")
    @Column(name = "phone", length = 15, unique = true)
    @NotBlank(message = "The field phone is required")
    @Size(min = 12, max = 13, message = "Phone number must be between 12 and 13 characters")
    @Pattern(regexp = "^(\\d{2})-(\\d{4,5})-(\\d{4})$", message = "Phone number must be in the format XX-XXXXX-XXXX or XX-XXXX-XXXX")
    private String phone;

    @Getter @Setter
    @Column(name = "email", unique = true, length = 50)
    @NotBlank(message = "The field email is required")
    @Size(max = 50, message = "The field email must be up to 50 characters")
    @Email(message = "Email should be valid")
    private String email;

    @Setter
    @Column(name = "user_password", length = 50)
    @NotBlank(message = "The field password is required")
    @Size(min = 8, max = 50, message = "Password must be between 8 and 50 characters")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", 
             message = "Password must contain at least one digit, one lowercase, one uppercase, one special character and no whitespace")
    private String userPassword;

    @Getter @Setter
    @Embedded
    @NotNull(message = "The field address is required")
    private Address address;
    
    @Getter @Setter
    @Column(name = "roles")
    @Enumerated(EnumType.STRING)
    @NotNull(message = "The field role is required")
    private Roles role;

    @Getter
    @OneToMany(mappedBy = "user")
    private List<Preferences> preferences = new ArrayList<>();

    @Getter
    @OneToMany(mappedBy = "user")
    private List<Recommendation> recommendations = new ArrayList<>();

    public User(String firstName, String lastName, Integer age, String gender, String cpf, String phone, String email, String userPassword, Address address, Roles role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.cpf = cpf;
        this.phone = phone;
        this.email = email;
        this.userPassword = userPassword;
        this.address = address;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        switch (role) {
            case MASTER:
                return List.of(new SimpleGrantedAuthority("ROLE_MASTER"));
            case ADMIN:
                return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
            case USER:
                return List.of(new SimpleGrantedAuthority("ROLE_USER"));
            default:
                return List.of(new SimpleGrantedAuthority("ROLE_GUEST"));
        }
    }

    @Override
    public String getPassword() {
        return userPassword;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
       return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}