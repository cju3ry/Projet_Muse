
package crypto;

import javax.crypto.KeyAgreement;
import javax.crypto.spec.DHParameterSpec;
import java.security.*;
import java.security.spec.InvalidParameterSpecException;
import java.util.Base64;
import java.util.Scanner;

/**
 * La classe Vigenere implémente l'algorithme de chiffrement de Vigenère
 * en utilisant une clé générée via l'accord de clé Diffie-Hellman.
 */
public class Vigenere {
    private String cle;

    /**
     * Constructeur de la classe Vigenere.
     * Génère une clé en utilisant l'accord de clé Diffie-Hellman.
     *
     * @throws NoSuchAlgorithmException si l'algorithme spécifié n'existe pas.
     * @throws InvalidAlgorithmParameterException si les paramètres de l'algorithme sont invalides.
     * @throws InvalidKeyException si la clé est invalide.
     * @throws InvalidParameterSpecException si les spécifications des paramètres sont invalides.
     */
    public Vigenere() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, InvalidParameterSpecException {
        genererCleSecrete();
    }

    /**
     * Comment faire pour réaliser les étapes de l'accord de clé Diffie-Hellman
     *
     * 1) Choix d'un nbre premier
     * 2) choisir un nombre générateur (voir le cours p.31)
     * 3) Choisis un entier a et transmets g^a  	→ Alice
     * 4) Envoie g^a					            → Alice
     * 5) Choisis un entier b et transmets g^b		→ Bob
     * 6) Envoie g^b					            → Bob
     * 7) Calcule g^ba 				                → Alice
     * 8) Calcule g^ba					            → Bob
     */

    /**
     * Génère un nombre premier grâce a la méthode du crible d'erathostene.
     *
     * @return le nombre premier généré.
     */
    private static int genererPremier(int n) {
        //TODO voir le crible d'erathostene
        if (n % n == 0 && n / 1 == n) {
            return n;
        } else {
            return genererPremier(n - 1);
        }
    }

    /**
     * Génère une clé en utilisant l'accord de clé Diffie-Hellman.
     */
    private void genererCleSecrete()  {

    }

    /**
     * Chiffre un message en utilisant l'algorithme de Vigenère.
     *
     * @param message le message à chiffrer.
     * @return le message chiffré.
     */
    public String chiffrer(String message) {
        return traiterMessage(message, true);
    }

    /**
     * Déchiffre un message en utilisant l'algorithme de Vigenère.
     *
     * @param message le message à déchiffrer.
     * @return le message déchiffré.
     */
    public String dechiffrer(String message) {
        return traiterMessage(message, false);
    }

    /**
     * Traite un message en le chiffrant ou en le déchiffrant.
     *
     * @param message le message à traiter.
     * @param chiffrer true pour chiffrer, false pour déchiffrer.
     * @return le message traité.
     */
    private String traiterMessage(String message, boolean chiffrer) {
        StringBuilder messageTraite = new StringBuilder();
        message = message.toUpperCase();
        String cleMaj = this.cle;

        for (int i = 0, j = 0; i < message.length(); i++) {
            char lettre = message.charAt(i);

            // Ignorer les espaces et les caractères non alphabétiques
            if (lettre < 'A' || lettre > 'Z') {
                messageTraite.append(lettre);
                continue;
            }

            int cleChar = cleMaj.charAt(j) - 'A';
            char lettreTraitee;
            if (chiffrer) {
                lettreTraitee = (char) (((lettre - 'A') + cleChar) % 26 + 'A');
            } else {
                lettreTraitee = (char) (((lettre - 'A') - cleChar + 26) % 26 + 'A');
            }
            messageTraite.append(lettreTraitee);

            j = (j + 1) % cleMaj.length();  // Avancer dans la clé et boucler si nécessaire
        }
        return messageTraite.toString();
    }

    /**
     * Point d'entrée de l'application.
     *
     * @param args les arguments de la ligne de commande.
     */
    public static void main(String[] args) {
        try {
            Vigenere vigenere = new Vigenere();
            Scanner scanner = new Scanner(System.in);

            // Choix de l'utilisateur : chiffrer ou déchiffrer
            int choix = 0;
            while (choix != 3) {
                System.out.println("---- Algorithme de Vigenère ----");
                System.out.println("1. Chiffrer un message");
                System.out.println("2. Déchiffrer un message");
                System.out.println("3. Quitter l'application");
                System.out.print("Votre choix : ");
                choix = scanner.nextInt();
                scanner.nextLine();  // Consommer la nouvelle ligne

                // Exécuter en fonction du choix de l'utilisateur
                if (choix == 1) {
                    System.out.print("Entrez le message : ");
                    String message = scanner.nextLine();
                    String messageChiffre = vigenere.chiffrer(message);
                    System.out.println("Message chiffré : " + messageChiffre + "\n");
                } else if (choix == 2) {
                    System.out.print("Entrez le message : ");
                    String message = scanner.nextLine();
                    String messageDechiffre = vigenere.dechiffrer(message);
                    System.out.println("Message déchiffré : " + messageDechiffre + "\n");
                } else if (choix == 3) {
                    System.out.print("\nMerci d'avoir choisi notre application de chiffrement de Vigenère et de Diffie-Hillman.");
                } else {
                    System.out.println("Choix invalide. Veuillez réessayer.\n");
                }
            }
            scanner.close();
        } catch (Exception e) {
            System.err.println("Une erreur s'est produite : " + e.getMessage());
        }
    }
}