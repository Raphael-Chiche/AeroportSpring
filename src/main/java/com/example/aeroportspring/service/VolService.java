package com.example.aeroportspring.service;

import com.example.aeroportspring.model.Vol;
import com.example.aeroportspring.repository.VolRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VolService {

    private final VolRepository volRepository;
    private final PassagerService passagerService;
    private final PersonnelService personnelService;
    private final TerminalService terminalService;
    private final AvionService avionService;
    private final CompagnieService compagnieService;
    private final AeroportService aeroportService;

    public VolService(VolRepository volRepository, PassagerService passagerService,
                      PersonnelService personnelService, TerminalService terminalService, AvionService avionService,
                      CompagnieService compagnieService, AeroportService aeroportService) {
        this.volRepository = volRepository;
        this.passagerService = passagerService;
        this.personnelService = personnelService;
        this.terminalService = terminalService;
        this.avionService = avionService;
        this.compagnieService = compagnieService;
        this.aeroportService = aeroportService;
    }

    public List<Vol> getVols() {
        return volRepository.findAll();
    }

    public Optional<Vol> getVol(int id) {
        return volRepository.findById(id);
    }

    public Vol creerVol(Vol vol) {
        return volRepository.save(vol);
    }

    // La compagnie se change avec affecterCompagnie (PUT /Vol/{id}/compagnie/{compagnieId})
    public Optional<Vol> modifierVol(int id, Vol nouvellesInfos) {
        return getVol(id).map(vol -> {
            vol.setDateDepart(nouvellesInfos.getDateDepart());
            vol.setDateArrivee(nouvellesInfos.getDateArrivee());
            vol.setPrix(nouvellesInfos.getPrix());
            vol.setDuree(nouvellesInfos.getDuree());
            return volRepository.save(vol);
        });
    }

    // Les lignes de vol_passager et vol_personnel sont supprimees automatiquement avec le vol
    public boolean supprimerVol(int id) {
        Optional<Vol> vol = volRepository.findById(id);
        if (vol.isEmpty()) {
            return false;
        }
        vol.get().retirerTerminal();
        volRepository.delete(vol.get());
        return true;
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
            return volRepository.save(vol);
        }));
    }

    public Optional<Vol> retirerPassager(int volId, int passagerId) {
        return getVol(volId).flatMap(vol -> passagerService.getPassager(passagerId).map(passager -> {
            passager.retirerVol(vol);
            return volRepository.save(vol);
        }));
    }

    public List<Vol> getVolsDuPassager(int passagerId) {
        return volRepository.findByPassagers_Id(passagerId);
    }

    // ----- Personnels -----

    public Optional<Vol> ajouterPersonnel(int volId, int personnelId) {
        return getVol(volId).flatMap(vol -> personnelService.getPersonnel(personnelId).map(personnel -> {
            vol.ajouterPersonnel(personnel);
            return volRepository.save(vol);
        }));
    }

    public Optional<Vol> retirerPersonnel(int volId, int personnelId) {
        return getVol(volId).flatMap(vol -> personnelService.getPersonnel(personnelId).map(personnel -> {
            vol.retirerPersonnel(personnel);
            return volRepository.save(vol);
        }));
    }

    public List<Vol> getVolsDuPersonnel(int personnelId) {
        return volRepository.findByPersonnels_Id(personnelId);
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
            return volRepository.save(vol);
        }));
    }

    public Optional<Vol> retirerTerminal(int volId) {
        return getVol(volId).map(vol -> {
            vol.retirerTerminal();
            return volRepository.save(vol);
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
            return volRepository.save(vol);
        }));
    }

    public Optional<Vol> retirerAvion(int volId) {
        return getVol(volId).map(vol -> {
            if (vol.getAvion() != null) {
                vol.getAvion().retirerVol(vol);
            }
            return volRepository.save(vol);
        });
    }

    // ----- Compagnie et destination -----

    public Optional<Vol> affecterCompagnie(int volId, int compagnieId) {
        return getVol(volId).flatMap(vol -> compagnieService.getCompagnie(compagnieId).map(compagnie -> {
            vol.setCompagnie(compagnie);
            return volRepository.save(vol);
        }));
    }

    public List<Vol> getVolsDeLaCompagnie(int compagnieId) {
        return volRepository.findByCompagnie_Id(compagnieId);
    }

    public Optional<Vol> affecterDestination(int volId, int aeroportId) {
        return getVol(volId).flatMap(vol -> aeroportService.getAeroport(aeroportId).map(aeroport -> {
            vol.setDestination(aeroport);
            return volRepository.save(vol);
        }));
    }
}
