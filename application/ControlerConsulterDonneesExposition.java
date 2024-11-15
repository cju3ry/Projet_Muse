package application;

import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import gestion_donnees.DonneesApplication;

import gestion_donnees.Exposition;

import gestion_donnees.Filtre;
import gestion_donnees.Visite;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

public class ControlerConsulterDonneesExposition {

	private DonneesApplication donnees;

	private Filtre filtresExpositions;

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
	private Button btnFiltre;

	@FXML
	private ScrollPane scrollPaneFiltres;

	@FXML
	private VBox panneauFiltres;

	@FXML
	private DatePicker expoDateDebut;

	@FXML
	private DatePicker expoDateFin;

	@FXML
	private ChoiceBox<String> heureDebut;

	@FXML
	private ChoiceBox<String> heureFin;

	@FXML
	private Button btnLancerFiltre;

	@FXML
	private Button btnReinitialiserFiltre;

	@FXML
	private TextArea textAreaConsultation;

	@FXML
	private Button btnSauvegarder;

	private StringBuilder strExpositionsLocal;

	private StringBuilder strExpositionsDistance;

	private StringBuilder strExpositonsSave;

	private boolean premierAffichageOk;

	private boolean donneesChargeesLocal; // Pour vérifier si les données sont déjà chargées en local

	private boolean donnesChargeesDistance; // Pour vérifier si les données sont déjà chargées a distance

	private boolean donnesChargeesSauvegarder;

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

		btnFiltre.setOnAction(event -> toggleFiltrePanel());
		btnLancerFiltre.setOnAction(event -> appliquerFiltre());
		btnReinitialiserFiltre.setOnAction(event -> reinitialiserFiltre());
	}

	// Méthode pour charger et afficher les données
	private void afficherDonnees() {
		filtresExpositions = new Filtre();
		boolean listeFiltreOk;
		donneesChargeesLocal = ControleurImporterLocal.isDonneesExpositionsChargees();
		donnesChargeesDistance = ControleurImporterDistance.isDonneesExpositionsChargees();
		donnesChargeesSauvegarder = ControleurPageDeGarde.isDonneesSaveChargees();

		strExpositionsLocal = ControleurImporterLocal.getStrExpositions();
		strExpositionsDistance = ControleurImporterDistance.getStrExpositions();
		strExpositonsSave = ControleurPageDeGarde.getStrExpositions();

		listeFiltreOk = true;

		if ((!donneesChargeesLocal || strExpositionsLocal == null) && (!donnesChargeesDistance || strExpositionsDistance == null)
				&& (!donnesChargeesSauvegarder || strExpositonsSave == null ))  { // Vérifie si les données n'ont pas déjà été chargées en local et a distance
			textAreaConsultation.setText("Les données ne sont pas encore disponibles.");
		}

		if (donneesChargeesLocal && !premierAffichageOk) {
			textAreaConsultation.setText(ControleurImporterLocal.getStrExpositions().toString());
			donnees = ControleurImporterLocal.getDonnees();
			premierAffichageOk = true;
			listeFiltreOk = false;
		} else if (donnesChargeesDistance && !premierAffichageOk) {
			textAreaConsultation.setText(ControleurImporterDistance.getStrExpositions().toString());
			donnees = ControleurImporterDistance.getDonnees();
			premierAffichageOk = true;
			listeFiltreOk = false;
		} else if (donnesChargeesSauvegarder && !premierAffichageOk) {
			donnees = ControleurPageDeGarde.getDonnees();
			textAreaConsultation.setText(ControleurPageDeGarde.getStrExpositions().toString());
			premierAffichageOk = true;
			listeFiltreOk = false;
		}

		if (premierAffichageOk && !listeFiltreOk) {
			listeFiltreOk = true;
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

	private void toggleFiltrePanel() {
		// Basculer la visibilité du panneau de filtres
		boolean isVisible = scrollPaneFiltres.isVisible();
		scrollPaneFiltres.setVisible(!isVisible);
		btnLancerFiltre.setVisible(!isVisible);
		btnReinitialiserFiltre.setVisible(!isVisible);
	}

	@FXML 
	void appliquerFiltre() {
		// Réinitialise les filtres pour partir de toutes les données disponibles
		filtresExpositions.reset();

		DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		String strDateDebut;
		String strDateFin;
		String strHeureDebut;
		String strHeureFin;
		String aAfficher = "";

		// Vérification et application du filtre par période de date
		if (expoDateDebut.getValue() != null && expoDateFin.getValue() != null) {
			try {
				// Conversion de LocalDate en String au format dd/MM/yyyy
				strDateDebut = expoDateDebut.getValue().format(dateTimeFormat);
				strDateFin = expoDateFin.getValue().format(dateTimeFormat);

				// Appel du filtre avec les dates sous forme de chaînes
				filtresExpositions.expoVisitePeriode(strDateDebut, strDateFin);
			} catch (Exception e) {
				e.printStackTrace();
				textAreaConsultation.setText("Erreur dans le format des dates.");
			}
		}

		// Vérification et application du filtre par horaire de visite
		if (heureDebut.getValue() != null && heureFin.getValue() != null) {
			strHeureDebut = heureDebut.getValue();
			strHeureFin = heureFin.getValue();

			try {
				filtresExpositions.expoVisiteHoraire(strHeureDebut, strHeureFin);
			} catch (ParseException e) {
				e.printStackTrace();
				textAreaConsultation.setText("Erreur dans le format des heures.");
			}
		}

		// Affichage des résultats filtrés
		if (!filtresExpositions.getListeExposition().isEmpty()) {
			for (Exposition exposition : filtresExpositions.getListeExposition()) {
				aAfficher += exposition.toString() + "\n";
			}
			textAreaConsultation.setText("\t\t\t\t\t\t\t\t\tRésultat pour votre recherche." 
					+ "\n\t\t\t\t\t\t\t\t     Nombre d'exposition(s) trouvée(s) : " 
					+ filtresExpositions.getListeExposition().size() + ".\n\n\n"
					+ aAfficher);
		} else {
			textAreaConsultation.setText("Aucun résultat à votre recherche.");
		}
	}

	@FXML
	void reinitialiserFiltre() {
		donneesChargeesLocal = ControleurImporterLocal.isDonneesExpositionsChargees();
		donnesChargeesDistance = ControleurImporterDistance.isDonneesExpositionsChargees();
		donnesChargeesSauvegarder = ControleurPageDeGarde.isDonneesSaveChargees();

		strExpositionsLocal = ControleurImporterLocal.getStrExpositions();
		strExpositionsDistance = ControleurImporterDistance.getStrExpositions();
		strExpositonsSave = ControleurPageDeGarde.getStrExpositions();

		if (donneesChargeesLocal) {
			textAreaConsultation.setText(ControleurImporterLocal.getStrExpositions().toString());
		} else if (donnesChargeesDistance) {
			textAreaConsultation.setText(ControleurImporterDistance.getStrExpositions().toString());
		} else if (donnesChargeesSauvegarder) {
			textAreaConsultation.setText(ControleurPageDeGarde.getStrExpositions().toString());
		}

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

		expoDateDebut.getEditor().clear();
		expoDateDebut.setValue(null);

		expoDateFin.getEditor().clear();
		expoDateFin.setValue(null);
	}

	@FXML
	void consulter(ActionEvent event) {
		reinitialiserFiltre();
		filtresExpositions.reset();
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
		filtresExpositions.reset();
		Main.setPageConsulter();
	}

	@FXML
	void sauvegarder(ActionEvent event) {
		Main.sauvegarder();
	}

}
