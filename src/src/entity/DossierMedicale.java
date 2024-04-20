package src.entity;

import src.entity.enums.StatutPaiement;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 * La classe DossierMedicale représente le dossier médical d'un patient, contenant ses consultations, informations personnelles et de paiement.
 */
public class DossierMedicale {
    private Long numeroDossier;
    private Patient patient;
    private Dentiste medecin;
    private List<Consultation> consultations;
    private LocalDate dateCreation;
    private StatutPaiement statutPaiement ;


    public DossierMedicale() {
        this.numeroDossier = 0L;
        this.patient = new Patient();
        this.medecin = new Dentiste();
        this.consultations = new ArrayList<>();
        this.dateCreation = LocalDate.now();
        this.statutPaiement = StatutPaiement.PAYE;
    }


    public DossierMedicale(Long numeroDossier, Patient patient, Dentiste medecin, List<Consultation> consultations, LocalDate dateCreation, StatutPaiement statutPaiement) {
        this.numeroDossier = numeroDossier;
        this.patient = patient;
        this.medecin = medecin;
        this.consultations = consultations;
        this.dateCreation = dateCreation;
        this.statutPaiement = statutPaiement;
    }

    public List<Consultation> getConsultations() {
        return consultations;
    }

    public void setConsultations(List<Consultation> consultations) {
        this.consultations = consultations;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Dentiste getMedecin() {
        return medecin;
    }

    public void setMedecin(Dentiste medecin) {
        this.medecin = medecin;
    }

    public Long getNumeroDossier() {
        return numeroDossier;
    }

    public void setNumeroDossier(Long numeroDossier) {
        this.numeroDossier = numeroDossier;
    }

    public StatutPaiement getStatutPaiement() {
        return statutPaiement;
    }

    public void setStatutPaiement(StatutPaiement statutPaiement) {
        this.statutPaiement = statutPaiement;
    }

    @Override

    public String toString() {
        return "DossierMedicale{" +
                "numeroDossier=" + numeroDossier +
                ", patient=\n" + patient + '\'' +
                ", medecin=\n" + medecin + '\'' +
                ", dateCreation=" + dateCreation +
                ", statutPaiement=\n" + statutPaiement + '\'' +
                '}';
    }


}

