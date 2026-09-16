package com.example.aeroportspring.controller;

import com.example.aeroportspring.model.Avion;
import com.example.aeroportspring.model.Compagnie;
import com.example.aeroportspring.model.Vol;
import com.example.aeroportspring.service.AvionService;
import com.example.aeroportspring.service.CompagnieService;
import com.example.aeroportspring.service.VolService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Compagnie")
public class CompagnieController {

    private final CompagnieService compagnieService;
    private final AvionService avionService;
    private final VolService volService;

    public CompagnieController(CompagnieService compagnieService, AvionService avionService, VolService volService) {
        this.compagnieService = compagnieService;
        this.avionService = avionService;
        this.volService = volService;
    }

    @GetMapping
    public List<Compagnie> getCompagnies() {
        return compagnieService.getCompagnies();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Compagnie> getCompagnie(@PathVariable int id) {
        return compagnieService.getCompagnie(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Compagnie> creerCompagnie(@RequestBody Compagnie compagnie) {
        Compagnie cree = compagnieService.creerCompagnie(compagnie);
        return ResponseEntity.status(HttpStatus.CREATED).body(cree);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Compagnie> modifierCompagnie(@PathVariable int id, @RequestBody Compagnie compagnie) {
        return compagnieService.modifierCompagnie(id, compagnie)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerCompagnie(@PathVariable int id) {
        if (compagnieService.supprimerCompagnie(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // GET /Compagnie/1/avions -> la flotte de la compagnie
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

    // GET /Compagnie/1/vols -> les vols operes par la compagnie
    @GetMapping("/{id}/vols")
    public ResponseEntity<List<Vol>> getVols(@PathVariable int id) {
        if (compagnieService.getCompagnie(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(volService.getVolsDeLaCompagnie(id));
    }
}
