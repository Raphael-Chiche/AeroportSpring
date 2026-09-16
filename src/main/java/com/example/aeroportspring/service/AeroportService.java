package com.example.aeroportspring.service;

import com.example.aeroportspring.model.Aeroport;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Service
public class AeroportService {

    private final List<Aeroport> aeroports = new ArrayList<>();

    private final TerminalService terminalService;
    private final PersonnelService personnelService;

    public AeroportService(TerminalService terminalService, PersonnelService personnelService) {
        this.terminalService = terminalService;
        this.personnelService = personnelService;
    }

    public Optional<Aeroport> getAeroport(int id) {
        return this.aeroports.stream()
                .filter(a -> a.getId() == id)
                .findFirst();
    }

    public Aeroport creerAeroport(Aeroport aeroport) {
        this.aeroports.add(aeroport);
        return aeroport;
    }

    public Optional<Aeroport> modifierAeroport(int id, Aeroport nouvellesInfos) {
        return getAeroport(id).map(aeroport -> {
            aeroport.setNom(nouvellesInfos.getNom());
            aeroport.setAdresse(nouvellesInfos.getAdresse());
            aeroport.setPays(nouvellesInfos.getPays());
            aeroport.setUTC(nouvellesInfos.getUTC());
            return aeroport;
        });
    }

    public boolean supprimerAeroport(int id) {
        return this.aeroports.removeIf(a -> a.getId() == id);
    }

    // Rattache le terminal a cet aeroport (et le detache de son ancien aeroport s'il en avait un)
    public Optional<Aeroport> ajouterTerminal(int aeroportId, int terminalId) {
        return getAeroport(aeroportId).flatMap(aeroport -> terminalService.getTerminal(terminalId).map(terminal -> {
            Aeroport ancien = terminal.getAeroport();
            if (ancien != null && ancien != aeroport) {
                ancien.retirerTerminal(terminal);
            }
            aeroport.ajouterTerminal(terminal);
            return aeroport;
        }));
    }

    public Optional<Aeroport> retirerTerminal(int aeroportId, int terminalId) {
        return getAeroport(aeroportId).flatMap(aeroport -> terminalService.getTerminal(terminalId).map(terminal -> {
            aeroport.retirerTerminal(terminal);
            return aeroport;
        }));
    }

    public Optional<Aeroport> ajouterPersonnel(int aeroportId, int personnelId) {
        return getAeroport(aeroportId).flatMap(aeroport -> personnelService.getPersonnel(personnelId).map(personnel -> {
            aeroport.ajouterPersonnel(personnel);
            return aeroport;
        }));
    }

    public Optional<Aeroport> retirerPersonnel(int aeroportId, int personnelId) {
        return getAeroport(aeroportId).flatMap(aeroport -> personnelService.getPersonnel(personnelId).map(personnel -> {
            aeroport.retirerPersonnel(personnel);
            return aeroport;
        }));
    }
}
