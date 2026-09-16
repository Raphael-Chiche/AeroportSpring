package com.example.aeroportspring.service;

import com.example.aeroportspring.model.Compagnie;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CompagnieService {

    private final List<Compagnie> compagnies = new ArrayList<>();

    public List<Compagnie> getCompagnies() {
        return this.compagnies;
    }

    public Optional<Compagnie> getCompagnie(int id) {
        return this.compagnies.stream()
                .filter(x -> x.getId() == id)
                .findFirst();
    }

    public Compagnie creerCompagnie(Compagnie compagnie) {
        this.compagnies.add(compagnie);
        return compagnie;
    }

    public Optional<Compagnie> modifierCompagnie(int id, Compagnie nouvellesInfos) {
        return getCompagnie(id).map(compagnie -> {
            compagnie.setNom(nouvellesInfos.getNom());
            return compagnie;
        });
    }

    public boolean supprimerCompagnie(int id) {
        return this.compagnies.removeIf(x -> x.getId() == id);
    }
}
