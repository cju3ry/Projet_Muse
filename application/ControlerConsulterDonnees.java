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
    private TextArea textAreaConsultation;

    @FXML
    void consulter(ActionEvent event) {
    	Main.setPageConsulter();
    }

    @FXML
    void exporter(ActionEvent event) {

    }

    @FXML
    void importer(ActionEvent event) {

    }

    @FXML
    void notice(ActionEvent event) {

    }

    @FXML
    void quitter(ActionEvent event) {
    	System.exit(1);
    }

    @FXML
    void revenirEnArriere(ActionEvent event) {
    	Main.setPageConsulter();
    }

}
