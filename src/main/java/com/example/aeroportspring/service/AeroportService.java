package com.example.aeroportspring.service;

import com.example.aeroportspring.model.Aeroport;
import com.example.aeroportspring.model.Terminal;
import com.example.aeroportspring.repository.AeroportRepository;
import com.example.aeroportspring.repository.VolRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AeroportService {

    private final AeroportRepository aeroportRepository;
    private final VolRepository volRepository;
    private final TerminalService terminalService;
    private final PersonnelService personnelService;

    public AeroportService(AeroportRepository aeroportRepository, VolRepository volRepository,
                           TerminalService terminalService, PersonnelService personnelService) {
        this.aeroportRepository = aeroportRepository;
        this.volRepository = volRepository;
        this.terminalService = terminalService;
        this.personnelService = personnelService;
    }

    public List<Aeroport> getAeroports() {
        return aeroportRepository.findAll();
    }

    public Optional<Aeroport> getAeroport(int id) {
        return aeroportRepository.findById(id);
    }

    public Aeroport creerAeroport(Aeroport aeroport) {
        return aeroportRepository.save(aeroport);
    }

    public Optional<Aeroport> modifierAeroport(int id, Aeroport nouvellesInfos) {
        return aeroportRepository.findById(id).map(aeroport -> {
            aeroport.setNom(nouvellesInfos.getNom());
            aeroport.setAdresse(nouvellesInfos.getAdresse());
            aeroport.setPays(nouvellesInfos.getPays());
            aeroport.setUTC(nouvellesInfos.getUTC());
            return aeroportRepository.save(aeroport);
        });
    }

    // On detache d'abord ses terminaux et les vols qui vont vers cet aeroport (cles etrangeres)
    @Transactional
    public boolean supprimerAeroport(int id) {
        Optional<Aeroport> aeroport = aeroportRepository.findById(id);
        if (aeroport.isEmpty()) {
            return false;
        }
        for (Terminal terminal : new ArrayList<>(aeroport.get().getTerminals())) {
            aeroport.get().retirerTerminal(terminal);
            terminalService.creerTerminal(terminal);
        }
        volRepository.findByDestination_Id(id).forEach(vol -> {
            vol.setDestination(null);
            volRepository.save(vol);
        });
        aeroportRepository.delete(aeroport.get());
        return true;
    }

    // Rattache le terminal a cet aeroport (et le detache de son ancien aeroport s'il en avait un).
    // C'est le terminal qui porte la cle etrangere (aeroport_id) : c'est donc lui qu'on enregistre.
    public Optional<Aeroport> ajouterTerminal(int aeroportId, int terminalId) {
        return getAeroport(aeroportId).flatMap(aeroport -> terminalService.getTerminal(terminalId).map(terminal -> {
            Aeroport ancien = terminal.getAeroport();
            if (ancien != null && ancien != aeroport) {
                ancien.retirerTerminal(terminal);
            }
            aeroport.ajouterTerminal(terminal);
            terminalService.creerTerminal(terminal);
            return aeroport;
        }));
    }

    public Optional<Aeroport> retirerTerminal(int aeroportId, int terminalId) {
        return getAeroport(aeroportId).flatMap(aeroport -> terminalService.getTerminal(terminalId).map(terminal -> {
            aeroport.retirerTerminal(terminal);
            terminalService.creerTerminal(terminal);
            return aeroport;
        }));
    }

    public Optional<Aeroport> ajouterPersonnel(int aeroportId, int personnelId) {
        return getAeroport(aeroportId).flatMap(aeroport -> personnelService.getPersonnel(personnelId).map(personnel -> {
            aeroport.ajouterPersonnel(personnel);
            return aeroportRepository.save(aeroport);
        }));
    }

    public Optional<Aeroport> retirerPersonnel(int aeroportId, int personnelId) {
        return getAeroport(aeroportId).flatMap(aeroport -> personnelService.getPersonnel(personnelId).map(personnel -> {
            aeroport.retirerPersonnel(personnel);
            return aeroportRepository.save(aeroport);
        }));
    }
}
