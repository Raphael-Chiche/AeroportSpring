package com.example.aeroportspring.model;


import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
public class
Avion {
    private static int CPT = 0;

    @Id
    private int id;
    @Setter
    @Enumerated(EnumType.STRING) // stocke "A380" en base plutot que son numero
    private ModeleAvion modeleAvion;
    @Setter
    @ManyToOne
    private Compagnie compagnie;
    @Setter
    private boolean enVol;
    @Setter
    private int capacite;

    protected Avion() {
        this.id = CPT++;
    }

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
