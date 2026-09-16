package com.example.aeroportspring.service;

import com.example.aeroportspring.model.Personnel;
import com.example.aeroportspring.model.Profession;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PersonnelService {

    private final List<Personnel> personnels = new ArrayList<>();

    public List<Personnel> getPersonnels() {
        return this.personnels;
    }

    public Optional<Personnel> getPersonnel(int id) {
        return this.personnels.stream()
                .filter(x -> x.getId() == id)
                .findFirst();
    }

    public Personnel creerPersonnel(Personnel personnel) {
        this.personnels.add(personnel);
        return personnel;
    }

    public Optional<Personnel> modifierPersonnel(int id, Personnel nouvellesInfos) {
        return getPersonnel(id).map(personnel -> {
            personnel.setNom(nouvellesInfos.getNom());
            personnel.setPrenom(nouvellesInfos.getPrenom());
            personnel.setProfession(nouvellesInfos.getProfession());
            return personnel;
        });
    }

    public boolean supprimerPersonnel(int id) {
        return this.personnels.removeIf(x -> x.getId() == id);
    }

    public Optional<Personnel> modifierProfession(int id, Profession profession) {
        return getPersonnel(id).map(personnel -> {
            personnel.setProfession(profession);
            return personnel;
        });
    }
}
