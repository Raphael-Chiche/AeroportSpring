package com.example.aeroportspring.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

@Entity
public class Personnel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nom;
    private String prenom;
    @Enumerated(EnumType.STRING)
    private Profession profession;

    // Constructeur vide obligatoire pour JPA
    protected Personnel() {
    }

    public Personnel(String nom, String prenom, Profession profession) {
        this.nom = nom;
        this.prenom = prenom;
        this.profession = profession;
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
