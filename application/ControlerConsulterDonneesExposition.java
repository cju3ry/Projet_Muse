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
    
    private boolean donneesChargees; // Pour vérifier si les données sont déjà chargées

    @FXML
    void initialize() {
        textAreaConsultation.setEditable(false);
        textAreaConsultation.setText("\n\n\n\n\n\n\n\n\n\n\n\t\t\t\t\t\t\t\t\t\t\tCliquez ici pour afficher les données.");

        // Déclenchement de l'événement au clic sur la TextArea
        textAreaConsultation.setOnMouseClicked(event -> afficherDonnees());
    }
 // Méthode pour charger et afficher les données
    private void afficherDonnees() {
    	donneesChargees = ControleurImporterLocal.isDonneesExpositionsChargees();
        StringBuilder strExpositions = ControleurImporterLocal.getStrExpositions();
        if (!donneesChargees || strExpositions == null) { // Vérifie si les données n'ont pas déjà été chargées
               textAreaConsultation.setText("\n\n\tLes données ne sont pas encore disponibles.");
        }

        if (donneesChargees) {
        	textAreaConsultation.setText(ControleurImporterLocal.getStrExpositions().toString());
        }
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
