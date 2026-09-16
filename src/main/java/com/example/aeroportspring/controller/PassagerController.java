package com.example.aeroportspring.controller;

import com.example.aeroportspring.model.Bagage;
import com.example.aeroportspring.model.Passager;
import com.example.aeroportspring.model.Vol;
import com.example.aeroportspring.service.PassagerService;
import com.example.aeroportspring.service.VolService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Passager")
public class PassagerController {

    private final PassagerService passagerService;
    private final VolService volService;

    public PassagerController(PassagerService passagerService, VolService volService) {
        this.passagerService = passagerService;
        this.volService = volService;
    }

    @GetMapping
    public List<Passager> getPassagers() {
        return passagerService.getPassagers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Passager> getPassager(@PathVariable int id) {
        return passagerService.getPassager(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Passager> creerPassager(@RequestBody Passager passager) {
        Passager cree = passagerService.creerPassager(passager);
        return ResponseEntity.status(HttpStatus.CREATED).body(cree);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Passager> modifierPassager(@PathVariable int id, @RequestBody Passager passager) {
        return passagerService.modifierPassager(id, passager)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerPassager(@PathVariable int id) {
        if (passagerService.supprimerPassager(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // PATCH /Passager/2/passeport?passeport=true
    @PatchMapping("/{id}/passeport")
    public ResponseEntity<Passager> modifierPasseport(@PathVariable int id, @RequestParam boolean passeport) {
        return passagerService.modifierPasseport(id, passeport)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PATCH /Passager/2/bagage?bagage=SOUTE
    @PatchMapping("/{id}/bagage")
    public ResponseEntity<Passager> modifierBagage(@PathVariable int id, @RequestParam Bagage bagage) {
        return passagerService.modifierBagage(id, bagage)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /Passager/2/vols -> les vols sur lesquels le passager est inscrit
    @GetMapping("/{id}/vols")
    public ResponseEntity<List<Vol>> getVols(@PathVariable int id) {
        if (passagerService.getPassager(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(volService.getVolsDuPassager(id));
    }
}
