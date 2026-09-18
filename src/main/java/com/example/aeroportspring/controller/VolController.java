package com.example.aeroportspring.controller;

import com.example.aeroportspring.model.Passager;
import com.example.aeroportspring.model.Personnel;
import com.example.aeroportspring.model.Vol;
import com.example.aeroportspring.service.VolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Vol")
@Tag(name = "Vols", description = "CRUD des vols, embarquement des passagers et de l'equipage, affectation terminal / avion / compagnie / destination")
public class VolController {

    private final VolService volService;

    public VolController(VolService volService) {
        this.volService = volService;
    }

    @Operation(summary = "Lister tous les vols")
    @GetMapping
    public List<Vol> getVols() {
        return volService.getVols();
    }

    @Operation(summary = "Recuperer un vol par son id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vol trouve"),
            @ApiResponse(responseCode = "404", description = "Vol inexistant", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Vol> getVol(
            @Parameter(description = "Identifiant du vol", example = "1") @PathVariable int id) {
        return volService.getVol(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Creer un vol")
    @ApiResponse(responseCode = "201", description = "Vol cree")
    @PostMapping
    public ResponseEntity<Vol> creerVol(@RequestBody Vol vol) {
        Vol cree = volService.creerVol(vol);
        return ResponseEntity.status(HttpStatus.CREATED).body(cree);
    }

    @Operation(summary = "Modifier un vol existant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vol modifie"),
            @ApiResponse(responseCode = "404", description = "Vol inexistant", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Vol> modifierVol(@PathVariable int id, @RequestBody Vol vol) {
        return volService.modifierVol(id, vol)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Supprimer un vol")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Vol supprime", content = @Content),
            @ApiResponse(responseCode = "404", description = "Vol inexistant", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerVol(@PathVariable int id) {
        if (volService.supprimerVol(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ----- Passagers -----

    @Operation(summary = "Lister les passagers d'un vol")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Passagers du vol"),
            @ApiResponse(responseCode = "404", description = "Vol inexistant", content = @Content)
    })
    @GetMapping("/{id}/passagers")
    public ResponseEntity<List<Passager>> getPassagers(@PathVariable int id) {
        return volService.getVol(id)
                .map(vol -> ResponseEntity.ok(vol.getPassagers()))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Ajouter un passager a un vol",
            description = "Le passager doit posseder un passeport et le vol doit avoir une place libre")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Passager embarque"),
            @ApiResponse(responseCode = "404", description = "Vol ou passager inexistant", content = @Content),
            @ApiResponse(responseCode = "409", description = "Regle metier non respectee (pas de passeport, vol complet...)",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
    })
    @PostMapping("/{id}/passagers/{passagerId}")
    public ResponseEntity<Vol> ajouterPassager(@PathVariable int id, @PathVariable int passagerId) {
        return volService.ajouterPassager(id, passagerId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Retirer un passager d'un vol")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Passager retire"),
            @ApiResponse(responseCode = "404", description = "Vol ou passager inexistant", content = @Content)
    })
    @DeleteMapping("/{id}/passagers/{passagerId}")
    public ResponseEntity<Vol> retirerPassager(@PathVariable int id, @PathVariable int passagerId) {
        return volService.retirerPassager(id, passagerId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ----- Personnels -----

    @Operation(summary = "Lister l'equipage d'un vol")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Equipage du vol"),
            @ApiResponse(responseCode = "404", description = "Vol inexistant", content = @Content)
    })
    @GetMapping("/{id}/personnels")
    public ResponseEntity<List<Personnel>> getPersonnels(@PathVariable int id) {
        return volService.getVol(id)
                .map(vol -> ResponseEntity.ok(vol.getPersonnels()))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Affecter un membre du personnel a un vol")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Personnel affecte"),
            @ApiResponse(responseCode = "404", description = "Vol ou personnel inexistant", content = @Content),
            @ApiResponse(responseCode = "409", description = "Regle metier non respectee",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
    })
    @PostMapping("/{id}/personnels/{personnelId}")
    public ResponseEntity<Vol> ajouterPersonnel(@PathVariable int id, @PathVariable int personnelId) {
        return volService.ajouterPersonnel(id, personnelId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Retirer un membre du personnel d'un vol")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Personnel retire"),
            @ApiResponse(responseCode = "404", description = "Vol ou personnel inexistant", content = @Content)
    })
    @DeleteMapping("/{id}/personnels/{personnelId}")
    public ResponseEntity<Vol> retirerPersonnel(@PathVariable int id, @PathVariable int personnelId) {
        return volService.retirerPersonnel(id, personnelId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ----- Terminal, avion, compagnie, destination -----

    @Operation(summary = "Affecter un terminal a un vol")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Terminal affecte"),
            @ApiResponse(responseCode = "404", description = "Vol ou terminal inexistant", content = @Content),
            @ApiResponse(responseCode = "409", description = "Terminal deja occupe",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
    })
    @PutMapping("/{id}/terminal/{terminalId}")
    public ResponseEntity<Vol> affecterTerminal(@PathVariable int id, @PathVariable int terminalId) {
        return volService.affecterTerminal(id, terminalId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Liberer le terminal d'un vol")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Terminal libere"),
            @ApiResponse(responseCode = "404", description = "Vol inexistant", content = @Content)
    })
    @DeleteMapping("/{id}/terminal")
    public ResponseEntity<Vol> retirerTerminal(@PathVariable int id) {
        return volService.retirerTerminal(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Affecter un avion a un vol")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Avion affecte"),
            @ApiResponse(responseCode = "404", description = "Vol ou avion inexistant", content = @Content),
            @ApiResponse(responseCode = "409", description = "Regle metier non respectee (avion deja en vol...)",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
    })
    @PutMapping("/{id}/avion/{avionId}")
    public ResponseEntity<Vol> affecterAvion(@PathVariable int id, @PathVariable int avionId) {
        return volService.affecterAvion(id, avionId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Retirer l'avion d'un vol")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Avion retire"),
            @ApiResponse(responseCode = "404", description = "Vol inexistant", content = @Content)
    })
    @DeleteMapping("/{id}/avion")
    public ResponseEntity<Vol> retirerAvion(@PathVariable int id) {
        return volService.retirerAvion(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Affecter une compagnie a un vol")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Compagnie affectee"),
            @ApiResponse(responseCode = "404", description = "Vol ou compagnie inexistant", content = @Content)
    })
    @PutMapping("/{id}/compagnie/{compagnieId}")
    public ResponseEntity<Vol> affecterCompagnie(@PathVariable int id, @PathVariable int compagnieId) {
        return volService.affecterCompagnie(id, compagnieId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Affecter l'aeroport de destination d'un vol")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Destination affectee"),
            @ApiResponse(responseCode = "404", description = "Vol ou aeroport inexistant", content = @Content)
    })
    @PutMapping("/{id}/destination/{aeroportId}")
    public ResponseEntity<Vol> affecterDestination(@PathVariable int id, @PathVariable int aeroportId) {
        return volService.affecterDestination(id, aeroportId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Regle metier non respectee (pas de passeport, vol complet, terminal occupe...) -> 409 + message
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> regleNonRespectee(IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
}
