package gestion_donnees;

/**
 * La classe VisiteException représente une exception spécifique aux visites.
 * Elle est utilisée pour signaler des erreurs liées aux visites dans l'application.
 */
public class VisiteException extends Exception {

    /**
     * Constructeur par défaut de la classe VisiteException.
     * Crée une nouvelle instance de VisiteException sans message d'erreur.
     */
    public VisiteException() {
        super();
    }

    /**
     * Constructeur de la classe VisiteException avec un message d'erreur.
     * Crée une nouvelle instance de VisiteException avec le message spécifié.
     *
     * @param message le message d'erreur.
     */
    public VisiteException(String message) {
        super(message);
    }

    /**
     * Constructeur de la classe VisiteException avec un message d'erreur et une cause.
     * Crée une nouvelle instance de VisiteException avec le message et la cause spécifiés.
     *
     * @param message le message d'erreur.
     * @param cause la cause de l'exception.
     */
    public VisiteException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructeur de la classe VisiteException avec une cause.
     * Crée une nouvelle instance de VisiteException avec la cause spécifiée.
     *
     * @param cause la cause de l'exception.
     */
    public VisiteException(Throwable cause) {
        super(cause);
    }
}