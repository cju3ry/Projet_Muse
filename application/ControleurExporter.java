package application;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

import gestion_donnees.Crypto;
import gestion_donnees.DonneesApplication;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

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
    public Button statistiques;

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

	@FXML
	private Button btnSauvegarder;

	@FXML
	private ImageView imageSpinner;

	private RotateTransition rotateTransition;


	/**
	 * Indique si les données ont été chargées et sauvegardées.
	 */
	private boolean donnesChargeesSauvegarder;

	/**
	 * Thread du serveur.
	 */
	private Thread serverThread;

	/**
	 * Socket du serveur.
	 */
	private ServerSocket serverSocket;

	@FXML
	void initialize() {
		imageSpinner.setVisible(false);
	}

@FXML
	void ecouterDemandeFichiers(ActionEvent event) {
		if (!ControleurImporterLocal.isDonneesConferencierChargees()
				&& !ControleurImporterLocal.isDonneesEmployesChargees()
				&& !ControleurImporterLocal.isDonneesExpositionsChargees()
				&& !ControleurImporterLocal.isDonneesVisitesChargees()
				&& !ControleurPageDeGarde.isDonneesSaveChargees()) {

			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Vous devez au préalable avoir importé les fichiers en local "
					+ "sur votre poste avant de pouvoir les exporter");
			alert.showAndWait();
		} else {
			labelEcouteLancee.setText("L'écoute est lancée");
			rotateTransition = new RotateTransition(Duration.seconds(1), imageSpinner);
			rotateTransition.setByAngle(360);
			rotateTransition.setCycleCount(Timeline.INDEFINITE);
			rotateTransition.setInterpolator(Interpolator.LINEAR);
			rotateTransition.play();			
			imageSpinner.setVisible(true);
			rotateTransition.play();
			rotateTransition.play();
			int port = 65412; // Port d'écoute
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

	/**
	 * Gère une requête de fichier entrante.
	 * @param socket le socket de connexion.
	 */
	private void handleRequest(Socket socket) {
		final String cheminFinal;
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8))) {
			// Lit la requête du client
			String requete = reader.readLine();
			String chemin = "";

			// Détermine le chemin du fichier à envoyer en fonction de la requête
			if ("employes".equals(requete)) {
				chemin = ControleurImporterLocal.cheminFichierEmployes;
			}

			// Détermine le chemin du fichier à envoyer en fonction de la requête
			if ("conferenciers".equals(requete)) {
				chemin = ControleurImporterLocal.cheminFichierConferenciers;
			}

			// Détermine le chemin du fichier à envoyer en fonction de la requête
			if ("expositions".equals(requete)) {
				chemin = ControleurImporterLocal.cheminFichierExpositions;
			}

			// Détermine le chemin du fichier à envoyer en fonction de la requête
			if ("visites".equals(requete)) {
				chemin = ControleurImporterLocal.cheminFichierVisites;
			}
			cheminFinal = chemin;
			System.out.print("le chemin final est " + cheminFinal);

			// Crée un CountDownLatch pour synchroniser l'envoi du fichier avec la réponse de l'utilisateur
			CountDownLatch latch = new CountDownLatch(1);

			// Affiche une alerte pour demander l'autorisation d'envoi
			Platform.runLater(() -> {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Demande de fichier");
				alert.setHeaderText("Demande de fichier reçue");
				alert.setContentText("Une demande de fichier \"" + cheminFinal + "\" a été reçue. Voulez-vous l'accepter ?");
				Optional<ButtonType> result = alert.showAndWait();

				// Si l'utilisateur accepte, on crypte et envoie le fichier
				if (result.get() == ButtonType.OK) {
					try {
						BufferedReader reader2 = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
						System.out.println("Demande aceptée par l'utilisateur.");

						// Creation d'un flux
						OutputStream output = socket.getOutputStream();

						// Envoie START pour indiquer le début de la communication
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

						// Envoi de p dans le flux
						output.write((p + "\n").getBytes(StandardCharsets.UTF_8));
						System.out.print("\np envoyé : " + p);

						// envoye de g dans le flux
						output.write((g + "\n").getBytes(StandardCharsets.UTF_8));
						System.out.print("\ng envoyé : " + g);

						// création et envoie de g^a dans le flux
						long gA = vigenere.genererGA(g,a,p);
						output.write((gA + "\n").getBytes(StandardCharsets.UTF_8));
						System.out.print("\ng ^ a envoyé : " + gA);

						// recoit g ^ b du client par le flux
						String gBstr = reader2.readLine();
						long gB = Long.parseLong(gBstr);
						System.out.println("\ng ^ b reçu du client : " + gB);

						// calcul de g ^ ab
						long cleCommune = vigenere.genererGAB(gB,a,p);
						System.out.print("\nLa clé commune pour serv est : " + cleCommune);
						vigenere.setCleCommune(cleCommune);
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
						// Envoyer le fichier crypté au client
						sendFile(socket, cheminFichierCrypte);
					} catch (IOException e) {
						System.err.println("Erreur lors de l'acceptation de la connexion : " + e.getMessage());
					}
					// Si l'utilisateur refuse, on envoie un message de refus
				} else {

					try {System.out.println("Demande refusée par l'utilisateur.");
					OutputStream output = socket.getOutputStream();
					output.write("REFUS".getBytes(StandardCharsets.UTF_8));
					output.flush();System.out.println("Demande refusée par l'utilisateur.");
					socket.close(); // Fermer la connexion si refusée
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
	/**
	 * Envoie un fichier au client.
	 * @param socket le socket de connexion.
	 * @param chemin le chemin du fichier à envoyer.
	 */
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

	/**
	 * Affiche l'adresse IP locale de la machine.
	 * @param event l'événement de clic sur le bouton.
	 */
	@FXML
	void afficherIp(ActionEvent event) {
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			StringBuilder ipAddresses = new StringBuilder();
			String fallBackIp = null;
			while (interfaces.hasMoreElements()) {
				NetworkInterface networkInterface = interfaces.nextElement();
				Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
				while (inetAddresses.hasMoreElements()) {
					InetAddress inetAddress = inetAddresses.nextElement();
					String ipAddress = inetAddress.getHostAddress();
					if (ipAddress.startsWith("10.")) {
						ipAddresses.append(ipAddress).append("\n");
					} else if (fallBackIp == null && !inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
						fallBackIp = ipAddress;
					}
				}
			}
			if (!ipAddresses.isEmpty()) {
				textAffichageIp.setText(ipAddresses.toString());
			} else if (fallBackIp != null) {
				textAffichageIp.setText(fallBackIp);
			} else {
				textAffichageIp.setText("Aucune adresse IP ne commence par 10.");
			}
		} catch (SocketException e) {
			Logger.getLogger(ControleurExporter.class.getName()).log(Level.SEVERE, "Erreur lors de la récupération de l'adresse IP", e);
		}
	}

	/**
	 * Arrête l'écoute des demandes de fichiers.
	 * @param event l'événement de clic sur le bouton.
	 */
	@FXML
	void arreterEcouter(ActionEvent event) {
		if (serverThread != null && serverThread.isAlive()) {
			try {
				if (serverSocket != null && !serverSocket.isClosed()) {
					serverSocket.close();
				}
				serverThread.interrupt();
				serverThread.join(); // Attend que le thread se termine
			} catch (IOException | InterruptedException e) {
				Logger.getLogger(ControleurExporter.class.getName()).log(Level.SEVERE, "Erreur lors de la fermeture du serveur", e);
			} finally {
				serverThread = null;
				labelEcouteLancee.setText("Aucune écoute en cours");
			}
		}
		
		if(rotateTransition != null) {
			rotateTransition.stop();
		}
		
		imageSpinner.setVisible(false);
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
    void statistiques(ActionEvent event) {
    	Main.setPageConsulterStatistiques();
    }

	@FXML
	void notice(ActionEvent event) {
		Main.afficherNotice();
	}

	@FXML
    void quitter(ActionEvent event) {
    	Main.quitterApllication();
    }

	@FXML
	void revenirArriere(ActionEvent event) {
		Main.setPageDeGarde();

	}

	@FXML
	void sauvegarder(ActionEvent event) {
		Main.sauvegarder();
	}
}