package com.example.aeroportspring.controller;

import com.example.aeroportspring.model.Terminal;
import com.example.aeroportspring.service.TerminalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Terminal")
public class TerminalController {

    private final TerminalService terminalService;

    public TerminalController(TerminalService terminalService) {
        this.terminalService = terminalService;
    }

    @GetMapping
    public List<Terminal> getTerminals() {
        return terminalService.getTerminals();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Terminal> getTerminal(@PathVariable int id) {
        return terminalService.getTerminal(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Terminal> creerTerminal(@RequestBody Terminal terminal) {
        Terminal cree = terminalService.creerTerminal(terminal);
        return ResponseEntity.status(HttpStatus.CREATED).body(cree);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Terminal> modifierTerminal(@PathVariable int id, @RequestBody Terminal terminal) {
        return terminalService.modifierTerminal(id, terminal)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerTerminal(@PathVariable int id) {
        if (terminalService.supprimerTerminal(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
