package testsUnitaires;

import gestion_donnees.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test pour la classe Filtre.
 * Teste les méthodes de filtrage sur différentes propriétés des objets gérés (Conferencier, Employe, Exposition, Visite).
 */
public class TestFiltre {

    private Filtre filtres; // Attribut de classe pour l'instance de Filtre
    private DonneesApplication donnees; // Attribut de classe pour DonneesApplication

    /**
     * Initialisation des objets utilisés dans les tests, avant chaque test.
     * Crée des instances de Filtre, Conferencier, Employe, Exposition et Visite,
     * et configure leurs attributs avec des valeurs de test.
     */
    @BeforeEach
    public void setup() {
        donnees = new DonneesApplication();
        filtres = new Filtre();

        // Importation des données depuis des fichiers CSV
        donnees.importerEmployes(donnees.LireCsv("employes.csv"));
        donnees.importerExpositions(donnees.LireCsv("exposition.csv"));
        donnees.importerConferenciers(donnees.LireCsv("conferencier.csv"));
        donnees.importerVisites(donnees.LireCsv("visite.csv"));
    }

    /**
     * Teste le filtrage des visites par le nom et prénom du conférencier.
     * Vérifie que la visite associée au conférencier "Dupont, Marie" est bien trouvée.
     */
    @Test
    public void testFiltreConferencierNom() {
        filtres.conferencierNom("Dupont", "Pierre");
        assertEquals(2, filtres.getListeVisite().size());
        assertEquals("R000003", filtres.getListeVisite().get(0).getId());
    }

    /**
     * Teste le filtrage des visites par le nom et prénom de l'employé.
     * Vérifie que la visite associée à l'employé "Johnson, Billy" est bien trouvée.
     */
    @Test
    public void testFiltreEmployeNom() {
        filtres.employeNom("Hugo", "Marie");
        assertEquals(5, filtres.getListeVisite().size());
    }

    /**
     * Teste le filtrage des visites par l'intitulé de l'exposition.
     * Vérifie que la visite associée à l'exposition "Exposition Art Moderne" est bien trouvée.
     */
    @Test
    public void testFiltreExpositionIntitule() {
        filtres.expositionIntitule("Les paysages impressionnistes");
        assertEquals(5, filtres.getListeVisite().size());
        assertEquals("R000008", filtres.getListeVisite().get(0).getId());
    }

    /**
     * Teste le filtrage des visites par l'intitulé de la visite.
     * Vérifie que la visite intitulée "Conférence sur l'art moderne" est bien trouvée.
     */
    @Test
    public void testFiltreVisiteIntitule() {
        filtres.visiteIntitule("Mme Noémie Legendre");
        assertEquals(2, filtres.getListeVisite().size());
        assertEquals("R000001", filtres.getListeVisite().get(0).getId());
    }

    /**
     * Teste le filtrage des visites par le numéro de téléphone associé.
     * Vérifie que la visite avec le numéro de téléphone "0123456789" est bien trouvée.
     */
    @Test
    public void testFiltreVisiteNumTel() {
        filtres.visiteNumTel("0600000000");
        assertEquals(2, filtres.getListeVisite().size());
        assertEquals("R000001", filtres.getListeVisite().get(0).getId());
    }

    /**
     * Teste le filtrage des visites par une date précise.
     */
    @Test
    public void testFiltreDatePrecise() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date date = format.parse("08/11/2024");
        filtres.datePrecise(date);
        assertEquals(2, filtres.getListeVisite().size());
        assertEquals("R000001", filtres.getListeVisite().get(0).getId());
    }

    /**
     * Teste le filtrage des visites par une heure précise.
     */
    @Test
    public void testFiltreHeurePrecise() throws ParseException {
        filtres.heurePrecise("10h00");
        assertEquals(4, filtres.getListeVisite().size());
        assertEquals("R000001", filtres.getListeVisite().get(0).getId());
    }

    // Ajout des autres tests selon les besoins de l'application

}
