package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class Main extends Application {
	
	private static Stage fenetrePrincipale;
	
	private static Parent conteneur;
	
	private static Scene scenePageDeGarde;
	
	private static Scene scenePageImporter;
	
	private static Scene scenePageImporterLocal;
	
	private static Scene scenePageImporterDistance;
	
	private static Scene scenePageExporter;
	
	private static Scene scenePageConsulter;
	
	private static Scene scenePageConsulterDonnees;
	
	private static FXMLLoader chargeurFxmlPageDeGarde = new FXMLLoader();
	
	private static FXMLLoader chargeurFxmlPageImporter = new FXMLLoader();
	
	private static FXMLLoader chargeurFxmlPageImporterLocal = new FXMLLoader();
	
	private static FXMLLoader chargeurFxmlPageImporterDistance = new FXMLLoader();
	
	private static FXMLLoader chargeurFxmlPageExporter = new FXMLLoader();
	
	private static FXMLLoader chargeurFxmlPageConsulter = new FXMLLoader();
	
	private static FXMLLoader chargeurFxmlPageConsulterDonnees = new FXMLLoader();
	
	@Override
	public void start(Stage primaryStage) {

		
		try {
		chargeurFxmlPageDeGarde.setLocation(getClass().getResource("/ihm/vuePageDeGarde.fxml"));
		conteneur = chargeurFxmlPageDeGarde.load();
		scenePageDeGarde = new Scene(conteneur);
		
		chargeurFxmlPageImporter.setLocation(getClass().getResource("/ihm/vueImporter.fxml"));
		conteneur = chargeurFxmlPageImporter.load();
		scenePageImporter = new Scene(conteneur);
		
		chargeurFxmlPageImporterLocal.setLocation(getClass().getResource("/ihm/vueImporterLocal.fxml"));
		conteneur = chargeurFxmlPageImporterLocal.load();
		scenePageImporterLocal = new Scene(conteneur);
		
		chargeurFxmlPageImporterDistance.setLocation(getClass().getResource("/ihm/vueImporterDistante.fxml"));
		conteneur = chargeurFxmlPageImporterDistance.load();
		scenePageImporterDistance = new Scene(conteneur);
		
		chargeurFxmlPageExporter.setLocation(getClass().getResource("/ihm/vueExporter.fxml"));
		conteneur = chargeurFxmlPageExporter.load();
		scenePageExporter = new Scene(conteneur);
		
		chargeurFxmlPageConsulter.setLocation(getClass().getResource("/ihm/vueConsulter.fxml"));
		conteneur = chargeurFxmlPageConsulter.load();
		scenePageConsulter = new Scene(conteneur);
		
		chargeurFxmlPageConsulterDonnees.setLocation(getClass().getResource("/ihm/vueConsulterDonnees.fxml"));
		conteneur = chargeurFxmlPageConsulterDonnees.load();
		scenePageConsulterDonnees = new Scene(conteneur);
	
		primaryStage.setTitle("PadeDeGarde");
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
	
	public static void setPageConsulterDonnees() {
		fenetrePrincipale.setScene(scenePageConsulterDonnees);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
