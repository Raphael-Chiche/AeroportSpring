package com.example.aeroportspring.controller;

import com.example.aeroportspring.model.Avion;
import com.example.aeroportspring.service.AvionService;
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
@RequestMapping("/Avion")
@Tag(name = "Avions", description = "CRUD des avions, rattachement a une compagnie et statut en vol")
public class AvionController {

    private final AvionService avionService;

    public AvionController(AvionService avionService) {
        this.avionService = avionService;
    }

    @Operation(summary = "Lister tous les avions")
    @GetMapping
    public List<Avion> getAvions(){
        return avionService.getAvions();
    }

    @Operation(summary = "Recuperer un avion par son id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Avion trouve"),
            @ApiResponse(responseCode = "404", description = "Avion inexistant", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Avion> getAvion(
            @Parameter(description = "Identifiant de l'avion", example = "1") @PathVariable int id){
        return avionService.getAvion(id)
                .map(ResponseEntity::ok)                     // trouve -> 200 + JSON
                .orElse(ResponseEntity.notFound().build());  // absent -> 404
    }

    @Operation(summary = "Creer un avion")
    @ApiResponse(responseCode = "201", description = "Avion cree")
    @PostMapping
    public ResponseEntity<Avion> creerAvion(@RequestBody Avion avion){
        Avion create = avionService.creerAvion(avion);
        return ResponseEntity.status(HttpStatus.CREATED).body(create);
    }

    @Operation(summary = "Modifier un avion existant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Avion modifie"),
            @ApiResponse(responseCode = "404", description = "Avion inexistant", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Avion> modifierAvion(@PathVariable int id, @RequestBody Avion avion){
        return avionService.modifierAvion(id, avion)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Supprimer un avion")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Avion supprime", content = @Content),
            @ApiResponse(responseCode = "404", description = "Avion inexistant", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerAvion(@PathVariable int id){
        if(avionService.supprimerAvion(id)){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Rattacher un avion a une compagnie")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Compagnie de l'avion mise a jour"),
            @ApiResponse(responseCode = "404", description = "Avion ou compagnie inexistant", content = @Content)
    })
    @PatchMapping("/{id}/compagnie/{compagnieId}")
    public ResponseEntity<Avion> modifierCompagnieAvion(@PathVariable int id, @PathVariable int compagnieId){
        return avionService.modifierCompagnieAvion(id, compagnieId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Mettre a jour le statut en vol d'un avion")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Statut mis a jour"),
            @ApiResponse(responseCode = "404", description = "Avion inexistant", content = @Content)
    })
    @PatchMapping("/{id}/enVol")
    public ResponseEntity<Avion> modifierIsVolAvion(@PathVariable int id,
            @Parameter(description = "true si l'avion est en vol", example = "true") @RequestParam boolean enVol){
        return avionService.modifierIsVolAvion(id, enVol)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
