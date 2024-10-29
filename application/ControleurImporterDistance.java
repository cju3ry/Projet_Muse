package application;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ControleurImporterDistance {
	
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
            } else {
                labelFichierSelectionne.setText(""); 
            }
        });
    }
    

    
    @FXML
    void recupIp(ActionEvent event) {
    	ipServ = textIpServ.getText();
    	System.out.print("L'ip du sreveur est " + ipServ);
    }
    
    @FXML
    void demanderFichier(ActionEvent event) {
        // Créer un Task pour l'importation
        Task<Void> importTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                String serverAddress = ipServ; 
                int port = 12345;

                try (Socket socket = new Socket(serverAddress, port);
                     InputStream input = socket.getInputStream();
                     BufferedInputStream bufferedInput = new BufferedInputStream(input);
                     PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                     FileOutputStream fileOutput = new FileOutputStream(getFileName())) {

                    // Envoi de la requête au serveur
                    writer.println(getRequest());

                    byte[] buffer = new byte[4096];
                    int bytesRead;

                    // Lecture et écriture des données reçues
                    while ((bytesRead = bufferedInput.read(buffer)) != -1) {
                        fileOutput.write(buffer, 0, bytesRead);
                    }

                    System.out.println("Fichier reçu avec succès !");
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
