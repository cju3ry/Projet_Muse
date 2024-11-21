package testsUnitaires;

import application.ControleurImporterLocal;
import gestion_donnees.DonneesApplication;
import gestion_donnees.Statistiques;
import gestion_donnees.Visite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class testStatistiques {

	private Statistiques statistiques;
	private DonneesApplication donneesDeBase;
	private ArrayList<Visite> visite;

	// Initialisation des données avant chaque test
	@BeforeEach
	public void setUp() {
		statistiques = new Statistiques();
		donneesDeBase = new DonneesApplication();
		donneesDeBase.importerEmployes(donneesDeBase.LireCsv("employes.csv"));
		donneesDeBase.importerConferenciers(donneesDeBase.LireCsv("conferenciers.csv"));
		donneesDeBase.importerExpositions(donneesDeBase.LireCsv("expositions.csv"));
		donneesDeBase.importerVisites(donneesDeBase.LireCsv("visites.csv"));
		statistiques.visiteFiltre = donneesDeBase.getVisites();
		visite = new ArrayList<>(donneesDeBase.getVisites());
	}

	// test de la methode conferencierInterne
	@Test
	public void testConferencierInterne() {
		statistiques.conferencierInterne();
		for (Visite visite : statistiques.visiteFiltre) {
			String conferencierId = visite.getConferencierId();
			assertTrue(donneesDeBase.getConferenciers().stream()
					.anyMatch(c -> c.getId().equals(conferencierId) && c.getEstEmploye()));
		}
	}

	// test de la methode conferencierExterne
	@Test
	public void testConferencierExterne() {
		statistiques.conferencierExterne();
		for (Visite visite : statistiques.visiteFiltre) {
			String conferencierId = visite.getConferencierId();
			assertTrue(donneesDeBase.getConferenciers().stream()
					.anyMatch(c -> c.getId().equals(conferencierId) && !c.getEstEmploye()));
		}
	}

	// test de la methode visitePeriode
	@Test
	public void testVisitePeriode() throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date dateDebut = format.parse("01/01/2023");
		Date dateFin = format.parse("31/12/2023");

		statistiques.visitePeriode(dateDebut, dateFin);

		for (Visite visite : statistiques.visiteFiltre) {
			assertTrue(!visite.getDateVisite().before(dateDebut) && !visite.getDateVisite().after(dateFin));
		}
	}

	// test de la methode visitePlageHoraire
	@Test
	public void testVisitePlageHoraire() throws ParseException {
		String heureDebut = "09h00";
		String heureFin = "17h00";

		statistiques.visitePlageHoraire(heureDebut, heureFin);

		SimpleDateFormat format = new SimpleDateFormat("HH'h'mm");
		Date debut = format.parse(heureDebut);
		Date fin = format.parse(heureFin);

		for (Visite visite : statistiques.visiteFiltre) {
			assertTrue(!visite.getHeureVisite().before(debut) && !visite.getHeureVisite().after(fin));
		}
	}

	// test de la methode getNbVisitesParConferencier
	@Test
	public void testGetNbVisitesParExposition() {
		Map<String, Integer> nbVisitesParExposition = statistiques.getNbVisitesParExposition();
		int totalVisites = statistiques.visiteFiltre.size();
		int sum = nbVisitesParExposition.values().stream().mapToInt(Integer::intValue).sum();
		assertEquals(totalVisites, sum);
	}

	// test de la methode getNbVisitesParConferencier
	@Test
	public void testGetPVisitesExpositions() {
		Map<String, Double> pVisitesExpositions = statistiques.getPVisitesExpositions();
		double totalPourcentage = calculerSomme(pVisitesExpositions);
		assertEquals(100.0, totalPourcentage, 0.01);
	}

	// test de la methode getNbVisitesParConferencier
	 private double calculerSomme(Map<String, Double> pVisitesExpositions) {
		double totalPourcentage = 0.0;
		for (Map.Entry<String, Double> entry : pVisitesExpositions.entrySet()) {
			System.out.println(entry.getKey() + " : " + entry.getValue());
			totalPourcentage += entry.getValue();
		}
		return totalPourcentage;
	}

	// test de la methode expositionTemporaire
	@Test
	public void testExpositionTemporaire() {
		statistiques.expositionTemporaire();
		for (Visite visite : statistiques.visiteFiltre) {
			String expositionId = visite.getExpositionId();
			assertTrue(donneesDeBase.getExpositions().stream()
					.anyMatch(e -> e.getId().equals(expositionId) && !e.estTemporaire()));
		}
	}

	// test de la methode expositionPermanente
	@Test
	public void testExpositionPermanente() {
		statistiques.expositionPermanente();
		for (Visite visite : statistiques.visiteFiltre) {
			String expositionId = visite.getExpositionId();
			assertTrue(donneesDeBase.getExpositions().stream()
					.anyMatch(e -> e.getId().equals(expositionId) && e.estTemporaire()));
		}
	}

	// test de la methode initialiserVisiteFiltre
	@Test
	public void testVisitePlageHoraire10h00_11h00() throws ParseException {
		String heureDebut = "10h00";
		String heureFin = "11h00";

		statistiques.visitePlageHoraire(heureDebut, heureFin);

		SimpleDateFormat format = new SimpleDateFormat("HH'h'mm");
		Date debut = format.parse(heureDebut);
		Date fin = format.parse(heureFin);

		ArrayList<String> expectedVisites = new ArrayList<>(Arrays.asList(
				"R000001", "R000006", "R000008", "R000010",
				"R000012", "R000014", "R000015", "R000016", "R000017"));

		ArrayList<String> actualVisites = new ArrayList<>();
		for (Visite visite : statistiques.visiteFiltre) {
			actualVisites.add(visite.getId());
		}

		assertEquals(expectedVisites, actualVisites);
	}

	// test de la methode visitePlageHoraire
	@Test
	public void testVisitePlageTemps() throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date dateDebut = format.parse("08/10/2024");
		Date dateFin = format.parse("01/11/2024");

		statistiques.visitePeriode(dateDebut, dateFin);

		ArrayList<String> expectedVisites = new ArrayList<>(Arrays.asList(
				"R000005", "R000007", "R000009", "R000010", "R000011", "R000013", "R000015"));

		ArrayList<String> actualVisites = new ArrayList<>();
		for (Visite visite : statistiques.visiteFiltre) {
			actualVisites.add(visite.getId());
		}
	}

	// test de la methode visiteConfExterne
	@Test
	public void testVisiteConfExterne() throws ParseException {
		statistiques.conferencierExterne();
		ArrayList<String> expectedVisites = new ArrayList<>(Arrays.asList("R000004", "R000005", "R000016", "R000017"));
		ArrayList<String> actualVisites = new ArrayList<>();
		for (Visite visite : statistiques.visiteFiltre) {
			actualVisites.add(visite.getId());
		}
		assertEquals(expectedVisites, actualVisites);
	}

	// test de la methode visiteConfInterne
	@Test
	public void testVisiteConfInterne() throws ParseException {
		statistiques.conferencierInterne();
		ArrayList<String> expectedVisites = new ArrayList<>(Arrays.asList(
				"R000001", "R000002", "R000003", "R000006", "R000007", "R000008", "R000009",
				"R000010", "R000011", "R000012", "R000013", "R000014", "R000015", "R000018" ));
		ArrayList<String> actualVisites = new ArrayList<>();
		for (Visite visite : statistiques.visiteFiltre) {
			actualVisites.add(visite.getId());
		}
		assertEquals(expectedVisites, actualVisites);
	}

	// test de la methode visitePeriode
	@Test
	public void testExpoTemporaire() throws ParseException {
		statistiques.expositionTemporaire();
		ArrayList<String> expectedVisites = new ArrayList<>(Arrays.asList(
				"R000001", "R000002", "R000003", "R000004", "R000005", "R000006"
		));
		ArrayList<String> actualVisites = new ArrayList<>();
		for (Visite visite : statistiques.visiteFiltre) {
			actualVisites.add(visite.getId());
		}
		assertEquals(expectedVisites, actualVisites);
	}

	// test de la methode expositionPermanente
	@Test
	public void testExpoPemanante() throws ParseException {
		statistiques.expositionPermanente();
		ArrayList<String> expectedVisites = new ArrayList<>(Arrays.asList(
				"R000007", "R000008", "R000009", "R000010", "R000011", "R000012",
				"R000013", "R000014", "R000015", "R000016", "R000017", "R000018"
		));
		ArrayList<String> actualVisites = new ArrayList<>();
		for (Visite visite : statistiques.visiteFiltre) {
			actualVisites.add(visite.getId());
		}
		assertEquals(expectedVisites, actualVisites);
	}

	// test de la methode getExpositionIntituleById
	@Test
	public void testGetExpositionIntituleById() {
		String expositionId = "E000001";
		String expectedIntitule = "Les paysages impressionnistes";
		String actualIntitule = statistiques.getExpositionIntituleById(expositionId);
		assertEquals(expectedIntitule, actualIntitule);
	}

	// test de la methode getNomPrenomConferencierById
	@Test
	public void testgetNomPrenomConferencierById() {
		String conferencierId = "C000001";
		String expectedNom = "Dupont Pierre";
		String actualNom = statistiques.getNomPrenomConferencierById(conferencierId);
		assertEquals(expectedNom, actualNom);
	}

	// test de la methode initialiserVisiteFiltre
	@Test
	public void testInitialiserVisiteFiltre() {
		statistiques.initialiserVisiteFiltre();
		statistiques.visiteFiltre = donneesDeBase.getVisites();
		assertEquals(visite.size(), statistiques.visiteFiltre.size());
		assertTrue(statistiques.visiteFiltre.containsAll(visite));
	}

	// test de la methode afficherPVisitesTConferencier
	@Test
	public void testAfficherPVisitesTConferencier() {
		StringBuilder result = statistiques.afficherPVisitesTConferencier();
		String expected = "Pourcentage de visites effectuées par des conférenciers internes: 77.77777777777779%\n" +
				"Pourcentage de visites effectuées par des conférenciers externes: 22.22222222222222%";
		assertEquals(expected, result.toString().trim());
	}

	// test de la methode reset
	@Test
	public void testReset() {
		statistiques.reset();
		// effet de bord on a du mal à tester cette methode
		assertEquals(14, statistiques.visiteFiltre.size());
	}

	// test de la methode affichagePVisitesExposition
	@Test
	public void testAffichagePVisitesExposition() {
		StringBuilder result = statistiques.affichagePVisitesExposition();
		String expected = "Exposition E000002: Les œuvres majeures de Pierre Soulages: 22.0% des visites\n" +
				"Exposition E000001: Les paysages impressionnistes: 28.0% des visites\n" +
				"Exposition E000004: L'Inde vue par Henri Cartier-Bresson: 17.0% des visites\n" +
				"Exposition E000003: Période bleue de Picasso: 11.0% des visites\n" +
				"Exposition E000006: L'impressionnisme selon Berthe Morisot: 11.0% des visites\n" +
				"Exposition E000005: Doisneau et la magazine Vogue: 11.0% des visites";
		assertEquals(expected, result.toString().trim());
	}
	// test de la methode afficherPVisitesConferencier
	@Test
	public void testAfficherPVisitesConferencier() {
		StringBuilder result = statistiques.afficherPVisitesConferencier();
		String expected = "Conférencier C000002: Lexpert Noemie: 22% des visites\n"+
		"Conférencier C000001: Dupont Pierre: 11% des visites\n"+
		"Conférencier C000004: Durand Bill: 17% des visites\n"+
		"Conférencier C000003: Dujardin Océane: 17% des visites\n"+
		"Conférencier C000006: Martin Martin: 6% des visites\n"+
		"Conférencier C000005: Dupont Max: 11% des visites\n"+
		"Conférencier C000008: Deneuve Zoé: 11% des visites\n"+
		"Conférencier C000007: Legrand Jean-Pierre: 6% des visites";
		assertEquals(expected, result.toString().trim());
	}
	
}