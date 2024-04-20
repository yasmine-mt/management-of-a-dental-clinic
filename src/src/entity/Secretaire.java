package src.entity;

import src.entity.Classe_G.Utilisateur;

import java.time.LocalDate;


/**
 * La classe Secretaire représente un employé de type secrétaire travaillant dans un cabinet médical ou dentaire.
 */
public class Secretaire extends Utilisateur {
    private Double salaireDeBase ;

    private Double prime ;

    public Double getSalaireDeBase() {
        return salaireDeBase;
    }

    public void setSalaireDeBase(Double salaireDeBase) {
        this.salaireDeBase = salaireDeBase;
    }


    public Double getPrime() {
        return prime;
    }

    public void setPrime(Double prime) {
        this.prime = prime;
    }
}
