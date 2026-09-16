package com.example.aeroportspring.service;

import com.example.aeroportspring.model.Bagage;
import com.example.aeroportspring.model.Passager;
import com.example.aeroportspring.repository.PassagerRepository;
import com.example.aeroportspring.repository.VolRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PassagerService {

    private final PassagerRepository passagerRepository;
    private final VolRepository volRepository;

    public PassagerService(PassagerRepository passagerRepository, VolRepository volRepository) {
        this.passagerRepository = passagerRepository;
        this.volRepository = volRepository;
    }

    public List<Passager> getPassagers() {
        return passagerRepository.findAll();
    }

    public Optional<Passager> getPassager(int id) {
        return passagerRepository.findById(id);
    }

    public Passager creerPassager(Passager passager) {
        return passagerRepository.save(passager);
    }

    public Optional<Passager> modifierPassager(int id, Passager nouvellesInfos) {
        return passagerRepository.findById(id).map(passager -> {
            passager.setNom(nouvellesInfos.getNom());
            passager.setPrenom(nouvellesInfos.getPrenom());
            passager.setPasseport(nouvellesInfos.getPasseport());
            passager.setBagage(nouvellesInfos.getBagage());
            return passagerRepository.save(passager);
        });
    }

    // On retire d'abord le passager de ses vols, sinon la table vol_passager bloque la suppression
    @Transactional
    public boolean supprimerPassager(int id) {
        Optional<Passager> passager = passagerRepository.findById(id);
        if (passager.isEmpty()) {
            return false;
        }
        volRepository.findByPassagers_Id(id).forEach(vol -> {
            vol.retirerPassager(passager.get());
            volRepository.save(vol);
        });
        passagerRepository.delete(passager.get());
        return true;
    }

    public Optional<Passager> modifierPasseport(int id, boolean passeport) {
        return passagerRepository.findById(id).map(passager -> {
            passager.setPasseport(passeport);
            return passagerRepository.save(passager);
        });
    }

    public Optional<Passager> modifierBagage(int id, Bagage bagage) {
        return passagerRepository.findById(id).map(passager -> {
            passager.setBagage(bagage);
            return passagerRepository.save(passager);
        });
    }
}
