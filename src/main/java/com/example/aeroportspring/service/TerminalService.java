package com.example.aeroportspring.service;

import com.example.aeroportspring.model.Terminal;
import com.example.aeroportspring.model.Vol;
import com.example.aeroportspring.repository.TerminalRepository;
import com.example.aeroportspring.repository.VolRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TerminalService {

    private final TerminalRepository terminalRepository;
    private final VolRepository volRepository;

    public TerminalService(TerminalRepository terminalRepository, VolRepository volRepository) {
        this.terminalRepository = terminalRepository;
        this.volRepository = volRepository;
    }

    public List<Terminal> getTerminals() {
        return terminalRepository.findAll();
    }

    public Optional<Terminal> getTerminal(int id) {
        return terminalRepository.findById(id);
    }

    // Sert aussi a enregistrer un terminal existant (ex : AeroportService quand on change son aeroport)
    public Terminal creerTerminal(Terminal terminal) {
        return terminalRepository.save(terminal);
    }

    public Optional<Terminal> modifierTerminal(int id, Terminal nouvellesInfos) {
        return terminalRepository.findById(id).map(terminal -> {
            terminal.setNom(nouvellesInfos.getNom());
            return terminalRepository.save(terminal);
        });
    }

    // On detache d'abord le vol qui part de ce terminal (colonne depart_id de la table vol)
    @Transactional
    public boolean supprimerTerminal(int id) {
        Optional<Terminal> terminal = terminalRepository.findById(id);
        if (terminal.isEmpty()) {
            return false;
        }
        Vol vol = terminal.get().getVol();
        if (vol != null) {
            vol.retirerTerminal();
            volRepository.save(vol);
        }
        if (terminal.get().getAeroport() != null) {
            terminal.get().getAeroport().retirerTerminal(terminal.get());
        }
        terminalRepository.delete(terminal.get());
        return true;
    }
}
