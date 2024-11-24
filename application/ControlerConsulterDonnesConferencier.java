package application;

import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
import javafx.stage.Stage;
import javafx.util.Duration;

public class ControlerConsulterDonnesConferencier {

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
	private ScrollPane scrollPaneFiltres;

	@FXML
	private VBox panneauFiltres;

	@FXML
	private Button btnFiltre;

	@FXML
	private Button btnLancerFiltre;

	@FXML
	private Button btnReinitialiserFiltre;

	@FXML
	private DatePicker confDateDebut;

	@FXML
	private DatePicker confDateFin;

	@FXML
	private Button btnSauvegarder;

	@FXML
	public Button statistiques;

	@FXML
	private ChoiceBox<String> heureDebut;

	@FXML
	private ChoiceBox<String> heureFin;

	@FXML
	private TextArea textAreaConsultation;
	
	@FXML
    private Label texteTrie;
	
	@FXML
	private CheckBox moyennesOk;
	
	@FXML
	private ChoiceBox<String> triePar;
	
	@FXML
	private Button genererPdf;

	private boolean donneesChargeesLocal; // Pour vérifier si les données sont déjà chargées en local

	private boolean donnesChargeesDistance; // Pour vérifier si les données sont déjà chargées a distance

	private boolean donnesChargeesSauvegarder;

	private StringBuilder strConferencierLocal;

	private StringBuilder strConferencierDistance;

	private StringBuilder strConferencierSave;

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
		triePar.setVisible(false);
		texteTrie.setVisible(false);
		moyennesOk.setVisible(false);
		
		confDateDebut.setOnAction(event -> afficherMoyenne());
		confDateFin.setOnAction(event -> afficherMoyenne());
		btnFiltre.setOnAction(event -> toggleFiltrePanel());
		btnLancerFiltre.setOnAction(event -> appliquerFiltre());
		btnReinitialiserFiltre.setOnAction(event -> reinitialiserFiltre());
	}

	// Méthode pour charger et afficher les données
	private void afficherDonnees() {
		filtres = new Filtre();
		boolean listeFiltreOk;
		donneesChargeesLocal = ControleurImporterLocal.isDonneesConferencierChargees();
		donnesChargeesDistance = ControleurImporterDistance.isDonneesConferencierChargees();
		donnesChargeesSauvegarder = ControleurPageDeGarde.isDonneesSaveChargees();

		strConferencierLocal = ControleurImporterLocal.getStrConferencier();
		strConferencierDistance = ControleurImporterDistance.getStrConferencier();
		strConferencierSave = ControleurPageDeGarde.getStrConferencier();

		listeFiltreOk = true;

		if ((!donneesChargeesLocal || strConferencierLocal == null) && (!donnesChargeesDistance || strConferencierDistance == null)
				&& (!donnesChargeesSauvegarder || strConferencierSave == null ))  { // Vérifie si les données n'ont pas déjà été chargées en local et a distance
			textAreaConsultation.setText("Les données ne sont pas encore disponibles.");
		}

		if (donneesChargeesLocal && !premierAffichageOk) {
			textAreaConsultation.setText("\n\n" + ControleurImporterLocal.getStrConferencier().toString());
			donnees = ControleurImporterLocal.getDonnees();
			premierAffichageOk = true;
			listeFiltreOk = false;
			contenuFichier = textAreaConsultation.getText();
		} else if (donnesChargeesDistance && !premierAffichageOk) {
			textAreaConsultation.setText("\n\n" + ControleurImporterDistance.getStrConferencier().toString());
			donnees = ControleurImporterDistance.getDonnees();
			premierAffichageOk = true;
			listeFiltreOk = false;
			contenuFichier = textAreaConsultation.getText();
		} else if (donnesChargeesSauvegarder && !premierAffichageOk) {
			textAreaConsultation.setText("\n\n" + ControleurPageDeGarde.getStrConferencier().toString());
			donnees = ControleurPageDeGarde.getDonnees();
			premierAffichageOk = true;
			listeFiltreOk = false;
			contenuFichier = textAreaConsultation.getText();
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
			
			triePar.setVisible(false);
			triePar.setItems(FXCollections.observableArrayList("Nombre de visite croissant", "Nombre de visite décroissant"));
		}
	}
	
	private void afficherMoyenne() {
		if (confDateDebut.getValue() != null && confDateFin.getValue() != null) {
			moyennesOk.setVisible(true);
		} else {
			moyennesOk.setVisible(false);
		}
	}

	private void toggleFiltrePanel() {
		// Basculer la visibilité du panneau de filtres
		boolean isVisible = scrollPaneFiltres.isVisible();
		scrollPaneFiltres.setVisible(!isVisible);
		btnLancerFiltre.setVisible(!isVisible);
		btnReinitialiserFiltre.setVisible(!isVisible);
		triePar.setVisible(!isVisible);
		texteTrie.setVisible(!isVisible);
	}

	@FXML 
	void appliquerFiltre() {
		if (filtres == null) {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Avertissement");
			alert.setHeaderText(null);
			alert.setContentText("Veuillez cliquer sur la zone de texte pour afficher les données avant de filtrer.");
			alert.showAndWait();
			return;
		}
		listeDesFiltres.clear();
		contenuFichier = "";
		// Réinitialise les filtres pour partir de toutes les données disponibles
		filtres.reset();

		DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		HashMap<Conferencier, Double> moyennes = new HashMap<>();
		Set<Entry<Conferencier, Double>> moyennesConf;
		double[] tableauMoyennes;
		
		String strDateDebut = "";
		String strDateFin = "";
		String strHeureDebut;
		String strHeureFin;
		String aAfficher = "";
		int i = 0;

		// Vérification et application du filtre par période de date
		if (confDateDebut.getValue() != null && confDateFin.getValue() != null) {
			try {
				// Conversion de LocalDate en String au format dd/MM/yyyy
				strDateDebut = confDateDebut.getValue().format(dateTimeFormat);
				strDateFin = confDateFin.getValue().format(dateTimeFormat);

				// Appel du filtre avec les dates sous forme de chaînes
				filtres.confVisitePeriode(strDateDebut, strDateFin);
				listeDesFiltres.add("Date de début : " + strDateDebut);
				listeDesFiltres.add("Date de fin : " + strDateFin);
			} catch (ParseException e) {
				e.printStackTrace();
				textAreaConsultation.setText("Erreur dans le format des dates.");

			} catch (IllegalArgumentException e )  {
				// affiche une boite d'alerte si la date de debut est superieur a la date de fin
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.setTitle("Avertissement");
				alert.setHeaderText(null);
				alert.setContentText(e.getMessage());
				alert.showAndWait();
				return;
			}
		}

		// Vérification et application du filtre par horaire de visite
		if (heureDebut.getValue() != null && heureFin.getValue() != null) {
			strHeureDebut = heureDebut.getValue();
			strHeureFin = heureFin.getValue();

			try {
				filtres.confVisiteHoraire(strHeureDebut, strHeureFin);
				listeDesFiltres.add("Heure de début : " + strHeureDebut);
				listeDesFiltres.add("Heure de fin : " + strHeureFin);
			} catch (ParseException e) {
				e.printStackTrace();
				textAreaConsultation.setText("Erreur dans le format des heures.");
			} catch (IllegalArgumentException e )  {
				// affiche une boite d'alerte si la date de debut est superieur a la date de fin
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.setTitle("Avertissement");
				alert.setHeaderText(null);
				alert.setContentText(e.getMessage());
				alert.showAndWait();
				return;
			}
		}
		
		tableauMoyennes = new double[filtres.getListeConferencier().size()];

		if (confDateDebut.getValue() != null && confDateFin.getValue() != null) {
			try {
				moyennes = filtres.confMoyennesPeriode(filtres.getListeConferencier(), strDateDebut, strDateFin);
				moyennesConf = moyennes.entrySet();
				
				for (Entry<Conferencier, Double> paire : moyennesConf) {
					tableauMoyennes[i] = paire.getValue();
					i++;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e )  {
				// affiche une boite d'alerte si la date de debut est superieur a la date de fin
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.setTitle("Avertissement");
				alert.setHeaderText(null);
				alert.setContentText(e.getMessage());
				alert.showAndWait();
				return;
			}
		}
		
		if(triePar.getValue() != null && triePar.getValue().equals("Nombre de visite croissant")) {
			filtres.conferenciersTrie(filtres.getListeConferencier(), true);
			listeDesFiltres.add("Trie par nombre de visite croissant");
		} else if (triePar.getValue() != null && triePar.getValue().equals("Nombre de visite décroissant")) {
			filtres.conferenciersTrie(filtres.getListeConferencier(), false);
			listeDesFiltres.add("Trie par nombre de visite décroissant");
		}
		
		i = 0;
		
		if (!filtres.getListeConferencier().isEmpty() && !moyennesOk.isSelected()) {
			for (Conferencier conferencier : filtres.getListeConferencier()) {
				aAfficher += conferencier + "\n\n";
				i++;
			}
			
			textAreaConsultation.setText("\t\t\t\t\t\t     Résultat pour votre recherche." 
					+ "\n\t\t\t\t            Nombre de conférencier(s) trouvée(s) : " 
					+ filtres.getListeConferencier().size() + ".\n\n\n"
					+ aAfficher);
			contenuFichier = aAfficher;
		} else if (moyennesOk.isSelected() && confDateDebut.getValue() != null && confDateFin.getValue() != null) {
			for (Conferencier conferencier : filtres.getListeConferencier()) {
				aAfficher += "\tMoyenne = " + tableauMoyennes[i] + "\n" + conferencier + "\n\n";
				i++;
			}
			listeDesFiltres.add("Moyenne des visites");
			contenuFichier = aAfficher;
			textAreaConsultation.setText("\t\t\t\t\t\t     Résultat pour votre recherche."
					+ "\n\t\t\t\t            Nombre de conférencier(s) trouvée(s) : " 
					+ filtres.getListeConferencier().size() + "."
					+ "\n\t\t            Avec moyennes de nombre de visites par nombre de jours"
					+ ".\n\n\n"
					+ aAfficher);
			contenuFichier = aAfficher;
		} else {
			textAreaConsultation.setText("Aucun résultat à votre recherche.");
		}
	}

	@FXML
	void reinitialiserFiltre() {
		listeDesFiltres.clear();
		// System.out.print("Donnes Charge distance" + donnesChargeesDistance);
		if ((!donneesChargeesLocal || strConferencierLocal == null) && (!donnesChargeesDistance || strConferencierDistance == null)
				&& (!donnesChargeesSauvegarder || strConferencierSave == null ))  { // Vérifie si les données n'ont pas déjà été chargées en local et a distance
			textAreaConsultation.setText("Les données ne sont pas encore disponibles.");
		}

		if (donneesChargeesLocal) {
			textAreaConsultation.setText("\n\n" + ControleurImporterLocal.getStrConferencier().toString());
		} else if (donnesChargeesDistance) {
			textAreaConsultation.setText("\n\n" + ControleurImporterDistance.getStrConferencier().toString());
		} else if (donnesChargeesSauvegarder) {
			textAreaConsultation.setText("\n\n" + ControleurPageDeGarde.getStrConferencier().toString());
		}
		contenuFichier = textAreaConsultation.getText();


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

		confDateDebut.getEditor().clear();
		confDateDebut.setValue(null);

		confDateFin.getEditor().clear();
		confDateFin.setValue(null);
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
	void statistiques(ActionEvent event) {
		if (premierAffichageOk) {
			reinitialiserFiltre();
			filtres.reset();
		}

		Main.setPageConsulterStatistiques();
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

	}

	@FXML
	void quitter(ActionEvent event) {
		Main.quitterApllication();
	}


	@FXML
	void sauvegarder(ActionEvent event) {
		Main.sauvegarder();
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
	void genererPdf(ActionEvent event) {

		titre = "Conférenciers";
		System.out.println("tire du pdf " + titre );
		System.out.println("conttenu du fichier" + contenuFichier);
		// Si aucun filtre n'a été appliqué, on ajoute dans la liste des filtres "Aucun filtre appliqué"
		if (listeDesFiltres.isEmpty()) {
			listeDesFiltres.add("Aucun filtre appliqué");
		}
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
}
