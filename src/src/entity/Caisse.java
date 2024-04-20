package src.entity;

import java.time.LocalDate;
import java.util.List;
/**
 * La classe Caisse représente la gestion financière globale d'un cabinet médical ou dentaire.
 */
public class Caisse {
    private List<SituationFinanciere> situationFinanciere;
    private Double recetteDuJour ;
    private Double recetteDumois;
    private Double recetteDeLAnnee;
    private Long idCaisse;
    public Caisse(){}

    public Caisse(List<SituationFinanciere> situationFinanciere, Double recetteDuJour, Double recetteDumois, Double recetteDeLAnnee, Long idCaisse) {
        this.situationFinanciere = situationFinanciere;
        this.recetteDuJour = recetteDuJour;
        this.recetteDumois = recetteDumois;
        this.recetteDeLAnnee = recetteDeLAnnee;
        this.idCaisse = idCaisse;
    }

    public List<SituationFinanciere> getSituationFinanciere() {
        return situationFinanciere;
    }

    public void setSituationFinanciere(List<SituationFinanciere> situationFinanciere) {
        this.situationFinanciere = situationFinanciere;
    }

    public Double getRecetteDuJour() {
        return recetteDuJour;
    }

    public void setRecetteDuJour(Double recetteDuJour) {
        this.recetteDuJour = recetteDuJour;
    }

    public Double getRecetteDumois() {
        return recetteDumois;
    }

    public void setRecetteDumois(Double recetteDumois) {
        this.recetteDumois = recetteDumois;
    }

    public Double getRecetteDeLAnnee() {
        return recetteDeLAnnee;
    }

    public void setRecetteDeLAnnee(Double recetteDeLAnnee) {
        this.recetteDeLAnnee = recetteDeLAnnee;
    }

    public Long getIdCaisse() {
        return idCaisse;
    }

    public void setIdCaisse(Long idCaisse) {
        this.idCaisse = idCaisse;
    }

    public Double recetteDuJour(LocalDate date) {
        recetteDuJour = 0.0;
        for (SituationFinanciere situation : situationFinanciere) {
            for (Facture facture : situation.getFactures()) {
                if (facture.getDateFacturation().equals(date)) {
                    recetteDuJour += facture.getMontantPaye();
                }
            }
        }
        return recetteDuJour;
    }

    public Double recetteDumois(int year, int month) {
        recetteDumois = 0.0;
        for (SituationFinanciere situation : situationFinanciere) {
            for (Facture facture : situation.getFactures()) {
                if (facture.getDateFacturation().getYear() == year &&
                        facture.getDateFacturation().getMonthValue() == month) {
                    recetteDumois += facture.getMontantPaye();
                }
            }
        }
        return recetteDumois;
    }
    public Double recetteDeLAnnee(int year) {
        recetteDeLAnnee = 0.0;
        for (SituationFinanciere situation : situationFinanciere) {
            for (Facture facture : situation.getFactures()) {
                if (facture.getDateFacturation().getYear() == year) {
                    recetteDeLAnnee += facture.getMontantPaye();
                }
            }
        }
        return recetteDeLAnnee;
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Caisse {");
        sb.append("\n  idCaisse=").append(idCaisse);
        sb.append("\n  recetteDuJour=").append(recetteDuJour);
        sb.append("\n  recetteDumois=").append(recetteDumois);
        sb.append("\n  recetteDeLAnnee=").append(recetteDeLAnnee);
        sb.append("\n  situationFinanciere=[");

        if (situationFinanciere != null && !situationFinanciere.isEmpty()) {
            for (SituationFinanciere situation : situationFinanciere) {
                sb.append("\n    ").append(situation);
            }
        } else {
            sb.append("No financial situations available");
        }

        sb.append("\n  ]");
        sb.append("\n}");
        return sb.toString();
    }

}
