package testsUnitaires;

import gestion_donnees.Visite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestVisite {
    private Visite visite;

    @BeforeEach
    public void setup() {
        visite = new Visite("VIS1234", "25/10/2024", "14h30", "Conférence sur l'art moderne", "0123456789");
    }

    // Test de création avec des paramètres valides
    @Test
    public void testVisiteCreationValide() {
        Visite visite = new Visite("VIS5678", "01/12/2024", "10h00", "Visite guidée", "0987654321");
        assertEquals("VIS5678", visite.getId());
    }

    // Test de création avec un ID invalide
    @Test
    public void testVisiteCreationIdInvalide() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Visite("123", "25/10/2024", "14h30", "Visite d'expo", "0123456789"));
        assertEquals("L'identifiant de la visite doit être de 7 caractères.", exception.getMessage());
    }

    // Test de création avec un numéro de téléphone invalide
    @Test
    public void testVisiteCreationTelephoneInvalide() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Visite("VIS1234", "25/10/2024", "14h30", "Visite d'expo", "1234"));
        assertEquals("Le numéro de téléphone doit être de 10 caractères.", exception.getMessage());
    }

    // Test de création avec un intitule nul
    @Test
    public void testVisiteCreationIntituleEnNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Visite("VIS1234", "25/10/2024", "14h30", null, "0123456789"));
        assertEquals("L'intitulé de la visite ne peut pas être nul.", exception.getMessage());
    }

    // Test de création avec une date ou une heure nulle
    @Test
    public void testVisiteCreationDateNulles() {
        IllegalArgumentException exception1 = assertThrows(IllegalArgumentException.class, () -> new Visite("VIS1234", null, "14h30", "Visite d'expo", "0123456789"));
        assertEquals("La date et l'heure de la visite doivent être spécifiées.", exception1.getMessage());

        IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class, () -> new Visite("VIS1234", "25/10/2024", null, "Visite d'expo", "0123456789"));
        assertEquals("La date et l'heure de la visite doivent être spécifiées.", exception2.getMessage());
    }

    // Test de création avec une date ou une heure mal formatée
    @Test
    public void testVisiteCreationFormatDateInvalide() {
        IllegalArgumentException exception1 = assertThrows(IllegalArgumentException.class, () -> new Visite("VIS1234", "25/13/2024", "14h30", "Visite d'expo", "0123456789"));
        assertEquals("Le format de la date doit être 'dd/MM/yyyy' et celui de l'heure 'HH'h'mm'.", exception1.getMessage());

        IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class, () -> new Visite("VIS1234", "25/10/2024", "25h30", "Visite d'expo", "0123456789"));
        assertEquals("Le format de la date doit être 'dd/MM/yyyy' et celui de l'heure 'HH'h'mm'.", exception2.getMessage());
    }

    // Test des méthodes setExpositionId, setConferencierId, et setEmployeId avec des identifiants valides
    @Test
    public void testSetIdsValides() {
        visite.setExpositionId("EXP1234");
        visite.setConferencierId("CONF123");
        visite.setEmployeId("EMP1234");
        assertEquals("EXP1234", visite.getExpositionId());
        assertEquals("CONF123", visite.getConferencierId());
        assertEquals("EMP1234", visite.getEmployeId());
    }

    // Test des méthodes setExpositionId, setConferencierId, et setEmployeId avec des identifiants invalides
    @Test
    public void testSetIdsInvalides() {
        IllegalArgumentException exception1 = assertThrows(IllegalArgumentException.class, () -> visite.setExpositionId("EXP12"));
        assertEquals("L'identifiant de l'exposition doit être de 7 caractères.", exception1.getMessage());

        IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class, () -> visite.setConferencierId(null));
        assertEquals("L'identifiant du conférencier doit être de 7 caractères.", exception2.getMessage());

        IllegalArgumentException exception3 = assertThrows(IllegalArgumentException.class, () -> visite.setEmployeId("123"));
        assertEquals("L'identifiant de l'employé doit être de 7 caractères.", exception3.getMessage());
    }

 // Test de la méthode toString
    @Test
    public void testToString() {
        String expected = "\tVisite : Conférence sur l'art moderne\n" +
                          "\tDate de la visite : 25/10/2024\n" +
                          "\tHeure de la visite : 14h30\n" +
                          "\tTéléphone : 0123456789\n" ;
        assertEquals(expected, visite.toString());
    }

}
