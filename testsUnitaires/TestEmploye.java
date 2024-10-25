package testsUnitaires;

import gestion_donnees.Employe;
import gestion_donnees.EmployeException;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Classe de test pour Employe.
 * Cette classe contient des tests unitaires pour vérifier le bon fonctionnement de la classe Employe.
 */
public class TestEmploye {

    private Employe employe;

    /**
     * Teste le constructeur de la classe Employe avec des paramètres valides.
     * @throws EmployeException si une erreur survient lors de la création de l'employé.
     */
    @Test
    public void testConstructeurOk() throws EmployeException {
        employe = new Employe("1234567", "Johnson", "Billy", "0000");
        assertEquals("1234567", employe.getId());
        assertEquals("Johnson", employe.getNom());
        assertEquals("Billy", employe.getPrenom());
        assertEquals("0000", employe.getNumTel());
    }

    /**
     * Teste le constructeur de la classe Employe avec un identifiant invalide.
     * Vérifie que l'exception EmployeException est bien levée.
     */
    @Test
    public void testConstructeurNOk() {
        assertThrows(EmployeException.class, () -> {
            new Employe("12345", "Johnson", "Billy", "0000");
        });
    }

    /**
     * Teste le constructeur de la classe Employe avec un identifiant null.
     * Vérifie que l'exception EmployeException est bien levée.
     */
    @Test
    public void testConstructeuridNull() {
        assertThrows(EmployeException.class, () -> {
            new Employe(null, "Johnson", "Billy", "0000");
        });
    }

    /**
     * Teste le constructeur de la classe Employe avec un numéro de téléphone invalide.
     * Vérifie que l'exception EmployeException est bien levée.
     */
    @Test
    public void testConstructeurMauvaisTel() {
        assertThrows(EmployeException.class, () -> {
            new Employe("1234567", "Johnson", "Billy", "00000");
        });
    }

    /**
     * Teste le constructeur de la classe Employe avec un numéro de téléphone null.
     * @throws EmployeException si une erreur survient lors de la création de l'employé.
     */
    @Test
    public void testConstructeurNullTel() throws EmployeException {
        employe = new Employe("1234567", "Johnson", "Billy", null);
        assertNull(employe.getNumTel());
    }

    /**
     * Teste la méthode toString de la classe Employe.
     * @throws EmployeException si une erreur survient lors de la création de l'employé.
     */
    @Test
    public void testToString() throws EmployeException {
        employe = new Employe("1234567", "Johnson", "Billy", "0000");
        assertEquals("	Employé(e) : Johnson Billy\n", employe.toString());
    }
}