package com.example.aeroportspring.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Vol {
    private static int CPT = 0;

    private int id;
    private Compagnie compagnie;
    private Avion avion;
    private List<Passager> passagers;
    private Date dateDepart;
    private Date dateArrivee;
    private Aeroport destination;
    private Terminal depart;
    private float prix;
    private String duree;
    private List<Personnel> personnels;

    public Vol(Compagnie compagnie, Avion avion, Date heureDepart, Date heureArrivee,
               Aeroport destination, Terminal depart, float prix, String duree) {
        this.id = CPT++;
        this.compagnie = compagnie;
        this.avion = avion;
        this.dateDepart = heureDepart;
        this.dateArrivee = heureArrivee;
        this.destination = destination;
        this.depart = depart;
        this.prix = prix;
        this.duree = duree;
        this.passagers = new ArrayList<Passager>();
        this.personnels = new ArrayList<Personnel>();
    }

    public int getId() {
        return this.id;
    }

    public Compagnie getCompagnie() {
        return this.compagnie;
    }

    public void setCompagnie(Compagnie compagnie) {
        this.compagnie = compagnie;
    }

    public Avion getAvion() {
        return this.avion;
    }

    public void setAvion(Avion avion) {
        this.avion = avion;
    }

    public List<Passager> getPassagers() {
        return this.passagers;
    }

    public Date getDateDepart() {
        return this.dateDepart;
    }

    public void setDateDepart(Date dateDepart) {
        this.dateDepart = dateDepart;
    }

    public Date getDateArrivee() {
        return this.dateArrivee;
    }

    public void setDateArrivee(Date dateArrivee) {
        this.dateArrivee = dateArrivee;
    }

    public Aeroport getDestination() {
        return this.destination;
    }

    public void setDestination(Aeroport destination) {
        this.destination = destination;
    }

    public Terminal getDepart() {
        return this.depart;
    }

    public void setDepart(Terminal depart) {
        this.depart = depart;
    }

    public float getPrix() {
        return this.prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public String getDuree() {
        return this.duree;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }

    public List<Personnel> getPersonnels() {
        return this.personnels;
    }

    public void ajouterPassager(Passager passager) {
        if (passager != null && !this.passagers.contains(passager)) {
            this.passagers.add(passager);
        }
    }

    public void retirerPassager(Passager passager) {
        this.passagers.remove(passager);
    }

    public int getNombrePassagers() {
        return this.passagers.size();
    }

    public void ajouterTerminal(Terminal terminal) {
        this.depart = terminal;
        if (terminal != null) {
            terminal.setVol(this);
        }
    }

    public void retirerTerminal() {
        if (this.depart != null) {
            if (this.depart.getVol() == this) {
                this.depart.setVol(null);
            }
            this.depart = null;
        }
    }

    public void ajouterPersonnel(Personnel personnel) {
        if (personnel != null && !this.personnels.contains(personnel)) {
            this.personnels.add(personnel);
        }
    }

    public void retirerPersonnel(Personnel personnel) {
        this.personnels.remove(personnel);
    }
}
