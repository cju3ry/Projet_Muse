package gestion_donnees;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import application.ControleurImporterLocal;

public class Filtre {

	private DonneesApplication donnees = ControleurImporterLocal.getDonnees();

	private ArrayList<Visite> visiteFiltre;
	private ArrayList<Exposition> expositionFiltre;
	private ArrayList<Conferencier> conferencierFiltre;

	private final ArrayList<Visite> visiteInitial = donnees.getVisites();
	private final ArrayList<Exposition> expositionInitial = donnees.getExpositions();
	private final ArrayList<Conferencier> conferencierInitial = donnees.getConferenciers();

	public Filtre() {
		this.visiteFiltre = new ArrayList<>(visiteInitial);
		this.expositionFiltre = new ArrayList<>();
		this.conferencierFiltre = new ArrayList<>();
	}

	private void initialiserVisiteFiltre() {
		if (visiteFiltre.isEmpty()) {
			this.visiteFiltre = new ArrayList<>(visiteInitial);
		}
	}

	private void initialiserExpositionFiltre() {
		if (!expositionFiltre.isEmpty()) {
			this.expositionFiltre = new ArrayList<>();
		}
	}

	private void initialiserConferencierFiltre() {
		if (!conferencierFiltre.isEmpty()) {
			this.conferencierFiltre = new ArrayList<>();
		}
	}

	public void conferencierNom(String nom, String prenom) {
		initialiserVisiteFiltre();
		String idConferencier = "";

		for (Conferencier conferencier : donnees.getConferenciers()) {
			if (nom.equals(conferencier.getNom()) && prenom.equals(conferencier.getPrenom())) {
				idConferencier = conferencier.getId();
				break;
			}
		}

		String finalIdConferencier = idConferencier;
		this.visiteFiltre.removeIf(visite -> !finalIdConferencier.equals(visite.getConferencierId()));
	}

	public void employeNom(String nom, String prenom) {
		initialiserVisiteFiltre();
		String idEmploye = "";

		for (Employe employe : donnees.getEmployes()) {
			if (nom.equals(employe.getNom()) && prenom.equals(employe.getPrenom())) {
				idEmploye = employe.getId();
				break;
			}
		}
		String finalIdEmploye = idEmploye;
		this.visiteFiltre.removeIf(visite -> !finalIdEmploye.equals(visite.getEmployeId()));
	}

	public void expositionIntitule(String intituleExpo) {
		initialiserVisiteFiltre();
		String idExposition = "";

		for (Exposition exposition : donnees.getExpositions()) {
			if (intituleExpo.equals(exposition.getIntitule())) {
				idExposition = exposition.getId();
				break;
			}
		}
		String finalIdExposition = idExposition;
		this.visiteFiltre.removeIf(visite -> !finalIdExposition.equals(visite.getExpositionId()));
	}

	public void visiteIntitule(String intituleVisite) {
		initialiserVisiteFiltre();
		this.visiteFiltre.removeIf(visite -> !intituleVisite.equals(visite.getIntitule()));
	}

	public void visiteNumTel(String numTel) {
		initialiserVisiteFiltre();
		this.visiteFiltre.removeIf(visite -> !numTel.equals(visite.getNumTel()));
	}

	public void datePrecise(Date datePrecise) {
		initialiserVisiteFiltre();
		this.visiteFiltre.removeIf(visite -> !datePrecise.equals(visite.getDateVisite()));
	}

	public void heurePrecise(String heurePrecise) throws ParseException {
		initialiserVisiteFiltre();
		SimpleDateFormat format = new SimpleDateFormat("HH'h'mm");
		Date heurePreciseDate = format.parse(heurePrecise);
		this.visiteFiltre.removeIf(visite -> !heurePreciseDate.equals(visite.getHeureVisite()));
	}

	public void datePeriode(Date dateDebut, Date dateFin) {
		initialiserVisiteFiltre();
		this.visiteFiltre.removeIf(visite -> 
		visite.getDateVisite().before(dateDebut) || visite.getDateVisite().after(dateFin));
	}

	public void heurePeriode(String dateHeureDebut, String dateHeureFin) throws ParseException {
		initialiserVisiteFiltre();
		SimpleDateFormat format = new SimpleDateFormat("HH'h'mm");
		Date heureDebut = format.parse(dateHeureDebut);
		Date heureFin = format.parse(dateHeureFin);
		this.visiteFiltre.removeIf(visite -> 
		visite.getHeureVisite().before(heureDebut) || visite.getHeureVisite().after(heureFin));
	}

	public void conferencierInterne() {
		initialiserVisiteFiltre();
		ArrayList<String> idConferencier = new ArrayList<>();

		for (Conferencier conferencier : donnees.getConferenciers()) {
			if (conferencier.getEstEmploye()) {
				idConferencier.add(conferencier.getId());
			}
		}

		this.visiteFiltre.removeIf(visite -> !idConferencier.contains(visite.getConferencierId()));
	}

	public void conferencierExterne() {
		initialiserVisiteFiltre();
		ArrayList<String> idConferencier = new ArrayList<>();

		for (Conferencier conferencier : donnees.getConferenciers()) {
			if (!conferencier.getEstEmploye()) {
				idConferencier.add(conferencier.getId());
			}
		}

		this.visiteFiltre.removeIf(visite -> !idConferencier.contains(visite.getConferencierId()));
	}

	public void expositionPermanente() {
		initialiserVisiteFiltre();
		ArrayList<String> idExposition = new ArrayList<>();

		for (Exposition exposition : donnees.getExpositions()) {
			if (!exposition.estTemporaire()) {
				idExposition.add(exposition.getId());
			}
		}

		this.visiteFiltre.removeIf(visite -> !idExposition.contains(visite.getExpositionId()));
	}

	public void expositionTemporaire() {
		initialiserVisiteFiltre();
		ArrayList<String> idExposition = new ArrayList<>();

		for (Exposition exposition : donnees.getExpositions()) {
			if (exposition.estTemporaire()) {
				idExposition.add(exposition.getId());
			}
		}

		this.visiteFiltre.removeIf(visite -> !idExposition.contains(visite.getExpositionId()));
	}

	public void expoVisitePeriode(String debut, String fin) throws ParseException {
		initialiserExpositionFiltre();

		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

		Date dateDebut = new Date();
		Date dateFin = new Date();
		Date dateVisite;

		dateDebut = format.parse(debut);
		dateFin = format.parse(fin);

		if (dateDebut.compareTo(dateFin) > 0) {
			throw new IllegalArgumentException("La date de début ne doit pas être supérieure à la date de fin.");
		}

		// Identifie les expositions visitées dans la période
		ArrayList<String> expositionsAvecVisitesDansPeriode = new ArrayList<>();
		for (Visite visite : visiteInitial) {
			dateVisite = visite.getDateVisite();
			if (!dateVisite.before(dateDebut) && !dateVisite.after(dateFin)) {
				expositionsAvecVisitesDansPeriode.add(visite.getExpositionId());
			}
		}

		// Ajoute les expositions sans visite dans la période spécifiée
		for (Exposition exposition : donnees.getExpositions()) {
			if (!expositionsAvecVisitesDansPeriode.contains(exposition.getId())
				&& !this.expositionFiltre.contains(exposition)) {
				this.expositionFiltre.add(exposition);
			}
		}
	}

	public void expoVisiteHoraire(String dateHeureDebut, String dateHeureFin) throws ParseException {
		initialiserExpositionFiltre();
		SimpleDateFormat formatHeure = new SimpleDateFormat("HH'h'mm");

		Date heureDebut = formatHeure.parse(dateHeureDebut);
		Date heureFin = formatHeure.parse(dateHeureFin);

		if (heureDebut.after(heureFin)) {
			throw new IllegalArgumentException("L'heure de début doit être avant l'heure de fin.");
		}

		// Identifie les expositions visitées dans l'intervalle d'heures spécifié
		ArrayList<String> expositionsAvecVisitesDansHoraire = new ArrayList<>();
		for (Visite visite : visiteInitial) {
			Date heureVisite = visite.getHeureVisite();
			if (!heureVisite.before(heureDebut) && !heureVisite.after(heureFin)) {
				expositionsAvecVisitesDansHoraire.add(visite.getExpositionId());
			}
		}

		// Ajoute les expositions sans visite dans l'intervalle horaire spécifié
		for (Exposition exposition : expositionInitial) {
			if (!expositionsAvecVisitesDansHoraire.contains(exposition.getId())
				&& !this.expositionFiltre.contains(exposition)) {
				this.expositionFiltre.add(exposition);
			}
		}
	}

	// Filtre conférencier par période de visite
	public void confVisitePeriode(Date dateDebut, Date dateFin) {
		initialiserConferencierFiltre();
		if (dateDebut.compareTo(dateFin) > 0) {
			throw new IllegalArgumentException("La date de début ne doit pas être supérieure à la date de fin.");
		}

		this.conferencierFiltre.removeIf(conferencier -> 
		visiteInitial.stream().noneMatch(visite -> 
		!visite.getDateVisite().before(dateDebut) &&
		!visite.getDateVisite().after(dateFin) &&
		visite.getConferencierId().equals(conferencier.getId())
				)
				);
	}

	// Filtre conférencier par horaire de visite
	public void confVisiteHoraire(String dateHeureDebut, String dateHeureFin) throws ParseException {
		initialiserConferencierFiltre();
		SimpleDateFormat formatHeure = new SimpleDateFormat("HH'h'mm");

		Date heureDebut = formatHeure.parse(dateHeureDebut);
		Date heureFin = formatHeure.parse(dateHeureFin);

		this.conferencierFiltre.removeIf(conferencier -> 
		visiteInitial.stream().noneMatch(visite -> 
		visite.getHeureVisite().after(heureDebut) &&
		visite.getHeureVisite().before(heureFin) &&
		visite.getConferencierId().equals(conferencier.getId())
				)
				);
	}

	public void reset() {
		this.visiteFiltre = new ArrayList<>(visiteInitial);
		this.expositionFiltre = new ArrayList<>();
		this.conferencierFiltre = new ArrayList<>();
	}

	public ArrayList<Visite> getListeVisite() {
		return this.visiteFiltre;
	}

	public ArrayList<Exposition> getListeExposition() {
		return this.expositionFiltre;
	}

	public ArrayList<Conferencier> getListeConferencier() {
		return this.conferencierFiltre;
	}

	public static void main(String[] args) throws ParseException {
		// Initialisation de l'application et importation des fichiers CSV avec chemins absolus
		DonneesApplication donnees = new DonneesApplication();
		Filtre filtres = new Filtre();

		DonneesApplication.importerEmployes(DonneesApplication.LireCsv("employes 28_08_24 17_26.csv"));
		DonneesApplication.importerExpositions(DonneesApplication.LireCsv("expositions 28_08_24 17_26.csv"));
		donnees.importerConferenciers(DonneesApplication.LireCsv("conferencier 28_08_24 17_26.csv"));
		DonneesApplication.importerVisites(DonneesApplication.LireCsv("visites 28_08_24 17_26.csv"));

		filtres.expoVisiteHoraire("10h00", "12h00");
		System.out.print("\n\n");
		for (Exposition exposition : filtres.getListeExposition()) {
			System.out.println(exposition.toString());
		}
	}
}
