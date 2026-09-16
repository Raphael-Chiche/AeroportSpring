package com.example.aeroportspring.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Compagnie {
    // L'id est genere par la base (1, 2, 3...) au moment du save : plus besoin de compteur
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // Integer et pas int : vaut null tant que la compagnie n'est pas enregistree
    private String nom;

    // Constructeur vide obligatoire pour JPA
    protected Compagnie() {
    }

    public Compagnie(String nom) {
        this.nom = nom;
    }

    public Integer getId() {
        return this.id;
    }

    public String getNom() {
        return this.nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
