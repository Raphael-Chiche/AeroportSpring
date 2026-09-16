package com.example.aeroportspring.service;

import com.example.aeroportspring.model.Bagage;
import com.example.aeroportspring.model.Passager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PassagerService {

    private final List<Passager> passagers = new ArrayList<>();

    public List<Passager> getPassagers() {
        return this.passagers;
    }

    public Optional<Passager> getPassager(int id) {
        return this.passagers.stream()
                .filter(x -> x.getId() == id)
                .findFirst();
    }

    public Passager creerPassager(Passager passager) {
        this.passagers.add(passager);
        return passager;
    }

    public Optional<Passager> modifierPassager(int id, Passager nouvellesInfos) {
        return getPassager(id).map(passager -> {
            passager.setNom(nouvellesInfos.getNom());
            passager.setPrenom(nouvellesInfos.getPrenom());
            passager.setPasseport(nouvellesInfos.getPasseport());
            passager.setBagage(nouvellesInfos.getBagage());
            return passager;
        });
    }

    public boolean supprimerPassager(int id) {
        return this.passagers.removeIf(x -> x.getId() == id);
    }

    public Optional<Passager> modifierPasseport(int id, boolean passeport) {
        return getPassager(id).map(passager -> {
            passager.setPasseport(passeport);
            return passager;
        });
    }

    public Optional<Passager> modifierBagage(int id, Bagage bagage) {
        return getPassager(id).map(passager -> {
            passager.setBagage(bagage);
            return passager;
        });
    }
}
