package com.example.aeroportspring.controller;

import com.example.aeroportspring.model.Passager;
import com.example.aeroportspring.service.PassagerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Passager")
public class PassagerController {

    private final PassagerService passagerService;

    public PassagerController(PassagerService passagerService) {
        this.passagerService = passagerService;
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
}
