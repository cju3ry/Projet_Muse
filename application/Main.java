package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

		primaryStage.setTitle("Data Bridge");
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

	public static void main(String[] args) {
		launch(args);
	}
	public static void afficherNotice() {
		VBox vbox = new VBox();
		vbox.setSpacing(10);

		String[] images = {"/imagesNotice/NoticeIntro.PNG", "/imagesNotice/Importer_1.PNG","/imagesNotice/Importer_1.PNG",
				           "/imagesNotice/Importer_2.PNG","/imagesNotice/Importer_3.PNG","/imagesNotice/Importer_4.PNG" ,
				           "/imagesNotice/Importer_5.PNG", "/imagesNotice/Consulter_1.PNG", "/imagesNotice/Consulter_2.PNG",
				           "/imagesNotice/Consulter_3.PNG","/imagesNotice/Consulter_4.PNG"};
		int i = 0;
		for (String imagePath : images) {
			Image image = new Image(images[i]);
			ImageView imageView = new ImageView(image);
			imageView.setPreserveRatio(true);
			imageView.setFitWidth(600); 
			vbox.getChildren().add(imageView);
			i++;
		}


		ScrollPane scrollPane = new ScrollPane(vbox);
		scrollPane.setFitToWidth(true);
		scrollPane.setPrefViewportHeight(400); 


		scrollPane.viewportBoundsProperty().addListener((observable, oldValue, newValue) -> {
			vbox.getChildren().stream()
			.filter(node -> node instanceof ImageView)
			.map(node -> (ImageView) node)
			.forEach(imageView -> imageView.setFitWidth(newValue.getWidth()));
		});

		Stage stage = new Stage();
//		stage.initModality(Modality.APPLICATION_MODAL); // bloque et fait en sorte qu'on ne puisse pas cliquer sur la fentre principage
		stage.initStyle(StageStyle.DECORATED);
		stage.setHeight(700);
		stage.setWidth(1000);
		stage.setTitle("Notice");

		Scene scene = new Scene(scrollPane);
		stage.setScene(scene);

		stage.showAndWait();
	}

}
