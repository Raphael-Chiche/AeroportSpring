package com.example.aeroportspring.model;

public class Passager {
    private static int CPT = 0;

    private int id;
    private String nom;
    private String prenom;
    private Boolean passeport;
    private Bagage bagage = Bagage.SAC;

    public Passager(String nom, String prenom) {
        this.id = CPT++;
        this.nom = nom;
        this.prenom = prenom;
        this.passeport = false;
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
