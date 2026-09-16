package com.example.aeroportspring.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class Terminal {
    private static int CPT = 1;
    @Id
    int id;
    // On coupe les references circulaires (Vol -> Terminal -> Vol, Aeroport -> Terminal -> Aeroport)
    // pour que la conversion en JSON ne tourne pas en boucle
    // Cote "inverse" : c'est le champ "depart" de Vol qui porte la cle etrangere
    @OneToOne(mappedBy = "depart")
    @JsonIgnoreProperties({"depart", "destination"})
    private Vol vol;
    private String nom;
    // Plusieurs terminaux -> un aeroport : colonne aeroport_id dans la table terminal
    @ManyToOne
    @JsonIgnoreProperties("terminals")
    private Aeroport aeroport;

    protected Terminal() {
        this.id = CPT++;
    }

    public Terminal(Vol vol, String nom, Aeroport aeroport) {
        this.id = CPT++; // équivalent à this.id = CPT et CPT = CPT + 1
        this.vol = vol;
        this.nom = nom;
        this.aeroport = aeroport;
    }

    public int getId() {
        return this.id;
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
