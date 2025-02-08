package com.recommendersystempe.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import com.recommendersystempe.enums.Roles;
import com.recommendersystempe.models.Address;
import com.recommendersystempe.models.User;

import jakarta.persistence.EntityManager;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    private User user;
    private Address address;

    @BeforeEach
    public void setUp() {
        // given / arrange
        userRepository.deleteAll();

        address = new Address(
            "Rua Exemplo", 100, "Apto 202", "Boa Viagem", 
            "PE", "Brasil", "50000000");

        user = new User("Douglas", "Fragoso", 30, "Masculino", "12345678900", "81-98765-4321", "douglas@example.com", "senha123", address, Roles.USER); 
    }

    @Test
    @Transactional
    void testGivenUser_whenSave_ThenReturUser() {
        // when / act
        User savedUser = userRepository.save(user);
        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        // then / assert
        assertNotNull(savedUser, "User must not be null");
        assertTrue(savedUser.getId() > 0, "User id must be greater than 0");
        assertNotNull(foundUser, "User must be present in the database");
    }

    @Test
    void testGivenUserList_whenFindAll_ThenReturnUserList() {
        // given / arrange
        Address address1 = new Address(
            "Rua Exemplo1", 101, "Apto 203", "Boa Viagem", 
            "PE", "Brasil", "50000003");

        User user1 = new User("Lucas", "Fragoso", 30, "Masculino", "12345678901", "81-98765-4322", "lucas@example.com", "senha123", address1, Roles.USER); 

        userRepository.save(user);
        userRepository.save(user1);

        // when / act
        List<User> UserList = userRepository.findAll();

        // then / assert
        assertNotNull(UserList, "User list must not be null");
        assertEquals(2, UserList.size(), "User list must have 2 users");
    }

    @Test
    void testGivenSaveUser_whenFindById_ThenReturnUser() {
        // given / arrange
        userRepository.save(user);

        // when / act
        Optional<User> foundUser = userRepository.findById(user.getId());

        // then / assert
        assertNotNull(foundUser, "User must not be null");
        assertTrue(foundUser.isPresent(), "User must be present in the database");
        assertEquals(foundUser.get().getId(), user.getId(), "User id must be the same");
    }

    @Test
    void testGivenSaveUser_whenFindByEmail_ThenReturnUser() {
        // given / arrange
        userRepository.save(user);

        // when / act
        User foundUser = userRepository.findByEmail(user.getEmail());

        // then / assert
        assertNotNull(foundUser, "User must not be null");
        assertEquals(foundUser.getEmail(), user.getEmail(), "User Email must be the same");
    }

    @Test
    @Transactional
    void testGivenUser_whenDeleteById_ThenReturnNull() {
        // given / arrange
        userRepository.save(user);

        // when / act
        userRepository.deleteById(user.getId());
        Optional<User> user1 = userRepository.findById(user.getId());

        // then / assert
        assertTrue(user1.isEmpty(), "User must be deleted");
    }

    @Test
    @Transactional
    void testGivenPersonList_whenUpdate_ThenReturnNothing() {
        // given / arrange
        userRepository.save(user);

        // when / act

        userRepository.update(user.getId(), "Lucas", "Fragoso1", 31, "Feminino", "81-98765-4322");

        entityManager.clear();

        User updatedUser = userRepository.findById(user.getId()).orElseThrow();

        // then / assert
        assertNotNull(updatedUser, "User must not be null");
        assertEquals("Lucas", updatedUser.getFirstName(), "User first name must be Lucas");
        assertEquals("Feminino", updatedUser.getGender(), "User gender must be Feminino");
    }

    @Test
    void testGivenUser_whenExistsByCpf_thenReturnTrue() {
        // given / arrange
        userRepository.save(user);

        // when / act
        boolean exists = userRepository.existsByCpf(user.getCpf());

        // then / assert
        assertTrue(exists, "User with CPF should exist in the database");
    }

    @Test
    void testGivenNonExistentCpf_whenExistsByCpf_thenReturnFalse() {
        // when / act
        boolean exists = userRepository.existsByCpf("99999999999");

        // then / assert
        assertFalse(exists, "User with given CPF should not exist in the database");
    }

    @Test
    void testGivenUser_whenExistsByEmail_thenReturnTrue() {
        // given / arrange
        userRepository.save(user);

        // when / act
        boolean exists = userRepository.existsByEmail(user.getEmail());

        // then / assert
        assertTrue(exists, "User with Email should exist in the database");
    }

    @Test
    void testGivenNonExistentEmail_whenExistsByEmail_thenReturnFalse() {
        // when / act
        boolean exists = userRepository.existsByEmail("nonexistent@example.com");

        // then / assert
        assertFalse(exists, "User with given email should not exist in the database");
    }

    @Test
    void testGivenUser_whenExistsByPhone_thenReturnTrue() {
        // given / arrange
        userRepository.save(user);

        // when / act
        boolean exists = userRepository.existsByPhone(user.getPhone());

        // then / assert
        assertTrue(exists, "User with Phone number should exist in the database");
    }

    @Test
    void testGivenNonExistentPhone_whenExistsByPhone_thenReturnFalse() {
        // when / act
        boolean exists = userRepository.existsByPhone("81-99999-8888");

        // then / assert
        assertFalse(exists, "User with given phone number should not exist in the database");
    }
}
