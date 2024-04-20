package src.exception;



public class InterventionMedecinNonTrouveException extends RuntimeException {
    private Long interventionId;

    public InterventionMedecinNonTrouveException(String message, Long interventionId) {
        super(message);
        this.interventionId = interventionId;
    }
    public InterventionMedecinNonTrouveException(String message) {
        super(message);
    }

    public Long getInterventionId() {
        return interventionId;
    }
}
