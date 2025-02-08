package com.recommendersystempe.models;

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
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private String firstName;

    @Getter @Setter
    @Column(name = "last_name", length = 20)
    private String lastName;
   
    @Getter @Setter
    @Column(name = "age")
    private Integer age;

    @Getter @Setter
    @Column(name = "gender", length = 9)
    private String gender;

    @Getter @Setter
    @Column(name = "cpf", length = 11, unique = true, updatable = false)
    private String cpf;

    @Getter @Setter
    @JsonFormat(pattern = "^(\\d{2})-(\\d{4,5})-(\\d{4})$")
    @Column(name = "phone", length = 15, unique = true)
    private String phone;

    @Getter @Setter
    @Column(name = "email", unique = true)
    private String email;

    @Setter
    @Column(name = "password", length = 100)
    private String userPassword;

    @Getter @Setter
    @Embedded
    private Address address;
    
    @Column(name = "role")
    private Roles role;

    //@Getter
    //@OneToMany(mappedBy = "user")
    // private List<Preferences> preferences = new ArrayList<>();

    //@Getter
    //@OneToMany(mappedBy = "user")
    // private List<Recommendation> recommendations = new ArrayList<>();

    /*Composição dos atributos
     * Anotação Lombok
     * Anotação JPA
     * Anotação de Composição
     */

    public User(String firstName, String lastName, Integer age, String gender, String cpf, String phone, String email, String userPassword, Address adress, Roles role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.cpf = cpf;
        this.phone = phone;
        this.email = email;
        this.userPassword = userPassword;
        this.address = adress;
        this.role = role;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
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