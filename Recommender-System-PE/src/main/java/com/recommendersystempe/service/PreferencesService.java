package com.recommendersystempe.service;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.recommendersystempe.dtos.PreferencesDTO;
import com.recommendersystempe.enums.Hobbies;
import com.recommendersystempe.enums.Motivation;
import com.recommendersystempe.enums.Themes;
import com.recommendersystempe.models.Preferences;
import com.recommendersystempe.models.User;
import com.recommendersystempe.repositories.PreferencesRepository;
import com.recommendersystempe.repositories.UserRepository;
import com.recommendersystempe.service.exception.GeneralException;

@Service
public class PreferencesService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PreferencesRepository preferencesRepository;

    @Transactional
    public PreferencesDTO insert(PreferencesDTO dto) {
        User user = searchUser();

        Preferences preferences = new Preferences();
        preferences.setUser(user);
        preferences.setDate(Instant.now());
        preferences.setCurrentLocation(dto.getCurrentLocation());

        // Salvando as preferências no banco
        preferencesRepository.save(preferences);

        // Adicionando as motivações
        List<Motivation> motivations = dto.getMotivation();
        preferences.addMotivation(motivations);

        // Adicionando os hobbies
        List<Hobbies> hobbies = dto.getHobbies();
        preferences.addHobbie(hobbies);

        // Adicionando os temas
        List<Themes> themes = dto.getThemes();
        preferences.addTheme(themes);

        // Salvando as alterações após adicionar as listas
        preferencesRepository.save(preferences);

        // Retornando o DTO com as informações atualizadas
        return new PreferencesDTO(preferences.getId(), preferences.getUser().getId(), preferences.getDate(),
                preferences.getMotivations(), preferences.getHobbies(), preferences.getThemes(), preferences.getCurrentLocation());
    }

    private User searchUser() {
        Authentication authenticated = SecurityContextHolder.getContext().getAuthentication();
        if (!(authenticated instanceof AnonymousAuthenticationToken)) {
            String userEmail = authenticated.getName();
            User user = userRepository.findByEmail(userEmail);
            if (user == null) {
                throw new GeneralException("User not found in database");
            }
            return user;
        }
        throw new GeneralException("User not authenticated");
    }
}
