package application;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import gestion_donnees.Conferencier;
import gestion_donnees.ConferencierException;
import gestion_donnees.DonneesApplication;
import gestion_donnees.Employe;
import gestion_donnees.EmployeException;
import gestion_donnees.Exposition;
import gestion_donnees.ExpositionException;
import gestion_donnees.Filtre;
import gestion_donnees.Visite;
import gestion_donnees.VisiteException;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

public class ControlerConsulterDonneesVisite {

	private DonneesApplication donnees;

	private Filtre filtres = new Filtre();
	
	private boolean premierAffichageOk;

	@FXML
	private Button btnConsulter;

	@FXML
	private Button btnExporter;

	@FXML
	private Button btnImporter;

	@FXML
	private Button btnNotice;

	@FXML
	private Button btnQuitter1;

	@FXML
	private Button btnRevenir;

	@FXML
	private TextArea textAreaConsultation;

	private boolean donneesChargeesLocal; // Pour vérifier si les données sont déjà chargées en local

	private boolean donnesChargeesDistance; // Pour vérifier si les données sont déjà chargées a distance

	@FXML
	private Button btnFiltre;

	@FXML
	private ScrollPane scrollPaneFiltres;

	@FXML
	private VBox panneauFiltres;

	@FXML
	private ChoiceBox<String> nomConf;

	@FXML
	private ChoiceBox<String> nomPrenomEmploye;

	@FXML
	private ChoiceBox<String> intituleExpo;

	@FXML
	private ChoiceBox<String> intituleVisite;

	@FXML
	private ChoiceBox<String> numTel;

	@FXML
	private ChoiceBox<String> heurePrecise;

	@FXML
	private ChoiceBox<String> heureDebut;

	@FXML
	private ChoiceBox<String> heureFin;

	@FXML
	private RadioButton confInterne;

	@FXML
	private RadioButton confExterne;
	
	@FXML
	private RadioButton expoPerm;

	@FXML
	private RadioButton expoTemp;

	@FXML
	private DatePicker visiteDate;

	@FXML
	private DatePicker visiteDateDebut;

	@FXML
	private DatePicker visiteDateFin;

	@FXML
	private Button btnLancerFiltre;
	
	@FXML
	private Button btnReinitialiserFiltre;
	
	private StringBuilder strVisitesLocal;
	
	private StringBuilder strVisitesDistance;

	private ToggleGroup categorieToggleGroup;

	@FXML
	void initialize() {
		textAreaConsultation.setEditable(false);
		textAreaConsultation.setText("\n\n\n\n\n\n\n\n\n\n\n\t\t\t\t\t\t\t\t\t\t\tCliquez ici pour afficher les données.");
		
		premierAffichageOk = false;
		// Déclenchement de l'événement au clic sur la TextArea
		textAreaConsultation.setOnMouseClicked(event -> afficherDonnees());

		// Par défaut, cacher le panneau et boutons des filtres
		scrollPaneFiltres.setVisible(false);
		btnLancerFiltre.setVisible(false);
		btnReinitialiserFiltre.setVisible(false);

		// Initialiser ToggleGroup pour les RadioButton
		categorieToggleGroup = new ToggleGroup();
		confInterne.setToggleGroup(categorieToggleGroup);
		confExterne.setToggleGroup(categorieToggleGroup);
		
		categorieToggleGroup = new ToggleGroup();
		expoPerm.setToggleGroup(categorieToggleGroup);
		expoTemp.setToggleGroup(categorieToggleGroup);
		
		visiteDate.setEditable(false);
		visiteDateDebut.setEditable(false);
		visiteDateFin.setEditable(false);

		btnFiltre.setOnAction(event -> toggleFiltrePanel());
		btnLancerFiltre.setOnAction(event -> appliquerFiltre());
		btnReinitialiserFiltre.setOnAction(event -> reinitialiserFiltre());
	}

	private void toggleFiltrePanel() {
		// Basculer la visibilité du panneau de filtres
		boolean isVisible = scrollPaneFiltres.isVisible();
		scrollPaneFiltres.setVisible(!isVisible);
		btnLancerFiltre.setVisible(!isVisible);
		btnReinitialiserFiltre.setVisible(!isVisible);
	}

	// Méthode pour charger et afficher les données
	private void afficherDonnees() {
		boolean listeFiltreOk;
		ArrayList<String> nomsConf = new ArrayList<>();
		ArrayList<String> nomsEmploye = new ArrayList<>();
		ArrayList<String> IntituleExpo = new ArrayList<>();
		ArrayList<String> IntituleVisites = new ArrayList<>();
		ArrayList<String> numtels = new ArrayList<>();

		donneesChargeesLocal = ControleurImporterLocal.isDonneesVisitesChargees();
		donnesChargeesDistance = ControleurImporterDistance.isDonneesVisitesChargees();
		strVisitesLocal = ControleurImporterLocal.getStrVisites();
		strVisitesDistance = ControleurImporterDistance.getStrVisites();
		// System.out.print("Donnes Charge distance" + donnesChargeesDistance);
		
		listeFiltreOk = true;

		if ((!donneesChargeesLocal || strVisitesLocal == null) && (!donnesChargeesDistance || strVisitesDistance == null))  { // Vérifie si les données n'ont pas déjà été chargées en local et a distance
			textAreaConsultation.setText("Les données ne sont pas encore disponibles.");
		}
		
		if (donneesChargeesLocal && !premierAffichageOk) {
			donnees = ControleurImporterLocal.getDonnees();
			textAreaConsultation.setText(ControleurImporterLocal.getStrVisites().toString());
			premierAffichageOk = true;
			listeFiltreOk = false;
		} else if (donnesChargeesDistance && !premierAffichageOk) {
			textAreaConsultation.setText(ControleurImporterDistance.getStrVisites().toString());
			premierAffichageOk = true;
			listeFiltreOk = false;
		}
		
		if (premierAffichageOk && !listeFiltreOk) {
			listeFiltreOk = true;
			for (Conferencier conferencier : donnees.getConferenciers()) {
				if (!nomsConf.contains(conferencier.getNom())) {
					nomsConf.add(conferencier.getNom() + " " + conferencier.getPrenom()); // Ajoutez chaque nom et prénom à la liste
				}
			}
	
			for (Employe employe : donnees.getEmployes()) {
				if (!nomsEmploye.contains(employe.getNom())) {
					nomsEmploye.add(employe.getNom() + " " + employe.getPrenom()); // Ajoutez chaque nom et prénom à la liste
				}
			}
	
			for (Exposition exposition : donnees.getExpositions()) {
				if (!IntituleExpo.contains(exposition.getIntitule())) {
					IntituleExpo.add(exposition.getIntitule()); // Ajoutez chaque intitulé à la liste
				}
			}
	
			for (Visite visite : donnees.getVisites()) {
				if (!IntituleVisites.contains(visite.getIntitule())) {
					IntituleVisites.add(visite.getIntitule()); // Ajoutez chaque intitulé à la liste
				}
			}
	
			for (Visite visite : donnees.getVisites()) {
				if (!numtels.contains(visite.getNumTel())) {
					numtels.add(visite.getNumTel()); // Ajoutez chaque numéro de téléphone à la liste
				}
			}
	
			Collections.sort(nomsConf);
			Collections.sort(nomsEmploye);
			Collections.sort(IntituleExpo);
			Collections.sort(IntituleVisites);
			Collections.sort(numtels);
	
			nomConf.setItems(FXCollections.observableArrayList(nomsConf)); 
			nomPrenomEmploye.setItems(FXCollections.observableArrayList(nomsEmploye));
			intituleExpo.setItems(FXCollections.observableArrayList(IntituleExpo));
			intituleVisite.setItems(FXCollections.observableArrayList(IntituleVisites));
			numTel.setItems(FXCollections.observableArrayList(numtels));
	
			heurePrecise.setItems(FXCollections.observableArrayList(
					"8h00", "8h30", "9h00",
					"9h30", "10h00", "10h30",
					"11h00", "11h30", "12h00", 
					"12h30", "13h30", "13h30",
					"14h00", "14h30", "15h00", 
					"15h30","16h00", "16h30", "17h00"
					));
	
			heureDebut.setItems(FXCollections.observableArrayList(
					"8h00", "8h30", "9h00",
					"9h30", "10h00", "10h30",
					"11h00", "11h30", "12h00", 
					"12h30", "13h30", "13h30",
					"14h00", "14h30", "15h00", 
					"15h30","16h00", "16h30", "17h00"
					));
	
			heureFin.setItems(FXCollections.observableArrayList(
					"8h00", "8h30", "9h00",
					"9h30", "10h00", "10h30",
					"11h00", "11h30", "12h00", 
					"12h30", "13h30", "13h30",
					"14h00", "14h30", "15h00", 
					"15h30","16h00", "16h30", "17h00"
					));
		}
	}

	@FXML 
	void appliquerFiltre() {
		filtres.reset();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String nom;
		String prenom;
		String strAnalyser;
		String strAnalyserBis;
		String[] champs;
		String aAfficher;
		Date date;
		Date dateDebut;
		Date dateFin;
		LocalDate datePrecise;
		LocalDate dateIntervalleDebut;
		LocalDate dateIntervalleFin;
		
		aAfficher = "";
		date = null;
		dateDebut = null;
		dateFin = null;
		dateIntervalleDebut = null;

		if (nomConf.getValue() != null 
			&& !filtres.getListeVisite().isEmpty()) {
			strAnalyser = nomConf.getValue();
			champs = strAnalyser.split(" ");
			nom = champs[0];
			prenom = champs[1];
			filtres.conferencierNom(nom, prenom);
		}

		if (nomPrenomEmploye.getValue() != null 
			&& !filtres.getListeVisite().isEmpty()) {
			strAnalyser = nomPrenomEmploye.getValue();
			champs = strAnalyser.split(" ");
			nom = champs[0];
			prenom = champs[1];
			filtres.employeNom(nom, prenom);
		}

		if (intituleExpo.getValue() != null 
			&& !filtres.getListeVisite().isEmpty()) {
			strAnalyser = intituleExpo.getValue();
			filtres.expositionIntitule(strAnalyser);
		}

		if (intituleVisite.getValue() != null 
			&& !filtres.getListeVisite().isEmpty()) {
			strAnalyser = intituleVisite.getValue();
			filtres.visiteIntitule(strAnalyser);
		}

		if (numTel.getValue() != null 
			&& !filtres.getListeVisite().isEmpty()) {
			strAnalyser = numTel.getValue();
			filtres.visiteNumTel(strAnalyser);
		}

		if (heurePrecise.getValue() != null 
			&& !filtres.getListeVisite().isEmpty()) {
			strAnalyser = heurePrecise.getValue();
			try {
				filtres.heurePrecise(strAnalyser);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		if (heureDebut.getValue() != null 
			&& heureFin.getValue() != null 
		&& !filtres.getListeVisite().isEmpty()) {
			strAnalyser = heureDebut.getValue();
			strAnalyserBis = heureFin.getValue();
			try {
				filtres.heurePeriode(strAnalyser, strAnalyserBis);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		if (visiteDate.getValue() != null 
			&& !filtres.getListeVisite().isEmpty()) {
			try {
				datePrecise = visiteDate.getValue();
				
				strAnalyser = datePrecise.format(dateTimeFormat);
				datePrecise = LocalDate.parse(strAnalyser, dateTimeFormat);
				
				date = format.parse(datePrecise.format(dateTimeFormat).toString());
				
				filtres.datePrecise(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		if (visiteDateDebut.getValue() != null 
			&& visiteDateFin.getValue() != null 
			&& !filtres.getListeVisite().isEmpty()) {
			try {
				dateIntervalleDebut = visiteDateDebut.getValue();
				dateIntervalleFin = visiteDateFin.getValue();
				
				strAnalyser = dateIntervalleDebut.format(dateTimeFormat);
				strAnalyserBis = dateIntervalleFin.format(dateTimeFormat);
				
				dateIntervalleDebut = LocalDate.parse(strAnalyser, dateTimeFormat);
				dateIntervalleFin = LocalDate.parse(strAnalyserBis, dateTimeFormat);
				
				dateDebut = format.parse(dateIntervalleDebut.format(dateTimeFormat).toString());
				dateFin = format.parse(dateIntervalleFin.format(dateTimeFormat).toString());
				
				filtres.datePeriode(dateDebut, dateFin);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		if (expoPerm.isSelected() 
			&& !filtres.getListeVisite().isEmpty()) {
			filtres.expositionPermanente();
		} else if (expoTemp.isSelected()) {
			filtres.expositionTemporaire();
		}
		
		if (confInterne.isSelected() 
			&& !filtres.getListeVisite().isEmpty()) {
			filtres.conferencierInterne();
		} else if (confExterne.isSelected()) {
			filtres.conferencierExterne();
		}

		if (!filtres.getListeVisite().isEmpty() && !(filtres.getListeVisite().size() == 18)) {
			for (Visite visite : filtres.getListeVisite()) {
				aAfficher += visite + "\n\n";
			}
			textAreaConsultation.setText("\t\t\t\t\t\t\t\t\tRésultat pour votre recherche." 
										 + "\n\t\t\t\t\t\t\t\t     Nombre de visite(s) trouvée(s) : " 
										 + filtres.getListeVisite().size() + ".\n\n\n"
										 + aAfficher);
		} else {
			textAreaConsultation.setText("Aucun résultat à votre recherche.");
		}
	}
	
	@FXML
	void reinitialiserFiltre() {
		ArrayList<String> nomsConf = new ArrayList<>();
		ArrayList<String> nomsEmploye = new ArrayList<>();
		ArrayList<String> IntituleExpo = new ArrayList<>();
		ArrayList<String> IntituleVisites = new ArrayList<>();
		ArrayList<String> numtels = new ArrayList<>();
		
		donneesChargeesLocal = ControleurImporterLocal.isDonneesVisitesChargees();
		donnesChargeesDistance = ControleurImporterDistance.isDonneesVisitesChargees();
		strVisitesLocal = ControleurImporterLocal.getStrVisites();
		strVisitesDistance = ControleurImporterDistance.getStrVisites();
		// System.out.print("Données Charge distance" + donnesChargeesDistance);
		
		if (donneesChargeesLocal) {
			textAreaConsultation.setText(ControleurImporterLocal.getStrVisites().toString());
		} else if (donnesChargeesDistance) {
			textAreaConsultation.setText(ControleurImporterDistance.getStrVisites().toString());
		}

		if ((!donneesChargeesLocal || strVisitesLocal == null) && (!donnesChargeesDistance || strVisitesDistance == null))  { // Vérifie si les données n'ont pas déjà été chargées en local et a distance
			textAreaConsultation.setText("Les données ne sont pas encore disponibles.");
		}
		
		for (Conferencier conferencier : donnees.getConferenciers()) {
			if (!nomsConf.contains(conferencier.getNom())) {
				nomsConf.add(conferencier.getNom() + " " + conferencier.getPrenom()); // Ajoutez chaque nom et prénom à la liste
			}
		}

		for (Employe employe : donnees.getEmployes()) {
			if (!nomsEmploye.contains(employe.getNom())) {
				nomsEmploye.add(employe.getNom() + " " + employe.getPrenom()); // Ajoutez chaque nom et prénom à la liste
			}
		}

		for (Exposition exposition : donnees.getExpositions()) {
			if (!IntituleExpo.contains(exposition.getIntitule())) {
				IntituleExpo.add(exposition.getIntitule()); // Ajoutez chaque intitulé à la liste
			}
		}

		for (Visite visite : donnees.getVisites()) {
			if (!IntituleVisites.contains(visite.getIntitule())) {
				IntituleVisites.add(visite.getIntitule()); // Ajoutez chaque intitulé à la liste
			}
		}

		for (Visite visite : donnees.getVisites()) {
			if (!numtels.contains(visite.getNumTel())) {
				numtels.add(visite.getNumTel()); // Ajoutez chaque numéro de téléphone à la liste
			}
		}

		Collections.sort(nomsConf);
		Collections.sort(nomsEmploye);
		Collections.sort(IntituleExpo);
		Collections.sort(IntituleVisites);
		Collections.sort(numtels);

		nomConf.setItems(FXCollections.observableArrayList(nomsConf)); 
		nomPrenomEmploye.setItems(FXCollections.observableArrayList(nomsEmploye));
		intituleExpo.setItems(FXCollections.observableArrayList(IntituleExpo));
		intituleVisite.setItems(FXCollections.observableArrayList(IntituleVisites));
		numTel.setItems(FXCollections.observableArrayList(numtels));

		heurePrecise.setItems(FXCollections.observableArrayList(
				"8h00", "8h30", "9h00",
				"9h30", "10h00", "10h30",
				"11h00", "11h30", "12h00", 
				"12h30", "13h30", "13h30",
				"14h00", "14h30", "15h00", 
				"15h30","16h00", "16h30", "17h00"
				));

		heureDebut.setItems(FXCollections.observableArrayList(
				"8h00", "8h30", "9h00",
				"9h30", "10h00", "10h30",
				"11h00", "11h30", "12h00", 
				"12h30", "13h30", "13h30",
				"14h00", "14h30", "15h00", 
				"15h30","16h00", "16h30", "17h00"
				));

		heureFin.setItems(FXCollections.observableArrayList(
				"8h00", "8h30", "9h00",
				"9h30", "10h00", "10h30",
				"11h00", "11h30", "12h00", 
				"12h30", "13h30", "13h30",
				"14h00", "14h30", "15h00", 
				"15h30","16h00", "16h30", "17h00"
				));
		
		visiteDate.getEditor().clear();
		visiteDate.setValue(null);
		
		visiteDateDebut.getEditor().clear();
		visiteDateDebut.setValue(null);
		
		visiteDateFin.getEditor().clear();
		visiteDateFin.setValue(null);
		
		confInterne.setSelected(false);
		confExterne.setSelected(false);
		
		expoPerm.setSelected(false);
		expoTemp.setSelected(false);
	}

	@FXML
	void consulter(ActionEvent event) {
		reinitialiserFiltre();
		filtres.reset();
		Main.setPageConsulter();
	}

	@FXML
	void exporter(ActionEvent event) {
		Main.setPageExporter();

	}

	@FXML
	void importer(ActionEvent event) {
		Main.setPageImporter();
	}

	@FXML
	void notice(ActionEvent event) {

	}

	@FXML
	void quitter(ActionEvent event) {
		System.exit(0);
	}

	@FXML
	void revenirEnArriere(ActionEvent event) {
		reinitialiserFiltre();
		filtres.reset();
		Main.setPageConsulter();
	}

}
