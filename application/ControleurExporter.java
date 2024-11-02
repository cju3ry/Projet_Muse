package application;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

import crypto.Crypto;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

public class ControleurExporter {
    @FXML
    private Button btnAfficherIp;

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
    private Button btnRecupA;

    @FXML
    private Button btnRecupB;

    @FXML
    private Button btnRecupNombre;

    @FXML
    private Button btnRevenirArriere;

    @FXML
    private Button btnArreterEcouter;

    @FXML
    private Label textAffichageIp;

    @FXML
    private CheckBox cBCryptage;

    @FXML
    private TextField tfA;

    @FXML
    private TextField tfB;

    @FXML
    private TextField tfBorneMax;

    @FXML
    private Button btnEcouterDemandeFichiers;

    @FXML
    private Label labelEcouteLancee;

    @FXML
    private Button btnCleCommune;

    private Thread serverThread;

    private ServerSocket serverSocket;

    private Crypto vigenere;

    private int borneMax;

    private int a;

    private int b;

    private static long resultatAliceEtape2;

    private static long resultatBobEtape2;

    private long cleCommune;

    @FXML
    void initialize() {
        afficherCaseNombre(null);
        btnCleCommune.setDisable(true);
    }

    private void mettreAJourBoutonCleCommune() {
        btnCleCommune.setDisable(!cBCryptage.isSelected() || borneMax <= 0 || a <= 0 || b <= 0);
    }

    @FXML
    void afficherCaseNombre(ActionEvent event) {
        tfBorneMax.setVisible(cBCryptage.isSelected());
        btnRecupNombre.setVisible(cBCryptage.isSelected());
        tfA.setVisible(cBCryptage.isSelected());
        tfB.setVisible(cBCryptage.isSelected());
        btnRecupA.setVisible(cBCryptage.isSelected());
        btnRecupB.setVisible(cBCryptage.isSelected());
        btnCleCommune.setVisible(cBCryptage.isSelected());
    }

    @FXML
    int recupNombre(ActionEvent event) {
        try {
            borneMax = Integer.parseInt(tfBorneMax.getText());
            if (borneMax <= 0) {
                throw new NumberFormatException("La borne max doit être un entier positif");
            }

            if (borneMax > 100000) {
                throw new IllegalArgumentException("La borne max ne doit pas dépasser 100 000");
            }

            System.out.println("La borne max est : " + borneMax);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText("La borne max doit être un entier positif");
            alert.showAndWait();
            return 0;

        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText("La borne max ne doit pas dépasser 100 000");
            alert.showAndWait();
            return 0;
        }
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setHeaderText("Le nombre max est  : " + borneMax);
        alert.showAndWait();
        mettreAJourBoutonCleCommune();
        return borneMax;
    }

    @FXML
    int recupA(ActionEvent event) {
        try {
            a = Integer.parseInt(tfA.getText());
            if (a <= 0) {
                throw new NumberFormatException("La valeur de a doit être un entier positif");
            }
            System.out.println("La valeur de a est : " + a);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText("La valeur de a doit être un entier positif");
            alert.showAndWait();
            return a;
        }
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setHeaderText("La valeur de a est : " + a);
        alert.showAndWait();
        mettreAJourBoutonCleCommune();
        return a;
    }

    @FXML
    int recupB(ActionEvent event) {
        try {
            b = Integer.parseInt(tfB.getText());
            if (b <= 0) {
                throw new NumberFormatException("La valeur de b doit être un entier positif");
            }
            System.out.println("La valeur de b est : " + b);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText("La valeur de b doit être un entier positif");
            alert.showAndWait();
            return b;
        }
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setHeaderText("La valeur de b est : " + b);
        alert.showAndWait();
        mettreAJourBoutonCleCommune();
        return b;
    }

    private long genererCleCommune(int a, int b , int borneMax) {
        Crypto vigenere = new Crypto(borneMax);
        this.a = vigenere.genererCleAlice(a);
        this.b = vigenere.genererCleBob(b);
        resultatAliceEtape2 = vigenere.setResultatAliceEtape2();
        resultatBobEtape2 = vigenere.setResultatBobEtape2();
        return vigenere.setCleCommune(resultatAliceEtape2);
    }

    @FXML
    void genererCleCommune(ActionEvent actionEvent) {
        cleCommune = genererCleCommune(a, b, borneMax);
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setHeaderText("La clé commune est : " + cleCommune);
        alert.showAndWait();
    }

    @FXML
    void ecouterDemandeFichiers(ActionEvent event) {
        if (!ControleurImporterLocal.isDonneesConferencierChargees()
                || !ControleurImporterLocal.isDonneesEmployesChargees()
                || !ControleurImporterLocal.isDonneesExpositionsChargees()
                || !ControleurImporterLocal.isDonneesVisitesChargees()) {

            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText("Vous devez au préalable avoir importé les fichiers en local "
                    + "sur votre poste avant de pouvoir les exporter");
            alert.showAndWait();
        } else {
            labelEcouteLancee.setText("L'écoute est lancée");
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setHeaderText("L'écoute a été lancée");
            alert.showAndWait();
            int port = 12345; // Port d'écoute
            serverThread = new Thread(() -> {
                try (ServerSocket serverSocket = this.serverSocket = new ServerSocket(port)) {
                    System.out.println("Serveur en attente de connexion...");
                    while (true) {
                        // Attente d'une connexion
                        Socket socket = serverSocket.accept();
                        System.out.println("Connexion établie avec " + socket.getInetAddress());
                        // Lance un thread pour gérer la requête
                        new Thread(() -> handleRequest(socket)).start();
                    }
                } catch (IOException e) {
                    System.err.println("Erreur lors de l'acceptation de la connexion : " + e.getMessage());
                    //Mets un message plus robuste
                }
            });
            serverThread.setDaemon(true); // Permet de fermer le serveur en même temps que l'application
            serverThread.start();
        }
    }

    private void handleRequest(Socket socket) {
        final String cheminFinal;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8))) {
            String requete = reader.readLine();
//	        System.out.print("la requete est "+ requete);
//	        String chemin = requete + ".csv";
            String chemin = "";
//	        System.out.print("Le chemin du fichier employé est : " + ControleurImporterLocal.cheminFichierEmployes);
            if ("employes".equals(requete)) {
                chemin = ControleurImporterLocal.cheminFichierEmployes;
//	          	System.out.print("\nchemin dans le if " + chemin);
            }

            if ("conferenciers".equals(requete)) {
                chemin = ControleurImporterLocal.cheminFichierConferenciers;
//	           	System.out.print("\nchemin dans le if " + chemin);
            }

            if ("expositions".equals(requete)) {
                chemin = ControleurImporterLocal.cheminFichierExpositions;
//	   	    	System.out.print("\nchemin dans le if " + chemin);
            }

            if ("visites".equals(requete)) {
                chemin = ControleurImporterLocal.cheminFichierVisites;
//	           	System.out.print("\nchemin dans le if " + chemin);
            }
            cheminFinal = chemin;
            System.out.print("le chemin final est " + cheminFinal);

            // Crée un CountDownLatch pour synchroniser l'envoi du fichier avec la réponse de l'utilisateur
            CountDownLatch latch = new CountDownLatch(1);
            // Afficher une alerte pour demander l'autorisation d'envoi
            Platform.runLater(() -> {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Demande de fichier");
                alert.setHeaderText("Demande de fichier reçue");
                alert.setContentText("Une demande de fichier \"" + cheminFinal + "\" a été reçue. Voulez-vous l'accepter ?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK &&  result.isPresent()) {
                    try {
                        System.out.println("Demande aceptée par l'utilisateur.");
                        OutputStream output = socket.getOutputStream();
                        output.write("START\n".getBytes(StandardCharsets.UTF_8));
                        output.flush();
                        sendFile(socket, cheminFinal); // Envoyer le fichier si l'utilisateur accepte
                    } catch (IOException e) {
                        System.err.println("Erreur lors de l'acceptation de la connexion : " + e.getMessage());
                    }

                } else {

                    try {System.out.println("Demande refusée par l'utilisateur.");OutputStream output = socket.getOutputStream();output.write("REFUS".getBytes(StandardCharsets.UTF_8));output.flush();System.out.println("Demande refusée par l'utilisateur.");socket.close(); // Fermer la connexion si refusé
                    } catch (IOException e) {
                        Logger.getLogger(ControleurExporter.class.getName()).log(Level.SEVERE, "Erreur lors de l'acceptation de la connexion", e);
                    }
                }
                latch.countDown(); // Libère le thread principal une fois la réponse donnée
            });
            // Attendre que l'utilisateur ait répondu
            latch.await();

        } catch (IOException | InterruptedException e) {
            Logger.getLogger(ControleurExporter.class.getName()).log(Level.SEVERE, "Erreur lors de l'acceptation de la connexion", e);
        }
    }

    private void sendFile(Socket socket, String chemin) {
        try (OutputStream output = socket.getOutputStream();
             BufferedOutputStream bufferedOutput = new BufferedOutputStream(output);
             FileInputStream fileInput = new FileInputStream(chemin)) {
            byte[] buffer = new byte[4096];
            int bytesRead;

            // Lecture du fichier et envoi des données au client
            while ((bytesRead = fileInput.read(buffer)) != -1) {
                bufferedOutput.write(buffer, 0, bytesRead);
            }

            bufferedOutput.flush(); // Vidage du buffer pour s'assurer que toutes les données sont envoyées
            System.out.println("Fichier envoyé avec succès !");
        } catch (IOException e) {
            System.out.println("Erreur lors de l'envoi du fichier : " + e.getMessage());
            Logger.getLogger(ControleurExporter.class.getName()).log(Level.SEVERE, "Erreur lors de l'envoi du fichier", e);
        } finally {
            try {
                socket.close(); // Fermer la connexion après l'envoi
            } catch (IOException e) {
                Logger.getLogger(ControleurExporter.class.getName()).log(Level.SEVERE, "Erreur lors de la fermeture de la connexion", e);
            }
        }
    }

    @FXML
    void afficherIp(ActionEvent event) {
        String adresseIPLocale ;

        try {
            InetAddress inetadr = InetAddress.getLocalHost();
            //adresse ip sur le réseau
            adresseIPLocale = inetadr.getHostAddress();
            System.out.println("Adresse IP locale = "+adresseIPLocale );
            textAffichageIp.setText(adresseIPLocale);

        } catch (UnknownHostException e) {
            Logger.getLogger(ControleurExporter.class.getName()).log(Level.SEVERE, "Erreur lors de la récupération de l'adresse IP locale", e);
        }
    }

    @FXML
    void arreterEcouter(ActionEvent event) {
        if (serverThread != null && serverThread.isAlive()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                Logger.getLogger(ControleurExporter.class.getName()).log(Level.SEVERE, "Erreur lors de la fermeture du serveur", e);
            }
            serverThread.interrupt();
            serverThread = null;
            labelEcouteLancee.setText("Aucune écoute en cours");
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setHeaderText("L'écoute a été arrêtée");
            alert.showAndWait();
        }
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