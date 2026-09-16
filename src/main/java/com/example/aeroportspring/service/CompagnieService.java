package com.example.aeroportspring.service;

import com.example.aeroportspring.model.Compagnie;
import com.example.aeroportspring.repository.CompagnieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompagnieService {

    // Le repository remplace la List : les compagnies sont stockees dans la base H2
    private final CompagnieRepository compagnieRepository;

    public CompagnieService(CompagnieRepository compagnieRepository) {
        this.compagnieRepository = compagnieRepository;
    }

    public List<Compagnie> getCompagnies() {
        return compagnieRepository.findAll();
    }

    public Optional<Compagnie> getCompagnie(int id) {
        return compagnieRepository.findById(id);
    }

    public Compagnie creerCompagnie(Compagnie compagnie) {
        return compagnieRepository.save(compagnie); // INSERT : l'id est rempli par la base
    }

    public Optional<Compagnie> modifierCompagnie(int id, Compagnie nouvellesInfos) {
        return compagnieRepository.findById(id).map(compagnie -> {
            compagnie.setNom(nouvellesInfos.getNom());
            return compagnieRepository.save(compagnie); // UPDATE : obligatoire pour enregistrer le changement
        });
    }

    public boolean supprimerCompagnie(int id) {
        if (!compagnieRepository.existsById(id)) {
            return false;
        }
        compagnieRepository.deleteById(id);
        return true;
    }
}
