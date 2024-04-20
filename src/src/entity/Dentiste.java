package src.entity;

import src.entity.Classe_G.Utilisateur;
import src.entity.enums.Specialite;


/**
 * La classe Dentiste représente un professionnel dentiste travaillant dans un cabinet dentaire.
 */
public class Dentiste extends Utilisateur {
    private Double salaireDeBase;
    private Specialite specialite;


    public Double getSalaireDeBase() {
        return salaireDeBase;
    }

    public void setSalaireDeBase(Double salaireDeBase) {
        this.salaireDeBase = salaireDeBase;
    }

    public Specialite getSpecialite() {
        return specialite;
    }

    public void setSpecialite(Specialite specialite) {
        this.specialite = specialite;
    }

    @Override
    public String toString() {
        return "Dentiste Information:\n" +
                "ID: " + getId() + "\n" +
                "Nom: " + getNom() + "\n" +
                "Prénom: " + getPrenom() + "\n" +
                "Salaire de Base: " + salaireDeBase + "\n" +
                "Spécialité: " + specialite + "\n";
    }

}
