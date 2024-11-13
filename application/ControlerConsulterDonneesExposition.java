package application;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import gestion_donnees.DonneesApplication;
import gestion_donnees.EmployeException;
import gestion_donnees.Exposition;
import gestion_donnees.ExpositionException;
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

	private DonneesApplication donnees = new DonneesApplication();

	private Filtre filtres = new Filtre();

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
	private TextArea textAreaConsultation;
	
	@FXML
    private Button btnSauvegarder;

	private boolean donneesChargeesLocal; // Pour vérifier si les données sont déjà chargées en local

	private boolean donnesChargeesDistance; // Pour vérifier si les données sont déjà chargées a distance
	
	private boolean donnesChargeesSauvegarder;

	@FXML
	void initialize() {
		textAreaConsultation.setEditable(false);
		textAreaConsultation.setText("\n\n\n\n\n\n\n\n\n\n\n\t\t\t\t\t\t\t\t\t\t\tCliquez ici pour afficher les données.");

		// Déclenchement de l'événement au clic sur la TextArea
		textAreaConsultation.setOnMouseClicked(event -> afficherDonnees());

		// Par défaut, cacher le panneau de filtres
		scrollPaneFiltres.setVisible(false);

		btnFiltre.setOnAction(event -> toggleFiltrePanel());
		btnLancerFiltre.setOnAction(event -> appliquerFiltre());

	}
	// Méthode pour charger et afficher les données
	private void afficherDonnees() {
		donneesChargeesLocal = ControleurImporterLocal.isDonneesExpositionsChargees();
		donnesChargeesDistance = ControleurImporterDistance.isDonneesExpositionsChargees();
		donnesChargeesSauvegarder = ControleurPadeDeGarde.isDonneesSaveChargees();
		StringBuilder strExpositionsLocal = ControleurImporterLocal.getStrExpositions();
		StringBuilder strExpositionsDistance = ControleurImporterDistance.getStrExpositions();
		StringBuilder strExpositonsSave = ControleurPadeDeGarde.getStrExpositions();
		System.out.print("Donnes Charge distance" + donnesChargeesDistance);
		if (donneesChargeesLocal) {
			textAreaConsultation.setText(ControleurImporterLocal.getStrExpositions().toString());
		} else if (donnesChargeesDistance) {
			textAreaConsultation.setText(ControleurImporterDistance.getStrExpositions().toString());
		} else if (donnesChargeesSauvegarder) {
       	 textAreaConsultation.setText(ControleurPadeDeGarde.getStrExpositions().toString());
       }
		if ((!donneesChargeesLocal || strExpositionsLocal == null) && (!donnesChargeesDistance || strExpositionsDistance == null)
				&& (!donnesChargeesSauvegarder || strExpositonsSave == null ))  { // Vérifie si les données n'ont pas déjà été chargées en local et a distance
			textAreaConsultation.setText("Les données ne sont pas encore disponibles.");
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
	}

	private void toggleFiltrePanel() {
		// Basculer la visibilité du panneau de filtres
		boolean isVisible = scrollPaneFiltres.isVisible();
		scrollPaneFiltres.setVisible(!isVisible);

		// TODO reset les filtres
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

		if (expoDateDebut.getValue() != null && expoDateFin.getValue() != null) {
		    try {
		        // Récupérer les dates sous forme de LocalDate
		        dateIntervalleDebut = expoDateDebut.getValue();
		        dateIntervalleFin = expoDateFin.getValue();
		        
		        // Si vous avez besoin de les formater en chaîne (par exemple, pour affichage ou autre traitement)
		        strAnalyser = dateIntervalleDebut.format(dateTimeFormat);
		        strAnalyserBis = dateIntervalleFin.format(dateTimeFormat);

		        // Conversion des LocalDate en Date, sans étapes superflues
		        // Utiliser les convertisseurs appropriés si vous en avez besoin, sinon utilisez directement les LocalDate
		        dateDebut = Date.from(dateIntervalleDebut.atStartOfDay(ZoneId.systemDefault()).toInstant());
		        dateFin = Date.from(dateIntervalleFin.atStartOfDay(ZoneId.systemDefault()).toInstant());
		        
		        // Appliquez les filtres
		        filtres.expoVisitePeriode(dateDebut, dateFin);
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		}

		if (heureDebut.getValue() != null && heureFin.getValue() != null) {
			strAnalyser = heureDebut.getValue();
			strAnalyserBis = heureFin.getValue();
			try {
				filtres.expoVisiteHoraire(strAnalyser, strAnalyserBis);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		if (!filtres.getListeExposition().isEmpty()) {
			for (Exposition exposition : filtres.getListeExposition()) {
				aAfficher += exposition + "\n\n";
			}
			textAreaConsultation.setText(aAfficher);
		} else {
			textAreaConsultation.setText("Aucun résultat à votre recherche.");
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
	
	@FXML
	void sauvegarder(ActionEvent event) {
		Main.sauvegarder();
	}

}
