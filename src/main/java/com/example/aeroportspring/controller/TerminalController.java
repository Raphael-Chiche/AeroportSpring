package com.example.aeroportspring.controller;

import com.example.aeroportspring.model.Terminal;
import com.example.aeroportspring.service.TerminalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Terminal")
@Tag(name = "Terminaux", description = "CRUD des terminaux")
public class TerminalController {

    private final TerminalService terminalService;

    public TerminalController(TerminalService terminalService) {
        this.terminalService = terminalService;
    }

    @Operation(summary = "Lister tous les terminaux")
    @GetMapping
    public List<Terminal> getTerminals() {
        return terminalService.getTerminals();
    }

    @Operation(summary = "Recuperer un terminal par son id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Terminal trouve"),
            @ApiResponse(responseCode = "404", description = "Terminal inexistant", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Terminal> getTerminal(
            @Parameter(description = "Identifiant du terminal", example = "1") @PathVariable int id) {
        return terminalService.getTerminal(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Creer un terminal")
    @ApiResponse(responseCode = "201", description = "Terminal cree")
    @PostMapping
    public ResponseEntity<Terminal> creerTerminal(@RequestBody Terminal terminal) {
        Terminal cree = terminalService.creerTerminal(terminal);
        return ResponseEntity.status(HttpStatus.CREATED).body(cree);
    }

    @Operation(summary = "Modifier un terminal existant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Terminal modifie"),
            @ApiResponse(responseCode = "404", description = "Terminal inexistant", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Terminal> modifierTerminal(@PathVariable int id, @RequestBody Terminal terminal) {
        return terminalService.modifierTerminal(id, terminal)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Supprimer un terminal")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Terminal supprime", content = @Content),
            @ApiResponse(responseCode = "404", description = "Terminal inexistant", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerTerminal(@PathVariable int id) {
        if (terminalService.supprimerTerminal(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
