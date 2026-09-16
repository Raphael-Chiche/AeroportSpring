package com.example.aeroportspring.controller;

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
}
