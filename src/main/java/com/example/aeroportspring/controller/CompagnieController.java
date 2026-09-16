package com.example.aeroportspring.controller;

import com.example.aeroportspring.model.Compagnie;
import com.example.aeroportspring.service.CompagnieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Compagnie")
public class CompagnieController {

    private final CompagnieService compagnieService;

    public CompagnieController(CompagnieService compagnieService) {
        this.compagnieService = compagnieService;
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
}
