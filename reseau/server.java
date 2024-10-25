package reseau;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

/**
 * La classe server représente un serveur réseau qui écoute les connexions entrantes,
 * reçoit une requête et envoie un fichier en réponse.
 */
public class server {
    public static void main(String[] args) {
        int port = 12345; // Port d'écoute

        try {
            // Création d'un ServerSocket pour écouter les connexions sur le port spécifié
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Serveur en attente de connexion...");

            // Acceptation d'une connexion entrante
            try (Socket socket = serverSocket.accept()) {
                System.out.println("Connexion établie avec " + socket.getInetAddress());
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                String chemin;
                String requete = reader.readLine();

                // Vérification de la requête et définition du chemin du fichier à envoyer
                if (requete.equals("employes")
                        || requete.equals("conferenciers")
                        || requete.equals("expositions")
                        || requete.equals("visites")) {
                    chemin = requete + ".csv";
                } else {
                    throw new IOException("Requête invalide");
                }

                // Flux de sortie pour envoyer le fichier
                OutputStream output = socket.getOutputStream();
                BufferedOutputStream bufferedOutput = new BufferedOutputStream(output);
                FileInputStream fileInput = new FileInputStream(chemin);

                byte[] buffer = new byte[4096];
                int bytesRead;

                // Lecture du fichier et envoi des données au client
                while ((bytesRead = fileInput.read(buffer)) != -1) {
                    bufferedOutput.write(buffer, 0, bytesRead);
                }

                // Vidage du buffer pour s'assurer que toutes les données sont envoyées
                bufferedOutput.flush();
                System.out.println("Fichier envoyé avec succès !");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}