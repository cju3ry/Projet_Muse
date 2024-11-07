package gestion_donnees;

/**
 * La classe EmployeException représente une exception spécifique aux employés.
 * Elle est utilisée pour signaler des erreurs liées aux employés dans l'application.
 */
public class EmployeException extends Exception {

    /**
     * Constructeur par défaut de la classe EmployeException.
     * Crée une nouvelle instance de EmployeException sans message d'erreur.
     */
    public EmployeException() {
        super();
    }

    /**
     * Constructeur de la classe EmployeException avec un message d'erreur.
     * Crée une nouvelle instance de EmployeException avec le message spécifié.
     *
     * @param message le message d'erreur.
     */
    public EmployeException(String message) {
        super(message);
    }

    /**
     * Constructeur de la classe EmployeException avec un message d'erreur et une cause.
     * Crée une nouvelle instance de EmployeException avec le message et la cause spécifiés.
     *
     * @param message le message d'erreur.
     * @param cause la cause de l'exception.
     */
    public EmployeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructeur de la classe EmployeException avec une cause.
     * Crée une nouvelle instance de EmployeException avec la cause spécifiée.
     *
     * @param cause la cause de l'exception.
     */
    public EmployeException(Throwable cause) {
        super(cause);
    }
}