package com.recommendersystempe.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
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

@ExtendWith(MockitoExtension.class) // Anotação que estende o MockitoExtension - Annotation that extends the MockitoExtension 
public class UserServiceTest {

    @Mock // Anotação do Mockito que cria um mock - Mockito annotation that creates a mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks // Anotação do Mockito que injeta os mocks criados - Mockito annotation that injects the created mocks
    private UserService userService;

    private User user;
    private UserDTO userDTO;
    private Address address;

    @BeforeEach
    public void setUp() {
        // given / arrange
        userRepository.deleteAll();
    }

    @Test
    void testGivenValidUserDTO_whenInsert_ThenReturnUserDTO() {
        // given / arrange
        address = new Address(
                "Rua Exemplo", 100, "Apto 202", "Boa Viagem", "Recife",
                "PE", "Brasil", "50000000");

        userDTO = new UserDTO("Douglas", "Fragoso", 30, "Masculino", "12345678900", "81-98765-4321",
                "douglas@example.com",
                "senha123", address);

        // Verificando se já existe um usuário com o CPF, Email ou Telefone informado - Checking if there is already a user with the informed CPF, Email or Phone
        given(userRepository.existsByCpf(userDTO.getCpf())).willReturn(false);
        given(userRepository.existsByEmail(userDTO.getEmail())).willReturn(false);
        given(userRepository.existsByPhone(userDTO.getPhone())).willReturn(false);

        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setAge(userDTO.getAge());
        user.setGender(userDTO.getGender());
        user.setCpf(userDTO.getCpf());
        user.setPhone(userDTO.getPhone());
        user.setEmail(userDTO.getEmail());
        // Criptografando senha
        given(passwordEncoder.encode(userDTO.getUserPassword())).willReturn("encodedPassword");
        user.setUserPassword("encodedPassword"); // A senha criptografada - The encrypted password
        user.setRole(Roles.USER);
        user.setAddress(userDTO.getAddress());

        // Mockando o método save para retornar o usuário com o ID - Mocking the save method to return the user with the ID
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
        address = new Address(
                "Rua Exemplo", 100, "Apto 202", "Boa Viagem", "Recife",
                "PE", "Brasil", "50000000");

        userDTO = new UserDTO("Douglas", "Fragoso", 30, "Masculino", "12345678900", "81-98765-4321",
                "douglas@example.com",
                "senha123", address);

        given(userRepository.existsByCpf(userDTO.getCpf())).willReturn(true);

        // when / then
        assertThrows(GeneralException.class, () -> userService.insert(userDTO));
    }

    @Test
    void testGivenUserWithExistingEmail_whenInsert_ThenThrowGeneralException() {
        // given / arrange
        address = new Address(
                "Rua Exemplo", 100, "Apto 202", "Boa Viagem", "Recife",
                "PE", "Brasil", "50000000");

        userDTO = new UserDTO("Douglas", "Fragoso", 30, "Masculino", "12345678900", "81-98765-4321",
                "douglas@example.com",
                "senha123", address);

        given(userRepository.existsByEmail(userDTO.getEmail())).willReturn(true);

        // when / then
        assertThrows(GeneralException.class, () -> userService.insert(userDTO));
    }

    @Test
    void testGivenUserWithExistingPhone_whenInsert_ThenThrowGeneralException() {
        // given / arrange
        address = new Address(
                "Rua Exemplo", 100, "Apto 202", "Boa Viagem", "Recife",
                "PE", "Brasil", "50000000");

        userDTO = new UserDTO("Douglas", "Fragoso", 30, "Masculino", "12345678900", "81-98765-4321",
                "douglas@example.com",
                "senha123", address);

        given(userRepository.existsByPhone(userDTO.getPhone())).willReturn(true);

        // when / then
        assertThrows(GeneralException.class, () -> userService.insert(userDTO));
    }

    @Test
    void testGivenUserList_whenFindAll_ThenReturnUserPage() {
        // given / arrange
        address = new Address(
                "Rua Exemplo", 100, "Apto 202", "Boa Viagem", "Recife",
                "PE", "Brasil", "50000000");

        user = new User("Douglas", "Fragoso", 30, "Masculino", "12345678900", "81-98765-4321",
                "douglas@example.com", "senha123", address, Roles.USER);
        Address address1 = new Address(
                "Rua Exemplo1", 101, "Apto 203", "Boa Viagem", "Recife",
                "PE", "Brasil", "50000003");

        User user1 = new User("Lucas", "Fragoso", 30, "Masculino", "12345678901", "81-98765-4322", "lucas@example.com",
                "senha123", address1, Roles.USER);

        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage = new PageImpl<>(List.of(user, user1), pageable, 2);

        given(userRepository.findAll(pageable)).willReturn(userPage);

        // when / act
        Page<UserDTO> userList = userService.findAll(pageable);

        // then / assert
        assertNotNull(userList);
        assertEquals(2, userList.getTotalElements());
    }

    @Test
    void testGivenId_whenFindById_ThenReturUserDTO() {
        // given / arrange
        address = new Address(
                "Rua Exemplo", 100, "Apto 202", "Boa Viagem", "Recife",
                "PE", "Brasil", "50000000");

        user = new User("Douglas", "Fragoso", 30, "Masculino", "12345678900", "81-98765-4321",
                "douglas@example.com", "senha123", address, Roles.USER);

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        // when / act
        UserDTO savedPerson = userService.findById(1L);

        // then / assert
        assertNotNull(savedPerson);
        assertEquals("Douglas", savedPerson.getFirstName());
    }

    @Test
    void testGivenPerson_whenUpdate_thenReturnNothing() {
        // given / arrange
        Address address = new Address(
                "Rua Exemplo", 100, "Apto 202", "Boa Viagem", "Recife",
                "PE", "Brasil", "50000000");

        User user = new User("Douglas", "Fragoso", 30, "Masculino", "12345678900", "81-98765-4321",
                "douglas@example.com", "senha123", address, Roles.USER);
        ReflectionTestUtils.setField(user, "id", 1L); // ID definido via reflection - ID set via reflection
 
        // Mockar autenticação - Mock authentication
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("douglas@example.com");
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Mockar repositório
        given(userRepository.findByEmail("douglas@example.com")).willReturn(user);

        // DTO sem ID (não é necessário para o update) - DTO without ID (not necessary for update)
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setAge(32);
        userDTO.setGender("Feminino");
        userDTO.setPhone("81-98765-4322");

        // when / act
        userService.update(userDTO);

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
    void testGivenPersonId_whenUpdateRole_thenReturnNothing() {
        // given / arrange
        Address address = new Address(
                "Rua Exemplo", 100, "Apto 202", "Boa Viagem", "Recife",
                "PE", "Brasil", "50000000");

        User user = new User("Douglas", "Fragoso", 30, "Masculino", "12345678900", "81-98765-4321",
                "douglas@example.com", "senha123", address, Roles.USER);
        ReflectionTestUtils.setField(user, "id", 1L); // ID definido via reflection - ID set via reflection
        given(userRepository.findById(2L)).willReturn(Optional.of(user));

        // 3. Mockar saveAndFlush - Mock saveAndFlush
        given(userRepository.saveAndFlush(any(User.class))).willReturn(user);

        // when / act
        userService.updateRole(2L);

        // then / assert
        // Verifica se o role foi atualizado para ADMIN - Check if the role was updated to ADMIN
        assertEquals(Roles.ADMIN, user.getRole());

        // Verifica se saveAndFlush foi chamado - Check if saveAndFlush was called
        verify(userRepository, times(1)).saveAndFlush(user);
    }

    @Test
    void testGivenUserId_whenDeleteById_thenReturnNothing() {
        // given / arrange
        address = new Address(
                "Rua Exemplo", 100, "Apto 202", "Boa Viagem", "Recife",
                "PE", "Brasil", "50000000");

        user = new User("Douglas", "Fragoso", 30, "Masculino", "12345678900", "81-98765-4321",
                "douglas@example.com", "senha123", address, Roles.USER);
        userRepository.save(user);

        Long userId = 1L;

        // when / act
        userService.deleteById(userId);

        // then / assert
        verify(userRepository, times(1)).deleteById(userId);
    }
}
