package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import gestion_donnees.Conferencier;
import gestion_donnees.ConferencierException;
import gestion_donnees.DonneesApplication;
import gestion_donnees.Employe;
import gestion_donnees.EmployeException;
import gestion_donnees.Exposition;
import gestion_donnees.ExpositionException;
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

	private DonneesApplication donnees = new DonneesApplication();

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
	private DatePicker visiteDate;

	@FXML
	private DatePicker visiteDateDebut;

	@FXML
	private DatePicker visiteDateFin;

	private ToggleGroup categorieToggleGroup;

	@FXML
	void initialize() {
		textAreaConsultation.setEditable(false);
		textAreaConsultation.setText("\n\n\n\n\n\n\n\n\n\n\n\t\t\t\t\t\t\t\t\t\t\tCliquez ici pour afficher les données.");

		// Déclenchement de l'événement au clic sur la TextArea
		textAreaConsultation.setOnMouseClicked(event -> afficherDonnees());

		// Par défaut, cacher le panneau de filtres
		scrollPaneFiltres.setVisible(false);

		// Initialiser ToggleGroup pour les RadioButton
		categorieToggleGroup = new ToggleGroup();
		confInterne.setToggleGroup(categorieToggleGroup);
		confExterne.setToggleGroup(categorieToggleGroup);

		btnFiltre.setOnAction(event -> toggleFiltrePanel());
	}

	private void toggleFiltrePanel() {
		// Basculer la visibilité du panneau de filtres
		boolean isVisible = scrollPaneFiltres.isVisible();
		scrollPaneFiltres.setVisible(!isVisible);
	}

	// Méthode pour charger et afficher les données
	private void afficherDonnees() {
		ArrayList<String> nomsConf = new ArrayList<>();
		ArrayList<String> nomsEmploye = new ArrayList<>();
		ArrayList<String> IntituleExpo = new ArrayList<>();
		ArrayList<String> IntituleVisites = new ArrayList<>();
		ArrayList<String> numtels = new ArrayList<>();

		donneesChargeesLocal = ControleurImporterLocal.isDonneesVisitesChargees();
		donnesChargeesDistance = ControleurImporterDistance.isDonneesVisitesChargees();
		StringBuilder strVisitesLocal = ControleurImporterLocal.getStrVisites();
		StringBuilder strVisitesDistance = ControleurImporterDistance.getStrVisites();
		System.out.print("Donnes Charge distance" + donnesChargeesDistance);
		
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
				IntituleExpo.add(exposition.getIntitule()); // Ajoutez chaque nom et prénom à la liste
			}
		}

		for (Visite visite : donnees.getVisites()) {
			if (!IntituleVisites.contains(visite.getIntitule())) {
				IntituleVisites.add(visite.getIntitule()); // Ajoutez chaque nom et prénom à la liste
			}
		}

		for (Visite visite : donnees.getVisites()) {
			if (!numtels.contains(visite.getNumTel())) {
				numtels.add(visite.getNumTel()); // Ajoutez chaque nom et prénom à la liste
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
				"9h30", "10h30", "11h00",
				"10h00", "11h30", "12h00", 
				"12h30", "13h30", "13h30",
				"14h00", "14h30", "15h00", 
				"15h30","16h00", "16h30", "17h00"
				));

		heureDebut.setItems(FXCollections.observableArrayList(
				"8h00", "8h30", "9h00",
				"9h30", "10h30", "11h00",
				"10h00", "11h30", "12h00", 
				"12h30", "13h30", "13h30",
				"14h00", "14h30", "15h00", 
				"15h30","16h00", "16h30", "17h00"
				));

		heureFin.setItems(FXCollections.observableArrayList(
				"8h00", "8h30", "9h00",
				"9h30", "10h30", "11h00",
				"10h00", "11h30", "12h00", 
				"12h30", "13h30", "13h30",
				"14h00", "14h30", "15h00", 
				"15h30","16h00", "16h30", "17h00"
				));
	}
	
	@FXML
	void filtrageConferencier(ActionEvent event) {
		if (donneesChargeesLocal) {
			textAreaConsultation.setText(ControleurImporterLocal.getStrVisites().toString());
		} else if (donnesChargeesDistance) {
			textAreaConsultation.setText(ControleurImporterDistance.getStrVisites().toString());
		}
	}

	@FXML
	void consulter(ActionEvent event) {
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
		Main.setPageConsulter();
	}

}
