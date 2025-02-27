package com.recommendersystempe.dtos;

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

    @Setter @Getter 
    private Roles role;

    /*Composição dos atributos
     * Anotação Bean Validation
     * Anotação Swagger
     */

    public UserDTO(String firstName, String lastName, Integer age, String gender, String cpf, String phone, String email, String userPassword, Address address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.cpf = cpf;
        this.phone = phone;
        this.email = email;
        this.userPassword = userPassword;
        this.address = address;
    }


    public UserDTO(Long id, String firstName, String lastName, Integer age, String gender, String cpf, String phone, String email, Address address, Roles role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.cpf = cpf;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.role = role;
    }

    public UserDTO(String firstName, String lastName, Integer age, String gender, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.phone = phone;
    }

    public UserDTO(Long id, String firstName, String lastName, Integer age, String gender, String phone) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.phone = phone;
    }

}