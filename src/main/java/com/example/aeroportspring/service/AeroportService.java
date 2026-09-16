package com.example.aeroportspring.service;

import com.example.aeroportspring.model.Aeroport;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AeroportService {

    private final List<Aeroport> aeroports = new ArrayList<>();

    public List<Aeroport> getAeroports() {
        return this.aeroports;
    }

    public Optional<Aeroport> getAeroport(int id) {
        return this.aeroports.stream()
                .filter(a -> a.getId() == id)
                .findFirst();
    }

    public Aeroport creerAeroport(Aeroport aeroport) {
        this.aeroports.add(aeroport);
        return aeroport;
    }

    public Optional<Aeroport> modifierAeroport(int id, Aeroport nouvellesInfos) {
        return getAeroport(id).map(aeroport -> {
            aeroport.setNom(nouvellesInfos.getNom());
            aeroport.setAdresse(nouvellesInfos.getAdresse());
            aeroport.setPays(nouvellesInfos.getPays());
            aeroport.setUTC(nouvellesInfos.getUTC());
            return aeroport;
        });
    }

    public boolean supprimerAeroport(int id) {
        return this.aeroports.removeIf(a -> a.getId() == id);
    }
}
