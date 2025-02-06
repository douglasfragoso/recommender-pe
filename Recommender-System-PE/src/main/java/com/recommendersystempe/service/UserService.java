package com.recommendersystempe.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.recommendersystempe.dto.UserDTO;
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

    @Autowired
    private ModelMapper modelMapper;

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
        User user = modelMapper.map(dto, User.class);
       
        //criptografando senha
        String codePassaword = passwordEncoder.encode(dto.getUserPassword());
        user.setUserPassword(codePassaword);

        //definindo o papel do usuário
        user.setRole(Roles.USER);

        // Mapeando AddressDTO para Address
        Address address = dto.getAddress();
        user.setAddress(address);

        // Salvando User
        user = userRepository.save(user);

        // Mapeando User para UserDTO
        return modelMapper.map(user, UserDTO.class);              
    }
}
