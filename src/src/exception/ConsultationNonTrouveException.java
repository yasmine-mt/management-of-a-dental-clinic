package src.exception;


public class ConsultationNonTrouveException extends Exception {

    public ConsultationNonTrouveException(String message) {
        super(message);
    }

    public ConsultationNonTrouveException(String message, Throwable cause) {
        super(message, cause);
    }
}
