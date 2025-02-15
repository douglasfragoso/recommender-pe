package com.recommendersystempe.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.recommendersystempe.dtos.UserDTO;
import com.recommendersystempe.enums.Roles;
import com.recommendersystempe.models.Address;
import com.recommendersystempe.models.User;
import com.recommendersystempe.repositories.UserRepository;
import com.recommendersystempe.service.UserService;
import com.recommendersystempe.service.exception.GeneralException;

@ExtendWith(MockitoExtension.class) // annotation that extends the MockitoExtension
public class UserServiceTest {

    @Mock // anotação do Mockito que cria um mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks // anotação do Mockito que injeta os mocks criados
    private UserService userService;

    private UserDTO userDTO;
    private Address address;

    @BeforeEach
    public void setUp() {
        // given / arrange
        userRepository.deleteAll();

        address = new Address(
                "Rua Exemplo", 100, "Apto 202", "Boa Viagem",
                "PE", "Brasil", "50000000");

        userDTO = new UserDTO("Douglas", "Fragoso", 30, "Masculino", "12345678900", "81-98765-4321",
                "douglas@example.com",
                "senha123", address);
    }

    @Test
    void testGivenValidUserDTO_whenInsert_ThenReturnUserDTO() {
        // given / arrange
        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setAge(userDTO.getAge());
        user.setGender(userDTO.getGender());
        user.setCpf(userDTO.getCpf());
        user.setPhone(userDTO.getPhone());
        user.setEmail(userDTO.getEmail());
        user.setUserPassword("encodedPassword"); // A senha criptografada
        user.setRole(Roles.USER);
        user.setAddress(userDTO.getAddress());

        // Verificando se já existe um usuário com o CPF, Email ou Telefone informado
        given(userRepository.existsByCpf(userDTO.getCpf())).willReturn(false);
        given(userRepository.existsByEmail(userDTO.getEmail())).willReturn(false);
        given(userRepository.existsByPhone(userDTO.getPhone())).willReturn(false);

        // Criptografando senha
        given(passwordEncoder.encode(userDTO.getUserPassword())).willReturn("encodedPassword");

        // Mockando o método save para retornar o usuário com o ID
        given(userRepository.saveAndFlush(any(User.class))).willReturn(user);

        // when / act
        UserDTO result = userService.insert(userDTO);

        // then / assert
        assertNotNull(result);
        assertEquals(userDTO.getFirstName(), result.getFirstName());
        assertEquals(userDTO.getEmail(), result.getEmail());
    }

    @Test
    void testGivenUserWithExistingCpf_whenInsert_ThenThrowGeneralException() {
        // given / arrange
        given(userRepository.existsByCpf(userDTO.getCpf())).willReturn(true);

        // when / then
        assertThrows(GeneralException.class, () -> userService.insert(userDTO));
    }

    @Test
    void testGivenUserWithExistingEmail_whenInsert_ThenThrowGeneralException() {
        // given / arrange
        given(userRepository.existsByEmail(userDTO.getEmail())).willReturn(true);

        // when / then
        assertThrows(GeneralException.class, () -> userService.insert(userDTO));
    }

    @Test
    void testGivenUserWithExistingPhone_whenInsert_ThenThrowGeneralException() {
        // given / arrange
        given(userRepository.existsByPhone(userDTO.getPhone())).willReturn(true);

        // when / then
        assertThrows(GeneralException.class, () -> userService.insert(userDTO));
    }

}
