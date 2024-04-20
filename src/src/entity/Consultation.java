package src.entity;

import src.entity.enums.TypeConsultation;

import java.time.LocalDate;
import java.util.List;
/**
 * La classe Consultation représente une visite médicale dans un cabinet médical ou dentaire.
 */
public class Consultation {
    private Long idConsultation;
    private List<InterventionMedecin> interventions ;
    private Long iddossierMedicale ;
    private LocalDate dateConsultation;
    private TypeConsultation typeConsultation;
    private List<Facture>  facture ;


    public Consultation(){}
    public Consultation(Long id) {idConsultation=id;}

    public Consultation(Long idConsultation, List<InterventionMedecin> interventions, Long iddossierMedicale, LocalDate dateConsultation, TypeConsultation typeConsultation, List<Facture> facture) {
        this.idConsultation = idConsultation;
        this.interventions = interventions;
        this.iddossierMedicale = iddossierMedicale;
        this.dateConsultation = dateConsultation;
        this.typeConsultation = typeConsultation;
        this.facture = facture;
    }

    public TypeConsultation getTypeConsultation() {
        return typeConsultation;
    }

    public void setTypeConsultation(TypeConsultation typeConsultation) {
        this.typeConsultation = typeConsultation;
    }

    public Long getIdConsultation() {
        return idConsultation;
    }

    public void setIdConsultation(Long idConsultation) {
        this.idConsultation = idConsultation;
    }

    public List<InterventionMedecin> getInterventions() {
        return interventions;
    }

    public void setInterventions(List<InterventionMedecin> interventions) {
        this.interventions = interventions;
    }



    public Long getIddossierMedicale() {
        return iddossierMedicale;
    }

    public void setIddossierMedicale(Long iddossierMedicale) {
        this.iddossierMedicale = iddossierMedicale;
    }

    public LocalDate getDateConsultation() {
        return dateConsultation;
    }

    public void setDateConsultation(LocalDate dateConsutation) {
        this.dateConsultation = dateConsutation;
    }

    public List<Facture> getFacture() {
        return facture;
    }

    public void setFacture(List<Facture> facture) {
        this.facture = facture;
    }



    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        String horizontalLine = "+-------------------------------------+\n";

        builder.append(horizontalLine);
        builder.append("|           Consultation Details       |\n");
        builder.append(horizontalLine);
        builder.append("| ID Consultation: ").append(idConsultation).append("\n");
        builder.append("| ID Dossier Médical: ").append(iddossierMedicale).append("\n");
        builder.append("| Date Consultation: ").append(dateConsultation).append("\n");
        builder.append("| Type Consultation: ").append(typeConsultation).append("\n");
        if (interventions != null && !interventions.isEmpty()) {
            builder.append("| Interventions:\n");
            for (InterventionMedecin intervention : interventions) {
                builder.append("|   - ").append(intervention).append("\n");
            }
        } else {
            builder.append("| Aucune intervention associée à cette consultation.\n");
        }
        builder.append("|, facture=").append(facture);
        builder.append(horizontalLine);

        return builder.toString();
    }

}
