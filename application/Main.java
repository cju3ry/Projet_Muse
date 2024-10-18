package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class Main extends Application {
	
	private static Stage fenetrePrincipale;
	
	private static Parent conteneur;
	
	private static Scene scenePageDeGarde;
	
	private static Scene scenePageConsulter;
	
	private static Scene scenePageConsulterDonnees;
	
	private static FXMLLoader chargeurFxmlPageDeGarde = new FXMLLoader();
	
	private static FXMLLoader chargeurFxmlPageConsulter = new FXMLLoader();
	
	private static FXMLLoader chargeurFxmlPageConsulterDonnees = new FXMLLoader();

	
	@Override
	public void start(Stage primaryStage) {

		
		try {
		chargeurFxmlPageDeGarde.setLocation(getClass().getResource("/vueIhm/PageDeGarde.fxml"));
		conteneur = chargeurFxmlPageDeGarde.load();
		scenePageDeGarde = new Scene(conteneur);
		
		chargeurFxmlPageConsulter.setLocation(getClass().getResource("/vueIhm/PageConsulter.fxml"));
		conteneur = chargeurFxmlPageConsulter.load();
		scenePageConsulter = new Scene(conteneur);
		
		chargeurFxmlPageConsulterDonnees.setLocation(getClass().getResource("/vueIhm/PageConsulterDonnees.fxml"));
		conteneur = chargeurFxmlPageConsulterDonnees.load();
		scenePageConsulterDonnees = new Scene(conteneur);
		
		primaryStage.setTitle("DATA BRIDGE");
		primaryStage.setScene(scenePageDeGarde);
		fenetrePrincipale = primaryStage;
		primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void setPageDeGarde() {
		fenetrePrincipale.setScene(scenePageDeGarde);
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
