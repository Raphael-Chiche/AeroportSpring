package com.example.aeroportspring.controller;

import com.example.aeroportspring.model.Aeroport;
import com.example.aeroportspring.model.Personnel;
import com.example.aeroportspring.model.Terminal;
import com.example.aeroportspring.service.AeroportService;
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
@RequestMapping("/Aeroport")
@Tag(name = "Aeroports", description = "CRUD des aeroports et gestion de leurs terminaux / personnels")
public class AeroportController {

    private final AeroportService aeroportService;

    public AeroportController(AeroportService aeroportService) {
        this.aeroportService = aeroportService;
    }

    @Operation(summary = "Lister tous les aeroports")
    @GetMapping
    public List<Aeroport> getAeroports() {
        return aeroportService.getAeroports();
    }

    @Operation(summary = "Recuperer un aeroport par son id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Aeroport trouve"),
            @ApiResponse(responseCode = "404", description = "Aeroport inexistant", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Aeroport> getAeroport(
            @Parameter(description = "Identifiant de l'aeroport", example = "1") @PathVariable int id) {
        return aeroportService.getAeroport(id)
                .map(ResponseEntity::ok)                     // trouve -> 200 + JSON
                .orElse(ResponseEntity.notFound().build());  // absent -> 404
    }

    @Operation(summary = "Creer un aeroport")
    @ApiResponse(responseCode = "201", description = "Aeroport cree")
    @PostMapping
    public ResponseEntity<Aeroport> creerAeroport(@RequestBody Aeroport aeroport) {
        Aeroport cree = aeroportService.creerAeroport(aeroport);
        return ResponseEntity.status(HttpStatus.CREATED).body(cree);
    }

    @Operation(summary = "Modifier un aeroport existant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Aeroport modifie"),
            @ApiResponse(responseCode = "404", description = "Aeroport inexistant", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Aeroport> modifierAeroport(@PathVariable int id, @RequestBody Aeroport aeroport) {
        return aeroportService.modifierAeroport(id, aeroport)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Supprimer un aeroport")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Aeroport supprime", content = @Content),
            @ApiResponse(responseCode = "404", description = "Aeroport inexistant", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerAeroport(@PathVariable int id) {
        if (aeroportService.supprimerAeroport(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ----- Terminaux -----

    @Operation(summary = "Lister les terminaux d'un aeroport")
    @GetMapping("/{id}/terminals")
    public ResponseEntity<List<Terminal>> getTerminals(@PathVariable int id) {
        return aeroportService.getAeroport(id)
                .map(aeroport -> ResponseEntity.ok(aeroport.getTerminals()))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Rattacher un terminal existant a un aeroport")
    @PostMapping("/{id}/terminals/{terminalId}")
    public ResponseEntity<Aeroport> ajouterTerminal(@PathVariable int id, @PathVariable int terminalId) {
        return aeroportService.ajouterTerminal(id, terminalId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Detacher un terminal d'un aeroport")
    @DeleteMapping("/{id}/terminals/{terminalId}")
    public ResponseEntity<Aeroport> retirerTerminal(@PathVariable int id, @PathVariable int terminalId) {
        return aeroportService.retirerTerminal(id, terminalId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ----- Personnels -----

    @Operation(summary = "Lister les personnels d'un aeroport")
    @GetMapping("/{id}/personnels")
    public ResponseEntity<List<Personnel>> getPersonnels(@PathVariable int id) {
        return aeroportService.getAeroport(id)
                .map(aeroport -> ResponseEntity.ok(aeroport.getPersonnels()))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Affecter un personnel existant a un aeroport")
    @PostMapping("/{id}/personnels/{personnelId}")
    public ResponseEntity<Aeroport> ajouterPersonnel(@PathVariable int id, @PathVariable int personnelId) {
        return aeroportService.ajouterPersonnel(id, personnelId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Retirer un personnel d'un aeroport")
    @DeleteMapping("/{id}/personnels/{personnelId}")
    public ResponseEntity<Aeroport> retirerPersonnel(@PathVariable int id, @PathVariable int personnelId) {
        return aeroportService.retirerPersonnel(id, personnelId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
