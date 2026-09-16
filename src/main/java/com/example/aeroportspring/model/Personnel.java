package com.example.aeroportspring.model;

public class Personnel {
    private static int CPT = 0;

    private int id;
    private String nom;
    private String prenom;
    private Profession profession;

    public Personnel(String nom, String prenom, Profession profession) {
        this.id = CPT++;
        this.nom = nom;
        this.prenom = prenom;
        this.profession = profession;
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

    public String getPrenom() {
        return this.prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Profession getProfession() {
        return this.profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }
}
