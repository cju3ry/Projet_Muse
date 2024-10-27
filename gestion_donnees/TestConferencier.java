package testsUnitaires;

import gestion_donnees.Conferencier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TestConferencier {
    private Conferencier conferencier;

    @BeforeEach
    public void setup() {
        conferencier = new Conferencier("ABC1234", "Dupont", "Marie");
    }

    // Test de création avec un ID invalide
    @Test
    public void testConferencierIdInvalide() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Conferencier("123", "Dupont", "Marie"));
        assertEquals("L'identifiant du conférencier doit être de 7 caractères.", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> new Conferencier(null, "Dupont", "Marie"));
        assertEquals("L'identifiant du conférencier ne peut pas être null.", exception.getMessage());
    }

    // Test de création avec un ID valide
    @Test
    public void testConferencierIdValide() {
        Conferencier conf = new Conferencier("ABC1234", "Durand", "Jean");
        assertEquals("ABC1234", conf.getId());
        assertEquals("Durand", conf.getNom());
        assertEquals("Jean", conf.getPrenom());
    }

    // Test de la méthode setNumTel avec un numéro valide
    @Test
    public void testSetNumTelValide() {
        conferencier.setNumTel("0123456789");
        assertEquals("0123456789", conferencier.getNumTel());
    }

    // Test de la méthode setNumTel avec un numéro invalide
    @Test
    public void testSetNumTelInvalide() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> conferencier.setNumTel("1234"));
        assertEquals("Le numéro de téléphone doit être de 10 caractères.", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> conferencier.setNumTel(null));
        assertEquals("Le numéro de téléphone ne peut pas être null.", exception.getMessage());
    }

    // Test de la méthode setSpecialitees avec un tableau valide
    @Test
    public void testSetSpecialiteesValides() {
        String[] specialites = {"Philosophie Antique", "Médiévale", "Renaissance", "Lumières", "Romantisme", "Révolution Industrielle"};
        conferencier.setSpecialitees(specialites);
        assertArrayEquals(specialites, conferencier.getSpecialites());
    }

    // Test de la méthode setSpecialitees avec un tableau invalide (plus de 6 spécialités)
    @Test
    public void testSetSpecialiteesInvalides() {
        String[] specialites = {"Philosophie Antique", "Médiévale", "Renaissance", "Lumières", "Romantisme", "Révolution Industrielle", "Guerres Mondiales"};
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> conferencier.setSpecialitees(specialites));
        assertEquals("Le nombre de spécialités ne doit pas dépasser 6 éléments.", exception.getMessage());
    }


    // Test de la méthode setIndisponibilite avec des dates valides
    @Test
    public void testSetIndisponibiliteValides() {
        ArrayList<String> indisponibilite = new ArrayList<>();
        indisponibilite.add("01/01/2024");
        indisponibilite.add("02/01/2024");
        conferencier.setIndisponibilite(indisponibilite);
        assertEquals(indisponibilite, conferencier.getIndisponibilite());
    }

    // Test de la méthode setIndisponibilite avec des dates invalides (ordre incorrect)
    @Test
    public void testSetIndisponibiliteOrdreInvalide() {
        ArrayList<String> indisponibilite = new ArrayList<>();
        indisponibilite.add("02/01/2024");
        indisponibilite.add("01/01/2024");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> conferencier.setIndisponibilite(indisponibilite));
        assertEquals("Les dates d'indisponibilité sont invalides ou non cohérentes.", exception.getMessage());
    }

    // Test de la méthode setIndisponibilite avec des dates mal formatées
    @Test
    public void testSetIndisponibiliteDateInvalide() {
        ArrayList<String> indisponibilite = new ArrayList<>();
        indisponibilite.add("32/13/2024"); // Date incorrecte
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> conferencier.setIndisponibilite(indisponibilite));
        assertEquals("Les dates d'indisponibilité sont invalides ou non cohérentes.", exception.getMessage());
    }

    // Test des méthodes getId, getNom et getPrenom
    @Test
    public void testGetters() {
        assertEquals("ABC1234", conferencier.getId());
        assertEquals("Dupont", conferencier.getNom());
        assertEquals("Marie", conferencier.getPrenom());
    }

    // Test de la méthode toString
    @Test
    public void testToString() {
    	conferencier.setNumTel("0123456789");
        conferencier.setEstEmploye(true);
        String[] specialites = {"Préhisoire", "Picasso"};
        conferencier.setSpecialitees(specialites);
        ArrayList<String> indisponibilites = new ArrayList<>();
        indisponibilites.add("10/10/2023");
        indisponibilites.add("15/10/2023");
        conferencier.setIndisponibilite(indisponibilites);
        String expected = "\tConférencier/Conférencière : Nom : Dupont Prenom : Marie\n" +
                		   "\tEmployé : Oui\n" +
                		   "\tNuméro de téléphone : 0123456789\n" +
                		   "\tSpécialités : Préhisoire, Picasso\n" +
                		   "\tIndisponibilités : 10/10/2023, 15/10/2023\n";
        assertEquals(expected, conferencier.toString());
    }

}
