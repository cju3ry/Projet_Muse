package testsUnitaires;

import gestion_donnees.Employe;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Classe de test pour Employe.
 */
public class TestEmploye {

    private Employe employe;

    // Test du constructeur avec des valeurs valides
    @Test
    public void testConstructeurOk() {
        employe = new Employe("1234567", "Johnson", "Billy", "0000");
        assertEquals("1234567", employe.getId());
        assertEquals("Johnson", employe.getNom());
        assertEquals("Billy", employe.getPrenom());
        assertEquals("0000", employe.getNumTel());
    }

    // Test du constructeur avec un ID invalide (moins de 7 caractères)
    @Test
    public void testConstructeurIdInvalide() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Employe("12345", "Johnson", "Billy", "0000");
        });
        assertEquals("L'identifiant doit être de 7 caractères.", exception.getMessage());
    }

    // Test du constructeur avec un ID null
    @Test
    public void testConstructeurIdNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Employe(null, "Johnson", "Billy", "0000");
        });
        assertEquals("L'identifiant doit être de 7 caractères.", exception.getMessage());
    }

    // Test du constructeur avec un numéro de téléphone invalide (plus de 4 caractères)
    @Test
    public void testConstructeurNumTelInvalide() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Employe("1234567", "Johnson", "Billy", "00000");
        });
        assertEquals("Le numéro de téléphone doit être de 4 caractères.", exception.getMessage());
    }

    // Test du constructeur avec un numéro de téléphone null
    @Test
    public void testConstructeurNumTelNull() {
        employe = new Employe("1234567", "Johnson", "Billy", null);
        assertNull(employe.getNumTel());
    }

 // Test de la méthode toString pour une sortie formatée correctement
    @Test
    public void testToString() {
        employe = new Employe("1234567", "Johnson", "Billy", "0000");
        assertEquals("\tEmployé(e) : Johnson Billy\n" + "\tNuméro de téléphone : 0000\n", employe.toString());
    }

}
