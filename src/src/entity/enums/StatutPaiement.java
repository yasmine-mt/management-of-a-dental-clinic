package src.entity.enums;

public enum StatutPaiement {
    EN_ATTENTE,IMPAYE,PAYE;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
