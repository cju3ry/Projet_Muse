package application;

import gestion_donnees.DonneesApplication;
import gestion_donnees.EmployeException;
import gestion_donnees.ExpositionException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class ControlerConsulterDonneesExposition {

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
			donnees1.importerExpositions(DonneesApplication.LireCsv("expositions.csv"));
		} catch (ExpositionException e) {
			e.printStackTrace();
		}
		String listeExpositions = donnees1.getExpositions().toString();
		System.out.print(listeExpositions.toString());
		textAreaConsultation.setText(listeExpositions.substring(1,listeExpositions.length()-1));
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
