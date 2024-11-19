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

import gestion_donnees.Conferencier;
import gestion_donnees.DonneesApplication;
import gestion_donnees.Exposition;
import gestion_donnees.Filtre;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

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

	private boolean donneesChargeesLocal; // Pour vérifier si les données sont déjà chargées en local

	private boolean donnesChargeesDistance; // Pour vérifier si les données sont déjà chargées a distance

	private boolean donnesChargeesSauvegarder;

	private StringBuilder strConferencierLocal;

	private StringBuilder strConferencierDistance;

	private StringBuilder strConferencierSave;

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
				&& !donnesChargeesSauvegarder || strConferencierSave == null )  { // Vérifie si les données n'ont pas déjà été chargées en local et a distance
			textAreaConsultation.setText("Les données ne sont pas encore disponibles.");
		}

		if (donneesChargeesLocal && !premierAffichageOk) {
			textAreaConsultation.setText("\n\n" + ControleurImporterLocal.getStrConferencier().toString());
			donnees = ControleurImporterLocal.getDonnees();
			premierAffichageOk = true;
			listeFiltreOk = false;
		} else if (donnesChargeesDistance && !premierAffichageOk) {
			textAreaConsultation.setText("\n\n" + ControleurImporterDistance.getStrConferencier().toString());
			donnees = ControleurImporterDistance.getDonnees();
			premierAffichageOk = true;
			listeFiltreOk = false;
		} else if (donnesChargeesSauvegarder && !premierAffichageOk) {
			textAreaConsultation.setText("\n\n" + ControleurPageDeGarde.getStrConferencier().toString());
			donnees = ControleurPageDeGarde.getDonnees();
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
			
			triePar.setVisible(false);
			triePar.setItems(FXCollections.observableArrayList("Nombre de visite croissant", "Nombre de visite décroissant"));
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
		moyennesOk.setVisible(!isVisible);
	}

	@FXML 
	void appliquerFiltre() {
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
				filtres.confVisiteHoraire(strHeureDebut, strHeureFin);
			} catch (ParseException e) {
				e.printStackTrace();
				textAreaConsultation.setText("Erreur dans le format des heures.");
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
			}
		}
		
		if(triePar.getValue() != null && triePar.getValue().equals("Nombre de visite croissant")) {
			filtres.conferenciersTrie(filtres.getListeConferencier(), true);
		} else if (triePar.getValue() != null && triePar.getValue().equals("Nombre de visite décroissant")) {
			filtres.conferenciersTrie(filtres.getListeConferencier(), false);
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
		} else if (moyennesOk.isSelected() && confDateDebut.getValue() != null && confDateFin.getValue() != null) {
			for (Conferencier conferencier : filtres.getListeConferencier()) {
				aAfficher += tableauMoyennes[i] + "\n" + conferencier + "\n\n";
				i++;
			}
			
			textAreaConsultation.setText("\t\t\t\t\t\t     Résultat pour votre recherche." 
					+ "\n\t\t\t\t            Nombre de conférencier(s) trouvée(s) : " 
					+ filtres.getListeConferencier().size() + ".\n\n\n"
					+ aAfficher);
		} else {
			textAreaConsultation.setText("Aucun résultat à votre recherche.");
		}
	}

	@FXML
	void reinitialiserFiltre() {
		strConferencierLocal = ControleurImporterLocal.getStrConferencier();
		strConferencierDistance = ControleurImporterDistance.getStrConferencier();
		// System.out.print("Donnes Charge distance" + donnesChargeesDistance);

		if (donneesChargeesLocal) {
			textAreaConsultation.setText("\n\n" + ControleurImporterLocal.getStrConferencier().toString());
		} else if (donnesChargeesDistance) {
			textAreaConsultation.setText("\n\n" + ControleurImporterDistance.getStrConferencier().toString());
		} else if (donnesChargeesSauvegarder) {
			textAreaConsultation.setText("\n\n" + ControleurPageDeGarde.getStrConferencier().toString());
		}

		if ((!donneesChargeesLocal || strConferencierLocal == null) && (!donnesChargeesDistance || strConferencierDistance == null)
				&& !donnesChargeesSauvegarder || strConferencierSave == null )  { // Vérifie si les données n'ont pas déjà été chargées en local et a distance
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
}
