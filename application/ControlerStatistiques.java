package application;


import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import gestion_donnees.Conferencier;
import gestion_donnees.DonneesApplication;
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

	@FXML
	private Button btnConsulter;

	@FXML
	private Button btnExporter;

	@FXML
	private Button btnFiltre;

	@FXML
	private Button btnImporter;

	@FXML
	private Button btnLancerFiltre;

	@FXML
	private Button btnNotice;

	@FXML
	private Button btnQuitter1;

	@FXML
	private Button btnReinitialiserFiltre;

	@FXML
	private Button btnRevenir;

	@FXML
	private ChoiceBox<String> choixConfExpo;

	@FXML
	private DatePicker confDateDebut;

	@FXML
	private DatePicker confDateFin;

	@FXML
	private RadioButton confExterne;

	@FXML
	private ChoiceBox<?> confHeureDebut;

	@FXML
	private ChoiceBox<?> confHeureFin;

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
	private ChoiceBox<?> expoHeureDebut;

	@FXML
	private ChoiceBox<?> expoHeureFin;

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
		btnLancerFiltre.setVisible(false);
		btnReinitialiserFiltre.setVisible(false);

		categorieToggleGroup = new ToggleGroup();
		confInterne.setToggleGroup(categorieToggleGroup);
		confExterne.setToggleGroup(categorieToggleGroup);

		categorieToggleGroup = new ToggleGroup();
		expoPerm.setToggleGroup(categorieToggleGroup);
		expoTemp.setToggleGroup(categorieToggleGroup);

		btnFiltre.setOnAction(event -> toggleFiltrePanel());
		btnLancerFiltre.setOnAction(event -> appliquerFiltre());
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
				&& choixConfExpo.getValue() == "Conferencier") {
			textAreaConsultation.setText(ControleurImporterLocal.getStrConferencier().toString());
			donnees = ControleurImporterLocal.getDonnees();
			premierAffichageOk = true;
			listeFiltreOk = false;
		} else if (donneesChargeesLocalExposition && !premierAffichageOk 
				&& choixConfExpo.getValue() == "Exposition") {
			textAreaConsultation.setText(ControleurImporterLocal.getStrExpositions().toString());
			donnees = ControleurImporterLocal.getDonnees();
			premierAffichageOk = true;
			listeFiltreOk = false;
		} else if (donnesChargeesDistanceConferencier && !premierAffichageOk
				&& choixConfExpo.getValue() == "Conferencier") {
			textAreaConsultation.setText(ControleurImporterDistance.getStrConferencier().toString());
			donnees = ControleurImporterDistance.getDonnees();
			premierAffichageOk = true;
			listeFiltreOk = false;
		} else if (donnesChargeesDistanceExposition && !premierAffichageOk
				&& choixConfExpo.getValue() == "Exposition") {
			textAreaConsultation.setText(ControleurImporterDistance.getStrExpositions().toString());
			donnees = ControleurImporterDistance.getDonnees();
			premierAffichageOk = true;
			listeFiltreOk = false;
		} else if (donnesChargeesSauvegarderConferencier && !premierAffichageOk
				&& choixConfExpo.getValue() == "Conferencier") {
			textAreaConsultation.setText(ControleurPageDeGarde.getStrConferencier().toString());
			donnees = ControleurPageDeGarde.getDonnees();
			premierAffichageOk = true;
			listeFiltreOk = false;
		} else if (donnesChargeesSauvegarderExposition && !premierAffichageOk
				&& choixConfExpo.getValue() == "Exposition") {
			textAreaConsultation.setText(ControleurPageDeGarde.getStrExpositions().toString());
			donnees = ControleurPageDeGarde.getDonnees();
			premierAffichageOk = true;
			listeFiltreOk = false;
		}

		if (premierAffichageOk) {
			choixConfExpo.setItems(FXCollections.observableArrayList("Conferencier", "Exposition"));
		}
	}

	private void toggleFiltrePanel() {
		// Basculer la visibilité du panneau de filtres
		boolean isVisible = scrollPaneFiltres.isVisible();
		scrollPaneFiltres.setVisible(!isVisible);
		btnLancerFiltre.setVisible(!isVisible);
		btnReinitialiserFiltre.setVisible(!isVisible);
	}

	private void toggleFiltreExpoConf() {
		// Basculer la visibilité des filtres
		if (choixConfExpo.getValue() == "Conferencier") {
			confDateDebut.setVisible(true);
			confDateFin.setVisible(true);
			confHeureDebut.setVisible(true);
			confHeureDebut.setVisible(true);
			confExterne.setVisible(true);
			confInterne.setVisible(true);
			confLabelDate.setVisible(true);
			confLabelHeureDebut.setVisible(true);
			confLabelHeureFin.setVisible(true);
			confLabelEtat.setVisible(true);

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
		} else if (choixConfExpo.getValue() == "Exposition") {
			confDateDebut.setVisible(true);
			confDateFin.setVisible(true);
			confHeureDebut.setVisible(true);
			confHeureDebut.setVisible(true);
			confExterne.setVisible(true);
			confInterne.setVisible(true);
			confLabelDate.setVisible(true);
			confLabelHeureDebut.setVisible(true);
			confLabelHeureFin.setVisible(true);
			confLabelEtat.setVisible(true);

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
		}
	}

	@FXML
	void reinitialiserFiltre() {
		if (donneesChargeesLocalConferencier && !premierAffichageOk 
				&& choixConfExpo.getValue() == "Conferencier") {
			textAreaConsultation.setText(ControleurImporterLocal.getStrConferencier().toString());
			donnees = ControleurImporterLocal.getDonnees();
		} else if (donneesChargeesLocalExposition && !premierAffichageOk 
				&& choixConfExpo.getValue() == "Exposition") {
			textAreaConsultation.setText(ControleurImporterLocal.getStrExpositions().toString());
			donnees = ControleurImporterLocal.getDonnees();
		} else if (donnesChargeesDistanceConferencier && !premierAffichageOk
				&& choixConfExpo.getValue() == "Conferencier") {
			textAreaConsultation.setText(ControleurImporterDistance.getStrConferencier().toString());
			donnees = ControleurImporterDistance.getDonnees();
		} else if (donnesChargeesDistanceExposition && !premierAffichageOk
				&& choixConfExpo.getValue() == "Exposition") {
			textAreaConsultation.setText(ControleurImporterDistance.getStrExpositions().toString());
			donnees = ControleurImporterDistance.getDonnees();
		} else if (donnesChargeesSauvegarderConferencier && !premierAffichageOk
				&& choixConfExpo.getValue() == "Conferencier") {
			textAreaConsultation.setText(ControleurPageDeGarde.getStrConferencier().toString());
			donnees = ControleurPageDeGarde.getDonnees();
		} else if (donnesChargeesSauvegarderExposition && !premierAffichageOk
				&& choixConfExpo.getValue() == "Exposition") {
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
	}

	@FXML 
	void appliquerFiltre() {

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
