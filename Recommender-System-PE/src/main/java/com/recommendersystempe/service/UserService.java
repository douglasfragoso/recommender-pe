package com.recommendersystempe.service;

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
import com.recommendersystempe.enums.Roles;
import com.recommendersystempe.models.Address;
import com.recommendersystempe.models.User;
import com.recommendersystempe.repositories.UserRepository;
import com.recommendersystempe.service.exception.GeneralException;;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UserDTO insert(UserDTO dto) {
        // Verificando se já existe um usuário com o CPF, Email ou Telefone informado
        if (userRepository.existsByCpf(dto.getCpf())) {
            throw new GeneralException("CPF already registered");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new GeneralException("Email already registered");
        }
        if (userRepository.existsByPhone(dto.getPhone())) {
            throw new GeneralException("Phone already registered");
        }

        // Mapeando UserDTO para User
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setAge(dto.getAge());
        user.setGender(dto.getGender());
        user.setCpf(dto.getCpf());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());

        // criptografando senha
        String codePassaword = passwordEncoder.encode(dto.getUserPassword());
        user.setUserPassword(codePassaword);

        // definindo o papel do usuário
        user.setRole(Roles.USER);

        // Mapeando AddressDTO para Address
        Address address = dto.getAddress();
        user.setAddress(address);

        // Salvando User
        user = userRepository.saveAndFlush(user);

        // Mapeando User para UserDTO
        return new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getAge(), user.getGender(),
                user.getCpf(), user.getPhone(), user.getEmail(), user.getAddress(), user.getRole());
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> findAll(Pageable pageable) {
        Page<User> list = userRepository.findAll(pageable);
        return list.map(x -> new UserDTO(x.getId(), x.getFirstName(), x.getLastName(), x.getAge(), x.getGender(),
                x.getCpf(), x.getPhone(), x.getEmail(), x.getAddress(), x.getRole()));
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new GeneralException("User not found, id does not exist: " + id));
        return new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getAge(), user.getGender(),
                user.getCpf(), user.getPhone(), user.getEmail(), user.getAddress(), user.getRole());
    }

    @Transactional
    public void update(UserDTO dto) {
        User user = searchUser();
        Long idUser = user.getId();

        userRepository.update(idUser, dto.getFirstName(), dto.getLastName(), dto.getAge(), dto.getGender(),
                dto.getPhone());
    }

    @Transactional
    public void updateRole(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new GeneralException("User not found, id does not exist: " + id));
        user.setRole(Roles.ADMIN);
        userRepository.saveAndFlush(user);
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
