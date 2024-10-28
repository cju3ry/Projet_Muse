
package crypto;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * La classe Vigenere implémente l'algorithme de chiffrement de Vigenère
 * en utilisant une clé générée via l'accord de clé Diffie-Hellman.
 */
public class Vigenere {

    /**
     * Comment faire pour réaliser les étapes de l'accord de clé Diffie-Hellman
     *
     * 1) Choix d'un nbre premier                               → Fait
     * 2) choisir un nombre générateur (voir le cours p.31)     s
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
        if (n < 2) {
            throw new IllegalArgumentException("Le nombre doit être supérieur à 2.");
        }
        while (n > 1) {
            if (estPremier(n)) {
                return n;
            }
            n--;
        }
        return 2; // Le plus petit nombre premier
    }

    /**
     * Vérifie si un nombre est premier.
     * @param n le nombre à vérifier.
     * @return true si le nombre est premier, false sinon.
     */
    private static boolean estPremier(int n) {
        if (n <= 1) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Vérifie si un nombre est un générateur pour un nombre premier donné p.
     *
     * @param g le nombre à vérifier.
     * @param p le nombre premier.
     * @return true si g est un générateur pour p, false sinon.
     */
    public static boolean estGenerateur(int g, int p) {
        Set<Integer> set = new HashSet<>();
        for (int i = 1; i < p; i++) {
            set.add((int) Math.pow(g, i) % p);
        }
        return set.size() == p - 1;
    }

    /**
     * Point d'entrée de l'application.
     *
     * @param args les arguments de la ligne de commande.
     */
    public static void main(String[] args) {

        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Borne Max : ");
            int borneMax = scanner.nextInt();
            int p = genererPremier(borneMax);
            int g = 3; // On choisit un générateur arbitrairement
            scanner.close();
        } catch(IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        /*
        int choix = 0;
        while (choix != 3) {
            System.out.println("---- Algorithme de Vigenère ----");
            System.out.println("1. Chiffrer un message");
            System.out.println("2. Déchiffrer un message");
            System.out.println("3. Quitter l'application");
            System.out.print("Votre choix : ");
            choix = scanner.nextInt();
            scanner.nextLine();
            if (choix == 1) {

            } else if (choix == 2) {

            } else if (choix == 3) {
                System.out.print("\nMerci d'avoir choisi notre application de chiffrement de Vigenère et de Diffie-Hillman.");
            } else {
                System.out.println("Choix invalide. Veuillez réessayer.\n");
            }
        }
        scanner.close();
        */
    }
}