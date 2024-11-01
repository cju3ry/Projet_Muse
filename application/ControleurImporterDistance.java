package application;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import gestion_donnees.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class ControleurImporterDistance {
	
	
	private static boolean ipEstChoisit = false;
	
	private static boolean fichierEstChoisit = false;
	
	private String fichierSelectionne;
	
	private String fichierRecu = "";
	
	private String ipServ;

    private static StringBuilder strConferencier;

    private  static StringBuilder strEmployes;

    private  static StringBuilder strExpositions;

    private  static StringBuilder strVisites;

    private static boolean donneesConferencierChargees = false;

    private static boolean donneesEmployesChargees = false;

    private static boolean donneesExpositionsChargees = false;

    private static boolean donneesVisitesChargees = false;

    private String cheminFichierConferenciers;

    private String cheminFichierEmployes;

    private String cheminFichierExpositions;

    private String cheminFichierVisites;

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
    private Button btnRevenirArriere;
    
    @FXML
    private Label labelFichierSelectionne;

    @FXML
    private ComboBox<String> conboBoxFichier;
    
    @FXML
    private Button btnDemanderFichier;
    
    @FXML
    private Button btnOkIP;


    @FXML
    private Label labelConferencierImporte;

    @FXML
    private Label labelEmployesImporte;

    @FXML
    private Label labelExpositionsImporte;


    @FXML
    private Label labelVisitesImporte;
    

    @FXML
    private TextField textIpServ;

    public static StringBuilder getStrConferencier() {
        return strConferencier;
    }
    public static StringBuilder getStrEmployes() {
        return strEmployes;
    }

    public static StringBuilder getStrExpositions() {
        return strExpositions;
    }

    public static StringBuilder getStrVisites() {
        return strVisites;
    }
    public static boolean isDonneesConferencierChargees() {
        return donneesConferencierChargees;
    }
    public static boolean isDonneesEmployesChargees() {
        return donneesEmployesChargees;
    }

    public static boolean isDonneesExpositionsChargees() {
        return donneesExpositionsChargees;
    }

    public static boolean isDonneesVisitesChargees() {
        return donneesVisitesChargees;
    }

    private DonneesApplication donnees = new DonneesApplication();
    public void initialize() {
    	btnDemanderFichier.setDisable(true);
        // Ajouter les options au ComboBox
        conboBoxFichier.getItems().clear();
        conboBoxFichier.getItems().addAll("Selectionner le fichier", "Employés", "Conférenciers", "Expositions", "Visites");
        conboBoxFichier.getSelectionModel().select("Selectionner le fichier");
        conboBoxFichier.setOnAction(event -> {
            String selectedFile = conboBoxFichier.getSelectionModel().getSelectedItem();
            if (!selectedFile.equals("Selectionner le fichier")) {
                labelFichierSelectionne.setText(selectedFile);
                fichierSelectionne = selectedFile;
                System.out.print(fichierSelectionne);
                fichierEstChoisit = true;
                mettreAJourEtatBtnDemande();
            } else {
                labelFichierSelectionne.setText(""); 
            }
        });
    }
    
    private void mettreAJourEtatBtnDemande() {
    	btnDemanderFichier.setDisable(!(ipEstChoisit && fichierEstChoisit));
    }
    

    
    @FXML
    void recupIp(ActionEvent event) {
    	ipServ = textIpServ.getText();
    	ipEstChoisit = true;
    	mettreAJourEtatBtnDemande();
    	System.out.print("L'ip du sreveur est " + ipServ);
    }
    
    @FXML

    void demanderFichier(ActionEvent event) {
        //si l'utilisateur n'a pas choisi les fichiers des conférenciers, employés et expositions avant visites il ne peut pas importer les visites
        if ("visites".equals(getRequest()) && !verifierImportationsPrealables()) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setHeaderText("Importation impossible");
            alert.setContentText("Vous devez d'abord importer les fichiers des conférenciers, employés et expositions.");
            alert.showAndWait();
            return;
        }
        // Créer un Task pour l'importation
        Task<Void> importTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
            	//TODO faire la verification du format de l'adresse ip et faire un traitement si jamais ce n'est pas la bonne car cela va lever une ecxption
                
            	//TODO faire en sorte de verfier que si la connection est refusée l'utilisateur soit averti
            	String serverAddress = ipServ; 
                int port = 12345;

                try (Socket socket = new Socket(serverAddress, port);
                     InputStream input = socket.getInputStream();
                     BufferedInputStream bufferedInput = new BufferedInputStream(input);
                     PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                	 BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                     FileOutputStream fileOutput = new FileOutputStream(getFileName())) {

                    // Envoi de la requête au serveur
                    writer.println(getRequest());
                    String message = reader.readLine();
                    System.out.println("Message reçu du serveur : " + message);
                    if ("REFUS".equals(message)) {
                        System.out.println("Le serveur a refusé l'envoi du fichier");
                        Platform.runLater(() -> {
                            Alert alert = new Alert(AlertType.ERROR);
                            alert.setHeaderText("Le serveur a refusé l'envoi du fichier");
                            alert.showAndWait();
                        });
                        fileOutput.close();
                        Files.deleteIfExists(Paths.get(getFileName()));
                        
                        
                    }
                    if ("START".equals(message)) {
                    System.out.print("le serveur a aceppeté l'envoi");
                    byte[] buffer = new byte[4096];
                    int bytesRead;

                    // Lecture et écriture des données reçues
                    while ((bytesRead = bufferedInput.read(buffer)) != -1) {
                        fileOutput.write(buffer, 0, bytesRead);
                    }

                    System.out.println("Fichier reçu avec succès !");
                    //importerFichierConferenciers();
                        System.out.println("avant avoir appelé importerFichierSelonSelection()");
                        System.out.print("le nom du fichier requete est " + getRequest());
                        fichierRecu = getRequest();
                        importerFichierSelonSelection();
                        System.out.println("Après avoir appelé importerFichierSelonSelection()");
                        Platform.runLater(() -> {
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setHeaderText("Le fichier a été reçu");
                        alert.showAndWait();
                    });
    				//TODO rajouter la posibilité d'ouvrir un filechooser pour voir ou le fichier c'est importé
                    }

                   
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        // Lancer le Task dans un nouveau thread
        new Thread(importTask).start();
    }
    private boolean verifierImportationsPrealables() {
        return donneesConferencierChargees && donneesEmployesChargees && donneesExpositionsChargees;
    }
    private void importerFichierSelonSelection() {
        System.out.println("Début de importerFichierSelonSelection");
        System.out.print("le nom du fichier est " + fichierRecu);
        switch (fichierRecu) {
            case "employes":
                System.out.println("Appel de importerFichierEmployes");
                importerFichierEmployes();
                break;
            case "conferenciers":
                System.out.println("Appel de importerFichierConferenciers");
                importerFichierConferenciers();
                break;
            case "expositions":
                System.out.println("Appel de importerFichierExpositions");
                importerFichierExpositions();
                break;
            case "visites":
                System.out.println("Appel de importerFichierVisites");
                importerFichierVisites();
                break;
            default:
                System.out.println("Fichier non reconnu");
                break;
        }
        System.out.println("Fin de importerFichierSelonSelection");
    }

    // Méthode pour obtenir la requête en fonction du fichier sélectionné
    private String getRequest() {
        switch (fichierSelectionne) {
            case "Employés": return "employes";
            case "Conférenciers": return "conferenciers";
            case "Expositions": return "expositions";
            case "Visites": return "visites";
            default: return "";
        }
    }

    // Méthode pour obtenir le nom du fichier de sortie en fonction du fichier sélectionné
    private String getFileName() {
        String userHome = System.getProperty("user.home");
        String downloadDir = userHome + "/Downloads/";
        switch (fichierSelectionne) {
            case "Employés": return downloadDir + "employesRecu.csv";
            case "Conférenciers": return downloadDir + "conferenciersRecu.csv";
            case "Expositions": return downloadDir + "expositionsRecu.csv";
            case "Visites": return downloadDir + "visitesRecu.csv";
            default: return downloadDir + "fichierRecu.csv";
        }
    }


    void importerFichierConferenciers() {

        strConferencier = new StringBuilder();
        String userHome = System.getProperty("user.home");
        cheminFichierConferenciers = userHome + "/Downloads/conferenciersRecu.csv";
        try {
            donnees.importerConferenciers(DonneesApplication.LireCsv(cheminFichierConferenciers));
            donneesConferencierChargees = true;
            Platform.runLater(() -> labelConferencierImporte.setText("Conférenciers"));
            ArrayList<Conferencier> listeDesConfernciers = donnees.getConferenciers();
            strConferencier.append("\n");
            for (int i = 0; i < listeDesConfernciers.size(); i++) {
                strConferencier.append(listeDesConfernciers.get(i).toString() + "\n");
            }
        } catch (IllegalArgumentException e) {
            Alert alerteNok = new Alert(AlertType.WARNING);
            alerteNok.setTitle("Importation échouée");
            alerteNok.setHeaderText("L'importation du fichier des conférenciers a échoué");
            alerteNok.setContentText(e.getMessage());
            alerteNok.showAndWait();
        }
    }
    void importerFichierEmployes() {
        strEmployes = new StringBuilder();
        String userHome = System.getProperty("user.home");
        cheminFichierEmployes = userHome + "/Downloads/employesRecu.csv";
        try {
            donnees.importerEmployes(DonneesApplication.LireCsv(cheminFichierEmployes));
            donneesEmployesChargees = true;
            ArrayList<Employe> listeDesEmployes = donnees.getEmployes();
            strEmployes.append("\n");
            for (int i = 0; i < listeDesEmployes.size(); i++) {
                strEmployes.append(listeDesEmployes.get(i).toString() + "\n");
            }
        } catch (IllegalArgumentException e) {
            Alert alerteNok = new Alert(AlertType.WARNING);
            alerteNok.setTitle("Importation échouée");
            alerteNok.setHeaderText("L'importation du fichier des employés a échoué");
            alerteNok.setContentText(e.getMessage());
            alerteNok.showAndWait();
        }
    }
    void importerFichierVisites() {


        strVisites = new StringBuilder();
        String userHome = System.getProperty("user.home");
        cheminFichierVisites = userHome + "/Downloads/visitesRecu.csv";
        try {
            donnees.importerVisites(DonneesApplication.LireCsv(cheminFichierVisites));
            donneesVisitesChargees = true;
            ArrayList<Visite> listeDesVisites = donnees.getVisites();
            strVisites.append("\n");
            for (int i = 0; i < listeDesVisites.size(); i++) {
                strVisites.append(listeDesVisites.get(i).toString() + "\n");
            }
        } catch (IllegalArgumentException e) {
            Alert alerteNok = new Alert(AlertType.WARNING);
            alerteNok.setTitle("Importation échouée");
            alerteNok.setHeaderText("L'importation du fichier des visites a échoué");
            alerteNok.setContentText(e.getMessage());
            alerteNok.showAndWait();
        }
    }
    void importerFichierExpositions() {
        strExpositions = new StringBuilder();
        String userHome = System.getProperty("user.home");
        String cheminFichierExpositions = userHome + "/Downloads/expositionsRecu.csv";
        try {
            donnees.importerExpositions(DonneesApplication.LireCsv(cheminFichierExpositions));
            donneesExpositionsChargees = true;
            ArrayList<Exposition> listeDesExpositions = donnees.getExpositions();
            strExpositions.append("\n");
            for (int i = 0; i < listeDesExpositions.size(); i++) {
                strExpositions.append(listeDesExpositions.get(i).toString() + "\n");
            }
        } catch (IllegalArgumentException e) {
            Alert alerteNok = new Alert(AlertType.WARNING);
            alerteNok.setTitle("Importation échouée");
            alerteNok.setHeaderText("L'importation du fichier des expositions a échoué");
            alerteNok.setContentText(e.getMessage());
            alerteNok.showAndWait();
        }
    }

    
    @FXML
    void importer(ActionEvent event) {
    	Main.setPageImporter();
    }

    @FXML
    void importerFichier(ActionEvent event) {

    }
    @FXML
    void exporter(ActionEvent event) {
    	Main.setPageExporter();
    }
    @FXML
    void notice(ActionEvent event) {
    	Main.afficherNotice();
    }
    @FXML
    void consulter(ActionEvent event) {
    	Main.setPageConsulter();
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
