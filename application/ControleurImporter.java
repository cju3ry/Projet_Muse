package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ControleurImporter {

    @FXML
    private Button btnConsulter;

    @FXML
    private Button btnExporter;

    @FXML
    private Button btnImporter;

    @FXML
    private Button btnImporterDistance;

    @FXML
    private Button btnImporterLocal;

    @FXML
    private Button btnNotice;

    @FXML
    private Button btnQuitter;
    
    @FXML
    private Button btnRevenirArriere;
    
    @FXML
	private Button btnSauvegarder;
    
    @FXML
    public Button statistiques;
	
	@FXML
	void sauvegarder(ActionEvent event) {
		Main.sauvegarder();
	}
	
	@FXML
    void statistiques(ActionEvent event) {
    	Main.setPageConsulterStatistiques();
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
	    Main.afficherNotice();
    }

    @FXML
    void quitter(ActionEvent event) {
    	Main.quitterApllication();
    }
    
    @FXML
    void importerDistance(ActionEvent event) {
    	Main.setPageImporterDistance();
    }

    @FXML
    void importerLocal(ActionEvent event) {
    	Main.setPageImporterLocal();

    }
    
    @FXML
    void revenirArriere(ActionEvent event) {
    	Main.setPageDeGarde();
    }
}
