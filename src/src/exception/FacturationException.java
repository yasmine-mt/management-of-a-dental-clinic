package src.exception;

/**
 * Exception liée à des problèmes de facturation, tels que des calculs incorrects ou des données manquantes.
 */
public class FacturationException extends Exception {
    /**
     * Constructeur prenant un message d'erreur en paramètre.
     *
     * @param message Le message d'erreur décrivant l'exception
     */
    public FacturationException(String message) {
        super(message);
    }
}
