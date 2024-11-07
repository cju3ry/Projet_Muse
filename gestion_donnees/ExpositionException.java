package gestion_donnees;

/**
 * La classe ExpositionException représente une exception spécifique aux expositions.
 * Elle est utilisée pour signaler des erreurs liées aux expositions dans l'application.
 */
public class ExpositionException extends Exception {

    /**
     * Constructeur par défaut de la classe ExpositionException.
     * Crée une nouvelle instance de ExpositionException sans message d'erreur.
     */
    public ExpositionException() {
        super();
    }

    /**
     * Constructeur de la classe ExpositionException avec un message d'erreur.
     * Crée une nouvelle instance de ExpositionException avec le message spécifié.
     *
     * @param message le message d'erreur.
     */
    public ExpositionException(String message) {
        super(message);
    }

    /**
     * Constructeur de la classe ExpositionException avec un message d'erreur et une cause.
     * Crée une nouvelle instance de ExpositionException avec le message et la cause spécifiés.
     *
     * @param message le message d'erreur.
     * @param cause la cause de l'exception.
     */
    public ExpositionException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructeur de la classe ExpositionException avec une cause.
     * Crée une nouvelle instance de ExpositionException avec la cause spécifiée.
     *
     * @param cause la cause de l'exception.
     */
    public ExpositionException(Throwable cause) {
        super(cause);
    }
}