package application;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;


import gestion_donnees.DonneesApplication;
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
						BufferedReader reader2 = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
						System.out.println("Demande aceptée par l'utilisateur.");

						OutputStream output = socket.getOutputStream();
						output.write("START\n".getBytes(StandardCharsets.UTF_8));
						int min = 0;
						int max = 8000;
						int range = max - min + 1;
						int borneMax = (int) (Math.random() * range) + min;

						// Création de l'objet Crypto avec une borne max aléatoire
						Crypto vigenere = new Crypto(borneMax);
						int a = (int) (Math.random() * range) + min;
						int p = vigenere.getP();
						int g = vigenere.getG();

						// envoie de p
						output.write((p + "\n").getBytes(StandardCharsets.UTF_8));
						System.out.print("\np envoyé : " + p);

						// envoye de g
						output.write((g + "\n").getBytes(StandardCharsets.UTF_8));
						System.out.print("\ng envoyé : " + g);

						// création et envoie de g ^ a
						long gA = vigenere.genererGA(g,a,p);
						output.write((gA + "\n").getBytes(StandardCharsets.UTF_8));
						System.out.print("\ng ^ a envoyé : " + gA);

						// recoit g ^ b
						String gBstr = reader2.readLine();
						Long gB = Long.parseLong(gBstr);
						System.out.println("\ng ^ b reçu du client : " + gB);

						// calcul de g ^ ab
						long cleCommune = vigenere.genererGAB(gB,a,p);
						System.out.print("\nLa clé commune pour serv est : " + cleCommune);

						long gABserv;

						long gBAclient;
						String cleClient = reader2.readLine();
						long cleB  = Long.parseLong(cleClient);
						System.out.println("\nla clee du client : " + cleClient);

						gBAclient = cleB;

						gABserv = cleCommune;

						if (gABserv != gBAclient) {
							throw new IllegalArgumentException("\nIl y a eu un problème\nLe résultat du serv est : " + gABserv + "\n Le résultat du client est : " + gBAclient);
						} else {
							System.out.println("\nParfait, il ont tous les 2, ce résultat : " + gABserv);
						}
						System.out.println("\nLa clé commune est : " + gABserv);

						vigenere.setCleCommune(gABserv);
						output.flush();

						String message = String.valueOf(DonneesApplication.LireCsv(cheminFinal));

						String messageChiffre = vigenere.chiffrerVigenere(message);
						String cheminFichierCrypte = cheminFinal.replace(".csv", "Crypte.csv");

						// ecriture du fichier crypté
						try (BufferedWriter writer = new BufferedWriter(new FileWriter(cheminFichierCrypte))) {
							writer.write(messageChiffre);
							writer.flush();
						} catch (IOException e) {
							System.err.println("Erreur lors de l'écriture du fichier : " + e.getMessage());
						}
						output.write((vigenere.getCleCommune()+"\n").getBytes(StandardCharsets.UTF_8));
						output.flush();
						//sendFile(socket, cheminFinal); // sans cryptage
						sendFile(socket, cheminFichierCrypte); // Envoyer le fichier crypté si l'utilisateur accepte
					} catch (IOException e) {
						System.err.println("Erreur lors de l'acceptation de la connexion : " + e.getMessage());
					}

				} else {

					try {System.out.println("Demande refusée par l'utilisateur.");
						OutputStream output = socket.getOutputStream();
						output.write("REFUS".getBytes(StandardCharsets.UTF_8));
						output.flush();System.out.println("Demande refusée par l'utilisateur.");
						socket.close(); // Fermer la connexion si refusé
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