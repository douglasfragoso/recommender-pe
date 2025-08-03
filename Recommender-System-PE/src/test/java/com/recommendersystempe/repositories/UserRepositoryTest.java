package com.recommendersystempe.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.recommendersystempe.enums.Roles;
import com.recommendersystempe.models.Address;
import com.recommendersystempe.models.User;

import jakarta.persistence.EntityManager;

@DataJpaTest
public class UserRepositoryTest {

    private static final Address ADDRESS = new Address(
            "Avenida Central", 250, "Casa 5", "Boa Viagem", "Recife",
            "PE", "Brasil", "01000000");

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    private User user;

    @BeforeEach
    public void setUp() {

        userRepository.deleteAll();

        user = new User(
                "Mariana",
                "Silva",
                28,
                "Feminino",
                "98765432100",
                "11-99876-5432",
                "mariana@example.com",
                "Segura456*",
                ADDRESS,
                Roles.USER);
    }

    @Test
    void testGivenUser_whenSaveReturUser() {
        // when / act
        User savedUser = userRepository.save(user);
        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        // then / assert
        assertAll(
                () -> assertNotNull(savedUser, "User must not be null"),
                () -> assertTrue(savedUser.getId() > 0, "User id must be greater than 0"),
                () -> assertNotNull(foundUser, "User must be present in the database"));
    }

    @Test
    void testGivenUserList_whenFindAllReturnUserList() {
        // given / arrange
        Address address1 = new Address(
                "Rua Exemplo1", 101, "Apto 203", "Boa Viagem", "Recife",
                "PE", "Brasil", "50000003");

        User user1 = new User("Lucas", "Fragoso", 30, "Masculino", "12345678909", "81-98765-4322", "lucas@example.com",
                "senha123", address1, Roles.USER);

        userRepository.save(user);
        userRepository.save(user1);

        // when / act
        List<User> userList = userRepository.findAll();

        // then / assert
        assertAll(
                () -> assertNotNull(userList, "User list must not be null"),
                () -> assertEquals(2, userList.size(), "User list must have 2 users"));

    }

    @Test
    void testGivenSaveUser_whenFindByIdReturnUser() {
        // given / arrange
        userRepository.save(user);

        // when / act
        Optional<User> foundUser = userRepository.findById(user.getId());

        // then / assert

        assertAll(
                () -> assertNotNull(foundUser, "User must not be null"),
                () -> assertTrue(foundUser.isPresent(), "User must be present in the database"),
                () -> assertEquals(foundUser.get().getId(), user.getId(), "User id must be the same"));
    }

    @Test
    void testGivenSaveUser_whenFindByEmailReturnUser() {
        // given / arrange
        userRepository.save(user);

        // when / act
        User foundUser = userRepository.findByEmail(user.getEmail());

        // then / assert
        assertAll(
                () -> assertNotNull(foundUser, "User must not be null"),
                () -> assertEquals(foundUser.getEmail(), user.getEmail(), "User Email must be the same"));
    }

    @Test
    void testGivenUser_whenDeleteByIdReturnNull() {
        // given / arrange
        userRepository.save(user);

        // when / act
        userRepository.deleteById(user.getId());
        Optional<User> user1 = userRepository.findById(user.getId());

        // then / assert
        assertTrue(user1.isEmpty(), "User must be deleted");
    }

    @Test
    void testGivenPersonList_whenUpdateReturnNothing() {
        // given / arrange
        userRepository.save(user);

        // when / act

        userRepository.update(user.getId(), "Lucas", "Fragoso1", 31, "Feminino", "81-98765-4322", "richard@example.com");

        // Clear the persistence context, causing all managed entities to become
        // detached
        entityManager.clear();

        User updatedUser = userRepository.findById(user.getId()).orElseThrow();

        // then / assert
        assertAll(
                () -> assertNotNull(updatedUser, "User must not be null"),
                () -> assertEquals("Lucas", updatedUser.getFirstName(), "User first name must be Lucas"),
                () -> assertEquals("Feminino", updatedUser.getGender(), "User gender must be Feminino"));
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
