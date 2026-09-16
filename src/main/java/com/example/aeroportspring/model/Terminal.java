package com.example.aeroportspring.model;

public class Terminal {
    private static int CPT = 1;

    int id;
    private Vol vol;
    private String nom;
    private Aeroport aeroport;

    public Terminal(Vol vol, String nom, Aeroport aeroport) {
        this.id = CPT++; // équivalent à this.id = CPT et CPT = CPT + 1
        this.vol = vol;
        this.nom = nom;
        this.aeroport = aeroport;
    }

    public Vol getVol() {
        return this.vol;
    }

    public void setVol(Vol vol) {
        this.vol = vol;
    }

    public String getNom() {
        return this.nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Aeroport getAeroport() {
        return this.aeroport;
    }

    public void setAeroport(Aeroport aeroport) {
        this.aeroport = aeroport;
    }
}
