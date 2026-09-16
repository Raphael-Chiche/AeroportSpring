package com.example.aeroportspring.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Compagnie {
    private static int CPT = 0;
    @Id
    private int id;
    private String nom;

    protected Compagnie() {
        this.id = CPT++;
    }

    public Compagnie(String nom) {
        this.id = CPT++;
        this.nom = nom;
    }

    public int getId() {
        return this.id;
    }

    public String getNom() {
        return this.nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
