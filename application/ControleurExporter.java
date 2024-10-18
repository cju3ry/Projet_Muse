package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ControleurExporter {

	@FXML
    private Button btnAfficherIp;

    @FXML
    private Button btnChoisirFichierConferencier;

    @FXML
    private Button btnChoisirFichierEmployes;

    @FXML
    private Button btnChoisirFichierExpositions;

    @FXML
    private Button btnChoisirFichierVisites;

    @FXML
    private Button btnConsulter;

    @FXML
    private Button btnEntrerIp;

    @FXML
    private Button btnExporter;

    @FXML
    private Button btnExporterFichiers;

    @FXML
    private Button btnImporter;

    @FXML
    private Button btnNotice;

    @FXML
    private Button btnQuitter;

    @FXML
    private Button btnRevenirArriere;

    @FXML
    private Button checkConferencier;

    @FXML
    private Button checkEmployes;

    @FXML
    private Button checkExpositions;

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
    void choisirFichierExpositions(ActionEvent event) {

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
    void exporterFichiers(ActionEvent event) {

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
    void revenirArriere(ActionEvent event) {
    	Main.setPageDeGarde();

    }

}
