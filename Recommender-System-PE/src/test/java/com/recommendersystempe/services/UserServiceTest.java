package com.recommendersystempe.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
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
import com.recommendersystempe.dtos.UserDTOUpdate;
import com.recommendersystempe.enums.Roles;
import com.recommendersystempe.models.Address;
import com.recommendersystempe.models.User;
import com.recommendersystempe.repositories.UserRepository;
import com.recommendersystempe.service.UserService;
import com.recommendersystempe.service.exception.GeneralException;

import jakarta.validation.Validator;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private static final Address ADDRESS = new Address(
            "Avenida Central", 250, "Casa 5", "Boa Viagem", "Recife",
            "PE", "Brasil", "01000000");

    private static final UserDTO USER_DTO = new UserDTO(
            "Richard", "Fragoso", LocalDate.of(1990, 12, 5), "Masculino", "12345678900", "81-98765-4321",
            "richard@example.com", "senha123", ADDRESS);

    private static final User USER = new User(
            "Richard", "Fragoso", LocalDate.of(1990, 12, 5), "Masculino", "12345678900", "81-98765-4321",
            "richard@example.com", "senha123", ADDRESS, Roles.USER);

    @Mock
    private Validator validator;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User createUser(Long id, String firstName, String lastName, LocalDate birthDate, String gender, String cpf,
            String phone, String email, String password, Address address, Roles role) {
        User user = new User(firstName, lastName, birthDate, gender, cpf, phone, email, password, address, role);
        ReflectionTestUtils.setField(user, "id", id);
        return user;
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
                () -> assertEquals(USER_DTO.getEmail(), result.getEmail(), "Email must be the same"));
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
                () -> assertEquals(1, result.getTotalElements(), "UserPage must have 1 element"));
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
                () -> assertEquals(USER.getEmail(), result.getEmail(), "Email must be the same"));
    }

    @Test
    void testUpdateOwnProfile_ShouldUpdateUserDetails() {
        // given / arrange
        User currentUser = createUser(1L, "Richard", "Fragoso", LocalDate.of(1990, 12, 5), "Masculino", "12345678900",
                "81-98765-4321",
                "richard@example.com", "senha123", ADDRESS, Roles.USER);

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("richard@example.com");
        SecurityContextHolder.getContext().setAuthentication(auth);

        given(userRepository.findByEmail("richard@example.com")).willReturn(currentUser);
        given(userRepository.existsByPhoneAndIdNot(anyString(), anyLong())).willReturn(false);
        given(userRepository.save(any(User.class))).willAnswer(invocation -> invocation.getArgument(0));

        UserDTOUpdate userDTOUpdate = new UserDTOUpdate();
        userDTOUpdate.setFirstName("Richard Updated");
        userDTOUpdate.setPhone("81999999999");

        // when / act
        userService.updateOwnProfile(userDTOUpdate);

        // then / assert
        verify(userRepository, times(1)).save(any(User.class));
        assertEquals("Richard Updated", currentUser.getFirstName());
        assertEquals("81999999999", currentUser.getPhone());
    }

    @Test
    void testUpdateUserById_ShouldUpdateUserDetails() {
        // given / arrange
        Long userId = 1L;
        User existingUser = createUser(userId, "John", "Doe", LocalDate.of(1990, 12, 5), "Masculino", "12345678900",
                "81-98765-4321",
                "john@example.com", "senha123", ADDRESS, Roles.USER);

        UserDTOUpdate userDTOUpdate = new UserDTOUpdate();
        userDTOUpdate.setFirstName("John Updated");
        userDTOUpdate.setEmail("john.updated@example.com");

        given(userRepository.findById(userId)).willReturn(Optional.of(existingUser));
        ;
        given(userRepository.save(any(User.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when / act
        userService.updateUserById(userId, userDTOUpdate);

        // then / assert
        verify(userRepository, times(1)).save(any(User.class));
        assertEquals("John Updated", existingUser.getFirstName());
        assertEquals("john.updated@example.com", existingUser.getEmail());
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