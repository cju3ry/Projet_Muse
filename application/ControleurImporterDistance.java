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
import java.util.Scanner;

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
	
	private String fichierRecu;
	
	private String ipServ;

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
    private TextField textIpServ;
    
    
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
        switch (fichierSelectionne) {
            case "Employés": return "employesRecu.csv";
            case "Conférenciers": return "conferenciersRecu.csv";
            case "Expositions": return "expositionsRecu.csv";
            case "Visites": return "visitesRecu.csv";
            default: return "fichierRecu.csv";
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
