package gestion_donnees;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import application.ControleurImporterLocal;

public class Filtre {
	// Données de l'application
	private DonneesApplication donnees = ControleurImporterLocal.getDonnees();
	// Données filtrées
	private ArrayList<Visite> visiteFiltre;
	private ArrayList<Exposition> expositionFiltre;
	private ArrayList<Conferencier> conferencierFiltre;
	// Données initiales
	private final ArrayList<Visite> visiteInitial = donnees.getVisites();
	private final ArrayList<Exposition> expositionInitial = donnees.getExpositions();
	private final ArrayList<Conferencier> conferencierInitial = donnees.getConferenciers();
	/**
	 * Constructeur de la classe Filtre
	 */
	public Filtre() {
		this.visiteFiltre = new ArrayList<>(visiteInitial);
		this.expositionFiltre = new ArrayList<>();
		this.conferencierFiltre = new ArrayList<>();
	}
	/**
	 * Méthode pour initialiser les visites filtrées
	 */
	private void initialiserVisiteFiltre() {
		if (visiteFiltre.isEmpty()) {
			this.visiteFiltre = new ArrayList<>(visiteInitial);
		}
	}
	/**
	 * Méthode pour initialiser les expositions filtrées
	 */
	private void initialiserExpositionFiltre() {
		if (expositionFiltre.isEmpty()) {
			this.expositionFiltre = new ArrayList<>(expositionInitial);
		}
	}
	/**
	 * Méthode pour initialiser les conférenciers filtrés
	 */
	private void initialiserConferencierFiltre() {
		if (conferencierFiltre.isEmpty()) {
			this.conferencierFiltre = new ArrayList<>(conferencierInitial);
		}
	}
	/**
	 * Méthode pour filtrer les visites par nom de l'employé
	 * @param nom Nom de l'employé
	 * @param prenom Prénom de l'employé
	 */
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
	/**
	 * Méthode pour filtrer les visites par nom de l'employé
	 * @param nom Nom de l'employé
	 * @param prenom Prénom de l'employé
	 */
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
	/**
	 * Méthode pour filtrer les visites par intitulé de l'exposition
	 * @param intituleExpo Intitulé de l'exposition
	 */
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
	/**
	 * Méthode pour filtrer les visites par intitulé de la visite
	 * @param intituleVisite Intitulé de la visite
	 */
	public void visiteIntitule(String intituleVisite) {
		initialiserVisiteFiltre();
		this.visiteFiltre.removeIf(visite -> !intituleVisite.equals(visite.getIntitule()));
	}
	/**
	 * Méthode pour filtrer les visites par numéro de téléphone
	 * @param numTel Numéro de téléphone
	 */
	public void visiteNumTel(String numTel) {
		initialiserVisiteFiltre();
		this.visiteFiltre.removeIf(visite -> !numTel.equals(visite.getNumTel()));
	}
	/**
	 * Méthode pour filtrer les visites par date précise
	 * @param datePrecise la Date précise
	 */
	public void datePrecise(Date datePrecise) {
		initialiserVisiteFiltre();
		this.visiteFiltre.removeIf(visite -> !datePrecise.equals(visite.getDateVisite()));
	}
	/**
	 * Méthode pour filtrer les visites par heure précise
	 * @param heurePrecise l'heure précise
	 */
	public void heurePrecise(String heurePrecise) throws ParseException {
		initialiserVisiteFiltre();
		SimpleDateFormat format = new SimpleDateFormat("HH'h'mm");
		Date heurePreciseDate = format.parse(heurePrecise);
		this.visiteFiltre.removeIf(visite -> !heurePreciseDate.equals(visite.getHeureVisite()));
	}
	/**
	 * Méthode pour filtrer les visites par période
	 * @param dateDebut Date de début de la période
	 * @param dateFin Date de fin de la période
	 */
	public void datePeriode(Date dateDebut, Date dateFin) {
		if(dateDebut.after(dateFin)) {
			throw new IllegalArgumentException("La date de fin ne peut être inféreiure à la date de début");
		}
		initialiserVisiteFiltre();
		this.visiteFiltre.removeIf(visite -> 
		visite.getDateVisite().before(dateDebut) || visite.getDateVisite().after(dateFin));
	}
	/**
	 * Méthode pour filtrer les visites par période
	 * @param dateHeureDebut Date de début de la période
	 * @param dateHeureFin Date de fin de la période
	 */
	public void heurePeriode(String dateHeureDebut, String dateHeureFin) throws ParseException {
		initialiserVisiteFiltre();
		SimpleDateFormat format = new SimpleDateFormat("HH'h'mm");
		Date heureDebut = format.parse(dateHeureDebut);
		Date heureFin = format.parse(dateHeureFin);
		this.visiteFiltre.removeIf(visite -> 
		visite.getHeureVisite().before(heureDebut) || visite.getHeureVisite().after(heureFin));
	}
	/**
	 * Méthode pour filtrer les visites par conférencier internes
	 */
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
	/**
	 * Méthode pour filtrer les visites par conférencier externes
	 */
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
	/**
	 * Méthode pour filtrer les visites par exposition permanente
	 */
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
	/**
	 * Méthode pour filtrer les visites par exposition temporaire
	 */
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
	/**
	 * Méthode pour filtrer les visites par période de l'exposition
	 * @param debut Date de début de la période
	 * @param fin Date de fin de la période
	 */
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
			if (!expositionsAvecVisitesDansPeriode.contains(exposition.getId())) {
				this.expositionFiltre.remove(exposition);
			}
		}
	}

	/**
	 * Méthode pour filtrer les visites par horaire de l'exposition
	 * @param dateHeureDebut
	 * @param dateHeureFin
	 * @throws ParseException
	 */
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
			if (!expositionsAvecVisitesDansHoraire.contains(exposition.getId())) {
				this.expositionFiltre.remove(exposition);
			}
		}
	}
	/**
	 * Méthode pour filtrer les visites par période concernant les conférenciers
	 * @param debut Date de début de la période
	 * @param fin Date de fin de la période
	 */
	public void confVisitePeriode(String debut, String fin) throws ParseException {
		initialiserConferencierFiltre();

		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

		Date dateDebut = format.parse(debut);
		Date dateFin = format.parse(fin);

		if (dateDebut.after(dateFin)) {
			throw new IllegalArgumentException("La date de début ne doit pas être supérieure à la date de fin.");
		}

		// Identifie les conférenciers ayant des visites dans la période
		ArrayList<String> conferenciersAvecVisitesDansPeriode = new ArrayList<>();
		for (Visite visite : visiteInitial) {
			Date dateVisite = visite.getDateVisite();
			if (!dateVisite.before(dateDebut) && !dateVisite.after(dateFin)) {
				conferenciersAvecVisitesDansPeriode.add(visite.getConferencierId());
			}
		}

		// Ajoute les conférenciers sans visites dans la période spécifiée
		for (Conferencier conferencier : donnees.getConferenciers()) {
			if (!conferenciersAvecVisitesDansPeriode.contains(conferencier.getId())) {
				this.conferencierFiltre.remove(conferencier);
			}
		}
	}
	/**
	 * Méthode pour filtrer les visites par horaire concernant les conférenciers
	 * @param dateHeureDebut Date de début de la période
	 * @param dateHeureFin Date de fin de la période
	 */
	public void confVisiteHoraire(String dateHeureDebut, String dateHeureFin) throws ParseException {
		initialiserConferencierFiltre();
		SimpleDateFormat formatHeure = new SimpleDateFormat("HH'h'mm");

		Date heureDebut = formatHeure.parse(dateHeureDebut);
		Date heureFin = formatHeure.parse(dateHeureFin);

		if (heureDebut.after(heureFin)) {
			throw new IllegalArgumentException("L'heure de début doit être avant l'heure de fin.");
		}

		// Identifie les conférenciers ayant des visites dans l'intervalle horaire
		ArrayList<String> conferenciersAvecVisitesDansHoraire = new ArrayList<>();
		for (Visite visite : visiteInitial) {
			Date heureVisite = visite.getHeureVisite();
			if (!heureVisite.before(heureDebut) && !heureVisite.after(heureFin)) {
				conferenciersAvecVisitesDansHoraire.add(visite.getConferencierId());
			}
		}

		// Ajoute les conférenciers sans visites dans l'intervalle horaire spécifié
		for (Conferencier conferencier : donnees.getConferenciers()) {
			if (!conferenciersAvecVisitesDansHoraire.contains(conferencier.getId())) {
				this.conferencierFiltre.remove(conferencier);
			}
		}
	}
	/**
	 * Méthode pour obtenir la moyenne des visites par conférencier dans une période
	 * @param liste Liste des conférenciers
	 * @param debut Date de début de la période
	 * @param fin Date de fin de la période
	 */
	public HashMap<Conferencier, Double> confMoyennesPeriode(ArrayList<Conferencier> liste, String debut, String fin) throws ParseException {
		initialiserConferencierFiltre();
		
		HashMap<Conferencier, Double> moyennes = new HashMap<>();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

		Date dateDebut = format.parse(debut);
		Date dateFin = format.parse(fin);
		double moyenne = 0;
		double nbVisite = 0;
		
		for (Conferencier conferencier : liste) {
			nbVisite = 0;
			for (Visite visite : visiteInitial) {
				Date dateVisite = visite.getDateVisite();
				if (conferencier.getId().equals(visite.getConferencierId())
						&& !dateVisite.before(dateDebut) && !dateVisite.after(dateFin)) {
					nbVisite++;
				}
			}
			
			moyenne = nbVisite / ((dateFin.getTime() - dateDebut.getTime()) / 86400000);
			moyennes.put(conferencier, (double) moyenne);
		}
		
		return moyennes;
	}

	/**
	 * Trie les expositions en fonction du nombre de visites par ordre croissant ou décroissant
	 * @param listExposition
	 * @param ordreCroissant
	 */
	public void expositionstrie(ArrayList<Exposition> listExposition, boolean ordreCroissant) {
		trierExpositions(listExposition, ordreCroissant);
	}
	/**
	 * Trie les expositions en fonction du nombre de visites par ordre croissant ou décroissant
	 * @param listExposition
	 * @param ordreCroissant
	 */
	public void trierExpositions(ArrayList<Exposition> listExposition, boolean ordreCroissant) {
		// Compter les visites pour chaque exposition
		Map<String, Integer> visiteCount = new HashMap<>();
		for (Visite visite : donnees.getVisites()) {
			visiteCount.put(visite.getExpositionId(), visiteCount.getOrDefault(visite.getExpositionId(), 0) + 1);
		}

		// Trier la liste des expositions en fonction du nombre de visites
		listExposition.sort((a, b) -> {
			int compA = visiteCount.getOrDefault(a.getId(), 0);
			int compB = visiteCount.getOrDefault(b.getId(), 0);

			// Retourner selon l'ordre croissant ou décroissant
			return ordreCroissant ? Integer.compare(compA, compB) : Integer.compare(compB, compA);
		});
	}

	/**
	 * Trie les conférenciers en fonction du nombre de visites par ordre croissant ou décroissant
	 * @param listConferencier
	 * @param ordreCroissant
	 */
	public void conferenciersTrie(ArrayList<Conferencier> listConferencier, boolean ordreCroissant) {
		trierConferencier(listConferencier, ordreCroissant);
	}
	/**
	 * Trie les conférenciers en fonction du nombre de visites par ordre croissant ou décroissant
	 * @param listConferencier
	 * @param ordreCroissant
	 */
	public void trierConferencier(ArrayList<Conferencier> listConferencier, boolean ordreCroissant) {
		// Compter les visites pour chaque exposition
		Map<String, Integer> visiteCount = new HashMap<>();
		for (Visite visite : donnees.getVisites()) {
			visiteCount.put(visite.getConferencierId(), visiteCount.getOrDefault(visite.getConferencierId(), 0) + 1);
		}

		// Trier la liste des conferenciers en fonction du nombre de visites
		listConferencier.sort((a, b) -> {
			int compA = visiteCount.getOrDefault(a.getId(), 0);
			int compB = visiteCount.getOrDefault(b.getId(), 0);

			// Retourner selon l'ordre croissant ou décroissant
			return ordreCroissant ? Integer.compare(compA, compB) : Integer.compare(compB, compA);
		});
	}
	/**
	 * Permets d'obtenir la moyenne des visites par exposition dans une période
	 * @param liste Liste des expositions
	 */
	public HashMap<Exposition, Double> expoMoyennesPeriode(ArrayList<Exposition> liste, String debut, String fin) throws ParseException {
		initialiserExpositionFiltre();
		
		HashMap<Exposition, Double> moyennes = new HashMap<>();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

		Date dateDebut = format.parse(debut);
		Date dateFin = format.parse(fin);
		double moyenne = 0;
		double nbVisite = 0;
		
		for (Exposition exposition : liste) {
			nbVisite = 0;
			for (Visite visite : visiteInitial) {
				Date dateVisite = visite.getDateVisite();
				if (exposition.getId().equals(visite.getExpositionId())
						&& !dateVisite.before(dateDebut) && !dateVisite.after(dateFin)) {
					nbVisite++;
				}
			}
			
			moyenne = nbVisite / ((dateFin.getTime() - dateDebut.getTime()) / 86400000);
			moyennes.put(exposition, (double) moyenne);
		}
		
		return moyennes;
	}
	/**
	 * Permets de réinitialiser les données filtrées
	 */
	public void reset() {
		this.visiteFiltre = new ArrayList<>(visiteInitial);
		this.expositionFiltre = new ArrayList<>(expositionInitial);
		this.conferencierFiltre = new ArrayList<>(conferencierInitial);
	}
	/**
	 * Permets d'obtenir la liste des visites filtrées
	 */
	public ArrayList<Visite> getListeVisite() {
		return this.visiteFiltre;
	}
	/**
	 * Permets d'obtenir la liste des expositions filtrées
	 */
	public ArrayList<Exposition> getListeExposition() {
		return this.expositionFiltre;
	}
	/**
	 * Permets d'obtenir la liste des conférenciers filtrés
	 */
	public ArrayList<Conferencier> getListeConferencier() {
		return this.conferencierFiltre;
	}
}
