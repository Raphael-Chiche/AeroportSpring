package com.example.aeroportspring.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Vol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    // Plusieurs vols -> une compagnie / un avion / une destination
    @ManyToOne
    private Compagnie compagnie;
    // MERGE : les avions ne sont pas encore enregistres par AvionService,
    // on les enregistre donc en base en meme temps que le vol
    @ManyToOne(cascade = CascadeType.MERGE)
    private Avion avion;
    // Un vol a plusieurs passagers, un passager peut prendre plusieurs vols
    @ManyToMany
    @JoinTable(name = "vol_passager")
    private List<Passager> passagers = new ArrayList<>();
    private Date dateDepart;
    private Date dateArrivee;
    @ManyToOne
    private Aeroport destination;
    // Un vol <-> un terminal de depart (cote proprietaire : colonne depart_id dans la table vol)
    @OneToOne
    private Terminal depart;
    private float prix;
    private String duree;
    @ManyToMany
    @JoinTable(name = "vol_personnel")
    private List<Personnel> personnels = new ArrayList<>();

    // Constructeur vide obligatoire pour JPA
    protected Vol() {
    }

    public Vol(Compagnie compagnie, Avion avion, Date heureDepart, Date heureArrivee,
               Aeroport destination, Terminal depart, float prix, String duree) {
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

    public Integer getId() {
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
