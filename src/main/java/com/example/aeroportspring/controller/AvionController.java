package com.example.aeroportspring.controller;

import com.example.aeroportspring.model.Avion;
import com.example.aeroportspring.model.Compagnie;
import com.example.aeroportspring.service.AvionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Avion")
public class AvionController {

    private final AvionService avionService;

    public AvionController(AvionService avionService) {
        this.avionService = avionService;
    }

    @GetMapping
    public List<Avion> getAvions(){
        return avionService.getAvions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Avion> getAvion(@PathVariable int id){
        return avionService.getAvion(id)
                .map(ResponseEntity::ok)                     // trouve -> 200 + JSON
                .orElse(ResponseEntity.notFound().build());  // absent -> 404
    }

    @PostMapping
    public ResponseEntity<Avion> creerAvion(@RequestBody Avion avion){
        Avion create = avionService.creerAvion(avion);
        return ResponseEntity.status(HttpStatus.CREATED).body(create);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Avion> modifierAvion(@PathVariable int id, @RequestBody Avion avion){
        return avionService.modifierAvion(id, avion)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerAvion(@PathVariable int id){
        if(avionService.supprimerAvion(id)){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/compagnie")
    public ResponseEntity<Avion> modifierCompagnieAvion(@PathVariable int id, @RequestBody Compagnie compagnie){
        return avionService.modifierCompagnieAvion(id, compagnie)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/enVol")
    public ResponseEntity<Avion> modifierIsVolAvion(@PathVariable int id, @RequestParam boolean enVol){
        return avionService.modifierIsVolAvion(id, enVol)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
