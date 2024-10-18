package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ControleurConsulter {

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
    	System.exit(0);
    }

    @FXML
    void consulterConferencier(ActionEvent event) {
    	Main.setPageConsulterDonnees();
    }

    @FXML
    void consulterEmployes(ActionEvent event) {
    	Main.setPageConsulterDonnees();
    }

    @FXML
    void consulterExpositions(ActionEvent event) {
    	Main.setPageConsulterDonnees();

    }

    @FXML
    void consulterVisites(ActionEvent event) {
    	Main.setPageConsulterDonnees();
    }
    

    @FXML
    void revenirEnArriere(ActionEvent event) {
    	Main.setPageDeGarde();
    }

}
