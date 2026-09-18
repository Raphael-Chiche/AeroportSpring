package com.example.aeroportspring.controller;

import com.example.aeroportspring.model.Avion;
import com.example.aeroportspring.model.Compagnie;
import com.example.aeroportspring.model.Vol;
import com.example.aeroportspring.service.AvionService;
import com.example.aeroportspring.service.CompagnieService;
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
@RequestMapping("/Compagnie")
@Tag(name = "Compagnies", description = "CRUD des compagnies aeriennes, leur flotte et leurs vols")
public class CompagnieController {

    private final CompagnieService compagnieService;
    private final AvionService avionService;
    private final VolService volService;

    public CompagnieController(CompagnieService compagnieService, AvionService avionService, VolService volService) {
        this.compagnieService = compagnieService;
        this.avionService = avionService;
        this.volService = volService;
    }

    @Operation(summary = "Lister toutes les compagnies")
    @GetMapping
    public List<Compagnie> getCompagnies() {
        return compagnieService.getCompagnies();
    }

    @Operation(summary = "Recuperer une compagnie par son id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Compagnie trouvee"),
            @ApiResponse(responseCode = "404", description = "Compagnie inexistante", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Compagnie> getCompagnie(
            @Parameter(description = "Identifiant de la compagnie", example = "1") @PathVariable int id) {
        return compagnieService.getCompagnie(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Creer une compagnie")
    @ApiResponse(responseCode = "201", description = "Compagnie creee")
    @PostMapping
    public ResponseEntity<Compagnie> creerCompagnie(@RequestBody Compagnie compagnie) {
        Compagnie cree = compagnieService.creerCompagnie(compagnie);
        return ResponseEntity.status(HttpStatus.CREATED).body(cree);
    }

    @Operation(summary = "Modifier une compagnie existante")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Compagnie modifiee"),
            @ApiResponse(responseCode = "404", description = "Compagnie inexistante", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Compagnie> modifierCompagnie(@PathVariable int id, @RequestBody Compagnie compagnie) {
        return compagnieService.modifierCompagnie(id, compagnie)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Supprimer une compagnie")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Compagnie supprimee", content = @Content),
            @ApiResponse(responseCode = "404", description = "Compagnie inexistante", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerCompagnie(@PathVariable int id) {
        if (compagnieService.supprimerCompagnie(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Lister la flotte d'une compagnie")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Flotte de la compagnie"),
            @ApiResponse(responseCode = "404", description = "Compagnie inexistante", content = @Content)
    })
    @GetMapping("/{id}/avions")
    public ResponseEntity<List<Avion>> getAvions(@PathVariable int id) {
        if (compagnieService.getCompagnie(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<Avion> avions = avionService.getAvions().stream()
                .filter(avion -> avion.getCompagnie() != null && avion.getCompagnie().getId() == id)
                .toList();
        return ResponseEntity.ok(avions);
    }

    @Operation(summary = "Lister les vols operes par une compagnie")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vols de la compagnie"),
            @ApiResponse(responseCode = "404", description = "Compagnie inexistante", content = @Content)
    })
    @GetMapping("/{id}/vols")
    public ResponseEntity<List<Vol>> getVols(@PathVariable int id) {
        if (compagnieService.getCompagnie(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(volService.getVolsDeLaCompagnie(id));
    }
}
