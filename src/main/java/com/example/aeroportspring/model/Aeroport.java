package com.example.aeroportspring.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Aeroport {
    private static int CPT = 0;

    @Id
    private int id;
    @Setter
    private String adresse;
    // Un aeroport a plusieurs terminaux. "mappedBy" : c'est le champ "aeroport" de Terminal qui porte la cle etrangere
    @OneToMany(mappedBy = "aeroport")
    private List<Terminal> terminals = new ArrayList<>();
    @Setter
    private String nom;
    @Setter
    private String pays;
    @Setter
    private String UTC;
    // Un aeroport a plusieurs personnels (Personnel ne connait pas son aeroport -> table de jointure)
    @OneToMany
    @JoinTable(name = "aeroport_personnel")
    private List<Personnel> personnels = new ArrayList<>();

    // Constructeur vide obligatoire pour JPA
    protected Aeroport() {
        this.id = CPT++;
    }

    public Aeroport(String adresse, String nom, String pays, String UTC) {
        this.id = CPT++;
        this.adresse = adresse;
        this.terminals = new ArrayList<Terminal>();
        this.nom = nom;
        this.pays = pays;
        this.UTC = UTC;
        this.personnels = new ArrayList<Personnel>();
    }

    public int getId() {
        return this.id;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public List<Terminal> getTerminals() {
        return this.terminals;
    }

    public String getNom() {
        return this.nom;
    }

    public String getPays() {
        return this.pays;
    }

    public String getUTC() {
        return this.UTC;
    }

    public List<Personnel> getPersonnels() {
        return this.personnels;
    }

    /** Ajoute un terminal a l'aeroport, sans doublon, et le rattache a cet aeroport. */
    public void ajouterTerminal(Terminal terminal) {
        if (terminal != null && !this.terminals.contains(terminal)) {
            this.terminals.add(terminal);
            terminal.setAeroport(this);
        }
    }

    public void retirerTerminal(Terminal terminal) {
        if (this.terminals.remove(terminal)) {
            terminal.setAeroport(null);
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
