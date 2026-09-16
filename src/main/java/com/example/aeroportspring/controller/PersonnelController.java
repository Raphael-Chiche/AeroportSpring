package com.example.aeroportspring.controller;

import com.example.aeroportspring.model.Personnel;
import com.example.aeroportspring.model.Profession;
import com.example.aeroportspring.model.Vol;
import com.example.aeroportspring.service.PersonnelService;
import com.example.aeroportspring.service.VolService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Personnel")
public class PersonnelController {

    private final PersonnelService personnelService;
    private final VolService volService;

    public PersonnelController(PersonnelService personnelService, VolService volService) {
        this.personnelService = personnelService;
        this.volService = volService;
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

    // PATCH /Personnel/1/profession?profession=PILOTE
    @PatchMapping("/{id}/profession")
    public ResponseEntity<Personnel> modifierProfession(@PathVariable int id, @RequestParam Profession profession) {
        return personnelService.modifierProfession(id, profession)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /Personnel/1/vols -> les vols auxquels ce membre du personnel est affecte
    @GetMapping("/{id}/vols")
    public ResponseEntity<List<Vol>> getVols(@PathVariable int id) {
        if (personnelService.getPersonnel(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(volService.getVolsDuPersonnel(id));
    }
}
