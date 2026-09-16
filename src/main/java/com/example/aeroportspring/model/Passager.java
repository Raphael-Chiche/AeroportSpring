package com.example.aeroportspring.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

@Entity
public class Passager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nom;
    private String prenom;
    private Boolean passeport;
    @Enumerated(EnumType.STRING)
    private Bagage bagage = Bagage.SAC;

    // Constructeur vide obligatoire pour JPA
    protected Passager() {
    }

    public Passager(String nom, String prenom) {
        this.nom = nom;
        this.prenom = prenom;
        this.passeport = false;
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

    public Boolean getPasseport() {
        return this.passeport;
    }

    public void setPasseport(Boolean passeport) {
        this.passeport = passeport;
    }

    public Bagage getBagage() {
        return this.bagage;
    }

    public void setBagage(Bagage bagage) {
        this.bagage = bagage;
    }

    public void ajouterVol(Vol vol) {
        if (vol != null && passeport == true) {
            vol.ajouterPassager(this);
        } else {
            System.out.println("Go back to your country !");
        }
    }

    public void retirerVol(Vol vol) {
        if (vol != null) {
            vol.retirerPassager(this);
        }
    }
}
