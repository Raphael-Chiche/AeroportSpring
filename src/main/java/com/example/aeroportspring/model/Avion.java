package com.example.aeroportspring.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
public class Avion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
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
    }

    public Avion(ModeleAvion modeleAvion, Compagnie compagnie, boolean enVol, int capacite) {
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
