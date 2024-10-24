package crypto;

import javax.crypto.KeyAgreement;
import javax.crypto.spec.DHParameterSpec;
import java.security.*;
import java.security.spec.InvalidParameterSpecException;
import java.util.Base64;
import java.util.Scanner;

public class Vigenere {
    private String cle;

    public Vigenere() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, InvalidParameterSpecException {
        genererCle();
    }

    private void genererCle() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, InvalidParameterSpecException {
        // Paramètres Diffie-Hellman
        int tailleCle = 2048;
        AlgorithmParameterGenerator algoParamgen = AlgorithmParameterGenerator.getInstance("DH");
        algoParamgen.init(tailleCle);
        AlgorithmParameters algoParams = algoParamgen.generateParameters();
        DHParameterSpec dhParamSpec = algoParams.getParameterSpec(DHParameterSpec.class);

        // Générer la paire de clés pour Alice
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("DH");
        keyPairGen.initialize(dhParamSpec);
        KeyPair aliceKeyPair = keyPairGen.generateKeyPair();

        // Générer la paire de clés pour Bob
        KeyPair bobKeyPair = keyPairGen.generateKeyPair();

        // Accord de clé entre Alice et Bob
        KeyAgreement aliceKeyAgree = KeyAgreement.getInstance("DH");
        aliceKeyAgree.init(aliceKeyPair.getPrivate());
        aliceKeyAgree.doPhase(bobKeyPair.getPublic(), true);

        KeyAgreement bobKeyAgree = KeyAgreement.getInstance("DH");
        bobKeyAgree.init(bobKeyPair.getPrivate());
        bobKeyAgree.doPhase(aliceKeyPair.getPublic(), true);

        // Générer le secret partagé
        byte[] aliceSharedSecret = aliceKeyAgree.generateSecret();
        byte[] bobSharedSecret = bobKeyAgree.generateSecret();

        // Utiliser le secret partagé comme clé
        String sharedSecret = Base64.getEncoder().encodeToString(aliceSharedSecret).toUpperCase();
        this.cle = sharedSecret.replaceAll("[^A-Z]", "");
        System.out.println(this.cle);
    }

    public String chiffrer(String message) {
        return traiterMessage(message, true);
    }

    public String dechiffrer(String message) {
        return traiterMessage(message, false);
    }

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