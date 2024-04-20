package src.entity;

import src.entity.enums.TypePaiement;

import java.text.DecimalFormat;
import java.time.LocalDate;
/**
 * La classe Facture représente une facture générée pour une consultation médicale.
 */
public class Facture {
    private Double montantRestant =0.0;
    private Long idsituationFinanciere ;
    private Long idConsultation ;
    private Double montantPaye;
    private Long idFacture;
    private LocalDate dateFacturation;
    private Double montantTotal;
    private TypePaiement typePaiement ;
    public Facture(){}


    public Facture( Long idsituationFinanciere, Long idConsultation, Double montantPaye, Long idFacture, LocalDate dateFacturation, Double montantTotal, TypePaiement typePaiement) {

        this.idsituationFinanciere = idsituationFinanciere;
        this.idConsultation = idConsultation;
        this.montantPaye = montantPaye;
        this.idFacture = idFacture;
        this.dateFacturation = dateFacturation;
        this.montantTotal = montantTotal;
        this.typePaiement = typePaiement;
        this.montantRestant = CalculmontantRestant();
    }

    public Double getMontantRestant() {
        return montantRestant;
    }

    public void setMontantRestant(Double montantRestant) {
        this.montantRestant = montantRestant;
    }

    public Long getIdsituationFinanciere() {
        return idsituationFinanciere;
    }

    public void setIdsituationFinanciere(Long idsituationFinanciere) {
        this.idsituationFinanciere = idsituationFinanciere;
    }

    public Double getMontantPaye() {
        return montantPaye;
    }

    public void setMontantPaye(Double montantPaye) {
        this.montantPaye = montantPaye;
    }

    public Long getIdFacture() {
        return idFacture;
    }

    public void setIdFacture(Long idFacture) {
        this.idFacture = idFacture;
    }

    public LocalDate getDateFacturation() {
        return dateFacturation;
    }

    public void setDateFacturation(LocalDate dateFacturation) {
        this.dateFacturation = dateFacturation;
    }

    public Double getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(Double montantTotal) {
        this.montantTotal = montantTotal;
    }



    public TypePaiement getTypePaiement() {
        return typePaiement;
    }

    public void setTypePaiement(TypePaiement typePaiement) {
        this.typePaiement = typePaiement;
    }
    private double CalculmontantRestant() {
        if (getMontantTotal() != null && getMontantPaye() != null) {
            return montantRestant = getMontantTotal() - getMontantPaye();
        } else {
            return montantRestant;
        }
    }


    public Long getIdConsultation() {
        return idConsultation;
    }

    public void setIdConsultation(Long idConsultation) {
        this.idConsultation = idConsultation;
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("#0.00");
        StringBuilder sb = new StringBuilder();
        sb.append("Facture {")
                .append("\n\tID: ").append(idFacture)
                .append("\n\tDate de facturation: ").append(dateFacturation)
                .append("\n\tMontant total: ").append(df.format(montantTotal))
                .append("\n\tMontant payé: ").append(df.format(montantPaye))
                .append("\n\tMontant restant: ").append(df.format(montantRestant))
                .append("\n}");
        return sb.toString();
    }

}
