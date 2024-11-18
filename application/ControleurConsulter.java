package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class ControleurConsulter {
	
	public String elementAAfficher;

	@FXML
    private Button btnConferencier;

    @FXML
    private Button btnConsulter;

    @FXML
    private Button btnEmployes;

    @FXML
    private Button btnExporter;

    @FXML
    private Button btnExpositions;

    @FXML
    private Button btnImporter;

    @FXML
    private Button btnNotice;

    @FXML
    private Button btnQuitter;

    @FXML
    private Button btnRevenir;

    @FXML
    private Button btnVisites;
    
    @FXML
	private Button btnSauvegarder;
    
    @FXML
    public Button statistiques;
    
    @FXML
	void sauvegarder(ActionEvent event) {
		Main.sauvegarder();
	}
    
    
    @FXML
    void importer(ActionEvent event) {
    	Main.setPageImporter();
    }
    
    @FXML
    void exporter(ActionEvent event) {
    	Main.setPageExporter();
    }
    
    @FXML
    void consulter(ActionEvent event) {
    	Main.setPageConsulter();
    }
    
    @FXML
    void notice(ActionEvent event) {
    	// A définir
    }
    
    @FXML
    void quitter(ActionEvent event) {
    	Main.quitterApllication();
    }
    
    @FXML
    void statistiques(ActionEvent event) {
    	Main.setPageConsulterStatistiques();
    }

    @FXML
    void consulterConferencier(ActionEvent event) {
    	Main.setPageConsulterDonneesConferencier();

    }

    @FXML
    void consulterEmployes(ActionEvent event) {
    	Main.setPageConsulterDonneesEmploye();
    }

    @FXML
    void consulterExpositions(ActionEvent event) {

    	Main.setPageConsulterDonneesExposition();

    }

    @FXML
    void consulterVisites(ActionEvent event) {
    	Main.setPageConsulterDonneesVisite();
    }
    

    @FXML
    void revenirEnArriere(ActionEvent event) {
    	Main.setPageDeGarde();
    }
    

}
