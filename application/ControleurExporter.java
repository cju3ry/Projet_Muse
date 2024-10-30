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

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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
	    private Button btnRevenirArriere;

	    @FXML
	    private Label textAffichageIp;
	    
	    @FXML
	    private Button btnEcouterDemandeFichiers;
	    
	    @FXML
	    private Label labelEcouteLancee;
	    
	    private Thread serverThread;
	    
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
				btnEcouterDemandeFichiers.setDisable(true);
				labelEcouteLancee.setText("L'écoute est lancée");
				int port = 12345; // Port d'écoute
				serverThread = new Thread(() -> {
					try (ServerSocket serverSocket = new ServerSocket(port)) {
						System.out.println("Serveur en attente de connexion...");
						while (true) {
							// Attente d'une connexion
							Socket socket = serverSocket.accept();
							System.out.println("Connexion établie avec " + socket.getInetAddress());
							// Lance un thread pour gérer la requête
							new Thread(() -> handleRequest(socket)).start();
						}
					} catch (IOException e) {
						e.printStackTrace();
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
//	            System.out.print("la requete est "+ requete);
//	            String chemin = requete + ".csv";
	            String chemin = "";
//	            System.out.print("Le chemin du fichier employé est : " + ControleurImporterLocal.cheminFichierEmployes);
	            if ("employes".equals(requete)) {
	            	chemin = ControleurImporterLocal.cheminFichierEmployes;
//	            	System.out.print("\nchemin dans le if " + chemin);
	            }
	            
	            if ("conferenciers".equals(requete)) {
	            	chemin = ControleurImporterLocal.cheminFichierConferenciers;
//	            	System.out.print("\nchemin dans le if " + chemin);
	            }
	            
	            if ("expositions".equals(requete)) {
	            	chemin = ControleurImporterLocal.cheminFichierExpositions;
//	            	System.out.print("\nchemin dans le if " + chemin);
	            }
	            
	            if ("visites".equals(requete)) {
	            	chemin = ControleurImporterLocal.cheminFichierVisites;
//	            	System.out.print("\nchemin dans le if " + chemin);
	            }
	            cheminFinal = chemin;
	            System.out.print("le chemin final est " + cheminFinal);
	            

	            // Crée un CountDownLatch pour synchroniser l'envoi du fichier avec la réponse de l'utilisateur
	            CountDownLatch latch = new CountDownLatch(1);

	            // Afficher une alerte pour demander l'autorisation d'envoi
	            Platform.runLater(() -> {
	                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
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
		                    btnEcouterDemandeFichiers.setDisable(false);
		                    labelEcouteLancee.setText("Aucune écoute en cours");
	                	} catch (IOException e) {
	                        e.printStackTrace();
	                    }
	                	
	                } else {
	                    
	                    try {
	                    System.out.println("Demande refusée par l'utilisateur.");
	                    OutputStream output = socket.getOutputStream();
	                    output.write("REFUS".getBytes(StandardCharsets.UTF_8));
	                    output.flush();
	                    System.out.println("Demande refusée par l'utilisateur.");
	                    btnEcouterDemandeFichiers.setDisable(false);
	                    labelEcouteLancee.setText("Aucune écoute en cours");
	                    socket.close(); // Fermer la connexion si refusé
	                    } catch (IOException e) {
	                        e.printStackTrace();
	                    }
	                }
	                latch.countDown(); // Libère le thread principal une fois la réponse donnée
	            });

	            // Attendre que l'utilisateur ait répondu
	            latch.await();
	            
	        } catch (IOException | InterruptedException e) {
	            e.printStackTrace();
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
	            e.printStackTrace();
	        } finally {
	            try {
	                socket.close(); // Fermer la connexion après l'envoi
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }

    @FXML
    void afficherIp(ActionEvent event) {
    	String adresseIPLocale ;

   	 try{
   	 InetAddress inetadr = InetAddress.getLocalHost();
   	 //adresse ip sur le réseau
   	 adresseIPLocale = (String) inetadr.getHostAddress();
   	 System.out.println("Adresse IP locale = "+adresseIPLocale );
   	 textAffichageIp.setText(adresseIPLocale);
   	 
   	 } catch (UnknownHostException e) {
   	 e.printStackTrace();
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
