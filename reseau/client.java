package reseau;

import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * La classe client représente un client réseau qui se connecte à un serveur,
 * envoie une requête et reçoit un fichier en réponse.
 */
public class client {
	public static void main(String[] args) {
		String serverAddress = "10.109.0.106"; // Adresse IP du serveur
		int port = 12345; // Port du serveur

		try {
			// Création d'une connexion socket avec le serveur
			Socket socket = new Socket(serverAddress, port);
			InputStream input = socket.getInputStream();
			BufferedInputStream bufferedInput = new BufferedInputStream(input);
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
			FileOutputStream fileOutput = new FileOutputStream("received_file.csv");

			Scanner entree = new Scanner(System.in);
			String requete;

			// Envoi de la requête "visite" au serveur
			requete = "expositions";
			writer.println(requete);

			byte[] buffer = new byte[4096];
			int bytesRead;

			// Lecture des données reçues du serveur et écriture dans un fichier
			while ((bytesRead = bufferedInput.read(buffer)) != -1) {
				fileOutput.write(buffer, 0, bytesRead);
			}

			System.out.println("Fichier reçu avec succès !");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}