package com.example.aeroportspring.service;

import com.example.aeroportspring.model.Avion;
import com.example.aeroportspring.model.Compagnie;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Service
public class AvionService {

    private final List<Avion> avions = new ArrayList<>();

    public Optional<Avion> getAvion(int id){
        return this.avions.stream()
                .filter(avion -> avion.getId() == id)
                .findFirst();
    }

    public Avion creerAvion(Avion avion){
        this.avions.add(avion);
        return avion;
    }

    public Optional<Avion> modifierAvion(int id, Avion newAvion){
        return getAvion(id).map(avion -> {
            avion.setModeleAvion(newAvion.getModeleAvion());
            avion.setCompagnie(newAvion.getCompagnie());
            avion.setEnVol(newAvion.isEnVol());
            avion.setCapacite(newAvion.getCapacite());
            return avion;
        });
    }

    public boolean supprimerAvion(int id) {
        return this.avions.removeIf(avion -> avion.getId() == id);
    }

    public Optional<Avion> modifierCompagnieAvion(int id, Compagnie newCompagnie){
        return getAvion(id).map(avion -> {
            avion.setCompagnie(newCompagnie);
            return avion;
        });
    }

    public Optional<Avion> modifierIsVolAvion(int id, boolean enVol){
        return getAvion(id).map(avion -> {
            avion.setEnVol(enVol);
            return avion;
        });
    }
}
