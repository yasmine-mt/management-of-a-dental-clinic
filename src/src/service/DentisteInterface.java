package src.service;
import src.entity.Consultation;
import src.entity.InterventionMedecin;
import src.exception.FormulaireException;
public interface DentisteInterface  {
    void creerConsultation() throws FormulaireException;
    void mettreAJourConsultation() throws FormulaireException;
    void supprimerConsultation(long consultationId) throws FormulaireException;

    void creerInterventionMedecin() throws FormulaireException;
    void mettreAJourInterventionMedecin(InterventionMedecin intervention) throws FormulaireException;
    void supprimerInterventionMedecin(long interventionId) throws FormulaireException;
}
