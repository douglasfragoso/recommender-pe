package com.recommendersystempe.service;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.recommendersystempe.dtos.POIDTO;
import com.recommendersystempe.dtos.PreferencesDTO;
import com.recommendersystempe.enums.Hobbies;
import com.recommendersystempe.enums.Motivations;
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

    @Autowired
    private RecommendationService recommendationService;

    @Transactional
    public List<POIDTO> insert(PreferencesDTO dto) {
        User user = searchUser();

        Preferences preferences = new Preferences();
        preferences.setUser(user);
        preferences.setDate(Instant.now());
        preferences.setCurrentLocation(dto.getCurrentLocation());

        // Salvando as preferências no banco - Saving preferences in the database
        preferencesRepository.save(preferences);

        // Adicionando as motivações - Adding motivations 
        List<Motivations> motivations = dto.getMotivations();
        preferences.addMotivation(motivations);

        // Adicionando os hobbies - Adding hobbies
        List<Hobbies> hobbies = dto.getHobbies();
        preferences.addHobbies(hobbies);

        // Adicionando os temas - Adding themes
        List<Themes> themes = dto.getThemes();
        preferences.addTheme(themes);

        // Salvando as alterações após adicionar as listas - Saving the changes after adding the lists
        preferencesRepository.save(preferences);

        // Retornando o DTO com as informações atualizadas - Returning the DTO with the updated information
        return recommendationService.recommendation(preferences);
    }

    @Transactional(readOnly = true)
    public Page<PreferencesDTO> findAll(Pageable pageable) {
        Page<Preferences> list = preferencesRepository.findAll(pageable);
        return list.map(x -> new PreferencesDTO(x.getId(), x.getUser().getId(), x.getDate(),
        x.getMotivations(), x.getHobbies(), x.getThemes(), x.getCurrentLocation()));
    }

    @Transactional(readOnly = true)
    public PreferencesDTO findById(Long id) {
        Preferences preferences = preferencesRepository.findById(id)
                .orElseThrow(() -> new GeneralException("Preferences not found, id does not exist: " + id));
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
