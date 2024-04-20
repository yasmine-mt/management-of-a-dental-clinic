package src.exception;

/**
 * Exception levée lorsqu'un patient est déjà créé o.
 */
public class PatientExistantException extends Exception {
    /**
     * Constructeur prenant un message d'erreur en paramètre.
     *
     * @param message Le message d'erreur décrivant l'exception
     */
    public PatientExistantException(String message) {
        super(message);
    }
}
