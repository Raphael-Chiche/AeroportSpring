package com.example.aeroportspring.controller;

import com.example.aeroportspring.model.Bagage;
import com.example.aeroportspring.model.Passager;
import com.example.aeroportspring.model.Vol;
import com.example.aeroportspring.service.PassagerService;
import com.example.aeroportspring.service.VolService;
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
@RequestMapping("/Passager")
@Tag(name = "Passagers", description = "CRUD des passagers, passeport, bagage et vols reserves")
public class PassagerController {

    private final PassagerService passagerService;
    private final VolService volService;

    public PassagerController(PassagerService passagerService, VolService volService) {
        this.passagerService = passagerService;
        this.volService = volService;
    }

    @Operation(summary = "Lister tous les passagers")
    @GetMapping
    public List<Passager> getPassagers() {
        return passagerService.getPassagers();
    }

    @Operation(summary = "Recuperer un passager par son id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Passager trouve"),
            @ApiResponse(responseCode = "404", description = "Passager inexistant", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Passager> getPassager(
            @Parameter(description = "Identifiant du passager", example = "1") @PathVariable int id) {
        return passagerService.getPassager(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Creer un passager")
    @ApiResponse(responseCode = "201", description = "Passager cree")
    @PostMapping
    public ResponseEntity<Passager> creerPassager(@RequestBody Passager passager) {
        Passager cree = passagerService.creerPassager(passager);
        return ResponseEntity.status(HttpStatus.CREATED).body(cree);
    }

    @Operation(summary = "Modifier un passager existant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Passager modifie"),
            @ApiResponse(responseCode = "404", description = "Passager inexistant", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Passager> modifierPassager(@PathVariable int id, @RequestBody Passager passager) {
        return passagerService.modifierPassager(id, passager)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Supprimer un passager")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Passager supprime", content = @Content),
            @ApiResponse(responseCode = "404", description = "Passager inexistant", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerPassager(@PathVariable int id) {
        if (passagerService.supprimerPassager(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Declarer si un passager possede un passeport")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Passeport mis a jour"),
            @ApiResponse(responseCode = "404", description = "Passager inexistant", content = @Content)
    })
    @PatchMapping("/{id}/passeport")
    public ResponseEntity<Passager> modifierPasseport(@PathVariable int id,
            @Parameter(description = "true si le passager a un passeport", example = "true") @RequestParam boolean passeport) {
        return passagerService.modifierPasseport(id, passeport)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Changer le type de bagage d'un passager")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Bagage mis a jour"),
            @ApiResponse(responseCode = "404", description = "Passager inexistant", content = @Content)
    })
    @PatchMapping("/{id}/bagage")
    public ResponseEntity<Passager> modifierBagage(@PathVariable int id,
            @Parameter(description = "Type de bagage") @RequestParam Bagage bagage) {
        return passagerService.modifierBagage(id, bagage)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Lister les vols reserves par un passager")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vols du passager"),
            @ApiResponse(responseCode = "404", description = "Passager inexistant", content = @Content)
    })
    @GetMapping("/{id}/vols")
    public ResponseEntity<List<Vol>> getVols(@PathVariable int id) {
        if (passagerService.getPassager(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(volService.getVolsDuPassager(id));
    }
}
