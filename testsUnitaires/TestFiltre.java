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
		donnees.importerEmployes(donnees.LireCsv("Projet_Musee-main\\testsUnitaires\\employes.csv"));
		donnees.importerExpositions(donnees.LireCsv("Projet_Musee-main\\testsUnitaires\\expositions.csv"));
		donnees.importerConferenciers(donnees.LireCsv("Projet_Musee-main\\testsUnitaires\\conferenciers.csv"));
		donnees.importerVisites(donnees.LireCsv("Projet_Musee-main\\testsUnitaires\\visites.csv"));
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

	/**
     * Vérifie que le filtre par période de dates fonctionne correctement.
     * Teste les visites incluses dans une plage de dates donnée.
     */
	@Test
	public void testFiltreDatePeriode() throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date dateDebut = format.parse("01/11/2024");
		Date dateFin = format.parse("12/11/2024");
		filtres.datePeriode(dateDebut, dateFin);
		assertEquals(3, filtres.getListeVisite().size());
	}

	/**
     * Vérifie que le filtre par période horaire fonctionne correctement.
     * Teste les visites incluses dans une plage horaire donnée.
     */
	@Test
	public void testFiltreHeurePeriode() throws ParseException {
		filtres.heurePeriode("09h00", "12h00");
		assertEquals(12, filtres.getListeVisite().size());
	}

	/**
     * Vérifie que le filtre pour conférenciers internes fonctionne correctement.
     * Teste uniquement les visites avec des conférenciers internes.
     */
	@Test
	public void testConferencierInterne() {
		filtres.conferencierInterne();
		assertEquals(14, filtres.getListeVisite().size()); 
	}

	/**
     * Vérifie que le filtre pour conférenciers externes fonctionne correctement.
     * Teste uniquement les visites avec des conférenciers externes.
     */
	@Test
	public void testConferencierExterne() {
		filtres.conferencierExterne();
		assertEquals(4, filtres.getListeVisite().size());
	}

	/**
     * Vérifie que le filtre pour les expostion permanente  fonctionne correctement.
     * Teste uniquement les expostion qui doivent être permanente.
     */
	@Test
	public void testExpositionPermanente() {
		filtres.expositionPermanente();
		assertEquals(6, filtres.getListeVisite().size());
	}

	/**
     * Vérifie que le filtre pour les expostion temporaire  fonctionne correctement.
     * Teste uniquement les expostion qui doivent être temporaire.
     */
	@Test
	public void testExpositionTemporaire() {
		filtres.expositionTemporaire();
		assertEquals(12, filtres.getListeVisite().size());
	}

	/**
     * Vérifie que le trie par confercnier  fonctionne correctement.
     * Teste si le premier de la liste est bien le premier attendue.
     */
	@Test
	public void testTrierConferenciers() {
		ArrayList<Conferencier> conferenciers = donnees.getConferenciers();
		filtres.conferenciersTrie(conferenciers, true);
		assertEquals("Martin", conferenciers.get(0).getNom()); // Exemple attendu
	}

	/**
     * Vérifie que le trie par exposition fonctionne correctement.
     * Teste si la premiere de la liste est bien la premiere attendue.
     */
	@Test
	public void testTrierExpositions() {
		ArrayList<Exposition> expositions = donnees.getExpositions();
		filtres.expositionstrie(expositions, false);
		assertEquals("Les paysages impressionnistes", expositions.get(0).getIntitule()); // Exemple attendu
	}

	/**
     * Vérifie que la moyenne des confercnier par période fonctionne correctement.
     * Teste si la moyenne n'est pas nulle et positive.
     */
	@Test
	public void testConfMoyennesPeriode() throws ParseException {
		ArrayList<Conferencier> conferenciers = donnees.getConferenciers();
		HashMap<Conferencier, Double> moyennes = filtres.confMoyennesPeriode(conferenciers, "01/01/2024", "31/12/2024");
		assertTrue(moyennes.size() > 0);
	}

	/**
     * Vérifie que la moyenne des expositions par période fonctionne correctement.
     * Teste si la moyenne n'est pas nulle et positive.
     */
	@Test
	public void testExpoMoyennesPeriode() throws ParseException {
		ArrayList<Exposition> expositions = donnees.getExpositions();
		HashMap<Exposition, Double> moyennes = filtres.expoMoyennesPeriode(expositions, "01/01/2024", "31/12/2024");
		assertTrue(moyennes.size() > 0);
	}

	/**
     * Vérifie que le filtre de conferencier fonctionne correctement.
     * Teste si on renvoit une liste vide si le nom n'existe pas.
     */
	@Test
	public void testFiltreConferencierNomAucunResultat() {
		filtres.conferencierNom("Inexistant", "Personne");
		assertEquals(0, filtres.getListeVisite().size());
	}

	/**
     * Vérifie que le filtre par heure fonctionne correctement.
     * Teste si on renvoit l'exception si l'heure est mauvaise.
     */
	@Test
	public void testFiltreHeurePreciseException() {
		assertThrows(ParseException.class, () -> {
			filtres.heurePrecise("test");
		});
	}

	/**
     * Vérifie que le filtre par date fonctionne correctement.
     * Teste si on renvoit l'exception si l'ordre des dates est inversé.
     */
	@Test
	public void testFiltreDatePeriodeException() {
		assertThrows(IllegalArgumentException.class, () -> {
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			Date dateDebut = format.parse("31/12/2024");
			Date dateFin = format.parse("01/01/2024");
			filtres.datePeriode(dateDebut, dateFin);
		});
	}

	/**
     * Vérifie que le reset fonctionne correctement.
     * Teste si on a bien la liste de base si on reset.
     */
	@Test
	public void testInitialiserVisiteFiltre() {
		filtres.reset();
		assertFalse(filtres.getListeVisite().isEmpty());
	}

	/**
     * Vérifie que le filtre par date pour les visite fonctionne correctement.
     * Teste si on obtient bien le résulat attendu.
     */
	@Test
	public void testExpoVisitePeriodeAvecResultats() throws ParseException {
		filtres.expoVisitePeriode("01/01/2024", "31/12/2024");
		assertEquals(6, filtres.getListeExposition().size());
	}

	/**
     * Vérifie que le filtre par date pour les visite fonctionne correctement.
     * Teste si on obtient bien le résulat attendu (0) si les dates n'existanet pas dans les données.
     */
	@Test
	public void testExpoVisitePeriodeSansResultats() throws ParseException {
		filtres.expoVisitePeriode("01/01/2030", "31/12/2030");
		assertEquals(0, filtres.getListeExposition().size());
	}

	/**
     * Vérifie que le filtre par date pour les visites fonctionne correctement.
     * Teste si on obtient bien une erreur si les dates sont inversées.
     */
	@Test
	public void testExpoVisitePeriodeDatesInvalides() {
		assertThrows(IllegalArgumentException.class, () -> {
			filtres.expoVisitePeriode("31/12/2024", "01/01/2024");
		});
	}

	/**
     * Vérifie que le filtre par heure pour les visites fonctionne correctement.
     * Teste si on obtient bien la bonne taille de liste.
     */
	@Test
	public void testExpoVisiteHoraireAvecResultats() throws ParseException {
		filtres.expoVisiteHoraire("09h00", "12h00");
		assertEquals(5, filtres.getListeExposition().size());
	}

	/**
     * Vérifie que le filtre par heure pour les visites fonctionne correctement.
     * Teste si on obtient bien une liste vide si les horraires ne correspondent pas.
     */
	@Test
	public void testExpoVisiteHoraireSansResultats() throws ParseException {
		filtres.expoVisiteHoraire("23h00", "23h59");
		assertEquals(0, filtres.getListeExposition().size());
	}

	/**
     * Vérifie que le filtre par heure pour les visites fonctionne correctement.
     * Teste si on obtient bien une erreur si les dates sont inversées.
     */
	@Test
	public void testExpoVisiteHoraireHeuresInvalides() {
		assertThrows(IllegalArgumentException.class, () -> {
			filtres.expoVisiteHoraire("12h00", "09h00");
		});
	}

	/**
     * Vérifie que le filtre par date pour les conferenciers fonctionne correctement.
     * Teste si on obtient bien le résulat attendu.
     */
	@Test
	public void testConfVisitePeriodeAvecResultats() throws ParseException {
		filtres.confVisitePeriode("01/01/2024", "31/12/2024");
		assertEquals(8, filtres.getListeConferencier().size());
	}

	/**
     * Vérifie que le filtre par date pour les conferenciers fonctionne correctement.
     * Teste si on obtient bien le résulat attendu (0) si les dates n'existanet pas dans les données.
     */
	@Test
	public void testConfVisitePeriodeSansResultats() throws ParseException {
		filtres.confVisitePeriode("01/01/2030", "31/12/2030");
		assertEquals(0, filtres.getListeConferencier().size());
	}

	/**
     * Vérifie que le filtre par date pour les conferenciers fonctionne correctement.
     * Teste si on obtient bien une erreur si les dates sont inversées.
     */
	@Test
	public void testConfVisitePeriodeDatesInvalides() {
		assertThrows(IllegalArgumentException.class, () -> {
			filtres.confVisitePeriode("31/12/2024", "01/01/2024");
		});
	}
	
	/**
     * Vérifie que le filtre par heure pour les conferenciers fonctionne correctement.
     * Teste si on obtient bien la bonne taille de liste.
     */
	@Test
	public void testConfVisiteHoraireAvecResultats() throws ParseException {
		filtres.confVisiteHoraire("09h00", "12h00");
		assertEquals(7, filtres.getListeConferencier().size());
	}

	/**
     * Vérifie que le filtre par heure pour les conferenciers fonctionne correctement.
     * Teste si on obtient bien une liste vide si les horraires ne correspondent pas.
     */
	@Test
	public void testConfVisiteHoraireSansResultats() throws ParseException {
		filtres.confVisiteHoraire("23h00", "23h59");
		assertEquals(0, filtres.getListeConferencier().size());
	}

	/**
     * Vérifie que le filtre par heure pour les conferenciers fonctionne correctement.
     * Teste si on obtient bien une erreur si les horraires sont inversées.
     */
	@Test
	public void testConfVisiteHoraireHeuresInvalides() {
		assertThrows(IllegalArgumentException.class, () -> {
			filtres.confVisiteHoraire("12h00", "09h00");
		});
	}
}
