package src.exception;

/**
 * Exception levée lorsqu'une consultation spécifique n'est pas trouvée dans le système.
 */
public class ConsultationNonTrouveeException extends Exception {
    /**
     * Constructeur prenant un message d'erreur en paramètre.
     *
     * @param message Le message d'erreur décrivant l'exception
     */
    public ConsultationNonTrouveeException(String message) {
        super(message);
    }
}
