package gestion_donnees;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

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
		if (expositionFiltre.isEmpty()) {
			this.expositionFiltre = new ArrayList<>(expositionInitial);
		}
	}

	private void initialiserConferencierFiltre() {
		if (conferencierFiltre.isEmpty()) {
			this.conferencierFiltre = new ArrayList<>(conferencierInitial);
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
			if (!expositionsAvecVisitesDansPeriode.contains(exposition.getId())) {
				this.expositionFiltre.remove(exposition);
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
			if (!expositionsAvecVisitesDansHoraire.contains(exposition.getId())) {
				this.expositionFiltre.remove(exposition);
			}
		}
	}

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
	
	public HashMap<String, Integer> confMoyennesPeriode(ArrayList<Conferencier> liste, String debut, String fin) throws ParseException {
		initialiserConferencierFiltre();
		
		HashMap<String, Integer> moyennes = new HashMap<>();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

		Date dateDebut = format.parse(debut);
		Date dateFin = format.parse(fin);
		long moyenne = 0;
		int nbVisite = 0;
		
		for (Conferencier conferencier : liste) {
			for (Visite visite : visiteInitial) {
				Date dateVisite = visite.getDateVisite();
				if (conferencier.getId().equals(visite.getConferencierId())
						&& !dateVisite.before(dateDebut) && !dateVisite.after(dateFin)) {
					nbVisite++;
				}
			}
			
			moyenne = nbVisite / ((dateDebut.getTime() / (60*60*24*1000)) - (dateFin.getTime() / (60*60*24*1000)));
			moyennes.put(conferencier.getId(), (int) moyenne);
		}
		
		return moyennes;
	}
	
	public HashMap<String, Integer> expoMoyennesPeriode(ArrayList<Exposition> liste, String debut, String fin) throws ParseException {
		initialiserConferencierFiltre();
		
		HashMap<String, Integer> moyennes = new HashMap<>();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

		Date dateDebut = format.parse(debut);
		Date dateFin = format.parse(fin);
		long moyenne = 0;
		int nbVisite = 0;
		
		for (Exposition exposition : liste) {
			for (Visite visite : visiteInitial) {
				Date dateVisite = visite.getDateVisite();
				if (exposition.getId().equals(visite.getExpositionId())
						&& !dateVisite.before(dateDebut) && !dateVisite.after(dateFin)) {
					nbVisite++;
				}
			}
			
			moyenne = nbVisite / ((dateDebut.getTime() / (60*60*24*1000)) - (dateFin.getTime() / (60*60*24*1000)));
			moyennes.put(exposition.getId(), (int) moyenne);
		}
		
		return moyennes;
	}

	public void reset() {
		this.visiteFiltre = new ArrayList<>(visiteInitial);
		this.expositionFiltre = new ArrayList<>(expositionInitial);
		this.conferencierFiltre = new ArrayList<>(conferencierInitial);
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
}
