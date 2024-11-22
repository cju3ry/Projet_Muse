package application;

import gestion_donnees.DonneesApplication;
import gestion_donnees.FichierPdf;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;

public class ControlerConsulterDonneesEmploye {
	
	private DonneesApplication donnees;

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
    public Button statistiques;
    
    @FXML
	private Button genererPdf;

    @FXML
    private TextArea textAreaConsultation;

    private boolean donneesChargeesLocal; // Pour vérifier si les données sont déjà chargées en local

    private boolean donnesChargeesDistance; // Pour vérifier si les données sont déjà chargées a distance
    

    private boolean premierAffichageOk;

    private boolean donnesChargeesSauvegarder;

    // String pour le titre de la page
    private String titre;

    // Liste contenant les filtres appliqués
    private ArrayList<String> listeDesFiltres= new ArrayList<>();

    // Liste contenant le contenu du fichier
    private String contenuFichier;
    
    @FXML
    void initialize() {
        textAreaConsultation.setEditable(false);
        textAreaConsultation.setText("\n\n\n\n\n\n\n\n\n\n\n\t\t\t\t\t\t\t\t\t\t\tCliquez ici pour afficher les données.");

        premierAffichageOk = false;
        
        // Déclenchement de l'événement au clic sur la TextArea
        textAreaConsultation.setOnMouseClicked(event -> afficherDonnees());
    }
    
    // Méthode pour charger et afficher les données
    private void afficherDonnees() {
        donneesChargeesLocal = ControleurImporterLocal.isDonneesEmployesChargees();
        donnesChargeesDistance = ControleurImporterDistance.isDonneesEmployesChargees();
        donnesChargeesSauvegarder = ControleurPageDeGarde.isDonneesSaveChargees();
        StringBuilder strEmployesLocal = ControleurImporterLocal.getStrEmployes();
        StringBuilder strEmployesDistance = ControleurImporterDistance.getStrEmployes();
        StringBuilder strEmployesSave = ControleurPageDeGarde.getStrEmployes();
        
        if ((!donneesChargeesLocal || strEmployesLocal == null) && (!donnesChargeesDistance || strEmployesDistance == null) 
        	&& (!donnesChargeesSauvegarder || strEmployesSave == null ))  { // Vérifie si les données n'ont pas déjà été chargées en local et a distance
            textAreaConsultation.setText("Les données ne sont pas encore disponibles.");
        }
        
        if (donneesChargeesLocal && !premierAffichageOk) {
            textAreaConsultation.setText(ControleurImporterLocal.getStrEmployes().toString());
            donnees = ControleurImporterLocal.getDonnees();
            premierAffichageOk = true;
            contenuFichier = textAreaConsultation.getText();
        } else if (donnesChargeesDistance && !premierAffichageOk) {
            textAreaConsultation.setText(ControleurImporterDistance.getStrEmployes().toString());
            donnees = ControleurImporterDistance.getDonnees();
            premierAffichageOk = true;
            contenuFichier = textAreaConsultation.getText();
        } else if (donnesChargeesSauvegarder && !premierAffichageOk) {
        	 textAreaConsultation.setText(ControleurPageDeGarde.getStrEmployes().toString());
           donnees = ControleurPageDeGarde.getDonnees();
           premierAffichageOk = true;
            contenuFichier = textAreaConsultation.getText();
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
    void statistiques(ActionEvent event) {
    	Main.setPageConsulterStatistiques();
    }

    @FXML
    void quitter(ActionEvent event) {
    	Main.quitterApllication();
    }

    @FXML
    void revenirEnArriere(ActionEvent event) {
    	Main.setPageConsulter();
    }
    
    @FXML
	void sauvegarder(ActionEvent event) {
		Main.sauvegarder();
	}

    @FXML
    void genererPdf(ActionEvent event) {
        titre = "Employés";
        System.out.println("tire du pdf " + titre );
        System.out.println("conttenu du fichier" + contenuFichier);
        // Si aucun filtre n'a été appliqué, on ajoute dans la liste des filtres "Aucun filtre appliqué"
        if (listeDesFiltres.isEmpty()) {
            listeDesFiltres.add("Aucun filtre appliqué");
        }
        // Si le contenu afficher est null on affiche une boite d'alerte
        // pour informer l'utilisateur qu'il ne peut pas générer de fichier pdf
        if (contenuFichier == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Avertissement");
            alert.setHeaderText(null);
            alert.setContentText("Aucun résultat n'est disponible. Impossible de générer le PDF.");
            alert.showAndWait();
            return;
        }
        FichierPdf fichierPdf = new FichierPdf(titre, listeDesFiltres, contenuFichier);
        fichierPdf.genererPdf();
        System.out.println("Fichier pdf généré avec succès !");
        afficherPopUp("Fichier pdf généré avec succès !", event);
        listeDesFiltres.clear();
    }

    private void afficherPopUp(String message, Event event){
        // Fenetre popup pour informer l'utilisateur que l'action a été effectuée
        Stage popupStage = new Stage();
        popupStage.initOwner(((Node) event.getSource()).getScene().getWindow());
        popupStage.setX(((Node) event.getSource()).getScene().getWindow().getX() + 10);
        popupStage.setY(((Node) event.getSource()).getScene().getWindow().getY() + 10);
        popupStage.setResizable(false);
        Label messageLabel = new Label(message);
        messageLabel.setStyle("-fx-font-size: 16px; -fx-padding: 10px;");

        // Recupérer la fenêtre parente
        Stage parentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        double parentWidth = parentStage.getWidth();
        double parentHeight = parentStage.getHeight();

        double centerX = parentStage.getX() + 400;
        double centerY= parentStage.getY() + 20;
        // Set la position de la fenêtre popup
        popupStage.setX(centerX);
        popupStage.setY(centerY);

        // Créer une scène
        Scene scene = new Scene(new StackPane(messageLabel));
        popupStage.setScene(scene);
        popupStage.show();

        // Close the pop-up after 2 seconds
        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(e -> popupStage.close());
        delay.play();
    }

}
