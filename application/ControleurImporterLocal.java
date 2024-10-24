package application;

import java.io.File;

import gestion_donnees.ConferencierException;
import gestion_donnees.DonneesApplication;
import gestion_donnees.EmployeException;
import gestion_donnees.ExpositionException;
import gestion_donnees.VisiteException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ControleurImporterLocal {
	
	public static String cheminFichierEmployes;
	
	public static String getCheminFichierEmployes() {
		return cheminFichierEmployes;
	}

	public String cheminFichierConferenciers;
	
	public String cheminFichierVisites;
	
	public String cheminFichierExpositions;

    @FXML
    private Button btnChoisirFichierConferenciers;

    @FXML
    private Button btnChoisirFichierEmployes;

    @FXML
    private Button btnChoisirFichierExpositions;

    @FXML
    private Button btnChoisirFichierVisites;

    @FXML
    private Button btnConsulter;

    @FXML
    private Button btnExporter;

    @FXML
    private Button btnImporter;

    @FXML
    private Button btnImporterFichiers;

    @FXML
    private Button btnNotice;

    @FXML
    private Button btnQuitter;

    @FXML
    private Button btnRevenirArriere;

    @FXML
    private Button checkExpositions;

    @FXML
    private Button checkFichierConferenciers;

    @FXML
    private Button choisirFicherVisites;

    @FXML
    private Button choisirFichierEmployes;

    @FXML
    void choisirFichierConferencier(ActionEvent event) {
    	File fichier = ouvrirFileChooser("Choisissez le fichier correspondant aux conférenciers");
    	 if (fichier != null) {
//    	        System.out.println("Fichier sélectionné : " + fichier.getAbsolutePath());
    	        cheminFichierConferenciers = fichier.getAbsolutePath();
    	        System.out.print("\ncheminFichierConferenciers : " + cheminFichierConferenciers);
    	        
    	    }
    }

    @FXML
    void choisirFichierEmployes(ActionEvent event) {
    	
    File fichier = ouvrirFileChooser("Choisissez le fichier correspondant aux employés");
   	if (fichier != null) {
//   	        System.out.println("Fichier sélectionné : " + fichier.getAbsolutePath());
   	        cheminFichierEmployes = fichier.getAbsolutePath();
   	        System.out.print("\ncheminFichierEmployes : " + cheminFichierEmployes);
   	    }
    }

    @FXML
    void choisirFichierExpositions(ActionEvent event) {

    File fichier = ouvrirFileChooser("Choisissez le fichier correspondant aux expositions");
   	if (fichier != null) {
//   	        System.out.println("Fichier sélectionné : " + fichier.getAbsolutePath());
   	        cheminFichierExpositions= fichier.getAbsolutePath();
   	        System.out.print("\ncheminFichierExpositions : " + cheminFichierExpositions);     
   	    }
    }

    @FXML
    void choisirFichierVisites(ActionEvent event) {
        File fichier = ouvrirFileChooser("Choisissez le fichier correspondant aux visites");
       	if (fichier != null) {
//       	        System.out.println("Fichier sélectionné : " + fichier.getAbsolutePath());
       	        cheminFichierVisites = fichier.getAbsolutePath();
       	        System.out.print("\ncheminFichierVisites : " + cheminFichierVisites);     
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
    void importerFichiers(ActionEvent event) {
    	DonneesApplication donnees = new DonneesApplication();
		
		try {
			donnees.importerEmployes(DonneesApplication.LireCsv(cheminFichierEmployes));
			donnees.importerExpositions(DonneesApplication.LireCsv(cheminFichierExpositions));
			donnees.importerConferenciers(DonneesApplication.LireCsv(cheminFichierConferenciers));
			donnees.importerVisites(DonneesApplication.LireCsv(cheminFichierVisites));
			System.out.print("Les fichiers sont importés dans l'application");
			Alert alerte = new Alert(AlertType.INFORMATION);
		    alerte.setTitle("Importation réussie");
		    alerte.setHeaderText("Importation des fichiers réussit");
		    alerte.setContentText("Les fichiers sont importés dans l'application");
		    alerte.showAndWait();
		} catch (EmployeException e) {
			
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExpositionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ConferencierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (VisiteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			System.out.print("Erreur avec le fichier des employes");
			Alert alerte = new Alert(AlertType.WARNING);
		    alerte.setTitle("Importation echouée");
		    alerte.setHeaderText("L'importation des fichiers a echouée");
		    alerte.setContentText("Les fichiers non pas été importés dans l'application");
		    alerte.showAndWait();
		}
		

    }

    @FXML
    void notice(ActionEvent event) {

    }

    @FXML
    void quitter(ActionEvent event) {
    	System.exit(0);
    }

    @FXML
    void revenirArriere(ActionEvent event) {
    	Main.setPageImporter();

    }
    
    private File ouvrirFileChooser(String titre) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("file chooser");

        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Fichiers Texte", "*.csv"),
            new FileChooser.ExtensionFilter("Tous les fichiers", "*.*")
        );

        Stage stage = (Stage) btnChoisirFichierConferenciers.getScene().getWindow(); 
        return fileChooser.showOpenDialog(stage);
    }

}
