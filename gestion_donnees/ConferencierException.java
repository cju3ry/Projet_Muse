package gestion_donnees;

/**
 * La classe ConferencierException représente une exception spécifique aux conférenciers.
 * Elle est utilisée pour signaler des erreurs liées aux conférenciers dans l'application.
 */
public class ConferencierException extends Exception {

    /**
     * Constructeur par défaut de la classe ConferencierException.
     * Crée une nouvelle instance de ConferencierException sans message d'erreur.
     */
    public ConferencierException() {
        super();
    }

    /**
     * Constructeur de la classe ConferencierException avec un message d'erreur.
     * Crée une nouvelle instance de ConferencierException avec le message spécifié.
     *
     * @param message le message d'erreur.
     */
    public ConferencierException(String message) {
        super(message);
    }

    /**
     * Constructeur de la classe ConferencierException avec un message d'erreur et une cause.
     * Crée une nouvelle instance de ConferencierException avec le message et la cause spécifiés.
     *
     * @param message le message d'erreur.
     * @param cause la cause de l'exception.
     */
    public ConferencierException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructeur de la classe ConferencierException avec une cause.
     * Crée une nouvelle instance de ConferencierException avec la cause spécifiée.
     *
     * @param cause la cause de l'exception.
     */
    public ConferencierException(Throwable cause) {
        super(cause);
    }
}