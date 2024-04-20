package src.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 * La classe SituationFinanciere représente la situation financière associée à un dossier médical.
 */
public class SituationFinanciere {
    private DossierMedicale dossierMedicale;
    private LocalDate dateCreation;
    private Double montantGlobaleRestant = 0.0;
    private Long idSituationFinanciere;
    private List<Facture> factures;
    public SituationFinanciere()
    {this.factures = new ArrayList<>();
        calculerMontantGlobaleRestant();
    }


    public SituationFinanciere(DossierMedicale dossierMedicale, LocalDate dateCreation,
                               Long idSituationFinanciere, List<Facture> factures) {
        this.dossierMedicale = dossierMedicale;
        this.dateCreation = dateCreation;
        this.idSituationFinanciere = idSituationFinanciere;
        this.factures = factures;
        this.montantGlobaleRestant = calculerMontantGlobaleRestant();
        calculerMontantGlobaleRestant();
    }

    public DossierMedicale getDossierMedicale() {
        return dossierMedicale;
    }

    public void setDossierMedicale(DossierMedicale dossierMedicale) {
        this.dossierMedicale = dossierMedicale;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Double getMontantGlobaleRestant() {
        return montantGlobaleRestant;
    }

    public void setMontantGlobaleRestant(Double montantGlobaleRestant) {
        this.montantGlobaleRestant = montantGlobaleRestant;
    }

    public Long getIdSituationFinanciere() {
        return idSituationFinanciere;
    }

    public void setIdSituationFinanciere(Long idSituationFinanciere) {
        this.idSituationFinanciere = idSituationFinanciere;
    }

    public List<Facture> getFactures() {
        return factures;
    }

    public void setFactures(List<Facture> factures) {
        this.factures = factures;
    }
    private double calculerMontantGlobaleRestant() {
        for (Facture facture : factures) {
            montantGlobaleRestant += facture.getMontantRestant();
        }

 return montantGlobaleRestant;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        String horizontalLine = "----------------------------------\n";
        sb.append(horizontalLine);
        sb.append("|           SituationFinanciere           |\n");
        sb.append(horizontalLine);
        sb.append("| Dossier Médical : ").append(dossierMedicale).append("\n");
        sb.append("| Date de création : ").append(dateCreation).append("\n");
        sb.append("| Montant global restant : ").append(montantGlobaleRestant).append("\n");
        sb.append("| ID Situation Financière : ").append(idSituationFinanciere).append("\n");
        sb.append("| Factures : ").append(factures).append("\n");
        sb.append(horizontalLine);
        sb.append("}");
        return sb.toString();
    }

}

