package com.example.aeroportspring.service;

import com.example.aeroportspring.model.Personnel;
import com.example.aeroportspring.model.Profession;
import com.example.aeroportspring.repository.AeroportRepository;
import com.example.aeroportspring.repository.PersonnelRepository;
import com.example.aeroportspring.repository.VolRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PersonnelService {

    private final PersonnelRepository personnelRepository;
    private final VolRepository volRepository;
    private final AeroportRepository aeroportRepository;

    public PersonnelService(PersonnelRepository personnelRepository, VolRepository volRepository,
                            AeroportRepository aeroportRepository) {
        this.personnelRepository = personnelRepository;
        this.volRepository = volRepository;
        this.aeroportRepository = aeroportRepository;
    }

    public List<Personnel> getPersonnels() {
        return personnelRepository.findAll();
    }

    public Optional<Personnel> getPersonnel(int id) {
        return personnelRepository.findById(id);
    }

    public Personnel creerPersonnel(Personnel personnel) {
        return personnelRepository.save(personnel);
    }

    public Optional<Personnel> modifierPersonnel(int id, Personnel nouvellesInfos) {
        return personnelRepository.findById(id).map(personnel -> {
            personnel.setNom(nouvellesInfos.getNom());
            personnel.setPrenom(nouvellesInfos.getPrenom());
            personnel.setProfession(nouvellesInfos.getProfession());
            return personnelRepository.save(personnel);
        });
    }

    // On retire d'abord le personnel de ses vols et aeroports (tables vol_personnel et aeroport_personnel)
    @Transactional
    public boolean supprimerPersonnel(int id) {
        Optional<Personnel> personnel = personnelRepository.findById(id);
        if (personnel.isEmpty()) {
            return false;
        }
        volRepository.findByPersonnels_Id(id).forEach(vol -> {
            vol.retirerPersonnel(personnel.get());
            volRepository.save(vol);
        });
        aeroportRepository.findByPersonnels_Id(id).forEach(aeroport -> {
            aeroport.retirerPersonnel(personnel.get());
            aeroportRepository.save(aeroport);
        });
        personnelRepository.delete(personnel.get());
        return true;
    }

    public Optional<Personnel> modifierProfession(int id, Profession profession) {
        return personnelRepository.findById(id).map(personnel -> {
            personnel.setProfession(profession);
            return personnelRepository.save(personnel);
        });
    }
}
