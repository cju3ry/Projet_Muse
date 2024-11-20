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

	@BeforeEach
	void setUp() {
		statistiques = new Statistiques();
		donneesDeBase = new DonneesApplication();
		donneesDeBase.importerEmployes(donneesDeBase.LireCsv("testsUnitaires\\employes.csv"));
		donneesDeBase.importerConferenciers(donneesDeBase.LireCsv("testsUnitaires\\conferenciers.csv"));
		donneesDeBase.importerExpositions(donneesDeBase.LireCsv("testsUnitaires\\expositions.csv"));
		donneesDeBase.importerVisites(donneesDeBase.LireCsv("testsUnitaires\\visites.csv"));
		statistiques.visiteFiltre = donneesDeBase.getVisites();
		visite = new ArrayList<>(donneesDeBase.getVisites());
	}

	@Test
	void testConferencierInterne() {
		statistiques.conferencierInterne();
		for (Visite visite : statistiques.visiteFiltre) {
			String conferencierId = visite.getConferencierId();
			assertTrue(donneesDeBase.getConferenciers().stream()
					.anyMatch(c -> c.getId().equals(conferencierId) && c.getEstEmploye()));
		}
	}

	@Test
	void testConferencierExterne() {
		statistiques.conferencierExterne();
		for (Visite visite : statistiques.visiteFiltre) {
			String conferencierId = visite.getConferencierId();
			assertTrue(donneesDeBase.getConferenciers().stream()
					.anyMatch(c -> c.getId().equals(conferencierId) && !c.getEstEmploye()));
		}
	}

	@Test
	void testVisitePeriode() throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date dateDebut = format.parse("01/01/2023");
		Date dateFin = format.parse("31/12/2023");

		statistiques.visitePeriode(dateDebut, dateFin);

		for (Visite visite : statistiques.visiteFiltre) {
			assertTrue(!visite.getDateVisite().before(dateDebut) && !visite.getDateVisite().after(dateFin));
		}
	}

	@Test
	void testVisitePlageHoraire() throws ParseException {
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

	@Test
	void testGetNbVisitesParExposition() {
		Map<String, Integer> nbVisitesParExposition = statistiques.getNbVisitesParExposition();
		int totalVisites = statistiques.visiteFiltre.size();
		int sum = nbVisitesParExposition.values().stream().mapToInt(Integer::intValue).sum();
		assertEquals(totalVisites, sum);
	}


	@Test
	void testGetPVisitesExpositions() {
		Map<String, Double> pVisitesExpositions = statistiques.getPVisitesExpositions();
		double totalPourcentage = calculerSomme(pVisitesExpositions);
		assertEquals(100.0, totalPourcentage, 0.01);
	}

	private double calculerSomme(Map<String, Double> pVisitesExpositions) {
		double totalPourcentage = 0.0;
		for (Map.Entry<String, Double> entry : pVisitesExpositions.entrySet()) {
			System.out.println(entry.getKey() + " : " + entry.getValue());
			totalPourcentage += entry.getValue();
		}
		return totalPourcentage;
	}

	@Test
	void testExpositionTemporaire() {
		statistiques.expositionTemporaire();
		for (Visite visite : statistiques.visiteFiltre) {
			String expositionId = visite.getExpositionId();
			assertTrue(donneesDeBase.getExpositions().stream()
					.anyMatch(e -> e.getId().equals(expositionId) && !e.estTemporaire()));
		}
	}

	@Test
	void testExpositionPermanente() {
		statistiques.expositionPermanente();
		for (Visite visite : statistiques.visiteFiltre) {
			String expositionId = visite.getExpositionId();
			assertTrue(donneesDeBase.getExpositions().stream()
					.anyMatch(e -> e.getId().equals(expositionId) && e.estTemporaire()));
		}
	}

	@Test
	void testVisitePlageHoraire10h00_11h00() throws ParseException {
		String heureDebut = "10h00";
		String heureFin = "11h00";

		statistiques.visitePlageHoraire(heureDebut, heureFin);

		SimpleDateFormat format = new SimpleDateFormat("HH'h'mm");
		Date debut = format.parse(heureDebut);
		Date fin = format.parse(heureFin);

		ArrayList<String> expectedVisites = new ArrayList<>(Arrays.asList("R000001",
				                                                          "R000006",
				                                                          "R000008",
				                                                          "R000010",
				                                                          "R000012",
				                                                          "R000014",
				                                                          "R000015",
				                                                          "R000016",
				                                                          "R000017"));

		ArrayList<String> actualVisites = new ArrayList<>();
		for (Visite visite : statistiques.visiteFiltre) {
			actualVisites.add(visite.getId());
		}

		assertEquals(expectedVisites, actualVisites);
	}
	@Test
	void testVisitePlageTemps() throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date dateDebut = format.parse("08/10/2024");
		Date dateFin = format.parse("01/11/2024");

		statistiques.visitePeriode(dateDebut, dateFin);

		ArrayList<String> expectedVisites = new ArrayList<>(Arrays.asList("R000005",
				"R000007",
				"R000009",
				"R000010",
				"R000011",
				"R000013",
				"R000015"));

		ArrayList<String> actualVisites = new ArrayList<>();
		for (Visite visite : statistiques.visiteFiltre) {
			actualVisites.add(visite.getId());
		}
	}

	@Test
	void testVisiteConfExterne() throws ParseException {
		statistiques.conferencierExterne();
		ArrayList<String> expectedVisites = new ArrayList<>(Arrays.asList("R000004",
				"R000005",
				"R000016",
				"R000017"));
		ArrayList<String> actualVisites = new ArrayList<>();
		for (Visite visite : statistiques.visiteFiltre) {
			actualVisites.add(visite.getId());
		}
		assertEquals(expectedVisites, actualVisites);
	}
	@Test
	void testVisiteConfInterne() throws ParseException {
		statistiques.conferencierInterne();
		ArrayList<String> expectedVisites = new ArrayList<>(Arrays.asList("R000001",
				"R000002", "R000003", "R000006", "R000007", "R000008", "R000009", "R000010",
				"R000011", "R000012", "R000013", "R000014", "R000015", "R000018"
				));
		ArrayList<String> actualVisites = new ArrayList<>();
		for (Visite visite : statistiques.visiteFiltre) {
			actualVisites.add(visite.getId());
		}
		assertEquals(expectedVisites, actualVisites);
	}

	@Test
	void testExpoTemporaire() throws ParseException {
		statistiques.expositionTemporaire();
		ArrayList<String> expectedVisites = new ArrayList<>(Arrays.asList(
				"R000001",
				"R000002",
				"R000003",
				"R000004",
				"R000005",
				"R000006"

		));
		ArrayList<String> actualVisites = new ArrayList<>();
		for (Visite visite : statistiques.visiteFiltre) {
			actualVisites.add(visite.getId());
		}
		assertEquals(expectedVisites, actualVisites);
	}

	@Test
	void testExpoPemanante() throws ParseException {
		statistiques.expositionPermanente();
		ArrayList<String> expectedVisites = new ArrayList<>(Arrays.asList(
				"R000007",
				"R000008",
				"R000009",
				"R000010",
				"R000011",
				"R000012",
				"R000013",
				"R000014",
				"R000015",
				"R000016",
				"R000017",
				"R000018"

		));
		ArrayList<String> actualVisites = new ArrayList<>();
		for (Visite visite : statistiques.visiteFiltre) {
			actualVisites.add(visite.getId());
		}
		assertEquals(expectedVisites, actualVisites);
	}

	@Test
	void testGetExpositionIntituleById() {
		String expositionId = "E000001";
		String expectedIntitule = "Les paysages impressionnistes";
		String actualIntitule = statistiques.getExpositionIntituleById(expositionId);
		assertEquals(expectedIntitule, actualIntitule);
	}

	@Test
	void testgetNomPrenomConferencierById() {
		String conferencierId = "C000001";
		String expectedNom = "Dupont Pierre";
		String actualNom = statistiques.getNomPrenomConferencierById(conferencierId);
		assertEquals(expectedNom, actualNom);
	}

}
