package com.example.aeroportspring.controller;

import com.example.aeroportspring.model.Passager;
import com.example.aeroportspring.model.Personnel;
import com.example.aeroportspring.model.Vol;
import com.example.aeroportspring.service.VolService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Vol")
public class VolController {

    private final VolService volService;

    public VolController(VolService volService) {
        this.volService = volService;
    }

    @GetMapping
    public List<Vol> getVols() {
        return volService.getVols();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vol> getVol(@PathVariable int id) {
        return volService.getVol(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Vol> creerVol(@RequestBody Vol vol) {
        Vol cree = volService.creerVol(vol);
        return ResponseEntity.status(HttpStatus.CREATED).body(cree);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vol> modifierVol(@PathVariable int id, @RequestBody Vol vol) {
        return volService.modifierVol(id, vol)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerVol(@PathVariable int id) {
        if (volService.supprimerVol(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ----- Passagers -----

    @GetMapping("/{id}/passagers")
    public ResponseEntity<List<Passager>> getPassagers(@PathVariable int id) {
        return volService.getVol(id)
                .map(vol -> ResponseEntity.ok(vol.getPassagers()))
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /Vol/1/passagers/4 -> ajoute le passager 4 au vol 1 (passeport requis, dans la limite des places)
    @PostMapping("/{id}/passagers/{passagerId}")
    public ResponseEntity<Vol> ajouterPassager(@PathVariable int id, @PathVariable int passagerId) {
        return volService.ajouterPassager(id, passagerId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}/passagers/{passagerId}")
    public ResponseEntity<Vol> retirerPassager(@PathVariable int id, @PathVariable int passagerId) {
        return volService.retirerPassager(id, passagerId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ----- Personnels -----

    @GetMapping("/{id}/personnels")
    public ResponseEntity<List<Personnel>> getPersonnels(@PathVariable int id) {
        return volService.getVol(id)
                .map(vol -> ResponseEntity.ok(vol.getPersonnels()))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/personnels/{personnelId}")
    public ResponseEntity<Vol> ajouterPersonnel(@PathVariable int id, @PathVariable int personnelId) {
        return volService.ajouterPersonnel(id, personnelId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}/personnels/{personnelId}")
    public ResponseEntity<Vol> retirerPersonnel(@PathVariable int id, @PathVariable int personnelId) {
        return volService.retirerPersonnel(id, personnelId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ----- Terminal, avion, compagnie, destination -----

    @PutMapping("/{id}/terminal/{terminalId}")
    public ResponseEntity<Vol> affecterTerminal(@PathVariable int id, @PathVariable int terminalId) {
        return volService.affecterTerminal(id, terminalId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}/terminal")
    public ResponseEntity<Vol> retirerTerminal(@PathVariable int id) {
        return volService.retirerTerminal(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/avion/{avionId}")
    public ResponseEntity<Vol> affecterAvion(@PathVariable int id, @PathVariable int avionId) {
        return volService.affecterAvion(id, avionId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}/avion")
    public ResponseEntity<Vol> retirerAvion(@PathVariable int id) {
        return volService.retirerAvion(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/compagnie/{compagnieId}")
    public ResponseEntity<Vol> affecterCompagnie(@PathVariable int id, @PathVariable int compagnieId) {
        return volService.affecterCompagnie(id, compagnieId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

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
