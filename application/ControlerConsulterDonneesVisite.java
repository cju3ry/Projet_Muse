package application;

import gestion_donnees.ConferencierException;
import gestion_donnees.DonneesApplication;
import gestion_donnees.EmployeException;
import gestion_donnees.ExpositionException;
import gestion_donnees.VisiteException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class ControlerConsulterDonneesVisite {

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
    private TextArea textAreaConsultation;

    @FXML
    void initialize() {
    	textAreaConsultation.setEditable(false);

    	DonneesApplication donnees1 = new DonneesApplication();
    	
    	try {
    		//TODO modifier pour passer le chemin choisit par l'utilisateur 
			donnees1.importerEmployes(DonneesApplication.LireCsv("employes.csv"));
		} catch (EmployeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	try {
    		//TODO modifier pour passer le chemin choisit par l'utilisateur 

			donnees1.importerExpositions(DonneesApplication.LireCsv("expositions.csv"));
		} catch (ExpositionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	try {
    		//TODO modifier pour passer le chemin choisit par l'utilisateur 
			donnees1.importerConferenciers(DonneesApplication.LireCsv("conferenciers.csv"));
		} catch (ConferencierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	try {
    		//TODO modifier pour passer le chemin choisit par l'utilisateur 
			donnees1.importerVisites(DonneesApplication.LireCsv("visites.csv"));
		} catch (VisiteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EmployeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String listeVisites = donnees1.getVisites().toString();
//		System.out.print(listeVisites.toString());
		textAreaConsultation.setText(listeVisites.substring(1,listeVisites.length()-1));
    
    
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

}
