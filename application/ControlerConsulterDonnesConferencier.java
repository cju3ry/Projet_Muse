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
	private Button btnSauvegarder;

    @FXML
    private TextArea textAreaConsultation;
    
    private boolean donneesChargeesLocal; // Pour vérifier si les données sont déjà chargées en local

    private boolean donnesChargeesDistance; // Pour vérifier si les données sont déjà chargées a distance
    
    private boolean donnesChargeesSauvegarder;
    
    
    
    @FXML
    void initialize() {
        textAreaConsultation.setEditable(false);
        textAreaConsultation.setText("\n\n\n\n\n\n\n\n\n\n\n\t\t\t\t\t\t\t\t\t\t\tCliquez ici pour afficher les données.");

        // Déclenchement de l'événement au clic sur la TextArea
        textAreaConsultation.setOnMouseClicked(event -> afficherDonnees());
    }
 // Méthode pour charger et afficher les données
    private void afficherDonnees() {
    	donneesChargeesLocal = ControleurImporterLocal.isDonneesConferencierChargees();
        donnesChargeesDistance = ControleurImporterDistance.isDonneesConferencierChargees();
        donnesChargeesSauvegarder = ControleurPadeDeGarde.isDonneesSaveChargees();
        StringBuilder strConferencierLocal = ControleurImporterLocal.getStrConferencier();
        StringBuilder strConferencierDistance = ControleurImporterDistance.getStrConferencier();
        StringBuilder strConferencierSave = ControleurPadeDeGarde.getStrConferencier();
        System.out.print("Donnes Charge distance" + donnesChargeesDistance);
        if (donneesChargeesLocal) {
            textAreaConsultation.setText(ControleurImporterLocal.getStrConferencier().toString());
        } else if (donnesChargeesDistance) {
            textAreaConsultation.setText(ControleurImporterDistance.getStrConferencier().toString());
        } else if (donnesChargeesSauvegarder) {
	       	 textAreaConsultation.setText(ControleurPadeDeGarde.getStrConferencier().toString());
	    }
        if ((!donneesChargeesLocal || strConferencierLocal == null) && (!donnesChargeesDistance || strConferencierDistance == null)
        		&& !donnesChargeesSauvegarder || strConferencierSave == null )  { // Vérifie si les données n'ont pas déjà été chargées en local et a distance
               textAreaConsultation.setText("Les données ne sont pas encore disponibles.");
        }

    }
    
	
	@FXML
	void sauvegarder(ActionEvent event) {
		Main.sauvegarder();
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
