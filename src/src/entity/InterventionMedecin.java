package src.entity;

import src.entity.enums.CategorieActe;



/**
 * La classe InterventionMedecin représente une intervention réalisée par un dentiste lors d'une consultation.
 */


public class InterventionMedecin {
    private static long dernierIdIntervention = 0;

    private String noteMedecin;
    private Double prixPatient;
    private Long dent;
    private Long idIntervention;
    private CategorieActe acte;
    private Long idConsultation;

    public InterventionMedecin() {}

    public InterventionMedecin(String noteMedecin, Double prixPatient, Long dent, Long idIntervention, CategorieActe acte, Long idConsultation) {
        this.noteMedecin = noteMedecin;
        this.prixPatient = prixPatient;
        this.dent = dent;
        this.idIntervention = idIntervention;
        this.acte = acte;
        this.idConsultation = idConsultation;
    }

    public String getNoteMedecin() {
        return noteMedecin;
    }

    public void setNoteMedecin(String noteMedecin) {
        this.noteMedecin = noteMedecin;
    }

    public Double getPrixPatient() {
        return prixPatient;
    }

    public void setPrixPatient(Double prixPatient) {
        this.prixPatient = prixPatient;
    }

    public Long getDent() {
        return dent;
    }

    public void setDent(Long dent) {
        this.dent = dent;
    }

    public Long getIdIntervention() {
        return idIntervention;
    }

    public void setIdIntervention(Long idIntervention) {
        this.idIntervention = idIntervention;
    }

    public CategorieActe getActe() {
        return acte;
    }

    public void setActe(CategorieActe acte) {
        this.acte = acte;
    }

    public static long getDernierIdIntervention() {
        return dernierIdIntervention;
    }

    public static void setDernierIdIntervention(long dernierIdIntervention) {
        InterventionMedecin.dernierIdIntervention = dernierIdIntervention;
    }

    public Long getIdConsultation() {
        return idConsultation;
    }

    public void setIdConsultation(Long idConsultation) {
        this.idConsultation = idConsultation;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        String horizontalLine = "+-------------------------------------+\n";

        builder.append(horizontalLine);
        builder.append("|   Informations sur l'intervention médicale   |\n");
        builder.append(horizontalLine);
        builder.append("| Identifiant de l'intervention: ").append(idIntervention).append("\n");
        builder.append("| Identifiant de la consultation: ").append(idConsultation).append("\n");
        builder.append("| Type d'acte: ");

        if (acte != null) {
            builder.append(acte.toString());
        } else {
            builder.append("null");
        }

        builder.append("\n| Prix pour le patient: ").append(prixPatient).append("\n");
        builder.append("| Dent: ").append(dent).append("\n");
        builder.append("| Note du médecin: '").append(noteMedecin).append('\'').append("\n");
        builder.append(horizontalLine);

        return builder.toString();
    }


}

