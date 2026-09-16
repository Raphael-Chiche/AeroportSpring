package com.example.aeroportspring.service;

import com.example.aeroportspring.model.Vol;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VolService {

    private final List<Vol> vols = new ArrayList<>();

    private final PassagerService passagerService;
    private final PersonnelService personnelService;
    private final TerminalService terminalService;
    private final AvionService avionService;
    private final CompagnieService compagnieService;
    private final AeroportService aeroportService;

    public VolService(PassagerService passagerService, PersonnelService personnelService,
                      TerminalService terminalService, AvionService avionService,
                      CompagnieService compagnieService, AeroportService aeroportService) {
        this.passagerService = passagerService;
        this.personnelService = personnelService;
        this.terminalService = terminalService;
        this.avionService = avionService;
        this.compagnieService = compagnieService;
        this.aeroportService = aeroportService;
    }

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

    // ----- Passagers -----

    // Les Optional vides signifient "vol ou passager introuvable" (-> 404).
    // Les regles metier non respectees levent une IllegalStateException (-> 409).
    public Optional<Vol> ajouterPassager(int volId, int passagerId) {
        return getVol(volId).flatMap(vol -> passagerService.getPassager(passagerId).map(passager -> {
            if (vol.getPassagers().contains(passager)) {
                return vol;
            }
            if (!Boolean.TRUE.equals(passager.getPasseport())) {
                throw new IllegalStateException("Le passager " + passagerId + " n'a pas de passeport");
            }
            if (vol.getAvion() != null && vol.getNombrePassagers() >= vol.getAvion().getCapacite()) {
                throw new IllegalStateException("Le vol " + volId + " est complet");
            }
            passager.ajouterVol(vol);
            return vol;
        }));
    }

    public Optional<Vol> retirerPassager(int volId, int passagerId) {
        return getVol(volId).flatMap(vol -> passagerService.getPassager(passagerId).map(passager -> {
            passager.retirerVol(vol);
            return vol;
        }));
    }

    public List<Vol> getVolsDuPassager(int passagerId) {
        return this.vols.stream()
                .filter(vol -> vol.getPassagers().stream().anyMatch(p -> p.getId() == passagerId))
                .toList();
    }

    // ----- Personnels -----

    public Optional<Vol> ajouterPersonnel(int volId, int personnelId) {
        return getVol(volId).flatMap(vol -> personnelService.getPersonnel(personnelId).map(personnel -> {
            vol.ajouterPersonnel(personnel);
            return vol;
        }));
    }

    public Optional<Vol> retirerPersonnel(int volId, int personnelId) {
        return getVol(volId).flatMap(vol -> personnelService.getPersonnel(personnelId).map(personnel -> {
            vol.retirerPersonnel(personnel);
            return vol;
        }));
    }

    public List<Vol> getVolsDuPersonnel(int personnelId) {
        return this.vols.stream()
                .filter(vol -> vol.getPersonnels().stream().anyMatch(p -> p.getId() == personnelId))
                .toList();
    }

    // ----- Terminal de depart -----

    public Optional<Vol> affecterTerminal(int volId, int terminalId) {
        return getVol(volId).flatMap(vol -> terminalService.getTerminal(terminalId).map(terminal -> {
            if (terminal.getVol() != null && terminal.getVol() != vol) {
                throw new IllegalStateException("Le terminal " + terminalId + " est deja occupe par le vol "
                        + terminal.getVol().getId());
            }
            vol.retirerTerminal();
            vol.ajouterTerminal(terminal);
            return vol;
        }));
    }

    public Optional<Vol> retirerTerminal(int volId) {
        return getVol(volId).map(vol -> {
            vol.retirerTerminal();
            return vol;
        });
    }

    // ----- Avion -----

    public Optional<Vol> affecterAvion(int volId, int avionId) {
        return getVol(volId).flatMap(vol -> avionService.getAvion(avionId).map(avion -> {
            if (avion.getCapacite() < vol.getNombrePassagers()) {
                throw new IllegalStateException("L'avion " + avionId + " n'a que " + avion.getCapacite()
                        + " places pour " + vol.getNombrePassagers() + " passagers");
            }
            avion.ajouterVol(vol);
            return vol;
        }));
    }

    public Optional<Vol> retirerAvion(int volId) {
        return getVol(volId).map(vol -> {
            if (vol.getAvion() != null) {
                vol.getAvion().retirerVol(vol);
            }
            return vol;
        });
    }

    // ----- Compagnie et destination -----

    public Optional<Vol> affecterCompagnie(int volId, int compagnieId) {
        return getVol(volId).flatMap(vol -> compagnieService.getCompagnie(compagnieId).map(compagnie -> {
            vol.setCompagnie(compagnie);
            return vol;
        }));
    }

    public List<Vol> getVolsDeLaCompagnie(int compagnieId) {
        return this.vols.stream()
                .filter(vol -> vol.getCompagnie() != null && vol.getCompagnie().getId() == compagnieId)
                .toList();
    }

    public Optional<Vol> affecterDestination(int volId, int aeroportId) {
        return getVol(volId).flatMap(vol -> aeroportService.getAeroport(aeroportId).map(aeroport -> {
            vol.setDestination(aeroport);
            return vol;
        }));
    }
}
