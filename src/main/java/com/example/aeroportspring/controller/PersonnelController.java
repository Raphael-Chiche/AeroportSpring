package com.example.aeroportspring.controller;

import com.example.aeroportspring.model.Personnel;
import com.example.aeroportspring.model.Profession;
import com.example.aeroportspring.model.Vol;
import com.example.aeroportspring.service.PersonnelService;
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
@RequestMapping("/Personnel")
@Tag(name = "Personnels", description = "CRUD des membres du personnel, profession et vols affectes")
public class PersonnelController {

    private final PersonnelService personnelService;
    private final VolService volService;

    public PersonnelController(PersonnelService personnelService, VolService volService) {
        this.personnelService = personnelService;
        this.volService = volService;
    }

    @Operation(summary = "Lister tous les membres du personnel")
    @GetMapping
    public List<Personnel> getPersonnels() {
        return personnelService.getPersonnels();
    }

    @Operation(summary = "Recuperer un membre du personnel par son id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Personnel trouve"),
            @ApiResponse(responseCode = "404", description = "Personnel inexistant", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Personnel> getPersonnel(
            @Parameter(description = "Identifiant du personnel", example = "1") @PathVariable int id) {
        return personnelService.getPersonnel(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Creer un membre du personnel")
    @ApiResponse(responseCode = "201", description = "Personnel cree")
    @PostMapping
    public ResponseEntity<Personnel> creerPersonnel(@RequestBody Personnel personnel) {
        Personnel cree = personnelService.creerPersonnel(personnel);
        return ResponseEntity.status(HttpStatus.CREATED).body(cree);
    }

    @Operation(summary = "Modifier un membre du personnel existant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Personnel modifie"),
            @ApiResponse(responseCode = "404", description = "Personnel inexistant", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Personnel> modifierPersonnel(@PathVariable int id, @RequestBody Personnel personnel) {
        return personnelService.modifierPersonnel(id, personnel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Supprimer un membre du personnel")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Personnel supprime", content = @Content),
            @ApiResponse(responseCode = "404", description = "Personnel inexistant", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerPersonnel(@PathVariable int id) {
        if (personnelService.supprimerPersonnel(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Changer la profession d'un membre du personnel")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profession mise a jour"),
            @ApiResponse(responseCode = "404", description = "Personnel inexistant", content = @Content)
    })
    @PatchMapping("/{id}/profession")
    public ResponseEntity<Personnel> modifierProfession(@PathVariable int id,
            @Parameter(description = "Nouvelle profession") @RequestParam Profession profession) {
        return personnelService.modifierProfession(id, profession)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Lister les vols auxquels un personnel est affecte")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vols du personnel"),
            @ApiResponse(responseCode = "404", description = "Personnel inexistant", content = @Content)
    })
    @GetMapping("/{id}/vols")
    public ResponseEntity<List<Vol>> getVols(@PathVariable int id) {
        if (personnelService.getPersonnel(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(volService.getVolsDuPersonnel(id));
    }
}
