package application;

import gestion_donnees.DonneesApplication;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * La classe Vigenere implémente l'algorithme de chiffrement de Vigenère
 * en utilisant une clé générée via l'accord de clé Diffie-Hellman.
 */
public class Crypto {

    /**
     * Le nombre premier p.
     */
    private int p;

    /**
     * Le générateur g.
     */
    private int g;

    /**
     * Le nombre aléatoire d'Alice.
     */
    private int a;

    /**
     * Le nombre aléatoire de Bob.
     */
    private int b;

    /**
     * Le résultat de l'étape 1 pour Alice.
     */
    private long resultatAliceEtape1;

    /**
     * Le résultat de l'étape 1 pour Bob.
     */
    private long resulatBobEtape1;

    /**
     * Le résultat de l'étape 2 pour Alice.
     */
    private static long resultatAliceEtape2;

    /**
     * Le résultat de l'étape 2 pour Bob.
     */
    private static long resultatBobEtape2;

    /**
     * La clé commune partagée par Alice et Bob.
     */
    private long cleCommune;

    /**
     * Constructeur de la classe Crypto.
     *
     * @param borneMax la borne maximale pour générer un nombre premier.
     * @throws IllegalArgumentException si la borne maximale est inférieure à 2.
     */
    public Crypto(int borneMax) {
        this.p = genererPremier(borneMax);
        this.g = genererGenerateur(p);
    }


    /**
     * Constructeur vide de la classe Crypto.
     */
    public Crypto() {

    }

    /**
     * Recupère la valeur de p.
     * @return la valeur de p.
     */
    public int getP() {
        return p;
    }

    /**
     * Recupère la valeur de g.
     * @return la valeur de g.
     */
    public int getG() {
        return g;
    }

    /**
     * Recupère la valeur de resultatAliceEtape1.
     * @return la valeur de resultatAliceEtape1.
     */
    public long getResultatAliceEtape1() {
        return resultatAliceEtape1;
    }

    /**
     * Recupère la valeur de resulatBobEtape1.
     * @return la valeur de resulatBobEtape1.
     */
    public long getResulatBobEtape1() {
        return resulatBobEtape1;
    }

    /**
     * Génère la clé pour Alice.
     *
     * @param a le nombre aléatoire d'Alice.
     * @return la clé générée pour Alice.
     */
    public int genererCleAlice(int a) {
        this.a = a;
        this.resultatAliceEtape1 = exponentiationModulaire(g, a, p);
        return a;
    }

    /**
     * Génère la clé pour Bob.
     *
     * @param b le nombre aléatoire de Bob.
     * @return la clé générée pour Bob.
     */
    public int genererCleBob(int b) {
        this.b = b;
        this.resulatBobEtape1 = exponentiationModulaire(g, b, p);
        return b;
    }

    /**
     * Affecte la valeur de resultatAliceEtape2.
     * @return la valeur de resultatAliceEtape2.
     */
    public long setResultatAliceEtape2() {
        resultatAliceEtape2 = exponentiationModulaire((int) resulatBobEtape1, a, p);
        return resultatAliceEtape2;
    }

    /**
     * Affecte la valeur de resultatBobEtape2.
     * @return la valeur de resultatBobEtape2.
     */
    public long setResultatBobEtape2() {
        resultatBobEtape2 = exponentiationModulaire((int) resultatAliceEtape1, b, p);
        return resultatBobEtape2;
    }

    /**
     * Recupère la valeur de cleCommune.
     * @return la valeur de cleCommune.
     */
    public long getCleCommune() {
        return cleCommune;
    }

    /**
     * Permet de mettre une valeur dans la variable cleCommune.
     *
     * @return la valeur de cleCommune.
     */
    public long setCleCommune(long cleCommune) {
        this.cleCommune = cleCommune;
        return cleCommune;
    }

    /**
     * Génère un nombre premier inférieur ou égal à n.
     *
     * @param n la borne supérieure pour la génération du nombre premier.
     * @return le nombre premier généré.
     * @throws IllegalArgumentException si n est inférieur à 2.
     */
    public static int genererPremier(int n) {
        // Vérifie si n est inférieur à 2
        if (n < 2) {
            throw new IllegalArgumentException("Le nombre doit être supérieur à 2.");
        }
        // Boucle pour trouver le plus grand nombre premier inférieur ou égal à n
        while (n > 1) {
            // Vérifie si n est un nombre premier
            if (estPremier(n)) {
                return n;
            }
            n--;
        }
        // Retourne 2 si aucun nombre premier n'est trouvé (2 est le plus petit nombre premier)
        return 2;
    }

    /**
     * Vérifie si un nombre est premier.
     * @param n le nombre à vérifier.
     * @return true si le nombre est premier, false sinon.
     */
    public static boolean estPremier(int n) {
        // Vérifie si le nombre est inférieur ou égal à 1
        if (n <= 1) {
            return false;
        }
        // Parcours les nombres de 2 à la racine carrée de n
        for (int i = 2; i <= Math.sqrt(n); i++) {
            // Si n est divisible par i, n n'est pas premier
            if (n % i == 0) {
                return false;
            }
        }
        // Si aucune division n'a donné un reste de 0, n est premier
        return true;
    }

    /**
     * Vérifie si un nombre est un générateur pour un nombre premier donné p.
     *
     * @param p le nombre premier.
     * @return true si g est un générateur pour p, false sinon.
     * @throws IllegalArgumentException si aucun générateur n'est trouvé pour p.
     */
    public static int genererGenerateur(int p) {
        // Parcourt les nombres de p-1 à 2 pour trouver un générateur
        for (int g = p - 1; g >= 2; g--) {

            // Crée un ensemble pour stocker les résultats sans doublons.
            Set<Integer> set = new HashSet<>();

            // Calcule g^i mod p pour chaque i de 1 à p-1 et ajoute le résultat à l'ensemble
            for (int i = 1; i < p; i++) {
                set.add(exponentiationModulaire(g, i, p));
            }

            // Si l'ensemble contient p-1 éléments distincts, g est un générateur
            if (set.size() == p - 1) {
                return g;
            }
        }

        // Lance une exception si aucun générateur n'est trouvé
        throw new IllegalArgumentException("Aucun générateur trouvé pour " + p);
    }

    /**
     * Calcule l'exponentiation modulaire.
     *
     * @param base la base
     * @param exposant l'exposant
     * @param modulo le modulo
     * @return le résultat de l'exponentiation modulaire.
     */
    public static int exponentiationModulaire(int base, int exposant, int modulo) {
        int resultat = 1;
        base = base % modulo;

        // Boucle tant que l'exposant est supérieur à 0
        while (exposant > 0) {
            // Si l'exposant est impair, multiplie le résultat par la base et prend le modulo
            if ((exposant & 1) == 1) {
                resultat = (resultat * base) % modulo;
            }
            // Divise l'exposant par 2 en utilisant un décalage binaire à droite.
            exposant = exposant >> 1;

            // Multiplie la base par elle-même et prend le modulo
            base = (base * base) % modulo;
        }
        return resultat;
    }

    /**
     * Vérifie si un nombre est un générateur pour un nombre premier donné p.
     *
     * @param p le nombre premier.
     * @param generateur le générateur à vérifier.
     * @return true si le générateur est valide, false sinon.
     */
    public static boolean estGenerateur(int p, int generateur) {
        Set<Integer> set = new HashSet<>();
        for (int i = 1; i < p; i++) {
            set.add(exponentiationModulaire(generateur, i, p));
        }
        return set.size() == p - 1;
    }

    /**
     * Chiffre un message en utilisant l'algorithme de Vigenère.
     *
     * @param message le message à chiffrer.
     * @return le message chiffré.
     */
    public String chiffrerVigenere(String message) {
        StringBuilder resultat = new StringBuilder();
        String cle = Long.toString(cleCommune);
        int longueurCle = cle.length();

        // Parcourt chaque caractère du message
        for (int i = 0; i < message.length(); i++) {
            // Récupère le i-ème caractère du message
            char caractere = message.charAt(i);

            caractere = (char) ((caractere + cle.charAt(i % longueurCle)) % 256);
            resultat.append(caractere);
        }

        return resultat.toString();
    }

    /**
     * Déchiffre un message en utilisant l'algorithme de Vigenère.
     *
     * @param message le message à déchiffrer.
     * @return le message déchiffré.
     */
    public String dechiffrerVigenere(String message) {
        StringBuilder resultat = new StringBuilder();
        String cle = Long.toString(cleCommune);
        int longueurCle = cle.length();

        // Parcourt chaque caractère du message
        for (int i = 0; i < message.length(); i++) {
            // Récupère le i-ème caractère du message
            char caractere = message.charAt(i);
            // Déchiffre le caractère en utilisant la clé et prend le modulo 94 pour rester dans la plage des caractères imprimables (32-126)
            caractere = (char) ((caractere - cle.charAt(i % longueurCle) + 256) % 256);
            resultat.append(caractere);
        }
        return resultat.toString();
    }

    /**
     * Point d'entrée de l'application.
     *
     * @param args les arguments de la ligne de commande.
     * @throws IllegalArgumentException si la borne maximale est inférieure à 2.
     */
    public static void main(String[] args) {

        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Entrez la borne maximale pour générer un nombre premier : ");
            int borneMax = scanner.nextInt();

            Crypto vigenere = new Crypto(borneMax);
            System.out.println("Le nombre premier généré est : " + vigenere.getP());
            System.out.println("Le générateur généré est : " + vigenere.getG());

            System.out.print("Entrez le nombre entier a pour Alice : ");
            int a = scanner.nextInt();
            scanner.nextLine();
            vigenere.genererCleAlice(a);
            System.out.println("Alice a généré : " + vigenere.getResultatAliceEtape1());

            System.out.print("Entrez le nombre entier b pour Bob : ");
            int b = scanner.nextInt();
            scanner.nextLine();
            vigenere.genererCleBob(b);
            System.out.println("Bob a généré : " + vigenere.getResulatBobEtape1());

            resultatAliceEtape2 = vigenere.setResultatAliceEtape2();
            resultatBobEtape2 = vigenere.setResultatBobEtape2();

            if (resultatAliceEtape2 != resultatBobEtape2) {
                throw new IllegalArgumentException("Il y a eu un problème\nLe résultat d'Alice est : " + resultatAliceEtape2 + "\n Le résultat de Bob est : " + resultatBobEtape2);
            } else {
                System.out.println("Parfait, il ont tous les 2, ce résultat : " + resultatAliceEtape2);
            }

            System.out.println("La clé commune est : " + resultatAliceEtape2);
            vigenere.setCleCommune(resultatAliceEtape2);

            String message = String.valueOf(DonneesApplication.LireCsv("conferencierCrypte.csv"));
            //String messageChiffre = vigenere.chiffrerVigenere(message);
            //System.out.println("Le message chiffré est : " + messageChiffre);

            String messageDechiffre = vigenere.dechiffrerVigenere(message);
            System.out.println("Le message déchiffré est : " + messageDechiffre);

        } catch(IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}