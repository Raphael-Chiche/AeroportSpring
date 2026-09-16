package com.example.aeroportspring.controller;

import com.example.aeroportspring.model.Personnel;
import com.example.aeroportspring.service.PersonnelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Personnel")
public class PersonnelController {

    private final PersonnelService personnelService;

    public PersonnelController(PersonnelService personnelService) {
        this.personnelService = personnelService;
    }

    @GetMapping
    public List<Personnel> getPersonnels() {
        return personnelService.getPersonnels();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Personnel> getPersonnel(@PathVariable int id) {
        return personnelService.getPersonnel(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Personnel> creerPersonnel(@RequestBody Personnel personnel) {
        Personnel cree = personnelService.creerPersonnel(personnel);
        return ResponseEntity.status(HttpStatus.CREATED).body(cree);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Personnel> modifierPersonnel(@PathVariable int id, @RequestBody Personnel personnel) {
        return personnelService.modifierPersonnel(id, personnel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerPersonnel(@PathVariable int id) {
        if (personnelService.supprimerPersonnel(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
