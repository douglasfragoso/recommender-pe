package com.recommendersystempe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.recommendersystempe.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    User findByEmail(String subject);

    @Transactional
    @Modifying(clearAutomatically = true) // Limpa o cache do EntityManager - Clears the EntityManager cache
    @Query(value = "UPDATE tb_users SET first_name = :firstName, last_name = :lastName, age = :age, gender = :gender, phone = :phone, email = :email WHERE id = :id", nativeQuery = true)
    void update(
            @Param("id") Long id,
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("age") Integer age,
            @Param("gender") String gender,
            @Param("phone") String phone,
            @Param("email") String email
            );
}
