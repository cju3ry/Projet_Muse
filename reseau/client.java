

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class client {
	public static void main(String[] args) {
		String serverAddress = "10.2.19.59"; // Adresse IP du serveur
		int port = 12345; // Port du serveur

		try {
			Socket socket = new Socket(serverAddress, port);
			InputStream input = socket.getInputStream();
	        BufferedInputStream bufferedInput = new BufferedInputStream(input);
	        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
	        FileOutputStream fileOutput = new FileOutputStream("received_file.csv");
	        
	        Scanner entree = new Scanner(System.in);
			String requete;  
			
			
			requete = "visite";
			
			writer.println(requete);
	        
			byte[] buffer = new byte[4096];
			int bytesRead;

			while ((bytesRead = bufferedInput.read(buffer)) != -1) {
                fileOutput.write(buffer, 0, bytesRead);
            }
			
			System.out.println("Fichier reçu avec succès !");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

