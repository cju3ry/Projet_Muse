

import java.io.*;
import java.net.*;

public class server {
    public static void main(String[] args) {
        int port = 12345; // Port d'écoute

        try {
        	ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Serveur en attente de connexion...");
            
            try (Socket socket = serverSocket.accept()) {
                System.out.println("Connexion établie avec " + socket.getInetAddress());
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8")); 
                String chemin;
                String requete = reader.readLine();
                
                if (requete.equals("employe")
                	|| requete.equals("conferencier")
                	|| requete.equals("exposition")
                	|| requete.equals("visite")) {
                	chemin = requete + ".csv";
                } else {
                	throw new IOException();
                }
    			
    			// Flux d'entrée pour recevoir le fichier
                OutputStream output = socket.getOutputStream();
                BufferedOutputStream bufferedOutput = new BufferedOutputStream(output);
                FileInputStream fileInput = new FileInputStream(chemin);
                
                byte[] buffer = new byte[4096];
                int bytesRead;

                // Lire le flux et écrire dans le fichier
                while ((bytesRead = fileInput.read(buffer)) != -1) {
                	bufferedOutput.write(buffer, 0, bytesRead);
                }
                
                bufferedOutput.flush();
                System.out.println("Fichier envoyé avec succès !");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}