package src.exception;


public class FormulaireException extends Exception {
    public FormulaireException() {
        super();
    }

    public FormulaireException(String message) {
        super(message);
    }

    public FormulaireException(String message, Throwable cause) {
        super(message, cause);
    }

    public FormulaireException(Throwable cause) {
        super(cause);
    }
}

