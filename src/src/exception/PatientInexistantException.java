package src.exception;

/**
 * Exception levée lorsqu'un patient spécifique n'est pas trouvé dans le système..
 */
public class PatientInexistantException extends Exception {
    /**
     * Constructeur prenant un message d'erreur en paramètre.
     *
     * @param message Le message d'erreur décrivant l'exception
     */
    public PatientInexistantException(String message) {
        super(message);
    }
}
