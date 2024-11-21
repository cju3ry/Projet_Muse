package testsUnitaires;

import gestion_donnees.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

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
		donnees.importerExpositions(donnees.LireCsv("expositions.csv"));
		donnees.importerConferenciers(donnees.LireCsv("conferenciers.csv"));
		donnees.importerVisites(donnees.LireCsv("visites.csv"));
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

	@Test
	public void testFiltreDatePeriode() throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date dateDebut = format.parse("01/11/2024");
		Date dateFin = format.parse("12/11/2024");
		filtres.datePeriode(dateDebut, dateFin);
		assertEquals(3, filtres.getListeVisite().size());
	}

	@Test
	public void testFiltreHeurePeriode() throws ParseException {
		filtres.heurePeriode("09h00", "12h00");
		assertEquals(12, filtres.getListeVisite().size());
	}

	@Test
	public void testConferencierInterne() {
		filtres.conferencierInterne();
		assertEquals(14, filtres.getListeVisite().size()); 
	}

	@Test
	public void testConferencierExterne() {
		filtres.conferencierExterne();
		assertEquals(4, filtres.getListeVisite().size());
	}

	@Test
	public void testExpositionPermanente() {
		filtres.expositionPermanente();
		assertEquals(6, filtres.getListeVisite().size());
	}

	@Test
	public void testExpositionTemporaire() {
		filtres.expositionTemporaire();
		assertEquals(12, filtres.getListeVisite().size());
	}

	@Test
	public void testTrierConferenciers() {
		ArrayList<Conferencier> conferenciers = donnees.getConferenciers();
		filtres.conferenciersTrie(conferenciers, true);
		assertEquals("Martin", conferenciers.get(0).getNom()); // Exemple attendu
	}

	@Test
	public void testTrierExpositions() {
		ArrayList<Exposition> expositions = donnees.getExpositions();
		filtres.expositionstrie(expositions, false);
		assertEquals("Les paysages impressionnistes", expositions.get(0).getIntitule()); // Exemple attendu
	}

	@Test
	public void testConfMoyennesPeriode() throws ParseException {
		ArrayList<Conferencier> conferenciers = donnees.getConferenciers();
		HashMap<Conferencier, Double> moyennes = filtres.confMoyennesPeriode(conferenciers, "01/01/2024", "31/12/2024");
		assertTrue(moyennes.size() > 0);
	}

	@Test
	public void testExpoMoyennesPeriode() throws ParseException {
		ArrayList<Exposition> expositions = donnees.getExpositions();
		HashMap<Exposition, Double> moyennes = filtres.expoMoyennesPeriode(expositions, "01/01/2024", "31/12/2024");
		assertTrue(moyennes.size() > 0);
	}

	@Test
	public void testFiltreConferencierNomAucunResultat() {
		filtres.conferencierNom("Inexistant", "Personne");
		assertEquals(0, filtres.getListeVisite().size());
	}

	@Test
	public void testFiltreHeurePreciseException() {
		assertThrows(ParseException.class, () -> {
			filtres.heurePrecise("invalidTime");
		});
	}

	@Test
	public void testFiltreDatePeriodeException() {
		assertThrows(IllegalArgumentException.class, () -> {
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			Date dateDebut = format.parse("31/12/2024");
			Date dateFin = format.parse("01/01/2024");
			filtres.datePeriode(dateDebut, dateFin);
		});
	}

	@Test
	public void testInitialiserVisiteFiltre() {
		filtres.reset();
		assertFalse(filtres.getListeVisite().isEmpty());
	}

	@Test
	public void testExpoVisitePeriodeAvecResultats() throws ParseException {
		filtres.expoVisitePeriode("01/01/2024", "31/12/2024");
		assertEquals(6, filtres.getListeExposition().size());
	}

	@Test
	public void testExpoVisitePeriodeSansResultats() throws ParseException {
		filtres.expoVisitePeriode("01/01/2030", "31/12/2030");
		assertEquals(0, filtres.getListeExposition().size());
	}

	@Test
	public void testExpoVisitePeriodeDatesInvalides() {
		assertThrows(IllegalArgumentException.class, () -> {
			filtres.expoVisitePeriode("31/12/2024", "01/01/2024");
		});
	}

	@Test
	public void testExpoVisiteHoraireAvecResultats() throws ParseException {
		filtres.expoVisiteHoraire("09h00", "12h00");
		assertEquals(5, filtres.getListeExposition().size());
	}

	@Test
	public void testExpoVisiteHoraireSansResultats() throws ParseException {
		filtres.expoVisiteHoraire("23h00", "23h59");
		assertEquals(0, filtres.getListeExposition().size());
	}

	@Test
	public void testExpoVisiteHoraireHeuresInvalides() {
		assertThrows(IllegalArgumentException.class, () -> {
			filtres.expoVisiteHoraire("12h00", "09h00");
		});
	}

	@Test
	public void testConfVisitePeriodeAvecResultats() throws ParseException {
		filtres.confVisitePeriode("01/01/2024", "31/12/2024");
		assertEquals(8, filtres.getListeConferencier().size());
	}

	@Test
	public void testConfVisitePeriodeSansResultats() throws ParseException {
		filtres.confVisitePeriode("01/01/2030", "31/12/2030");
		assertEquals(0, filtres.getListeConferencier().size());
	}

	@Test
	public void testConfVisitePeriodeDatesInvalides() {
		assertThrows(IllegalArgumentException.class, () -> {
			filtres.confVisitePeriode("31/12/2024", "01/01/2024");
		});
	}
	@Test
	public void testConfVisiteHoraireAvecResultats() throws ParseException {
		filtres.confVisiteHoraire("09h00", "12h00");
		assertEquals(7, filtres.getListeConferencier().size());
	}

	@Test
	public void testConfVisiteHoraireSansResultats() throws ParseException {
		filtres.confVisiteHoraire("23h00", "23h59");
		assertEquals(0, filtres.getListeConferencier().size());
	}

	@Test
	public void testConfVisiteHoraireHeuresInvalides() {
		assertThrows(IllegalArgumentException.class, () -> {
			filtres.confVisiteHoraire("12h00", "09h00");
		});
	}







}
