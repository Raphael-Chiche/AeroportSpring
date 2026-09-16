package com.example.aeroportspring.model;

import lombok.Getter;
import lombok.Setter;

@Getter
public class
Avion {
    private static int CPT = 0;

    private int id;
    @Setter
    private ModeleAvion modeleAvion;
    @Setter
    private Compagnie compagnie;
    @Setter
    private boolean enVol;
    @Setter
    private int capacite;

    public Avion(ModeleAvion modeleAvion, Compagnie compagnie, boolean enVol, int capacite) {
        this.id = CPT++;
        this.modeleAvion = modeleAvion;
        this.compagnie = compagnie;
        this.enVol = enVol;
        this.capacite = capacite;
    }

    public void retirerCompagnie() {
        this.compagnie = null;
    }

    public void ajouterVol(Vol vol) {
        if (vol != null) {
            vol.setAvion(this);
        }
    }

    public void retirerVol(Vol vol) {
        if (vol != null && vol.getAvion() == this) {
            vol.setAvion(null);
        }
    }
}
