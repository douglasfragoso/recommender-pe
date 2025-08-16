package com.recommendersystempe.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.recommendersystempe.dtos.POIDTO;
import com.recommendersystempe.dtos.POIDTOUpdate;
import com.recommendersystempe.enums.Hobbies;
import com.recommendersystempe.enums.Motivations;
import com.recommendersystempe.enums.Themes;
import com.recommendersystempe.models.Address;
import com.recommendersystempe.models.POI;
import com.recommendersystempe.repositories.POIRepository;
import com.recommendersystempe.service.exception.GeneralException;

@Service
public class POIService {

    @Autowired
    private POIRepository poiRepository;

    @Transactional
    public POIDTO insert(POIDTO dto) {
        // Mapeando POIDTO para POI - Mapping POIDTO to POI
        POI poi = new POI();
        poi.setName(dto.getName());
        poi.setDescription(dto.getDescription());
        poi.setAddress(dto.getAddress());

        // Salvando o POI no banco - Saving the POI in the database
        poiRepository.save(poi);

        // Adicionando as motivações - Adding motivations
        List<Motivations> motivations = dto.getMotivations();
        poi.addMotivation(motivations);

        // Adicionando os hobbies - Adding hobbies
        List<Hobbies> hobbies = dto.getHobbies();
        poi.addHobbies(hobbies);

        // Adicionando os temas - Adding themes
        List<Themes> themes = dto.getThemes();
        poi.addTheme(themes);

        // Salvando as alterações após adicionar as listas - Saving the changes after
        // adding the lists
        poiRepository.save(poi);

        return new POIDTO(poi.getId(), poi.getName(), poi.getDescription(), poi.getMotivations(), poi.getHobbies(),
                poi.getThemes(), poi.getAddress());
    }

    @Transactional(readOnly = true)
    public Page<POIDTO> findAll(Pageable pageable) {
        Page<POI> list = poiRepository.findAll(pageable);
        return list.map(x -> new POIDTO(x.getId(), x.getName(), x.getDescription(), x.getMotivations(), x.getHobbies(),
                x.getThemes(), x.getAddress()));
    }

    @Transactional(readOnly = true)
    public POIDTO findById(Long id) {
        POI poi = poiRepository.findById(id)
                .orElseThrow(() -> new GeneralException("POI not found, id does not exist: " + id));
        return new POIDTO(poi.getId(), poi.getName(), poi.getDescription(), poi.getMotivations(), poi.getHobbies(),
                poi.getThemes(), poi.getAddress());
    }

    @Transactional
    public void update(Long id, POIDTOUpdate dto) {
        POI poiToUpdate = poiRepository.findById(id)
                .orElseThrow(() -> new GeneralException("POI not found, id does not exist: " + id));

        if (dto.getName() != null && poiRepository.existsByNameAndIdNot(dto.getName(), id)) {
            throw new GeneralException("POI name already in use by another record.");
        }

        if (dto.getName() != null) {
            poiToUpdate.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            poiToUpdate.setDescription(dto.getDescription());
        }
        if (dto.getAddress() != null) {
            Address addressToUpdate = poiToUpdate.getAddress();

            if (dto.getAddress().getStreet() != null) {
                addressToUpdate.setStreet(dto.getAddress().getStreet());
            }
            if (dto.getAddress().getNumber() != null) {
                addressToUpdate.setNumber(dto.getAddress().getNumber());
            }
            if (dto.getAddress().getComplement() != null) {
                addressToUpdate.setComplement(dto.getAddress().getComplement());
            }
            if (dto.getAddress().getNeighborhood() != null) {
                addressToUpdate.setNeighborhood(dto.getAddress().getNeighborhood());
            }
            if (dto.getAddress().getCity() != null) {
                addressToUpdate.setCity(dto.getAddress().getCity());
            }
            if (dto.getAddress().getState() != null) {
                addressToUpdate.setState(dto.getAddress().getState());
            }
            if (dto.getAddress().getZipCode() != null) {
                addressToUpdate.setZipCode(dto.getAddress().getZipCode());
            }
        }

        if (dto.getMotivations() != null && !dto.getMotivations().isEmpty()) {
            poiToUpdate.getMotivations().clear();
            poiToUpdate.getMotivations().addAll(dto.getMotivations());
        }
        if (dto.getHobbies() != null && !dto.getHobbies().isEmpty()) {
            poiToUpdate.getHobbies().clear();
            poiToUpdate.getHobbies().addAll(dto.getHobbies());
        }
        if (dto.getThemes() != null && !dto.getThemes().isEmpty()) {
            poiToUpdate.getThemes().clear();
            poiToUpdate.getThemes().addAll(dto.getThemes());
        }
        poiRepository.save(poiToUpdate);
    }

}
