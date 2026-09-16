package com.example.aeroportspring.service;

import com.example.aeroportspring.model.Terminal;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TerminalService {

    private final List<Terminal> terminals = new ArrayList<>();

    public List<Terminal> getTerminals() {
        return this.terminals;
    }

    public Optional<Terminal> getTerminal(int id) {
        return this.terminals.stream()
                .filter(x -> x.getId() == id)
                .findFirst();
    }

    public Terminal creerTerminal(Terminal terminal) {
        this.terminals.add(terminal);
        return terminal;
    }

    public Optional<Terminal> modifierTerminal(int id, Terminal nouvellesInfos) {
        return getTerminal(id).map(terminal -> {
            terminal.setNom(nouvellesInfos.getNom());
            return terminal;
        });
    }

    public boolean supprimerTerminal(int id) {
        return this.terminals.removeIf(x -> x.getId() == id);
    }
}
