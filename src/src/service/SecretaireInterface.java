package src.service;

import src.Dao.fileBase.CaisseDao;
import src.entity.*;
import src.exception.FormulaireException;

public interface SecretaireInterface  {
    void createPatient(Patient patient) throws FormulaireException;
    void updatePatient(Patient patient) throws FormulaireException;
    void deletePatient(long patientId) throws FormulaireException;
    void createDossierMedical(DossierMedicale dossierMedical, long patientId) throws FormulaireException;
    void updateDossierMedical(DossierMedicale dossierMedical) throws FormulaireException;
    void deleteDossierMedical(long dossierMedicalId) throws FormulaireException;

    SituationFinanciere createSituationFinanciere() throws FormulaireException;

    void ajouterFactureASituationFinanciere(Long idSituationFinanciere, Facture facture) throws FormulaireException;

    void ajouterSituationFinanciereACaisse(Long idCaisse, SituationFinanciere situationFinanciere) throws FormulaireException;

    void modifierMontantPayeFacture(Long idFacture, Double nouveauMontant) throws FormulaireException;

    public Caisse createCaisse( Long situationFinanciereId) throws FormulaireException;
    void enregistrerPaiement(Facture facture, double montantPaye) throws FormulaireException;

    double consulterSoldeCaisse() throws FormulaireException;
    void ajouterAuSoldeCaisse(double montant) throws FormulaireException;
    void retirerDuSoldeCaisse(double montant) throws FormulaireException;
}
