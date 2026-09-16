package com.example.aeroportspring.service;

import com.example.aeroportspring.model.Avion;
import com.example.aeroportspring.repository.AvionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AvionService {

    private final AvionRepository avionRepository;
    private final CompagnieService compagnieService;

    public AvionService(AvionRepository avionRepository, CompagnieService compagnieService){
        this.avionRepository = avionRepository;
        this.compagnieService = compagnieService;
    }

    public List<Avion> getAvions(){
        return avionRepository.findAll();
    }

    public Optional<Avion> getAvion(int id){
        return avionRepository.findById(id);
    }

    public Avion creerAvion(Avion avion){
        return avionRepository.save(avion);
    }

    public Optional<Avion> modifierAvion(int id, Avion newAvion){
        return avionRepository.findById(id).map(avion -> {
            avion.setModeleAvion(newAvion.getModeleAvion());
            avion.setCompagnie(newAvion.getCompagnie());
            avion.setEnVol(newAvion.isEnVol());
            avion.setCapacite(newAvion.getCapacite());
            return avionRepository.save(avion);
        });
    }

    public boolean supprimerAvion(int id) {
        if(!avionRepository.existsById(id)){
            return false;
        }
        avionRepository.deleteById(id);
        return true;
    }

    // On recoit l'id de la compagnie et on va la chercher en base :
    // on est sur qu'elle existe, sinon le save planterait (compagnie non enregistree)
    public Optional<Avion> modifierCompagnieAvion(int id, int compagnieId){
        return avionRepository.findById(id).flatMap(avion ->
                compagnieService.getCompagnie(compagnieId).map(compagnie -> {
                    avion.setCompagnie(compagnie);
                    return avionRepository.save(avion);
                }));
    }

    public Optional<Avion> modifierIsVolAvion(int id, boolean enVol){
        return avionRepository.findById(id).map(avion -> {
            avion.setEnVol(enVol);
            return avionRepository.save(avion);
        });
    }
}
