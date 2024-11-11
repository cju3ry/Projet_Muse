package gestion_donnees;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Filtre {

	private DonneesApplication donnees = new DonneesApplication();

	private ArrayList<Visite> visiteFiltre;
	private ArrayList<Exposition> expositionFiltre;
	private ArrayList<Conferencier> conferencierFiltre;

	private final ArrayList<Visite> visiteInitial = donnees.getVisites();
	private final ArrayList<Exposition> expositionInitial = donnees.getExpositions();
	private final ArrayList<Conferencier> conferencierInitial = donnees.getConferenciers();

	public Filtre() {
		this.visiteFiltre = new ArrayList<>(visiteInitial);
		this.expositionFiltre = new ArrayList<>(expositionInitial);
		this.conferencierFiltre = new ArrayList<>(conferencierInitial);
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

	// Filtre exposition par période de visite
	public void expoVisitePeriode(Date dateDebut, Date dateFin) {
	    initialiserExpositionFiltre();
	    
	    // Vérification des dates de début et de fin
	    if (dateDebut.compareTo(dateFin) > 0) {
	        throw new IllegalArgumentException("La date de début ne doit pas être supérieure à la date de fin.");
	    }

	    // Liste pour stocker les expositions ayant une visite dans l'intervalle
	    ArrayList<String> expositionsAvecVisitesDansPeriode = new ArrayList<>();

	    // Parcours de toutes les visites pour trouver les expositions visitées dans la période
	    for (Visite visite : visiteInitial) {
	        Date dateVisite = visite.getDateVisite();
	        if (dateVisite != null && !dateVisite.before(dateDebut) && !dateVisite.after(dateFin)) {
	            expositionsAvecVisitesDansPeriode.add(visite.getExpositionId());
	        }
	    }

	    // On filtre les expositions pour conserver celles qui ne sont pas dans la liste
	    this.expositionFiltre.removeIf(exposition -> expositionsAvecVisitesDansPeriode.contains(exposition.getId()));
	}

	// Filtre exposition par horaire de visite
	public void expoVisiteHoraire(String dateHeureDebut, String dateHeureFin) throws ParseException {
		initialiserExpositionFiltre();
		SimpleDateFormat formatHeure = new SimpleDateFormat("HH'h'mm");

		Date heureDebut = formatHeure.parse(dateHeureDebut);
		Date heureFin = formatHeure.parse(dateHeureFin);

		this.expositionFiltre.removeIf(exposition -> 
			visiteInitial.stream().noneMatch(visite -> 
				visite.getHeureVisite().after(heureDebut) &&
				visite.getHeureVisite().before(heureFin) &&
				visite.getExpositionId().equals(exposition.getId())
			)
		);
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
	
	public static void main(String[] args) throws ParseException {
	    // Initialisation de l'application et importation des fichiers CSV avec chemins absolus
	    DonneesApplication donnees = new DonneesApplication();
	    Filtre filtres = new Filtre();
	    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
	    
	    Date dateDebut = new Date();
	    Date dateFin = new Date();
	    
	    dateDebut = format.parse("01/11/2024");
	    dateFin = format.parse("02/11/2024");

	    donnees.importerEmployes(donnees.LireCsv("employes 28_08_24 17_26.csv"));
	    donnees.importerExpositions(donnees.LireCsv("expositions 28_08_24 17_26.csv"));
	    donnees.importerConferenciers(donnees.LireCsv("conferencier 28_08_24 17_26.csv"));
	    donnees.importerVisites(donnees.LireCsv("visites 28_08_24 17_26.csv"));
	    
	    filtres.expoVisitePeriode(dateDebut, dateFin);
	    System.out.print("\n\n");
	    for (Exposition exposition : filtres.getListeExposition()) {
	    	System.out.print(exposition.toString());
	    }
	}
}
