package com.recommendersystempe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.recommendersystempe.enums.Roles;
import com.recommendersystempe.models.Address;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class UserDTO {
    @Getter
    private Long id;

    @Getter @Setter 
    private String firstName;

    @Getter @Setter 
    private String lastName;

    @Getter @Setter 
    private Integer age;

    @Getter @Setter 
    private String gender;

    @Getter @Setter 
    private String cpf;

    @Getter @Setter 
    private String phone;

    @Getter @Setter 
    private String email;

    @Getter @Setter 
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)  //Atributo não será exibido
    private String userPassword;

    @Getter @Setter 
    private Address address;

    @Getter 
    private Roles role;

    /*Composição dos atributos
     * Anotação Bean Validation
     * Anotação Swagger
     */

     public UserDTO(String firstName, String lastName, Integer age, String cpf, String phone, String email, Roles role, Address address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.cpf = cpf;
        this.phone = phone;
        this.email = email;
        this.role = role;
        this.address = address;
    }
}
