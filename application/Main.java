package application;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URI;

import gestion_donnees.DonneesApplication;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class Main extends Application {

	public static Stage fenetrePrincipale;

	private static Parent conteneur;

	private static Scene scenePageDeGarde;

	private static Scene scenePageImporter;

	private static Scene scenePageImporterLocal;

	private static Scene scenePageImporterDistance;

	private static Scene scenePageExporter;

	private static Scene scenePageConsulter;

	private static Scene scenePageConsulterDonneesConferencier;

	private static Scene scenePageConsulterDonneesEmploye;

	private static Scene scenePageConsulterDonneesVisite;

	private static Scene scenePageConsulterDonneesExposition;

	private static Scene scenePageStatitiques;

	private static FXMLLoader chargeurFxmlPageDeGarde = new FXMLLoader();

	private static FXMLLoader chargeurFxmlPageImporter = new FXMLLoader();

	private static FXMLLoader chargeurFxmlPageImporterLocal = new FXMLLoader();

	private static FXMLLoader chargeurFxmlPageImporterDistance = new FXMLLoader();

	private static FXMLLoader chargeurFxmlPageExporter = new FXMLLoader();

	private static FXMLLoader chargeurFxmlPageConsulter = new FXMLLoader();

	private static FXMLLoader chargeurFxmlPageConsulterDonneesConferencier = new FXMLLoader();

	private static FXMLLoader chargeurFxmlPageConsulterDonneesEmploye = new FXMLLoader();

	private static FXMLLoader chargeurFxmlPageConsulterDonneesVisite = new FXMLLoader();

	private static FXMLLoader chargeurFxmlPageConsulterDonneesExposition = new FXMLLoader();

	private static FXMLLoader chargeurFxmlPageStatistiques = new FXMLLoader();

	@Override
	public void start(Stage primaryStage) {


		try {
			chargeurFxmlPageDeGarde.setLocation(getClass().getResource("/ihm/vuePageDeGarde.fxml"));
			conteneur = chargeurFxmlPageDeGarde.load();
			scenePageDeGarde = new Scene(conteneur);
			scenePageDeGarde.getStylesheets().add(getClass().getResource("application.css").toExternalForm());


			chargeurFxmlPageImporter.setLocation(getClass().getResource("/ihm/vueImporter.fxml"));
			conteneur = chargeurFxmlPageImporter.load();
			scenePageImporter = new Scene(conteneur);
			scenePageImporter.getStylesheets().add(getClass().getResource("application.css").toExternalForm());


			chargeurFxmlPageImporterLocal.setLocation(getClass().getResource("/ihm/vueImporterLocal.fxml"));
			conteneur = chargeurFxmlPageImporterLocal.load();
			scenePageImporterLocal = new Scene(conteneur);
			scenePageImporterLocal.getStylesheets().add(getClass().getResource("application.css").toExternalForm());


			chargeurFxmlPageImporterDistance.setLocation(getClass().getResource("/ihm/vueImporterDistante.fxml"));
			conteneur = chargeurFxmlPageImporterDistance.load();
			scenePageImporterDistance = new Scene(conteneur);
			scenePageImporterDistance.getStylesheets().add(getClass().getResource("application.css").toExternalForm());


			chargeurFxmlPageExporter.setLocation(getClass().getResource("/ihm/vueExporter.fxml"));
			conteneur = chargeurFxmlPageExporter.load();
			scenePageExporter = new Scene(conteneur);
			scenePageExporter.getStylesheets().add(getClass().getResource("application.css").toExternalForm());


			chargeurFxmlPageConsulter.setLocation(getClass().getResource("/ihm/vueConsulter.fxml"));
			conteneur = chargeurFxmlPageConsulter.load();
			scenePageConsulter = new Scene(conteneur);
			scenePageConsulter.getStylesheets().add(getClass().getResource("application.css").toExternalForm());


			chargeurFxmlPageConsulterDonneesConferencier.setLocation(getClass().getResource("/ihm/vueConsulterDonneesConferencier.fxml"));
			conteneur = chargeurFxmlPageConsulterDonneesConferencier.load();
			scenePageConsulterDonneesConferencier = new Scene(conteneur);
			scenePageConsulterDonneesConferencier.getStylesheets().add(getClass().getResource("application.css").toExternalForm());


			chargeurFxmlPageConsulterDonneesEmploye.setLocation(getClass().getResource("/ihm/vueConsulterDonneesEmploye.fxml"));
			conteneur = chargeurFxmlPageConsulterDonneesEmploye.load();
			scenePageConsulterDonneesEmploye = new Scene(conteneur);
			scenePageConsulterDonneesEmploye.getStylesheets().add(getClass().getResource("application.css").toExternalForm());


			chargeurFxmlPageConsulterDonneesExposition.setLocation(getClass().getResource("/ihm/vueConsulterDonneesExposition.fxml"));
			conteneur = chargeurFxmlPageConsulterDonneesExposition.load();
			scenePageConsulterDonneesExposition = new Scene(conteneur);
			scenePageConsulterDonneesExposition.getStylesheets().add(getClass().getResource("application.css").toExternalForm());


			chargeurFxmlPageConsulterDonneesVisite.setLocation(getClass().getResource("/ihm/vueConsulterDonneesVisite.fxml"));
			conteneur = chargeurFxmlPageConsulterDonneesVisite.load();
			scenePageConsulterDonneesVisite = new Scene(conteneur);
			scenePageConsulterDonneesVisite.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			chargeurFxmlPageStatistiques.setLocation(getClass().getResource("/ihm/vueStatistiques.fxml"));
			conteneur = chargeurFxmlPageStatistiques.load();
			scenePageStatitiques = new Scene(conteneur);
			scenePageStatitiques.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			primaryStage.setScene(scenePageDeGarde);
			//		primaryStage.initStyle(StageStyle.UNDECORATED);
			fenetrePrincipale = primaryStage;
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void setPageDeGarde() {
		fenetrePrincipale.setScene(scenePageDeGarde);
	}

	public static void setPageImporter() {
		fenetrePrincipale.setScene(scenePageImporter);
	}

	public static void setPageImporterLocal() {
		fenetrePrincipale.setScene(scenePageImporterLocal);
	}

	public static void setPageImporterDistance() {
		fenetrePrincipale.setScene(scenePageImporterDistance);
	}

	public static void setPageExporter() {
		fenetrePrincipale.setScene(scenePageExporter);
	}

	public static void setPageConsulter() {
		fenetrePrincipale.setScene(scenePageConsulter);
	}

	//	public static void setPageConsulterDonnees() {
	//		fenetrePrincipale.setScene(scenePageConsulterDonnees);
	//	}

	public static void setPageConsulterDonneesConferencier() {
		fenetrePrincipale.setScene(scenePageConsulterDonneesConferencier);
	}

	public static void setPageConsulterDonneesEmploye() {
		fenetrePrincipale.setScene(scenePageConsulterDonneesEmploye);
	}

	public static void setPageConsulterDonneesExposition() {
		fenetrePrincipale.setScene(scenePageConsulterDonneesExposition);
	}

	public static void setPageConsulterDonneesVisite() {
		fenetrePrincipale.setScene(scenePageConsulterDonneesVisite);
	}

	public static void setPageConsulterStatistiques() {
		fenetrePrincipale.setScene(scenePageStatitiques);
	}

	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Affiche l'aide à l'utilisateur dans une page web
	 */
//	public static void afficherNotice() {
//		String cheminFichier = "notice/Notice.pdf";
//		File fichierPdf = new File(cheminFichier);
//
//		if (fichierPdf.exists()) {
//			try {
//				URI uri = fichierPdf.toURI();
//				String url = uri.toString();
//				Desktop.getDesktop().browse(new URI(url));
//			} catch (Exception e) {
//				System.err.println("Erreur lors de l'ouverture du fichier PDF: " + e.getMessage());
//			}
//		} else {
//			System.err.println("Le fichier PDF n'existe pas.");
//		}
//	}

	public static void afficherNotice() {
		String cheminFichier = "Notice.pdf";
		try {
			File fichierPdf = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
			File pdfFile = new File(fichierPdf, cheminFichier);
			Desktop.getDesktop().browse(pdfFile.toURI());
		} catch (Exception e) {
			System.err.println("Erreur lors de l'ouverture du fichier PDF: " + e.getMessage());
		}
	}

	private static DonneesApplication donnees = new DonneesApplication();

	public static void sauvegarder() {
		if(donnees.getEmployes().isEmpty() || donnees.getConferenciers().isEmpty() || donnees.getExpositions().isEmpty() || donnees.getVisites().isEmpty()) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Sauvegarde réussie");
			alert.setHeaderText(null);
			alert.setContentText("Les données sont vides. Impossible de sauvegarder.");
			alert.showAndWait();
		} else {
			try {
				// Vérifie si le chemin src/save/employesData.ser est bien là
				File dossier = new File("save");
				if (!dossier.exists()) {
					dossier.mkdirs(); // Crée le dossier save s'il n'existe pas
				}
				// Sauvegarde des données des employés
				try (FileOutputStream fos = new FileOutputStream("save\\employesData");
						ObjectOutputStream oos = new ObjectOutputStream(fos)) {
					oos.writeObject(donnees.getEmployes());
				} catch (IOException e) {
					e.printStackTrace();
				}

				// Sauvegarde des données des conférenciers
				try (FileOutputStream fos = new FileOutputStream("save\\conferencierseData");
						ObjectOutputStream oos = new ObjectOutputStream(fos)) {
					oos.writeObject(donnees.getConferenciers());
				} catch (IOException e) {
					e.printStackTrace();
				}

				// Sauvegarde des données des expositions
				try (FileOutputStream fos = new FileOutputStream("save\\expositionsData");
						ObjectOutputStream oos = new ObjectOutputStream(fos)) {
					oos.writeObject(donnees.getExpositions());
				} catch (IOException e) {
					e.printStackTrace();
				}

				// Sauvegarde des données des visites
				try (FileOutputStream fos = new FileOutputStream("save\\visitesData");
						ObjectOutputStream oos = new ObjectOutputStream(fos)) {
					oos.writeObject(donnees.getVisites());
				} catch (IOException e) {
					e.printStackTrace();
				}

				// Affichage d'un message de confirmation
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Sauvegarde réussie");
				alert.setHeaderText(null);
				alert.setContentText("Les données ont été sauvegardées avec succès.");
				alert.showAndWait();

			} catch (Exception e) {
				e.printStackTrace();
				// Affichage d'un message d'erreur si la sauvegarde échoue
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Erreur de sauvegarde");
				alert.setHeaderText(null);
				alert.setContentText("Une erreur est survenue lors de la sauvegarde des données.");
				alert.showAndWait();
			}
		}
	}
	
	public static void quitterApllication() {
		  
	    Alert alert = new Alert(AlertType.CONFIRMATION);
	    alert.setTitle("Confirmation");
	    alert.setHeaderText("Êtes-vous sûr de vouloir quitter ? Les données non sauvegardées seront perdu");
	    alert.setContentText("Cliquez sur OK pour quitter, ou sur Annuler pour rester.");

	    alert.showAndWait().ifPresent(response -> {
	        if (response == javafx.scene.control.ButtonType.OK) {
	        	Platform.exit();
	        }
	    });
	}
}
