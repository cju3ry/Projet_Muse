package application;

import java.io.*;
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
        String ipPattern =
                "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        ipServ = textIpServ.getText();
        if (ipServ.matches(ipPattern)) {
            ipEstChoisit = true;
            mettreAJourEtatBtnDemande();
            System.out.print("L'ip du serveur est " + ipServ);
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText("Adresse IP invalide");
            alert.setContentText("Veuillez entrer une adresse IP valide.\nExemple : 192.168.1.22 " +
                    "\nLe format a respecter est : xxx.xxx.xxx.xxx");
            alert.showAndWait();
        }
    }

    @FXML

    void demanderFichier(ActionEvent event) {
        // Vérifier si le fichier demandé a déjà été importé
        if (("employes".equals(getRequest()) && donneesEmployesChargees) ||
                ("conferenciers".equals(getRequest()) && donneesConferencierChargees) ||
                ("expositions".equals(getRequest()) && donneesExpositionsChargees) ||
                ("visites".equals(getRequest()) && donneesVisitesChargees)) {

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setHeaderText("Fichier déjà importé");
            alert.setContentText("Le fichier \"" + fichierSelectionne + "\" a déjà été importé.");
            alert.showAndWait();
            return;
        }
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
                    Platform.runLater(() -> {
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setHeaderText("Demande envoyée");
                        alert.setContentText("Votre demande a été envoyée au serveur.");
                        alert.showAndWait();
                    });
                    String message = reader.readLine();
                    String cleCommune = reader.readLine();
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
                    	System.out.print("\nle serveur a accepté l'envoi");
                        System.out.print("\ncleCommune est " + cleCommune);
                        long cleCommuneLong = Long.parseLong(cleCommune);
                        System.out.print("le serveur a aceppeté l'envoi");
                        byte[] buffer = new byte[4096];
                        int bytesRead;

                        // Lecture et écriture des données reçues
                        while ((bytesRead = bufferedInput.read(buffer)) != -1) {
                            fileOutput.write(buffer, 0, bytesRead);
                        }

                      String messageCrypte = "";
						try (BufferedReader reader2 = new BufferedReader(new FileReader(getFileName()))) {
							StringBuilder sb = new StringBuilder();
							String line;
							while ((line = reader2.readLine()) != null) {
								sb.append(line);
							}
							messageCrypte = sb.toString();
						} catch (IOException e) {
							System.err.println("Erreur lors de la lecture du fichier crypté : " + e.getMessage());
						}

                        Crypto vigenere = new Crypto();
                        vigenere.setCleCommune(cleCommuneLong);
						String messageDechiffre = vigenere.dechiffrerVigenere(messageCrypte);
                        messageDechiffre = messageDechiffre.substring(1, messageDechiffre.length() - 1);
                        //messageDechiffre = messageDechiffre.replaceAll("(?<!;),", "");
                        messageDechiffre = messageDechiffre.replaceAll(",\\sN", "N");
                        messageDechiffre = messageDechiffre.replaceAll(",\\sC", "C");
                        messageDechiffre = messageDechiffre.replaceAll(",\\sE", "E");
                        messageDechiffre = messageDechiffre.replaceAll(",\\sR", "R");

                        System.out.print("\nLe message déchiffré est : " + messageDechiffre);
                        try (BufferedWriter writer2 = new BufferedWriter(new FileWriter(getFileName()))) {
                            writer2.write(messageDechiffre);
                            writer2.flush();
                        } catch (IOException e) {
                            System.err.println("Erreur lors de l'écriture du fichier déchiffré : " + e.getMessage());
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
                            alert.setHeaderText("Le fichier a été reçu et les données ont été importées avec succès.");
                            alert.showAndWait();
                        });
                    }


                } catch (IOException e) {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setHeaderText("Erreur de connexion");
                        alert.setContentText("Impossible de se connecter au serveur. Veuillez vérifier " +
                                "l'adresse IP et le port.\nVérifiez également que le serveur est en écoute.");
                        alert.showAndWait();
                    });
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
            Platform.runLater(() -> labelEmployesImporte.setText("Employés"));
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
            Platform.runLater(() -> labelVisitesImporte.setText("Visites"));
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
            Platform.runLater(() -> labelExpositionsImporte.setText("Expositions"));
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