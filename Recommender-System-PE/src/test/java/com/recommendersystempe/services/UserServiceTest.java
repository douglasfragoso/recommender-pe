package com.recommendersystempe.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
// import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import com.recommendersystempe.dtos.UserDTO;
import com.recommendersystempe.enums.Roles;
import com.recommendersystempe.models.Address;
import com.recommendersystempe.models.User;
import com.recommendersystempe.repositories.UserRepository;
import com.recommendersystempe.service.UserService;
import com.recommendersystempe.service.exception.GeneralException;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private static final Address ADDRESS = new Address(
            "Avenida Central", 250, "Casa 5", "Boa Viagem", "Recife",
            "PE", "Brasil", "01000000");

    private static final UserDTO USER_DTO = new UserDTO(
            "Richard", "Fragoso", 30, "Masculino", "12345678900", "81-98765-4321",
            "richard@example.com", "senha123", ADDRESS);

    private static final User USER = new User(
            "Richard", "Fragoso", 30, "Masculino", "12345678900", "81-98765-4321",
            "richard@example.com", "senha123", ADDRESS, Roles.USER);

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User createUser(Long id, String firstName, String lastName, int age, String gender, String cpf, String phone, String email, String password, Address address, Roles role) {
        User user = new User(firstName, lastName, age, gender, cpf, phone, email, password, address, role);
        ReflectionTestUtils.setField(user, "id", id);
        return user;
    }

    private UserDTO createUserDTO(String firstName, String lastName, int age, String gender, String cpf, String phone, String email, String password, Address address) {
        return new UserDTO(firstName, lastName, age, gender, cpf, phone, email, password, address);
    }

    @Test
    void testInsertUser_ShouldReturnUserDTO() {
        // given / arrange
        given(userRepository.existsByCpf(USER_DTO.getCpf())).willReturn(false);
        given(userRepository.existsByEmail(USER_DTO.getEmail())).willReturn(false);
        given(userRepository.existsByPhone(USER_DTO.getPhone())).willReturn(false);

        given(passwordEncoder.encode(USER_DTO.getUserPassword())).willReturn("encodedPassword");
        given(userRepository.saveAndFlush(any(User.class))).willReturn(USER);

        // when / act
        UserDTO result = userService.insert(USER_DTO);

        // then / assert
        assertAll(
                () -> assertNotNull(result, "UserDTO must not be null"),
                () -> assertEquals(USER_DTO.getFirstName(), result.getFirstName(), "First name must be the same"),
                () -> assertEquals(USER_DTO.getEmail(), result.getEmail(), "Email must be the same")
        );
    }

    @Test
    void testInsertUser_WithExistingCpf_ShouldThrowException() {
        // given / arrange
        given(userRepository.existsByCpf(USER_DTO.getCpf())).willReturn(true);

        // when / then
        assertThrows(GeneralException.class, () -> userService.insert(USER_DTO), "Should throw exception");
    }

    @Test
    void testInsertUser_WithExistingEmail_ShouldThrowException() {
        // given / arrange
        given(userRepository.existsByEmail(USER_DTO.getEmail())).willReturn(true);

        // when / then
        assertThrows(GeneralException.class, () -> userService.insert(USER_DTO), "Should throw exception");
    }

    @Test
    void testInsertUser_WithExistingPhone_ShouldThrowException() {
        // given / arrange
        given(userRepository.existsByPhone(USER_DTO.getPhone())).willReturn(true);

        // when / then
        assertThrows(GeneralException.class, () -> userService.insert(USER_DTO), "Should throw exception");
    }

    @Test
    void testFindAllUsers_ShouldReturnUserPage() {
        // given / arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage = new PageImpl<>(List.of(USER), pageable, 1);

        given(userRepository.findAll(pageable)).willReturn(userPage);

        // when / act
        Page<UserDTO> result = userService.findAll(pageable);

        // then / assert
        assertAll(
                () -> assertNotNull(result, "UserPage must not be null"),
                () -> assertEquals(1, result.getTotalElements(), "UserPage must have 1 element")
        );
    }

    @Test
    void testFindUserById_ShouldReturnUserDTO() {
        // given / arrange
        given(userRepository.findById(anyLong())).willReturn(Optional.of(USER));

        // when / act
        UserDTO result = userService.findById(1L);

        // then / assert
        assertAll(
                () -> assertNotNull(result, "UserDTO must not be null"),
                () -> assertEquals(USER.getFirstName(), result.getFirstName(), "First name must be the same"),
                () -> assertEquals(USER.getEmail(), result.getEmail(), "Email must be the same")
        );
    }

    @Test
    void testUpdateUser_ShouldUpdateUserDetails() {
        // given / arrange
        User user = createUser(1L, "Richard", "Fragoso", 30, "Masculino", "12345678900", "81-98765-4321",
                "richard@example.com", "senha123", ADDRESS, Roles.USER);

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("richard@example.com");
        SecurityContextHolder.getContext().setAuthentication(auth);

        given(userRepository.findByEmail("richard@example.com")).willReturn(user);

        UserDTO userDTO = createUserDTO("John", "Doe", 32, "Feminino", "12345678900", "81-98765-4322",
                "richard@example.com", "senha123", ADDRESS);

        // when / act
        userService.update(null, userDTO);

        // then / assert
        verify(userRepository, times(1)).update(
                1L,
                "John",
                "Doe",
                32,
                "Feminino",
                "81-98765-4322");
    }

    @Test
    void testUpdateUserRole_ShouldUpdateRole() {
        // given / arrange
        User user = createUser(1L, "Richard", "Fragoso", 30, "Masculino", "12345678900", "81-98765-4321",
                "richard@example.com", "senha123", ADDRESS, Roles.USER);

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(userRepository.saveAndFlush(any(User.class))).willReturn(user);

        // when / act
        userService.updateRole(1L);

        // then / assert
        assertAll(
                () -> assertEquals(Roles.ADMIN, user.getRole(), "Role must be ADMIN"),
                () -> verify(userRepository, times(1)).saveAndFlush(user)
        );
    }

    @Test
    void testDeleteUserById_ShouldDeleteUser() {
        // given / arrange
        Long userId = 1L;

        // when / act
        userService.deleteById(userId);

        // then / assert
        verify(userRepository, times(1)).deleteById(userId);
    }
}