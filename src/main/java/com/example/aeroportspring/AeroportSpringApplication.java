package com.example.aeroportspring;

import com.example.aeroportspring.model.*;
import com.example.aeroportspring.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Date;

@SpringBootApplication
public class AeroportSpringApplication {

    private static int reussis = 0;
    private static int echoues = 0;

    public static void main(String[] args) {
        SpringApplication.run(AeroportSpringApplication.class, args);
    }

    // Lance les tests au demarrage de l'application, une fois les services crees par Spring
    @Bean
    CommandLineRunner tests(AeroportService aeroportService, CompagnieService compagnieService,
                            AvionService avionService, TerminalService terminalService,
                            PassagerService passagerService, PersonnelService personnelService,
                            VolService volService, PlatformTransactionManager transactionManager) {
        TransactionTemplate transaction = new TransactionTemplate(transactionManager);
        return args -> transaction.executeWithoutResult(status -> {
            System.out.println("\n========== TESTS ==========");

            // ----- Creation des donnees -----
            Aeroport cdg = aeroportService.creerAeroport(new Aeroport("Roissy", "CDG", "France", "UTC+1"));
            Aeroport orly = aeroportService.creerAeroport(new Aeroport("Orly", "ORY", "France", "UTC+1"));
            Compagnie airFrance = compagnieService.creerCompagnie(new Compagnie("Air France"));
            Avion grandAvion = avionService.creerAvion(new Avion(ModeleAvion.A380, airFrance, false, 2));
            Avion petitAvion = avionService.creerAvion(new Avion(ModeleAvion.A340, null, false, 1));
            Terminal t1 = terminalService.creerTerminal(new Terminal(null, "T1", null));
            Terminal t2 = terminalService.creerTerminal(new Terminal(null, "T2", null));
            Passager jean = passagerService.creerPassager(new Passager("Dupont", "Jean"));
            Passager paul = passagerService.creerPassager(new Passager("Martin", "Paul"));
            Passager marie = passagerService.creerPassager(new Passager("Durand", "Marie"));
            Personnel luc = personnelService.creerPersonnel(new Personnel("Bernard", "Luc", Profession.STEWART));
            Vol vol = volService.creerVol(new Vol(null, null, new Date(), new Date(), null, null, 150f, "2h"));
            Vol autreVol = volService.creerVol(new Vol(null, null, new Date(), new Date(), null, null, 90f, "1h"));

            System.out.println("\n--- CRUD ---");
            verifier("Aeroport retrouve par son id", aeroportService.getAeroport(cdg.getId()).isPresent());
            verifier("Id inconnu -> vide", passagerService.getPassager(999).isEmpty());
            compagnieService.modifierCompagnie(airFrance.getId(), new Compagnie("Air France KLM"));
            // On relit en base : findById renvoie un nouvel objet, "airFrance" n'est pas modifie directement
            verifier("Compagnie modifiee",
                    compagnieService.getCompagnie(airFrance.getId()).get().getNom().equals("Air France KLM"));
            verifier("Suppression d'un id inconnu -> false", !volService.supprimerVol(999));

            System.out.println("\n--- Modifications ciblees ---");
            passagerService.modifierBagage(jean.getId(), Bagage.SOUTE);
            verifier("Bagage modifie", jean.getBagage() == Bagage.SOUTE);
            personnelService.modifierProfession(luc.getId(), Profession.PILOTE);
            verifier("Profession modifiee", luc.getProfession() == Profession.PILOTE);

            System.out.println("\n--- Passagers d'un vol ---");
            verifier("Passager sans passeport refuse",
                    leveErreur(() -> volService.ajouterPassager(vol.getId(), jean.getId())));
            passagerService.modifierPasseport(jean.getId(), true);
            passagerService.modifierPasseport(paul.getId(), true);
            passagerService.modifierPasseport(marie.getId(), true);
            verifier("Passeport modifie", jean.getPasseport());

            volService.affecterAvion(vol.getId(), grandAvion.getId());
            verifier("Avion affecte au vol", vol.getAvion().getId().equals(grandAvion.getId()));

            volService.ajouterPassager(vol.getId(), jean.getId());
            volService.ajouterPassager(vol.getId(), paul.getId());
            verifier("2 passagers ajoutes", vol.getNombrePassagers() == 2);
            volService.ajouterPassager(vol.getId(), jean.getId());
            verifier("Pas de doublon si on ajoute 2 fois le meme passager", vol.getNombrePassagers() == 2);
            verifier("Vol complet -> 3e passager refuse",
                    leveErreur(() -> volService.ajouterPassager(vol.getId(), marie.getId())));
            verifier("Passager ou vol inconnu -> vide", volService.ajouterPassager(vol.getId(), 999).isEmpty());

            verifier("Avion trop petit pour les passagers refuse",
                    leveErreur(() -> volService.affecterAvion(vol.getId(), petitAvion.getId())));
            verifier("L'avion n'a pas change", vol.getAvion().getId().equals(grandAvion.getId()));

            verifier("Vols de Jean = 1", volService.getVolsDuPassager(jean.getId()).size() == 1);
            volService.retirerPassager(vol.getId(), paul.getId());
            verifier("Paul retire du vol", !vol.getPassagers().contains(paul));
            verifier("Vols de Paul = 0", volService.getVolsDuPassager(paul.getId()).isEmpty());

            System.out.println("\n--- Personnel d'un vol ---");
            volService.ajouterPersonnel(vol.getId(), luc.getId());
            verifier("Personnel ajoute au vol", vol.getPersonnels().contains(luc));
            verifier("Vols de Luc = 1", volService.getVolsDuPersonnel(luc.getId()).size() == 1);
            volService.retirerPersonnel(vol.getId(), luc.getId());
            verifier("Personnel retire du vol", vol.getPersonnels().isEmpty());

            System.out.println("\n--- Terminal de depart ---");
            volService.affecterTerminal(vol.getId(), t1.getId());
            verifier("Vol part du T1", vol.getDepart() == t1);
            verifier("T1 connait son vol", t1.getVol() == vol);
            verifier("T1 deja occupe -> refuse pour un autre vol",
                    leveErreur(() -> volService.affecterTerminal(autreVol.getId(), t1.getId())));
            volService.affecterTerminal(vol.getId(), t2.getId());
            verifier("Vol deplace vers T2", vol.getDepart() == t2 && t2.getVol() == vol);
            verifier("T1 libere", t1.getVol() == null);
            volService.retirerTerminal(vol.getId());
            verifier("Terminal retire du vol", vol.getDepart() == null && t2.getVol() == null);

            System.out.println("\n--- Compagnie, destination, avion ---");
            volService.affecterCompagnie(vol.getId(), airFrance.getId());
            verifier("Compagnie affectee", vol.getCompagnie().getId().equals(airFrance.getId()));
            verifier("Vols d'Air France = 1", volService.getVolsDeLaCompagnie(airFrance.getId()).size() == 1);
            volService.affecterDestination(vol.getId(), orly.getId());
            verifier("Destination affectee", vol.getDestination() == orly);
            volService.retirerAvion(vol.getId());
            verifier("Avion retire du vol", vol.getAvion() == null);

            System.out.println("\n--- Aeroport ---");
            aeroportService.ajouterTerminal(cdg.getId(), t1.getId());
            verifier("T1 rattache a CDG", cdg.getTerminals().contains(t1) && t1.getAeroport() == cdg);
            aeroportService.ajouterTerminal(orly.getId(), t1.getId());
            verifier("T1 deplace vers Orly", orly.getTerminals().contains(t1) && t1.getAeroport() == orly);
            verifier("T1 n'est plus a CDG", !cdg.getTerminals().contains(t1));
            aeroportService.retirerTerminal(orly.getId(), t1.getId());
            verifier("T1 retire d'Orly", orly.getTerminals().isEmpty() && t1.getAeroport() == null);

            aeroportService.ajouterPersonnel(cdg.getId(), luc.getId());
            verifier("Personnel ajoute a CDG", cdg.getPersonnels().contains(luc));
            aeroportService.retirerPersonnel(cdg.getId(), luc.getId());
            verifier("Personnel retire de CDG", cdg.getPersonnels().isEmpty());
            verifier("Aeroport inconnu -> vide", aeroportService.ajouterPersonnel(999, luc.getId()).isEmpty());

            System.out.println("\n========== " + reussis + " reussi(s), " + echoues + " echoue(s) ==========\n");

            // On annule les donnees de test a la fin. Obligatoire : les tests "leveErreur" font lever
            // des exceptions dans les services, ce qui marque la transaction "a annuler" ; un commit planterait
            status.setRollbackOnly();
        });
    }

    private static void verifier(String nom, boolean condition) {
        if (condition) {
            reussis++;
            System.out.println("[OK]    " + nom);
        } else {
            echoues++;
            System.out.println("[ECHEC] " + nom);
        }
    }

    // Renvoie true si l'action a bien ete refusee par une regle metier
    private static boolean leveErreur(Runnable action) {
        try {
            action.run();
            return false;
        } catch (IllegalStateException e) {
            System.out.println("        -> " + e.getMessage());
            return true;
        }
    }
}
