package com.recommendersystempe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.recommendersystempe.enums.Status;
import com.recommendersystempe.models.User;
import com.recommendersystempe.repositories.UserRepository;
import com.recommendersystempe.service.exception.GeneralException;

//Usado implicitamente pelo Spring Security para autenticação - Used implicitly by Spring Security for authentication
@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // Permite a busca de um usuário por email - Allows the search for a user by email
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);

        if (user.getStatus() != Status.ACTIVE) {
            throw new GeneralException("User is not active");
        }
        return user;
    }

    public BCryptPasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
