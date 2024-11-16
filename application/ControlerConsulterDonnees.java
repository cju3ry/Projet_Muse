package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class ControlerConsulterDonnees {
	
    @FXML
    private Button btnConsulter;

    @FXML
    private Button btnExporter;

    @FXML
    private Button btnImporter;

    @FXML
    private Button btnNotice;

    @FXML
    private Button btnQuitter;

    @FXML
    private Button btnRevenir;
    
    @FXML
	private Button btnSauvegarder;

    @FXML
    public TextArea textAreaConsultation;
    
    @FXML
	void sauvegarder(ActionEvent event) {
		Main.sauvegarder();
	}
    
    @FXML
    public TextArea getTextAreaConsultation() {
		return textAreaConsultation;
	}

	@FXML
    void initialize() {
    	textAreaConsultation.setEditable(false);
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
    	Main.quitterApllication();
    }

    @FXML
    void revenirEnArriere(ActionEvent event) {
    	Main.setPageConsulter();
    }

    
}
