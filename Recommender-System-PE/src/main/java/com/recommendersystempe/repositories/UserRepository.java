package com.recommendersystempe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.recommendersystempe.models.User;

public interface UserRepository extends JpaRepository <User, Long>{

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    User findByEmail(String subject);

    @Transactional
    @Modifying
    @Query(value = "UPDATE tb_users SET first_name = :firstName, last_name = :lastName, age = :age, gender = :gender, phone = :phone WHERE id = :id", nativeQuery = true)
    void update(Long id, String firstName, String lastName, Integer age, String gender, String phone);

}
