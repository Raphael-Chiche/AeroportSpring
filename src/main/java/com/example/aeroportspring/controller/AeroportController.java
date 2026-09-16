package com.example.aeroportspring.controller;

import com.example.aeroportspring.model.Aeroport;
import com.example.aeroportspring.model.Personnel;
import com.example.aeroportspring.model.Terminal;
import com.example.aeroportspring.service.AeroportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Aeroport")
public class AeroportController {

    private final AeroportService aeroportService;

    public AeroportController(AeroportService aeroportService) {
        this.aeroportService = aeroportService;
    }

    @GetMapping
    public List<Aeroport> getAeroports() {
        return aeroportService.getAeroports();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aeroport> getAeroport(@PathVariable int id) {
        return aeroportService.getAeroport(id)
                .map(ResponseEntity::ok)                     // trouve -> 200 + JSON
                .orElse(ResponseEntity.notFound().build());  // absent -> 404
    }

    // POST /Aeroport  avec un JSON dans le corps -> cree l'aeroport, renvoie 201
    @PostMapping
    public ResponseEntity<Aeroport> creerAeroport(@RequestBody Aeroport aeroport) {
        Aeroport cree = aeroportService.creerAeroport(aeroport);
        return ResponseEntity.status(HttpStatus.CREATED).body(cree);
    }

    // PUT /Aeroport/3  avec un JSON -> modifie l'aeroport 3, ou 404
    @PutMapping("/{id}")
    public ResponseEntity<Aeroport> modifierAeroport(@PathVariable int id, @RequestBody Aeroport aeroport) {
        return aeroportService.modifierAeroport(id, aeroport)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /Aeroport/3  -> 204 si supprime, 404 sinon
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerAeroport(@PathVariable int id) {
        if (aeroportService.supprimerAeroport(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ----- Terminaux -----

    @GetMapping("/{id}/terminals")
    public ResponseEntity<List<Terminal>> getTerminals(@PathVariable int id) {
        return aeroportService.getAeroport(id)
                .map(aeroport -> ResponseEntity.ok(aeroport.getTerminals()))
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /Aeroport/1/terminals/2 -> rattache le terminal 2 a l'aeroport 1
    @PostMapping("/{id}/terminals/{terminalId}")
    public ResponseEntity<Aeroport> ajouterTerminal(@PathVariable int id, @PathVariable int terminalId) {
        return aeroportService.ajouterTerminal(id, terminalId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}/terminals/{terminalId}")
    public ResponseEntity<Aeroport> retirerTerminal(@PathVariable int id, @PathVariable int terminalId) {
        return aeroportService.retirerTerminal(id, terminalId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ----- Personnels -----

    @GetMapping("/{id}/personnels")
    public ResponseEntity<List<Personnel>> getPersonnels(@PathVariable int id) {
        return aeroportService.getAeroport(id)
                .map(aeroport -> ResponseEntity.ok(aeroport.getPersonnels()))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/personnels/{personnelId}")
    public ResponseEntity<Aeroport> ajouterPersonnel(@PathVariable int id, @PathVariable int personnelId) {
        return aeroportService.ajouterPersonnel(id, personnelId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}/personnels/{personnelId}")
    public ResponseEntity<Aeroport> retirerPersonnel(@PathVariable int id, @PathVariable int personnelId) {
        return aeroportService.retirerPersonnel(id, personnelId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
