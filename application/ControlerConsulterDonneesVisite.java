package application;

import java.sql.SQLOutput;
import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import gestion_donnees.*;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ControlerConsulterDonneesVisite {

	private DonneesApplication donnees;

	private Filtre filtres;

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
    public Button statistiques;
	
	@FXML
	private Button genererPdf;

	@FXML
	private TextArea textAreaConsultation;

	private boolean donneesChargeesLocal; // Pour vérifier si les données sont déjà chargées en local

	private boolean donnesChargeesDistance; // Pour vérifier si les données sont déjà chargées a distance

	private boolean donnesChargeesSauvegarder;

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

	private StringBuilder strVisitesSave;

	@FXML
	private Button btnSauvegarder;

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
		donnesChargeesSauvegarder = ControleurPageDeGarde.isDonneesSaveChargees();

		strVisitesLocal = ControleurImporterLocal.getStrVisites();
		strVisitesDistance = ControleurImporterDistance.getStrVisites();
		strVisitesSave = ControleurPageDeGarde.getStrVisites();

		listeFiltreOk = true;

		if ((!donneesChargeesLocal || strVisitesLocal == null) && (!donnesChargeesDistance || strVisitesDistance == null)
				&& (!donnesChargeesSauvegarder || strVisitesSave == null ))  { // Vérifie si les données n'ont pas déjà été chargées en local et a distance
			textAreaConsultation.setText("Les données ne sont pas encore disponibles.");
		}

		if (donneesChargeesLocal && !premierAffichageOk) {
			donnees = ControleurImporterLocal.getDonnees();
			textAreaConsultation.setText(ControleurImporterLocal.getStrVisites().toString());
			premierAffichageOk = true;
			listeFiltreOk = false;
			contenuFichier = textAreaConsultation.getText();
		} else if (donnesChargeesDistance && !premierAffichageOk) {
			textAreaConsultation.setText(ControleurImporterDistance.getStrVisites().toString());
			donnees = ControleurImporterDistance.getDonnees();
			premierAffichageOk = true;
			listeFiltreOk = false;
			contenuFichier = textAreaConsultation.getText();
		} else if (donnesChargeesSauvegarder && !premierAffichageOk) {
			textAreaConsultation.setText(ControleurPageDeGarde.getStrVisites().toString());
			donnees = ControleurPageDeGarde.getDonnees();
			premierAffichageOk = true;
			listeFiltreOk = false;
			contenuFichier = textAreaConsultation.getText();
		}

		if (premierAffichageOk && !listeFiltreOk) {
			listeFiltreOk = true;
			filtres = new Filtre();
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
		listeDesFiltres.clear();
		contenuFichier = "";
		// si filtres null c'est-à-dire que l'utilisateur n'a pas cliqué que la fenetre
		// une fentre lui dit quil faut cliquer sur le fenetre avant de filter les données
		if (filtres == null) {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Avertissement");
			alert.setHeaderText(null);
			alert.setContentText("Veuillez cliquer sur la zone de texte pour afficher les données avant de filtrer.");
			alert.showAndWait();
			return;
		}
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
			listeDesFiltres.add("Nom de l'employé : " + nom + " " + prenom);
		}

		if (nomPrenomEmploye.getValue() != null 
				&& !filtres.getListeVisite().isEmpty()) {
			strAnalyser = nomPrenomEmploye.getValue();
			champs = strAnalyser.split(" ");
			nom = champs[0];
			prenom = champs[1];
			filtres.employeNom(nom, prenom);
			listeDesFiltres.add("Nom du conférencier : " + nom + " " + prenom);
		}

		if (intituleExpo.getValue() != null 
				&& !filtres.getListeVisite().isEmpty()) {
			strAnalyser = intituleExpo.getValue();
			filtres.expositionIntitule(strAnalyser);
			listeDesFiltres.add("Intitulé de l'exposition : " + strAnalyser);
		}

		if (intituleVisite.getValue() != null 
				&& !filtres.getListeVisite().isEmpty()) {
			strAnalyser = intituleVisite.getValue();
			filtres.visiteIntitule(strAnalyser);
			listeDesFiltres.add("Intitulé de la visite : " + strAnalyser);
		}

		if (numTel.getValue() != null 
				&& !filtres.getListeVisite().isEmpty()) {
			strAnalyser = numTel.getValue();
			filtres.visiteNumTel(strAnalyser);
			listeDesFiltres.add("Numéro de téléphone : " + strAnalyser);
		}

		if (heurePrecise.getValue() != null 
				&& !filtres.getListeVisite().isEmpty()) {
			strAnalyser = heurePrecise.getValue();
			try {
				filtres.heurePrecise(strAnalyser);
				listeDesFiltres.add("Heure précise : " + strAnalyser);
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
				listeDesFiltres.add("Heure de début : " + strAnalyser);
				listeDesFiltres.add("heure de fin : " + strAnalyserBis);
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

		if (visiteDate.getValue() != null 
				&& !filtres.getListeVisite().isEmpty()) {
			try {
				datePrecise = visiteDate.getValue();

				strAnalyser = datePrecise.format(dateTimeFormat);
				datePrecise = LocalDate.parse(strAnalyser, dateTimeFormat);

				date = format.parse(datePrecise.format(dateTimeFormat).toString());

				filtres.datePrecise(date);
				listeDesFiltres.add("Date précise : " + datePrecise);
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
				try {
					filtres.datePeriode(dateDebut, dateFin);
				} catch (IllegalArgumentException e){
					// boite d'alerte pour avertir l'utilisateur
					Alert alert = new Alert(Alert.AlertType.WARNING);
					alert.setTitle("Avertissement");
					alert.setHeaderText(null);
					alert.setContentText(e.getMessage());
					alert.showAndWait();
					reinitialiserFiltre();
				}
				listeDesFiltres.add("Date de début : " + dateIntervalleDebut);
				listeDesFiltres.add("Date de fin : " + dateIntervalleFin);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		if (expoPerm.isSelected() 
				&& !filtres.getListeVisite().isEmpty()) {
			filtres.expositionPermanente();
			listeDesFiltres.add("Exposition permanente");
		} else if (expoTemp.isSelected()) {
			filtres.expositionTemporaire();
			listeDesFiltres.add("Exposition temporaire");
		}

		if (confInterne.isSelected() 
				&& !filtres.getListeVisite().isEmpty()) {
			filtres.conferencierInterne();
			listeDesFiltres.add("Conférencier interne");
		} else if (confExterne.isSelected()) {
			filtres.conferencierExterne();
			listeDesFiltres.add("Conférencier externe");
		}

		if (!filtres.getListeVisite().isEmpty()) {
			for (Visite visite : filtres.getListeVisite()) {
				aAfficher += visite + "\n\n";
			}
			textAreaConsultation.setText("\t\t\t\t\t\t\t\t\tRésultat pour votre recherche." 
					+ "\n\t\t\t\t\t\t\t\t     Nombre de visite(s) trouvée(s) : " 
					+ filtres.getListeVisite().size() + ".\n\n\n"
					+ aAfficher);
			contenuFichier = aAfficher;
		} else if (filtres.getListeVisite().size() == donnees.getVisites().size()) {
			textAreaConsultation.setText("Aucun(s) filtre(s) appliqué(s).");
		} else {
			textAreaConsultation.setText("Aucun résultat à votre recherche.");
		}
	}

	@FXML
	void reinitialiserFiltre() {
		listeDesFiltres.clear();
		ArrayList<String> nomsConf = new ArrayList<>();
		ArrayList<String> nomsEmploye = new ArrayList<>();
		ArrayList<String> IntituleExpo = new ArrayList<>();
		ArrayList<String> IntituleVisites = new ArrayList<>();
		ArrayList<String> numtels = new ArrayList<>();


		if ((!donneesChargeesLocal || strVisitesLocal == null) && (!donnesChargeesDistance || strVisitesDistance == null)
				&& (!donnesChargeesSauvegarder || strVisitesSave == null ))  { // Vérifie si les données n'ont pas déjà été chargées en local et a distance
			textAreaConsultation.setText("Les données ne sont pas encore disponibles.");
		}

		if (donneesChargeesLocal) {
			textAreaConsultation.setText(ControleurImporterLocal.getStrVisites().toString());
			contenuFichier = textAreaConsultation.getText();
		} else if (donnesChargeesDistance) {
			textAreaConsultation.setText(ControleurImporterDistance.getStrVisites().toString());
			contenuFichier = textAreaConsultation.getText();
		} else if (donnesChargeesSauvegarder) {
			textAreaConsultation.setText(ControleurPageDeGarde.getStrVisites().toString());
			contenuFichier = textAreaConsultation.getText();
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
		if (premierAffichageOk) {
			reinitialiserFiltre();
			filtres.reset();
		}
		
		Main.setPageConsulter();
	}

	@FXML
	void exporter(ActionEvent event) {
		if (premierAffichageOk) {
			reinitialiserFiltre();
			filtres.reset();
		}
		
		Main.setPageExporter();

	}

	@FXML
	void importer(ActionEvent event) {
		if (premierAffichageOk) {
			reinitialiserFiltre();
			filtres.reset();
		}
		
		Main.setPageImporter();
	}

	@FXML
	void notice(ActionEvent event) {
		Main.afficherNotice();
	}
	
	@FXML
    void statistiques(ActionEvent event) {
		if (premierAffichageOk) {
			reinitialiserFiltre();
			filtres.reset();
		}
		
    	Main.setPageConsulterStatistiques();
    }

	@FXML
    void quitter(ActionEvent event) {
    	Main.quitterApllication();
    }

	@FXML
	void revenirEnArriere(ActionEvent event) {
		if (premierAffichageOk) {
			reinitialiserFiltre();
			filtres.reset();
		}
		
		Main.setPageConsulter();
	}

	@FXML
	void sauvegarder(ActionEvent event) {
		Main.sauvegarder();
	}

	@FXML
	void genererPdf(ActionEvent event) {
		titre = "Visites";
		System.out.println("Titre du PDF : " + titre);
		System.out.println("Contenu du fichier : " + contenuFichier);

		if (listeDesFiltres.isEmpty()) {
			listeDesFiltres.add("Aucun filtre appliqué");
		}

		if (contenuFichier == null) {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Avertissement");
			alert.setHeaderText(null);
			alert.setContentText("Aucun résultat n'est disponible. Impossible de générer le PDF.");
			alert.showAndWait();
			return;
		}

		// Créer une instance de FileChooser
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Enregistrer le fichier PDF");
		fileChooser.getExtensionFilters().add(
				new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf")
		);

		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		java.io.File file = fileChooser.showSaveDialog(stage);

		if (file != null) {
			String filePath = file.getAbsolutePath();
			if (!filePath.endsWith(".pdf")) {
				filePath += ".pdf";
			}
			FichierPdf fichierPdf = new FichierPdf(titre, listeDesFiltres, contenuFichier);
			boolean success = fichierPdf.genererPdf(filePath);
			if (success) {
				System.out.println("Fichier PDF généré avec succès à : " + filePath);
				afficherPopUp("Fichier PDF généré avec succès à : " + filePath, event);
			} else {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Erreur");
				alert.setHeaderText(null);
				alert.setContentText("Une erreur est survenue lors de la génération du PDF.");
				alert.showAndWait();
			}
		} else {
			System.out.println("Sauvegarde annulée par l'utilisateur.");
		}
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
	//TODO regler le probleme que si on n'applique aucun filtre ca met rien dans le rapport
}
