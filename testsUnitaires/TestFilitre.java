package testsUnitaires;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import gestion_donnees.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestFilitre {

    private Filtre filtre;
    private DonneesApplication donnees;

    @BeforeEach
    public void setUp() throws ParseException {
        // Initialisation des objets nécessaires pour chaque test
        donnees = new DonneesApplication();
        filtre = new Filtre();

        // Exemple de date de test
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date glaceALaFraise = format.parse("01/11/2024");
        Date diaboloMenthe = format.parse("03/12/2024");

        // Charger les données fictives
        donnees.importerConferenciers(DonneesApplication.LireCsv("conferencier_test.csv"));
        donnees.importerEmployes(DonneesApplication.LireCsv("employes_test.csv"));
        donnees.importerExpositions(DonneesApplication.LireCsv("expositions_test.csv"));
        donnees.importerVisites(DonneesApplication.LireCsv("visites_test.csv"));
    }

    @Test
    public void testFiltreConferencierNom() {
        // Test de la méthode filtreConferencierNom
        String nom = "Lexpert";
        String prenom = "Noemie";

        filtre.filtreConferencierNom(nom, prenom);

        // Vérifier que la liste des visites ne contient que celles du conférencier correspondant
        for (Visite visite : filtre.getListeVisite()) {
            assertEquals(nom, visite.getConferencierNom());
            assertEquals(prenom, visite.getConferencierPrenom());
        }
    }

    @Test
    public void testFiltreExpositionIntitule() {
        // Test de la méthode filtreExpositionIntitule
        String intituleExpo = "Les paysages impressionnistes";

        filtre.filtreExpositionIntitule(intituleExpo);

        // Vérifier que la liste des visites ne contient que celles liées à l'exposition demandée
        for (Visite visite : filtre.getListeVisite()) {
            assertEquals(intituleExpo, visite.getExpositionIntitule());
        }
    }

    @Test
    public void testFiltreDatePeriode() throws ParseException {
        // Test de la méthode filtreDatePeriode
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date dateDebut = format.parse("01/11/2024");
        Date dateFin = format.parse("03/12/2024");

        filtre.filtreDatePeriode(dateDebut, dateFin);

        // Vérifier que toutes les visites sont dans la période
        for (Visite visite : filtre.getListeVisite()) {
            assertTrue(visite.getDateVisite().after(dateDebut) || visite.getDateVisite().equals(dateDebut));
            assertTrue(visite.getDateVisite().before(dateFin) || visite.getDateVisite().equals(dateFin));
        }
    }

    @Test
    public void testFiltreHeurePeriode() throws ParseException {
        // Test de la méthode filtreHeurePeriode
        SimpleDateFormat format = new SimpleDateFormat("HH'h'mm");
        String heureDebut = "13h00";
        String heureFin = "17h00";

        filtre.filtreHeurePeriode(heureDebut, heureFin);

        // Vérifier que toutes les visites sont dans la période horaire
        for (Visite visite : filtre.getListeVisite()) {
            assertTrue(visite.getHeureVisite().after(format.parse(heureDebut)) || visite.getHeureVisite().equals(format.parse(heureDebut)));
            assertTrue(visite.getHeureVisite().before(format.parse(heureFin)) || visite.getHeureVisite().equals(format.parse(heureFin)));
        }
    }

    @Test
    public void testFiltreExpositionPermanente() {
        // Test de la méthode filtreExpositionPermanente
        filtre.filtreExpositionPermanente();

        // Vérifier que la liste des expositions filtrées contient uniquement des expositions permanentes
        for (Exposition exposition : filtre.getListeExposition()) {
            assertFalse(exposition.estTemporaire());
        }
    }

    @Test
    public void testFiltreDatePrecise() throws ParseException {
        // Test de la méthode filtreDatePrecise
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date datePrecise = format.parse("01/11/2024");

        filtre.filtreDatePrecise(datePrecise);

        // Vérifier que toutes les visites sont à la date exacte
        for (Visite visite : filtre.getListeVisite()) {
            assertEquals(datePrecise, visite.getDateVisite());
        }
    }

    @Test
    public void testFiltreHeurePrecise() throws ParseException {
        // Test de la méthode filtreHeurePrecise
        SimpleDateFormat format = new SimpleDateFormat("HH'h'mm");
        String heurePrecise = "13h00";

        filtre.filtreHeurePrecise(heurePrecise);

        // Vérifier que toutes les visites sont à l'heure exacte
        for (Visite visite : filtre.getListeVisite()) {
            assertEquals(heurePrecise, format.format(visite.getHeureVisite()));
        }
    }

    @Test
    public void testFiltreDatePeriodeException() {
        // Vérifier que la méthode filtreDatePeriode lance une exception si la date de début est après la date de fin
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date dateDebut = null;
        Date dateFin = null;

        try {
            dateDebut = format.parse("05/12/2024");
            dateFin = format.parse("03/12/2024");
            filtre.filtreDatePeriode(dateDebut, dateFin);
            fail("Exception attendue");
        } catch (IllegalArgumentException e) {
            assertEquals("La date de début ne doit pas être supérieure à la date de fin.", e.getMessage());
        }
    }
}

