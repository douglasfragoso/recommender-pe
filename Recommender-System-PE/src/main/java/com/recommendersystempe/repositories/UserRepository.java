package com.recommendersystempe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recommendersystempe.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    User findByEmail(String subject);

    boolean existsByCpfAndIdNot(String cpf, Long id);

    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByPhoneAndIdNot(String phone, Long id);

}
