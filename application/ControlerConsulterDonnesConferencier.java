package application;

import gestion_donnees.ConferencierException;
import gestion_donnees.DonneesApplication;
import gestion_donnees.ExpositionException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class ControlerConsulterDonnesConferencier {

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
			donnees1.importerConferenciers(DonneesApplication.LireCsv("conferenciers.csv"));
		} catch (ConferencierException e) {
			e.printStackTrace();
		}
		String listeConferencier = donnees1.getConferenciers().toString();
		System.out.print(listeConferencier.toString());
		textAreaConsultation.setText(listeConferencier.substring(1,listeConferencier.length()-1));
    
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
