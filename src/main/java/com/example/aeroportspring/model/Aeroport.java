package com.example.aeroportspring.model;

import java.util.ArrayList;
import java.util.List;

public class Aeroport {
    private static int CPT = 0;

    private int id;
    private String adresse;
    private List<Terminal> terminals;
    private String nom;
    private String pays;
    private String UTC;
    private List<Personnel> personnels;

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

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public List<Terminal> getTerminals() {
        return this.terminals;
    }

    public String getNom() {
        return this.nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPays() {
        return this.pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getUTC() {
        return this.UTC;
    }

    public void setUTC(String UTC) {
        this.UTC = UTC;
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
