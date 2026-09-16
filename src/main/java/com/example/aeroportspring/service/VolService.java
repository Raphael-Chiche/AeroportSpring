package com.example.aeroportspring.service;

import com.example.aeroportspring.model.Vol;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VolService {

    private final List<Vol> vols = new ArrayList<>();

    public List<Vol> getVols() {
        return this.vols;
    }

    public Optional<Vol> getVol(int id) {
        return this.vols.stream()
                .filter(x -> x.getId() == id)
                .findFirst();
    }

    public Vol creerVol(Vol vol) {
        this.vols.add(vol);
        return vol;
    }

    public Optional<Vol> modifierVol(int id, Vol nouvellesInfos) {
        return getVol(id).map(vol -> {
            vol.setCompagnie(nouvellesInfos.getCompagnie());
            vol.setDateDepart(nouvellesInfos.getDateDepart());
            vol.setDateArrivee(nouvellesInfos.getDateArrivee());
            vol.setPrix(nouvellesInfos.getPrix());
            vol.setDuree(nouvellesInfos.getDuree());
            return vol;
        });
    }

    public boolean supprimerVol(int id) {
        return this.vols.removeIf(x -> x.getId() == id);
    }
}
