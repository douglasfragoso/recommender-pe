package com.recommendersystempe.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.recommendersystempe.dtos.UserDTO;
import com.recommendersystempe.dtos.UserDTOUpdate;
import com.recommendersystempe.enums.Roles;
import com.recommendersystempe.models.Address;
import com.recommendersystempe.models.User;
import com.recommendersystempe.repositories.UserRepository;
import com.recommendersystempe.service.exception.GeneralException;

import jakarta.validation.Validator;;

@Service
public class UserService {

    @Autowired
    private Validator validator;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UserDTO insert(UserDTO dto) {
        // Verificando se já existe um usuário com o CPF, Email ou Telefone informado -
        // Checking if there is already a user with the informed CPF, Email or Phone
        if (userRepository.existsByCpf(dto.getCpf())) {
            throw new GeneralException("CPF already registered");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new GeneralException("Email already registered");
        }
        if (userRepository.existsByPhone(dto.getPhone())) {
            throw new GeneralException("Phone already registered");
        }

        // Mapeando UserDTO para User - Mapping UserDTO to User
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setBirthDate(dto.getBirthDate());
        user.setGender(dto.getGender());
        user.setCpf(dto.getCpf());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());

        // Criptografando senha - encrypting password
        String codePassaword = passwordEncoder.encode(dto.getUserPassword());
        user.setUserPassword(codePassaword);

        // Definindo o papel do usuário - Defining the user's role
        user.setRole(Roles.USER);

        // Mapeando AddressDTO para Address - Mapping AddressDTO to Address
        Address address = dto.getAddress();
        Set<jakarta.validation.ConstraintViolation<Address>> violations = validator.validate(address);
        if (!violations.isEmpty()) {
            String messages = violations.stream()
                    .map(v -> v.getPropertyPath() + " " + v.getMessage())
                    .collect(Collectors.joining(", "));
            throw new jakarta.validation.ConstraintViolationException(
                    "Invalid address: " + messages, violations);
        }
        user.setAddress(address);

        // Salvando User - Saving User
        user = userRepository.saveAndFlush(user);

        // Mapeando User para UserDTO - Mapping User to UserDTO
        return new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getBirthDate(), user.getGender(),
                user.getCpf(), user.getPhone(), user.getEmail(), user.getAddress(), user.getRole(), user.getStatus());
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> findAll(Pageable pageable) {
        Page<User> list = userRepository.findAll(pageable);
        return list.map(x -> new UserDTO(x.getId(), x.getFirstName(), x.getLastName(), x.getBirthDate(), x.getGender(),
                x.getCpf(), x.getPhone(), x.getEmail(), x.getAddress(), x.getRole()));
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new GeneralException("User not found, id does not exist: " + id));
        return new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getBirthDate(), user.getGender(),
                user.getCpf(), user.getPhone(), user.getEmail(), user.getAddress(), user.getRole());
    }

    @Transactional
    public void updateOwnProfile(UserDTOUpdate dto) {
        User currentUser = searchUser();

        if (dto.getPhone() != null && !dto.getPhone().isEmpty() &&
                userRepository.existsByPhoneAndIdNot(dto.getPhone(), currentUser.getId())) {
            throw new GeneralException("Phone already registered by another user");
        }

        if (dto.getFirstName() != null && !dto.getFirstName().isEmpty()) {
            currentUser.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null && !dto.getLastName().isEmpty()) {
            currentUser.setLastName(dto.getLastName());
        }
        if (dto.getBirthDate() != null) { // LocalDate can't be empty string
            currentUser.setBirthDate(dto.getBirthDate());
        }
        if (dto.getGender() != null && !dto.getGender().isEmpty()) {
            currentUser.setGender(dto.getGender());
        }
        if (dto.getPhone() != null && !dto.getPhone().isEmpty()) {
            currentUser.setPhone(dto.getPhone());
        }

        if (dto.getAddress() != null) {
            Address addressToUpdate = currentUser.getAddress();

            if (dto.getAddress().getStreet() != null && !dto.getAddress().getStreet().isEmpty()) {
                addressToUpdate.setStreet(dto.getAddress().getStreet());
            }
            if (dto.getAddress().getNumber() != null && !dto.getAddress().getNumber().equals(0)) {
                addressToUpdate.setNumber(dto.getAddress().getNumber());
            }
            if (dto.getAddress().getComplement() != null && !dto.getAddress().getComplement().isEmpty()) {
                addressToUpdate.setComplement(dto.getAddress().getComplement());
            }
            if (dto.getAddress().getNeighborhood() != null && !dto.getAddress().getNeighborhood().isEmpty()) {
                addressToUpdate.setNeighborhood(dto.getAddress().getNeighborhood());
            }
            if (dto.getAddress().getCity() != null && !dto.getAddress().getCity().isEmpty()) {
                addressToUpdate.setCity(dto.getAddress().getCity());
            }
            if (dto.getAddress().getState() != null && !dto.getAddress().getState().isEmpty()) {
                addressToUpdate.setState(dto.getAddress().getState());
            }
            if (dto.getAddress().getZipCode() != null && !dto.getAddress().getZipCode().isEmpty()) {
                addressToUpdate.setZipCode(dto.getAddress().getZipCode());
            }
        }

        userRepository.save(currentUser);
    }

    @Transactional
    public void updateUserById(Long id, UserDTOUpdate dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new GeneralException("User not found, id does not exist: " + id));

        if (dto.getEmail() != null && !dto.getEmail().isEmpty() &&
                userRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
            throw new GeneralException("Email already registered by another user");
        }
        if (dto.getPhone() != null && !dto.getPhone().isEmpty() &&
                userRepository.existsByPhoneAndIdNot(dto.getPhone(), id)) {
            throw new GeneralException("Phone already registered by another user");
        }
        if (dto.getCpf() != null && !dto.getCpf().isEmpty() &&
                userRepository.existsByCpfAndIdNot(dto.getCpf(), id)) {
            throw new GeneralException("CPF already registered by another user");
        }

        if (dto.getFirstName() != null && !dto.getFirstName().isEmpty()) {
            user.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null && !dto.getLastName().isEmpty()) {
            user.setLastName(dto.getLastName());
        }
        if (dto.getBirthDate() != null) {
            user.setBirthDate(dto.getBirthDate());
        }
        if (dto.getGender() != null && !dto.getGender().isEmpty()) {
            user.setGender(dto.getGender());
        }
        if (dto.getCpf() != null && !dto.getCpf().isEmpty()) {
            user.setCpf(dto.getCpf());
        }
        if (dto.getPhone() != null && !dto.getPhone().isEmpty()) {
            user.setPhone(dto.getPhone());
        }
        if (dto.getEmail() != null && !dto.getEmail().isEmpty()) {
            user.setEmail(dto.getEmail());
        }
        if (dto.getRole() != null) {
            user.setRole(dto.getRole());
        }
        if (dto.getStatus() != null) {
            user.setStatus(dto.getStatus());
        }

        userRepository.save(user);
    }

    @Transactional
    public void deleteById(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new GeneralException("Cannot delete user, it has related data");
        }

    }

    private User searchUser() {
        Authentication autenticado = SecurityContextHolder.getContext().getAuthentication();
        if (!(autenticado instanceof AnonymousAuthenticationToken)) {
            String userEmail = autenticado.getName();
            User user = userRepository.findByEmail(userEmail);
            if (user == null) {
                throw new GeneralException("User not found in database");
            }
            return user;
        }
        throw new GeneralException("User not authenticated");
    }

}
