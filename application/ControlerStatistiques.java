package application;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import gestion_donnees.Conferencier;
import gestion_donnees.DonneesApplication;
import gestion_donnees.FichierPdf;
import gestion_donnees.Statistiques;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ControlerStatistiques {

	private DonneesApplication donnees;

	private Statistiques stats;

	@FXML
	private Button btnConsulter;

	@FXML
	private Button btnExporter;

	@FXML
	private Button btnStat;

	@FXML
	private Button btnImporter;

	@FXML
	private Button btnLancerStat;

	@FXML
	private Button btnNotice;

	@FXML
	private Button btnQuitter1;

	@FXML
	private Button btnReinitialiserFiltre;

	@FXML
	private Button btnRevenir;
	
	@FXML
	private Button genererPdf;

	@FXML
	private ChoiceBox<String> choixConfExpo;

	@FXML
	private DatePicker confDateDebut;

	@FXML
	private DatePicker confDateFin;

	@FXML
	private RadioButton confExterne;

	@FXML
	private ChoiceBox<String> confHeureDebut;

	@FXML
	private ChoiceBox<String> confHeureFin;

	@FXML
	private RadioButton confInterne;

	@FXML
	private Label confLabelDate;

	@FXML
	private Label confLabelEtat;

	@FXML
	private Label confLabelHeureDebut;

	@FXML
	private Label confLabelHeureFin;

	@FXML
	private DatePicker expoDateDebut;

	@FXML
	private DatePicker expoDateFin;

	@FXML
	private ChoiceBox<String> expoHeureDebut;

	@FXML
	private ChoiceBox<String> expoHeureFin;

	@FXML
	private Label expoLabelDates;

	@FXML
	private Label expoLabelEtat;

	@FXML
	private Label expoLabelHeureDebut;

	@FXML
	private Label expoLabelHeureFin;

	@FXML
	private RadioButton expoPerm;

	@FXML
	private RadioButton expoTemp;

	@FXML
	private VBox panneauFiltres;
	
	@FXML
	private Button btnSauvegarder;

	@FXML
	private ScrollPane scrollPaneFiltres;

	@FXML
	private TextArea textAreaConsultation;

	private boolean donneesChargeesLocalConferencier; // Pour vérifier si les données sont déjà chargées en local

	private boolean donnesChargeesDistanceConferencier; // Pour vérifier si les données sont déjà chargées a distance

	private boolean donnesChargeesSauvegarderConferencier;

	private boolean donneesChargeesLocalExposition; // Pour vérifier si les données sont déjà chargées en local

	private boolean donnesChargeesDistanceExposition; // Pour vérifier si les données sont déjà chargées a distance

	private boolean donnesChargeesSauvegarderExposition;

	private StringBuilder strConferencierLocal;

	private StringBuilder strConferencierDistance;

	private StringBuilder strConferencierSave;

	private StringBuilder strExpositionLocal;

	private StringBuilder strExpositionDistance;

	private StringBuilder strExpositionSave;

	private boolean premierAffichageOk;

	private ToggleGroup categorieToggleGroup;

	// String pour le titre de la page
	private String titre;

	// Liste contenant les filtres appliqués
	private ArrayList<String> listeDesFiltres= new ArrayList<>();

	// Liste contenant le contenu du fichier
	private String contenuFichier;

	@FXML
	void initialize() {
		textAreaConsultation.setEditable(false);
		textAreaConsultation.setText("\n\n\n\n\n\n\n\n\n\n\n\t\t\t\t\t\t" +
				"Sélectionnez un type de données à afficher à l'aide de la liste déroulante en haut à droite, " +
				"\n\t\t\t\t\t\t\t\t\tPuis cliquez sur la zone de texte pour afficher les données.");

		premierAffichageOk = false;
		// Déclenchement de l'événement au clic sur la TextArea
		textAreaConsultation.setOnMouseClicked(event -> afficherDonnees());

		choixConfExpo.setOnAction(event -> toggleFiltreExpoConf());
		choixConfExpo.setItems(FXCollections.observableArrayList("Conferencier", "Exposition"));
		choixConfExpo.setValue("Choisir :");

		confDateDebut.setVisible(false);
		confDateFin.setVisible(false);
		confHeureDebut.setVisible(false);
		confHeureFin.setVisible(false);
		confExterne.setVisible(false);
		confInterne.setVisible(false);
		confLabelDate.setVisible(false);
		confLabelHeureDebut.setVisible(false);
		confLabelHeureFin.setVisible(false);
		confLabelEtat.setVisible(false);

		expoDateDebut.setVisible(false);
		expoDateFin.setVisible(false);
		expoHeureDebut.setVisible(false);
		expoHeureFin.setVisible(false);
		expoPerm.setVisible(false);
		expoTemp.setVisible(false);
		expoLabelDates.setVisible(false);
		expoLabelHeureDebut.setVisible(false);
		expoLabelHeureFin.setVisible(false);
		expoLabelEtat.setVisible(false);

		// Par défaut, cacher le panneau et boutons des filtres
		scrollPaneFiltres.setVisible(false);
		btnLancerStat.setVisible(false);
		btnReinitialiserFiltre.setVisible(false);

		categorieToggleGroup = new ToggleGroup();
		confInterne.setToggleGroup(categorieToggleGroup);
		confExterne.setToggleGroup(categorieToggleGroup);

		categorieToggleGroup = new ToggleGroup();
		expoPerm.setToggleGroup(categorieToggleGroup);
		expoTemp.setToggleGroup(categorieToggleGroup);

		btnStat.setOnAction(event -> toggleFiltrePanel());
		btnLancerStat.setOnAction(event -> appliquerFiltreStat());
		btnReinitialiserFiltre.setOnAction(event -> reinitialiserFiltre());
	}

	// Méthode pour charger et afficher les données
	private void afficherDonnees() {
		boolean listeFiltreOk;
		donneesChargeesLocalConferencier = ControleurImporterLocal.isDonneesConferencierChargees();
		donnesChargeesDistanceConferencier = ControleurImporterDistance.isDonneesConferencierChargees();
		donnesChargeesSauvegarderConferencier = ControleurPageDeGarde.isDonneesSaveChargees();

		donneesChargeesLocalExposition = ControleurImporterLocal.isDonneesConferencierChargees();
		donnesChargeesDistanceExposition = ControleurImporterDistance.isDonneesConferencierChargees();
		donnesChargeesSauvegarderExposition = ControleurPageDeGarde.isDonneesSaveChargees();

		strConferencierLocal = ControleurImporterLocal.getStrConferencier();
		strConferencierDistance = ControleurImporterDistance.getStrConferencier();
		strConferencierSave = ControleurPageDeGarde.getStrConferencier();

		strExpositionLocal = ControleurImporterLocal.getStrConferencier();
		strExpositionDistance = ControleurImporterDistance.getStrConferencier();
		strExpositionSave = ControleurPageDeGarde.getStrConferencier();

		listeFiltreOk = true;

		if ((!donneesChargeesLocalConferencier || strConferencierLocal == null) 
				&& (!donnesChargeesDistanceConferencier || strConferencierDistance == null)
				&& !donnesChargeesSauvegarderConferencier || strConferencierSave == null 
				&& (!donneesChargeesLocalExposition || strExpositionLocal == null) 
				&& (!donnesChargeesDistanceExposition || strExpositionDistance == null)
				&& !donnesChargeesSauvegarderExposition || strExpositionSave == null ) { // Vérifie si les données n'ont pas déjà été chargées en local et a distance
			textAreaConsultation.setText("Les données ne sont pas encore disponibles.");
		}

		if (donneesChargeesLocalConferencier && !premierAffichageOk 
				&& choixConfExpo.getValue().equals("Conferencier")) {
			textAreaConsultation.setText(ControleurImporterLocal.getStrConferencier().toString());
			donnees = ControleurImporterLocal.getDonnees();
			premierAffichageOk = true;
			listeFiltreOk = false;
		} else if (donneesChargeesLocalExposition && !premierAffichageOk 
				&& choixConfExpo.getValue().equals("Exposition")) {
			textAreaConsultation.setText(ControleurImporterLocal.getStrExpositions().toString());
			donnees = ControleurImporterLocal.getDonnees();
			premierAffichageOk = true;
			listeFiltreOk = false;
		} else if (donnesChargeesDistanceConferencier && !premierAffichageOk
				&& choixConfExpo.getValue().equals("Conferencier")) {
			textAreaConsultation.setText(ControleurImporterDistance.getStrConferencier().toString());
			donnees = ControleurImporterDistance.getDonnees();
			premierAffichageOk = true;
			listeFiltreOk = false;
		} else if (donnesChargeesDistanceExposition && !premierAffichageOk
				&& choixConfExpo.getValue().equals("Exposition")) {
			textAreaConsultation.setText(ControleurImporterDistance.getStrExpositions().toString());
			donnees = ControleurImporterDistance.getDonnees();
			premierAffichageOk = true;
			listeFiltreOk = false;
		} else if (donnesChargeesSauvegarderConferencier && !premierAffichageOk
				&& choixConfExpo.getValue().equals("Conferencier")) {
			textAreaConsultation.setText(ControleurPageDeGarde.getStrConferencier().toString());
			donnees = ControleurPageDeGarde.getDonnees();
			premierAffichageOk = true;
			listeFiltreOk = false;
		} else if (donnesChargeesSauvegarderExposition && !premierAffichageOk
				&& choixConfExpo.getValue().equals("Exposition")) {
			textAreaConsultation.setText(ControleurPageDeGarde.getStrExpositions().toString());
			donnees = ControleurPageDeGarde.getDonnees();
			premierAffichageOk = true;
			listeFiltreOk = false;
		}
		contenuFichier = textAreaConsultation.getText();
		confHeureDebut.setItems(FXCollections.observableArrayList(
				"8h00", "8h30", "9h00",
				"9h30", "10h00", "10h30",
				"11h00", "11h30", "12h00",
				"12h30", "13h30", "13h30",
				"14h00", "14h30", "15h00",
				"15h30","16h00", "16h30", "17h00"
		));

		confHeureFin.setItems(FXCollections.observableArrayList(
				"8h00", "8h30", "9h00",
				"9h30", "10h00", "10h30",
				"11h00", "11h30", "12h00",
				"12h30", "13h30", "13h30",
				"14h00", "14h30", "15h00",
				"15h30","16h00", "16h30", "17h00"
		));

		expoHeureDebut.setItems(FXCollections.observableArrayList(
				"8h00", "8h30", "9h00",
				"9h30", "10h00", "10h30",
				"11h00", "11h30", "12h00",
				"12h30", "13h30", "13h30",
				"14h00", "14h30", "15h00",
				"15h30","16h00", "16h30", "17h00"
		));

		expoHeureFin.setItems(FXCollections.observableArrayList(
				"8h00", "8h30", "9h00",
				"9h30", "10h00", "10h30",
				"11h00", "11h30", "12h00",
				"12h30", "13h30", "13h30",
				"14h00", "14h30", "15h00",
				"15h30","16h00", "16h30", "17h00"
		));
		
		if (premierAffichageOk && !listeFiltreOk) {
			stats = new Statistiques();
		}
	}

	@FXML 
	void appliquerFiltreStat() {
		if (choixConfExpo.getValue().equals("Conferencier")) {
			afficherStatConf();
			listeDesFiltres.add("Statistiques sur les conferenciers");
		} else if (choixConfExpo.getValue().equals("Exposition")) {
			afficherStatExpo();
			listeDesFiltres.add("Statistiques sur les expositions");
		}
	}

	private void afficherStatConf() {
		listeDesFiltres.clear();
		// si filtres null c'est-à-dire que l'utilisateur n'a pas cliqué que la fenetre
		// une fentre lui dit quil faut cliquer sur le fenetre avant de filter les données
		if (stats == null) {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Avertissement");
			alert.setHeaderText(null);
			alert.setContentText("Veuillez cliquer sur la zone de texte pour afficher les données avant de filtrer.");
			alert.showAndWait();
			return;
		}
		stats.reset();

		DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

		String strAnalyser;
		String strAnalyserBis;
		Date dateDebut;
		Date dateFin;
		LocalDate dateIntervalleDebut;
		LocalDate dateIntervalleFin;

		if (confDateDebut.getValue() != null 
				&& confDateFin.getValue() != null) {
			try {
				dateIntervalleDebut = confDateDebut.getValue();
				dateIntervalleFin = confDateFin.getValue();

				strAnalyser = dateIntervalleDebut.format(dateTimeFormat);
				strAnalyserBis = dateIntervalleFin.format(dateTimeFormat);

				dateIntervalleDebut = LocalDate.parse(strAnalyser, dateTimeFormat);
				dateIntervalleFin = LocalDate.parse(strAnalyserBis, dateTimeFormat);

				dateDebut = format.parse(dateIntervalleDebut.format(dateTimeFormat).toString());
				dateFin = format.parse(dateIntervalleFin.format(dateTimeFormat).toString());

				stats.visitePeriode(dateDebut, dateFin);
				listeDesFiltres.add("Date de début : " + strAnalyser);
				listeDesFiltres.add("Date de fin : " + strAnalyserBis);
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e){
				// boite d'alerte pour avertir l'utilisateur
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.setTitle("Avertissement");
				alert.setHeaderText(null);
				alert.setContentText(e.getMessage());
				alert.showAndWait();
				reinitialiserFiltre();
			}
		}

		if (confHeureDebut.getValue() != null 
				&& confHeureFin.getValue() != null) {
			strAnalyser = confHeureDebut.getValue();
			strAnalyserBis = confHeureFin.getValue();
			try {
				stats.visitePlageHoraire(strAnalyser, strAnalyserBis);
				listeDesFiltres.add("Heure de début : " + strAnalyser);
				listeDesFiltres.add("Heure de fin : " + strAnalyserBis);
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e){
				// boite d'alerte pour avertir l'utilisateur
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.setTitle("Avertissement");
				alert.setHeaderText(null);
				alert.setContentText(e.getMessage());
				alert.showAndWait();
				reinitialiserFiltre();
			}
		}

		if (confInterne.isSelected()) {
			stats.conferencierInterne();
		} else if (confExterne.isSelected()) {
			stats.conferencierExterne();
		}

		textAreaConsultation.setText("\t\t\t\t\t\t\t\t\tRésultat pour votre recherche.\n\n\n" 
				+ stats.afficherPVisitesConferencier());
		contenuFichier = textAreaConsultation.getText();
	}

	private void afficherStatExpo() {
		listeDesFiltres.clear();
		// si filtres null c'est-à-dire que l'utilisateur n'a pas cliqué que la fenetre
		// une fentre lui dit quil faut cliquer sur le fenetre avant de filter les données
		if (stats == null) {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Avertissement");
			alert.setHeaderText(null);
			alert.setContentText("Veuillez cliquer sur la zone de texte pour afficher les données avant de filtrer.");
			alert.showAndWait();
			return;
		}

		stats.reset();

		DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

		String strAnalyser;
		String strAnalyserBis;
		Date dateDebut;
		Date dateFin;
		LocalDate dateIntervalleDebut;
		LocalDate dateIntervalleFin;

		if (expoDateDebut.getValue() != null 
				&& expoDateFin.getValue() != null) {
			try {
				dateIntervalleDebut = expoDateDebut.getValue();
				dateIntervalleFin = expoDateFin.getValue();

				strAnalyser = dateIntervalleDebut.format(dateTimeFormat);
				strAnalyserBis = dateIntervalleFin.format(dateTimeFormat);

				dateIntervalleDebut = LocalDate.parse(strAnalyser, dateTimeFormat);
				dateIntervalleFin = LocalDate.parse(strAnalyserBis, dateTimeFormat);

				dateDebut = format.parse(dateIntervalleDebut.format(dateTimeFormat).toString());
				dateFin = format.parse(dateIntervalleFin.format(dateTimeFormat).toString());

				stats.visitePeriode(dateDebut, dateFin);
				listeDesFiltres.add("Date de début : " + strAnalyser);
				listeDesFiltres.add("Date de fin : " + strAnalyserBis);
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e){
				// boite d'alerte pour avertir l'utilisateur
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.setTitle("Avertissement");
				alert.setHeaderText(null);
				alert.setContentText(e.getMessage());
				alert.showAndWait();
				reinitialiserFiltre();
			}
		}

		if (expoHeureDebut.getValue() != null 
				&& expoHeureFin.getValue() != null) {
			strAnalyser = expoHeureDebut.getValue();
			strAnalyserBis = expoHeureFin.getValue();
			try {
				stats.visitePlageHoraire(strAnalyser, strAnalyserBis);
				listeDesFiltres.add("Heure de début : " + strAnalyser);
				listeDesFiltres.add("Heure de fin : " + strAnalyserBis);
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e){
				// boite d'alerte pour avertir l'utilisateur
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.setTitle("Avertissement");
				alert.setHeaderText(null);
				alert.setContentText(e.getMessage());
				alert.showAndWait();
				reinitialiserFiltre();
			}
		}

		if (expoPerm.isSelected()) {
			stats.expositionPermanente();
			listeDesFiltres.add("Exposition permanente");
		} else if (expoTemp.isSelected()) {
			stats.expositionTemporaire();
			listeDesFiltres.add("Exposition temporaire");
		}

		textAreaConsultation.setText("\t\t\t\t\t\t\t\t\tRésultat pour votre recherche.\n\n\n" 
				+ stats.affichagePVisitesExposition());
		contenuFichier = textAreaConsultation.getText();

	}

	private void toggleFiltrePanel() {
		// Basculer la visibilité du panneau de filtres
		boolean isVisible = scrollPaneFiltres.isVisible();
		scrollPaneFiltres.setVisible(!isVisible);
		btnLancerStat.setVisible(!isVisible);
		btnReinitialiserFiltre.setVisible(!isVisible);
	}

	private void toggleFiltreExpoConf() {
		Insets insetPaddingConf = new Insets(0, 0, 0, 0);
		Insets insetPaddingExpo = new Insets(-415, 0, 0, 0);
		if (donneesChargeesLocalConferencier
				&& choixConfExpo.getValue().equals("Conferencier")) {
			textAreaConsultation.setText(ControleurImporterLocal.getStrConferencier().toString());
			donnees = ControleurImporterLocal.getDonnees();
			premierAffichageOk = true;
		} else if (donneesChargeesLocalExposition
				&& choixConfExpo.getValue().equals("Exposition")) {
			textAreaConsultation.setText(ControleurImporterLocal.getStrExpositions().toString());
			donnees = ControleurImporterLocal.getDonnees();
			premierAffichageOk = true;
		} else if (donnesChargeesDistanceConferencier
				&& choixConfExpo.getValue().equals("Conferencier")) {
			textAreaConsultation.setText(ControleurImporterDistance.getStrConferencier().toString());
			donnees = ControleurImporterDistance.getDonnees();
			premierAffichageOk = true;
		} else if (donnesChargeesDistanceExposition
				&& choixConfExpo.getValue().equals("Exposition")) {
			textAreaConsultation.setText(ControleurImporterDistance.getStrExpositions().toString());
			donnees = ControleurImporterDistance.getDonnees();
			premierAffichageOk = true;
		} else if (donnesChargeesSauvegarderConferencier
				&& choixConfExpo.getValue().equals("Conferencier")) {
			textAreaConsultation.setText(ControleurPageDeGarde.getStrConferencier().toString());
			donnees = ControleurPageDeGarde.getDonnees();
			premierAffichageOk = true;
		} else if (donnesChargeesSauvegarderExposition
				&& choixConfExpo.getValue().equals("Exposition")) {
			textAreaConsultation.setText(ControleurPageDeGarde.getStrExpositions().toString());
			donnees = ControleurPageDeGarde.getDonnees();
			premierAffichageOk = true;
		}
		
		// Basculer la visibilité des filtres
		if (choixConfExpo.getValue().equals("Conferencier")) {
			confDateDebut.setVisible(true);
			confDateFin.setVisible(true);
			confHeureDebut.setVisible(true);
			confHeureFin.setVisible(true);
			confExterne.setVisible(true);
			confInterne.setVisible(true);
			confLabelDate.setVisible(true);
			confLabelHeureDebut.setVisible(true);
			confLabelHeureFin.setVisible(true);
			confLabelEtat.setVisible(true);

			expoDateDebut.setVisible(false);
			expoDateFin.setVisible(false);
			expoHeureDebut.setVisible(false);
			expoHeureFin.setVisible(false);
			expoPerm.setVisible(false);
			expoTemp.setVisible(false);
			expoLabelDates.setVisible(false);
			expoLabelHeureDebut.setVisible(false);
			expoLabelHeureFin.setVisible(false);
			expoLabelEtat.setVisible(false);
			panneauFiltres.setPadding(insetPaddingConf);
		} else if (choixConfExpo.getValue().equals("Exposition")) {
			confDateDebut.setVisible(false);
			confDateFin.setVisible(false);
			confHeureDebut.setVisible(false);
			confHeureFin.setVisible(false);
			confExterne.setVisible(false);
			confInterne.setVisible(false);
			confLabelDate.setVisible(false);
			confLabelHeureDebut.setVisible(false);
			confLabelHeureFin.setVisible(false);
			confLabelEtat.setVisible(false);

			expoDateDebut.setVisible(true);
			expoDateFin.setVisible(true);
			expoHeureDebut.setVisible(true);
			expoHeureFin.setVisible(true);
			expoPerm.setVisible(true);
			expoTemp.setVisible(true);
			expoLabelDates.setVisible(true);
			expoLabelHeureDebut.setVisible(true);
			expoLabelHeureFin.setVisible(true);
			expoLabelEtat.setVisible(true);
			panneauFiltres.setPadding(insetPaddingExpo);
		}
		
		if (premierAffichageOk) {
			stats = new Statistiques();
		}
		contenuFichier = textAreaConsultation.getText();
	}

	@FXML
	void reinitialiserFiltre() {
		listeDesFiltres.clear();
		if (donneesChargeesLocalConferencier
				&& choixConfExpo.getValue().equals("Conferencier")) {
			textAreaConsultation.setText(ControleurImporterLocal.getStrConferencier().toString());
			donnees = ControleurImporterLocal.getDonnees();
		} else if (donneesChargeesLocalExposition 
				&& choixConfExpo.getValue().equals("Exposition")) {
			textAreaConsultation.setText(ControleurImporterLocal.getStrExpositions().toString());
			donnees = ControleurImporterLocal.getDonnees();
		} else if (donnesChargeesDistanceConferencier
				&& choixConfExpo.getValue().equals("Conferencier")) {
			textAreaConsultation.setText(ControleurImporterDistance.getStrConferencier().toString());
			donnees = ControleurImporterDistance.getDonnees();
		} else if (donnesChargeesDistanceExposition
				&& choixConfExpo.getValue().equals("Exposition")) {
			textAreaConsultation.setText(ControleurImporterDistance.getStrExpositions().toString());
			donnees = ControleurImporterDistance.getDonnees();
		} else if (donnesChargeesSauvegarderConferencier 
				&& choixConfExpo.getValue().equals("Conferencier")) {
			textAreaConsultation.setText(ControleurPageDeGarde.getStrConferencier().toString());
			donnees = ControleurPageDeGarde.getDonnees();
		} else if (donnesChargeesSauvegarderExposition
				&& choixConfExpo.getValue().equals("Exposition")) {
			textAreaConsultation.setText(ControleurPageDeGarde.getStrExpositions().toString());
			donnees = ControleurPageDeGarde.getDonnees();
		}
		contenuFichier = textAreaConsultation.getText();
		confDateDebut.getEditor().clear();
		confDateDebut.setValue(null);

		confDateFin.getEditor().clear();
		confDateFin.setValue(null);

		expoDateDebut.getEditor().clear();
		expoDateDebut.setValue(null);

		expoDateFin.getEditor().clear();
		expoDateFin.setValue(null);

		confInterne.setSelected(false);
		confExterne.setSelected(false);

		expoPerm.setSelected(false);
		expoTemp.setSelected(false);

		confHeureDebut.setItems(FXCollections.observableArrayList(
				"8h00", "8h30", "9h00",
				"9h30", "10h00", "10h30",
				"11h00", "11h30", "12h00",
				"12h30", "13h30", "13h30",
				"14h00", "14h30", "15h00",
				"15h30","16h00", "16h30", "17h00"
		));

		confHeureFin.setItems(FXCollections.observableArrayList(
				"8h00", "8h30", "9h00",
				"9h30", "10h00", "10h30",
				"11h00", "11h30", "12h00",
				"12h30", "13h30", "13h30",
				"14h00", "14h30", "15h00",
				"15h30","16h00", "16h30", "17h00"
		));

		expoHeureDebut.setItems(FXCollections.observableArrayList(
				"8h00", "8h30", "9h00",
				"9h30", "10h00", "10h30",
				"11h00", "11h30", "12h00",
				"12h30", "13h30", "13h30",
				"14h00", "14h30", "15h00",
				"15h30","16h00", "16h30", "17h00"
		));

		expoHeureFin.setItems(FXCollections.observableArrayList(
				"8h00", "8h30", "9h00",
				"9h30", "10h00", "10h30",
				"11h00", "11h30", "12h00",
				"12h30", "13h30", "13h30",
				"14h00", "14h30", "15h00",
				"15h30","16h00", "16h30", "17h00"
		));
	}

	@FXML
	void consulter(ActionEvent event) {
		if (premierAffichageOk) {
			stats.reset();
			reinitialiserFiltre();
		}
		
		Main.setPageConsulter();
	}
	
	@FXML
    void statistiques(ActionEvent event) {
		if (premierAffichageOk) {
			stats.reset();
			reinitialiserFiltre();
		}
		
    	Main.setPageConsulterStatistiques();
    }

	@FXML
	void exporter(ActionEvent event) {
		if (premierAffichageOk) {
			stats.reset();
			reinitialiserFiltre();
		}
		
		Main.setPageExporter();

	}

	@FXML
	void importer(ActionEvent event) {
		if (premierAffichageOk) {
			stats.reset();
			reinitialiserFiltre();
		}
		
		Main.setPageImporter();
	}

	@FXML
	void notice(ActionEvent event) {

	}

	@FXML
    void quitter(ActionEvent event) {
    	Main.quitterApllication();
    }

	@FXML
	void revenirEnArriere(ActionEvent event) {
		if (premierAffichageOk) {
			stats.reset();
		}
		
		Main.setPageDeGarde();
	}
	
	@FXML
	void sauvegarder(ActionEvent event) {
		Main.sauvegarder();
	}

	@FXML
	void genererPdf(ActionEvent event) {
		// Si le contenu afficher est null on affiche une boite d'alerte
		// pour informer l'utilisateur qu'il ne peut pas générer de fichier pdf
		if (contenuFichier == null) {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Avertissement");
			alert.setHeaderText(null);
			alert.setContentText("Aucun résultat n'est disponible. Impossible de générer le PDF.");
			alert.showAndWait();
			return;
		}
		if (contenuFichier.isEmpty()) {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Avertissement");
			alert.setHeaderText(null);
			alert.setContentText("Aucun résultat n'est disponible. Impossible de générer le PDF.");
			alert.showAndWait();
			return;
		}
		titre = "Statistiques";
		System.out.println("tire du pdf " + titre );
		System.out.println("conttenu du fichier" + contenuFichier);
		// Si aucun filtre n'a été appliqué, on ajoute dans la liste des filtres "Aucun filtre appliqué"
		if (listeDesFiltres.isEmpty()) {
			listeDesFiltres.add("Aucun filtre appliqué");
		}

		FichierPdf fichierPdf = new FichierPdf(titre, listeDesFiltres, contenuFichier);
		fichierPdf.genererPdf();
		System.out.println("Fichier pdf généré avec succès !");
		afficherPopUp("Fichier pdf généré avec succès !", event);
		listeDesFiltres.clear();
	}

	private void afficherPopUp(String message, Event event){
		// Fenetre popup pour informer l'utilisateur que l'action a été effectuée
		Stage popupStage = new Stage();
		popupStage.initOwner(((Node) event.getSource()).getScene().getWindow());
		popupStage.setX(((Node) event.getSource()).getScene().getWindow().getX() + 10);
		popupStage.setY(((Node) event.getSource()).getScene().getWindow().getY() + 10);
		popupStage.setResizable(false);
		Label messageLabel = new Label(message);
		messageLabel.setStyle("-fx-font-size: 16px; -fx-padding: 10px;");

		// Recupérer la fenêtre parente
		Stage parentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		double parentWidth = parentStage.getWidth();
		double parentHeight = parentStage.getHeight();

		double centerX = parentStage.getX() + 400;
		double centerY= parentStage.getY() + 20;
		// Set la position de la fenêtre popup
		popupStage.setX(centerX);
		popupStage.setY(centerY);

		// Créer une scène
		Scene scene = new Scene(new StackPane(messageLabel));
		popupStage.setScene(scene);
		popupStage.show();

		// Close the pop-up after 2 seconds
		PauseTransition delay = new PauseTransition(Duration.seconds(2));
		delay.setOnFinished(e -> popupStage.close());
		delay.play();
	}
		//TODO Regeler le probleme de generation de pdf
}
