package testsUnitaires;

import gestion_donnees.Exposition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.*;

public class TestExposition {
    private Exposition exposition;

    @BeforeEach
    public void setup() {
        exposition = new Exposition("EXP1234", "Exposition Art Moderne", "01/01/2024", "31/12/2024");
    }

    // Test de création avec des paramètres valides (ID et intitulé uniquement)
    @Test
    public void testExpositionCreationValide() {
        Exposition exposition = new Exposition("EXP5678", "Histoire de l'Art");
        assertEquals("EXP5678", exposition.getId());
        assertEquals("Exposition : Histoire de l'Art", exposition.toString().trim());
    }

    // Test de création avec des dates valides pour l'exposition
    @Test
    public void testExpositionCreationDatesValides() {
        Exposition exposition = new Exposition("EXP1234", "Exposition Peinture", "01/03/2024", "01/06/2024");
        assertEquals("EXP1234", exposition.getId());
    }

    // Test de création avec un ID invalide
    @Test
    public void testExpositionCreationIdInvalide() {
        assertThrows(IllegalArgumentException.class, () -> new Exposition("123", "Exposition d'Art"));
    }

    // Test de création avec un intitulé nul
    @Test
    public void testExpositionCreationIntituleEnNull() {
        assertThrows(IllegalArgumentException.class, () -> new Exposition("EXP1234", null));
    }

    // Test de création avec une date de début et de fin dans un mauvais format
    @Test
    public void testExpositionCreationFormatDateInvalide() {
        assertThrows(IllegalArgumentException.class, () -> new Exposition("EXP1234", "Exposition Photographie", "31-12-2024", "01-01-2025"));
    }

    // Test de création avec une date de début après la date de fin
    @Test
    public void testExpositionCreationMauvaisOrdreDate() {
        assertThrows(IllegalArgumentException.class, () -> new Exposition("EXP1234", "Exposition Sculpture", "02/01/2025", "01/01/2025"));
    }

    // Test de la méthode setPeriode avec des valeurs valides
    @Test
    public void testSetPeriodeValide() {
        exposition.setPeriode("2022", "2024");
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        assertEquals("2022", format.format(exposition.getPeriodeDebut()));
        assertEquals("2024", format.format(exposition.getPeriodeFin()));
    }

    // Test de la méthode setPeriode avec des valeurs invalides
    @Test
    public void testSetPeriodeInvalide() {
        assertThrows(IllegalArgumentException.class, () -> exposition.setPeriode("2025", "2024"));
    }

    // Test de la méthode setNbOeuvre avec un nombre valide
    @Test
    public void testSetNbOeuvreValide() {
        exposition.setNbOeuvre(5);
    }

    // Test de la méthode setNbOeuvre avec un nombre invalide
    @Test
    public void testSetNbOeuvreInvalide() {
        assertThrows(IllegalArgumentException.class, () -> exposition.setNbOeuvre(0));
    }

    // Test de la méthode setMotCles avec des mots-clés valides
    @Test
    public void testSetMotClesValides() {
        String[] motsCles = {"art", "peinture", "histoire"};
        exposition.setMotCles(motsCles);
    }

    // Test de la méthode setMotCles avec trop de mots-clés
    @Test
    public void testSetMotClesInvalides() {
        String[] motsCles = {"art", "peinture", "histoire", "moderne", "abstrait", "classique", "sculpture", "contemporain", "musée", "exposition", "visite"};
        assertThrows(IllegalArgumentException.class, () -> exposition.setMotCles(motsCles));
    }

    // Test de la méthode setResume avec une valeur valide
    @Test
    public void testSetResumeValide() {
        exposition.setResume("Une exposition présentant les œuvres d'art moderne.");
    }

    // Test de la méthode setResume avec une valeur nulle
    @Test
    public void testSetResumeNull() {
        assertThrows(IllegalArgumentException.class, () -> exposition.setResume(null));
    }

    // Test de la méthode toString
    @Test
    public void testToString() {
        String expected = "	Exposition : Exposition Art Moderne\n";
        assertEquals(expected, exposition.toString());
    }

    // Test de la méthode estTemporaire pour vérifier si l'exposition est temporaire
    @Test
    public void testEstTemporaire() {
        assertFalse(exposition.estTemporaire());
    }
}
