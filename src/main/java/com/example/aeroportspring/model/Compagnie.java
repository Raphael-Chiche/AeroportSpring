package com.example.aeroportspring.model;

public class Compagnie {
    private static int CPT = 0;

    private int id;
    private String nom;

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
