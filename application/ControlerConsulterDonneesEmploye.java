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
import javafx.stage.FileChooser;
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
        Main.afficherNotice();
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

        if (listeDesFiltres.isEmpty()) {
            listeDesFiltres.add("Aucun filtre appliqué");
        }

        if (contenuFichier == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Avertissement");
            alert.setHeaderText(null);
            alert.setContentText("Aucun résultat n'est disponible. Impossible de générer le PDF.");
            alert.showAndWait();
            return;
        }

        // Créer une instance de FileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le fichier PDF");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf")
        );

        // Ouvre la boîte de dialogue de sauvegarde
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        java.io.File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            // Utilise l'emplacement choisi pour sauvegarder le PDF
            String filePath = file.getAbsolutePath();
            if (!filePath.endsWith(".pdf")) {
                filePath += ".pdf"; // Ajouter l'extension si elle est manquante
            }
            FichierPdf fichierPdf = new FichierPdf(titre, listeDesFiltres, contenuFichier);
            boolean success = fichierPdf.genererPdf(filePath); // Adaptez la méthode genererPdf pour accepter un chemin de fichier
            if (success) {
                System.out.println("Fichier PDF généré avec succès à : " + filePath);
                afficherPopUp("Fichier PDF généré avec succès à : " + filePath, event);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Une erreur est survenue lors de la génération du PDF.");
                alert.showAndWait();
            }
        } else {
            System.out.println("Sauvegarde annulée par l'utilisateur.");
        }
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

        // Recupére la fenêtre parente
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
        // Ferme la fenêtre après 2 secondes
        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(e -> popupStage.close());
        delay.play();
    }

}
