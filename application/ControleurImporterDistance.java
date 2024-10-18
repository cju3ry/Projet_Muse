package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ControleurImporterDistance {

    @FXML
    private Button btnAfficherIp;

    @FXML
    private Button btnConferencier;

    @FXML
    private Button btnConsulter;

    @FXML
    private Button btnEmployes;

    @FXML
    private Button btnEntrerIp;

    @FXML
    private Button btnExporter;

    @FXML
    private Button btnExposition;

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
    private Button btnVisites;

    @FXML
    private Button checkConferencier;

    @FXML
    private Button checkEmployes;

    @FXML
    private Button checkExpositions;

    @FXML
    private Button checkIp;

    @FXML
    private Button checkVisites;

    @FXML
    void afficherIp(ActionEvent event) {

    }

    @FXML
    void choisirFichierConferencier(ActionEvent event) {
    	
    }

    @FXML
    void choisirFichierEmployes(ActionEvent event) {

    }

    @FXML
    void choisirFichierExposition(ActionEvent event) {

    }

    @FXML
    void choisirFichierVisites(ActionEvent event) {

    }

    @FXML
    void consulter(ActionEvent event) {
    	Main.setPageConsulter();
    }

    @FXML
    void entrerIp(ActionEvent event) {

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
    void importerFichier(ActionEvent event) {

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

}
