package application;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import gestion_donnees.Conferencier;
import gestion_donnees.DonneesApplication;
import gestion_donnees.Statistiques;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

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

	@FXML
	void initialize() {
		textAreaConsultation.setEditable(false);
		textAreaConsultation.setText("\n\n\n\n\n\n\n\n\n\n\n\t\t\t\t\t\t\t\t\t\t\tCliquez ici pour afficher les données.");

		premierAffichageOk = false;
		// Déclenchement de l'événement au clic sur la TextArea
		textAreaConsultation.setOnMouseClicked(event -> afficherDonnees());

		choixConfExpo.setOnAction(event -> toggleFiltreExpoConf());
		choixConfExpo.setItems(FXCollections.observableArrayList("Conferencier", "Exposition"));
		choixConfExpo.setValue("Choisir :");

		confDateDebut.setVisible(false);
		confDateFin.setVisible(false);
		confHeureDebut.setVisible(false);
		confHeureDebut.setVisible(false);
		confExterne.setVisible(false);
		confInterne.setVisible(false);
		confLabelDate.setVisible(false);
		confLabelHeureDebut.setVisible(false);
		confLabelHeureFin.setVisible(false);
		confLabelEtat.setVisible(false);

		expoDateDebut.setVisible(false);
		expoDateFin.setVisible(false);
		expoHeureDebut.setVisible(false);
		expoHeureDebut.setVisible(false);
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
		} else if (choixConfExpo.getValue().equals("Exposition")) {
			afficherStatExpo();
		}
	}

	private void afficherStatConf() {
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
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		if (confHeureDebut.getValue() != null 
				&& confHeureFin.getValue() != null) {
			strAnalyser = confHeureDebut.getValue();
			strAnalyserBis = confHeureFin.getValue();
			try {
				stats.visitePlageHoraire(strAnalyser, strAnalyserBis);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		if (confInterne.isSelected()) {
			stats.conferencierInterne();
		} else if (confExterne.isSelected()) {
			stats.conferencierExterne();
		}

		textAreaConsultation.setText("\t\t\t\t\t\t\t\t\tRésultat pour votre recherche.\n\n\n" 
				+ stats.afficherPVisitesConferencier());
	}

	private void afficherStatExpo() {
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
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		if (expoHeureDebut.getValue() != null 
				&& expoHeureFin.getValue() != null) {
			strAnalyser = expoHeureDebut.getValue();
			strAnalyserBis = expoHeureFin.getValue();
			try {
				stats.visitePlageHoraire(strAnalyser, strAnalyserBis);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		if (expoPerm.isSelected()) {
			stats.expositionPermanente();
		} else if (expoTemp.isSelected()) {
			stats.expositionTemporaire();
		}

		textAreaConsultation.setText("\t\t\t\t\t\t\t\t\tRésultat pour votre recherche.\n\n\n" 
				+ stats.affichagePVisitesExposition());
	}

	private void toggleFiltrePanel() {
		// Basculer la visibilité du panneau de filtres
		boolean isVisible = scrollPaneFiltres.isVisible();
		scrollPaneFiltres.setVisible(!isVisible);
		btnLancerStat.setVisible(!isVisible);
		btnReinitialiserFiltre.setVisible(!isVisible);
	}

	private void toggleFiltreExpoConf() {
		if (donneesChargeesLocalConferencier && !premierAffichageOk 
				&& choixConfExpo.getValue().equals("Conferencier")) {
			textAreaConsultation.setText(ControleurImporterLocal.getStrConferencier().toString());
			donnees = ControleurImporterLocal.getDonnees();
			premierAffichageOk = true;
		} else if (donneesChargeesLocalExposition && !premierAffichageOk 
				&& choixConfExpo.getValue().equals("Exposition")) {
			textAreaConsultation.setText(ControleurImporterLocal.getStrExpositions().toString());
			donnees = ControleurImporterLocal.getDonnees();
			premierAffichageOk = true;
		} else if (donnesChargeesDistanceConferencier && !premierAffichageOk
				&& choixConfExpo.getValue().equals("Conferencier")) {
			textAreaConsultation.setText(ControleurImporterDistance.getStrConferencier().toString());
			donnees = ControleurImporterDistance.getDonnees();
			premierAffichageOk = true;
		} else if (donnesChargeesDistanceExposition && !premierAffichageOk
				&& choixConfExpo.getValue().equals("Exposition")) {
			textAreaConsultation.setText(ControleurImporterDistance.getStrExpositions().toString());
			donnees = ControleurImporterDistance.getDonnees();
			premierAffichageOk = true;
		} else if (donnesChargeesSauvegarderConferencier && !premierAffichageOk
				&& choixConfExpo.getValue().equals("Conferencier")) {
			textAreaConsultation.setText(ControleurPageDeGarde.getStrConferencier().toString());
			donnees = ControleurPageDeGarde.getDonnees();
			premierAffichageOk = true;
		} else if (donnesChargeesSauvegarderExposition && !premierAffichageOk
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
		}
		
		if (premierAffichageOk) {
			stats = new Statistiques();
		}
	}

	@FXML
	void reinitialiserFiltre() {
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

		confDateDebut.getEditor().clear();
		confDateDebut.setValue(null);

		confDateFin.getEditor().clear();
		confDateFin.setValue(null);

		expoDateDebut.getEditor().clear();
		expoDateDebut.setValue(null);

		expoDateDebut.getEditor().clear();
		expoDateDebut.setValue(null);

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
		}
		
		Main.setPageConsulter();
	}
	
	@FXML
    void statistiques(ActionEvent event) {
		if (premierAffichageOk) {
			stats.reset();
		}
		
    	Main.setPageConsulterStatistiques();
    }

	@FXML
	void exporter(ActionEvent event) {
		if (premierAffichageOk) {
			stats.reset();
		}
		
		Main.setPageExporter();

	}

	@FXML
	void importer(ActionEvent event) {
		if (premierAffichageOk) {
			stats.reset();
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

}
