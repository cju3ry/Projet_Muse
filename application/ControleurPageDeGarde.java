package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Optional;

import gestion_donnees.Conferencier;
import gestion_donnees.DonneesApplication;
import gestion_donnees.Employe;
import gestion_donnees.Exposition;
import gestion_donnees.Visite;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public class ControleurPageDeGarde {

	private static DonneesApplication donnees = new DonneesApplication();

	private static boolean isDonneesChargees = false;

	private  static StringBuilder strConferencier;

	private  static StringBuilder strEmployes;

	private  static StringBuilder strExpositions;

	private  static StringBuilder strVisites;

	@FXML
	private Button btnConsulter;

	@FXML Button btnExporter;

	@FXML
	private Button btnImporter;

	@FXML
	private Button btnNotice;

	@FXML
	private Button btnQuitter;
	
	@FXML
	private Button btnSauvegarder;
	
	@FXML
	void sauvegarder(ActionEvent event) {
		Main.sauvegarder();
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
    void quitter(ActionEvent event) {
    	Main.quitterApllication();
    }

	@FXML
	void notice(ActionEvent event) {
		Main.afficherNotice();
	}
	
	public static DonneesApplication getDonnees() {
		return donnees;
	}

	@FXML
	public void initialize() {
		String pathEmploye = "save\\employesData";
		File fichierEmploye = new File(pathEmploye);

		String pathConferenciers = "save\\conferencierseData";
		File fichierConferenciers = new File(pathConferenciers);

		String pathExpoitions = "save\\expositionsData";
		File fichierExpositions = new File(pathExpoitions);

		String pathVisites = "save\\visitesData";
		File fichierVisite = new File(pathVisites);

		if (fichierEmploye.exists() && fichierConferenciers.exists() &&
				fichierExpositions.exists() && fichierVisite.exists()) {

			// Utiliser Platform.runLater pour afficher l'alerte après que la fenêtre principale soit chargée
			Platform.runLater(() -> {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Recharges les donnees sauvegardées");
				alert.setContentText("Une sauvegarde a été détectée. Voulez vous la recharger ?");
				Optional<ButtonType> result = alert.showAndWait();

				if (result.isPresent() && result.get() == ButtonType.OK) {
					chargerDonnees();
				}
			});
		}
	}

	private void chargerDonnees() {
		try (FileInputStream fis = new FileInputStream("save\\employesData");
				ObjectInputStream ois = new ObjectInputStream(fis)) {
			donnees.setEmployes((ArrayList) ois.readObject());
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		try (FileInputStream fis = new FileInputStream("save\\conferencierseData");
				ObjectInputStream ois = new ObjectInputStream(fis)) {
			donnees.setConferenciers((ArrayList) ois.readObject());
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		try (FileInputStream fis = new FileInputStream("save\\expositionsData");
				ObjectInputStream ois = new ObjectInputStream(fis)) {
			donnees.setExpositions((ArrayList) ois.readObject());
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		try (FileInputStream fis = new FileInputStream("save\\visitesData");
				ObjectInputStream ois = new ObjectInputStream(fis)) {
			donnees.setVisites((ArrayList) ois.readObject());
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		strEmployes = new StringBuilder();
		strConferencier = new StringBuilder();
		strExpositions = new StringBuilder();
		strVisites = new StringBuilder();

		ArrayList<Employe> listeDesEmployes = donnees.getEmployes();
		strEmployes.append("\n");
		for (int i = 0; i < listeDesEmployes.size(); i++) {
			strEmployes.append(listeDesEmployes.get(i).toString()).append("\n");
		}

		ArrayList<Exposition> listeDesExpositions = donnees.getExpositions();
		strExpositions.append("\n");
		for (int i = 0; i < listeDesExpositions.size(); i++) {
			strExpositions.append(listeDesExpositions.get(i).toString()).append("\n");
		}

		ArrayList<Visite> listeDesVistes = donnees.getVisites();
		strVisites.append("\n");
		for (int i = 0; i < listeDesVistes.size(); i++) {
			strVisites.append(listeDesVistes.get(i).toString()).append("\n");
		}

		ArrayList<Conferencier> listeDesConfernciers = donnees.getConferenciers();
		strConferencier.append("\n");
		for (int i = 0; i < listeDesConfernciers.size(); i++) {
			strConferencier.append(listeDesConfernciers.get(i).toString()).append("\n");
		}
		isDonneesChargees = true;
	}



	/** Getters pour les données importées des conférenciers
	 * @return strConferencier la liste des conférenciers importés
	 * */
	public static StringBuilder getStrConferencier() {
		return strConferencier;
	}
	/** Getters pour les données importées des employés
	 * @return strEmployes la liste des employés importés
	 * */
	public static StringBuilder getStrEmployes() {
		return strEmployes;
	}

	/** Getters pour les données importées des expositions
	 * @return strExpositions la liste des expositions importées
	 * */
	public static StringBuilder getStrExpositions() {
		return strExpositions;
	}

	/** Getters pour les données importées des visites
	 * @return strVisites la liste des visites importées
	 * */
	public static StringBuilder getStrVisites() {
		return strVisites;
	}

	public static boolean isDonneesSaveChargees() {
		return isDonneesChargees;
	}
	
	@FXML
    void statistiques(ActionEvent event) {
    	Main.setPageConsulterStatistiques();
    }
}
