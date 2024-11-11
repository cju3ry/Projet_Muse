package testUnitaires;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import application.*;

public class TestCrypto {

    private Crypto crypto;

    // Méthode d'initialisation exécutée avant chaque test
    @BeforeEach
    public void setUp() {
        // Initialisation de l'objet Crypto avec une borne de 100
        crypto = new Crypto(100); // Initialise l'objet avec une borne de 100
    }

    // Test du constructeur avec une borne valide pour la génération de p et g
    @Test
    public void testConstructeurAvecBorne() {
        // Vérifie que le nombre premier p est supérieur ou égal à 2
        assertTrue(crypto.getP() >= 2, "Le nombre premier p doit être supérieur ou égal à 2");
        // Vérifie que le générateur g est supérieur à 1
        assertTrue(crypto.getG() > 1, "Le générateur g doit être supérieur à 1");
    }

    // Test du constructeur sans paramètres
    @Test
    public void testConstructeurSansParametres() {
        // Crée un nouvel objet Crypto sans paramètres
        Crypto cryptoSansParam = new Crypto();
        // Vérifie que la valeur de p est initialisée à 0
        assertEquals(0, cryptoSansParam.getP(), "La valeur de p devrait être initialisée à 0");
    }

    // Test de la méthode estPremier
    @Test
    public void testEstPremier() {
        // Vérifie que le nombre 7 est reconnu comme un nombre premier
        assertTrue(Crypto.estPremier(7), "7 est un nombre premier");
        // Vérifie que le nombre 10 est reconnu comme non premier
        assertFalse(Crypto.estPremier(10), "10 n'est pas un nombre premier");
    }

    // Test de la méthode genererPremier
    @Test
    public void testGenererPremier() {
        // Vérifie que le plus grand nombre premier inférieur ou égal à 100 est bien 97
        assertEquals(97, Crypto.genererPremier(100), "Le plus grand nombre premier <= 100 est 97");
    }

    // Test de la méthode genererGenerateur
    @Test
    public void testGenererGenerateur() {
        // Génère un générateur pour le nombre premier 7
        int generateur = Crypto.genererGenerateur(7); // On attend un générateur de 7
        // Vérifie que le générateur est valide pour 7
        assertTrue(Crypto.estGenerateur(7, generateur), "Le générateur trouvé devrait être valide pour 7");
    }

    // Test de l'exponentiation modulaire
    @Test
    public void testExponentiationModulaire() {
        // Vérifie que l'exponentiation modulaire de 3^4 % 5 donne bien 4
        assertEquals(1, Crypto.exponentiationModulaire(3, 4, 5), "3^4 % 5 devrait être 4");
    }

    // Test de la méthode genererGA
    @Test
    public void testGenererGA() {
        // Génère gA à partir de g, de l'exposant 5 et de p
        long gA = crypto.genererGA(crypto.getG(), 5, crypto.getP());
        // Vérifie que gA est calculé correctement par rapport à l'exponentiation modulaire
        assertEquals(gA, Crypto.exponentiationModulaire(crypto.getG(), 5, crypto.getP()), "gA devrait être calculé correctement");
    }

    // Test de la méthode genererGB
    @Test
    public void testGenererGB() {
        // Génère gB à partir de g, de l'exposant 3 et de p
        long gB = crypto.genererGB(crypto.getG(), 3, crypto.getP());
        // Vérifie que gB est calculé correctement par rapport à l'exponentiation modulaire
        assertEquals(gB, Crypto.exponentiationModulaire(crypto.getG(), 3, crypto.getP()), "gB devrait être calculé correctement");
    }

    // Test de la méthode genererGAB
    @Test
    public void testGenererGAB() {
        // Génère gB à partir de g, de l'exposant 3 et de p
        long gB = crypto.genererGB(crypto.getG(), 3, crypto.getP());
        // Génère gAB à partir de gB, de l'exposant 5 et de p
        long gAB = crypto.genererGAB(gB, 5, crypto.getP());
        // Vérifie que gAB est calculé correctement par rapport à l'exponentiation modulaire
        assertEquals(gAB, Crypto.exponentiationModulaire((int) gB, 5, crypto.getP()), "gAB devrait être calculé correctement");
    }

    // Test de la méthode genererGBA
    @Test
    public void testGenererGBA() {
        // Génère gA à partir de g, de l'exposant 5 et de p
        long gA = crypto.genererGA(crypto.getG(), 5, crypto.getP());
        // Génère gBA à partir de gA, de l'exposant 3 et de p
        long gBA = crypto.genererGBA(gA, 3, crypto.getP());
        // Vérifie que gBA est calculé correctement par rapport à l'exponentiation modulaire
        assertEquals(gBA, Crypto.exponentiationModulaire((int) gA, 3, crypto.getP()), "gBA devrait être calculé correctement");
    }

    // Test du chiffrement Vigenère
    @Test
    public void testChiffrerVigenere() {
        // Initialisation de la clé commune pour le test
        crypto.setCleCommune(123456);
        // Message à chiffrer
        String message = "HELLO";
        // Chiffre le message avec Vigenère
        String messageChiffre = crypto.chiffrerVigenere(message);
        // Vérifie que le message chiffré est différent du message initial
        assertNotEquals(message, messageChiffre, "Le message chiffré doit être différent du message initial");
    }

    // Test du déchiffrement Vigenère
    @Test
    public void testDechiffrerVigenere() {
        // Initialisation de la clé commune pour le test
        crypto.setCleCommune(123456);
        // Message à chiffrer
        String message = "HELLO";
        // Chiffre le message
        String messageChiffre = crypto.chiffrerVigenere(message);
        // Déchiffre le message
        String messageDechiffre = crypto.dechiffrerVigenere(messageChiffre);
        // Vérifie que le message déchiffré correspond au message initial
        assertEquals(message, messageDechiffre, "Le message déchiffré doit correspondre au message initial");
    }

    // Test des méthodes setCleCommune et getCleCommune
    @Test
    public void testSetEtGetCleCommune() {
        // Définit une clé commune pour le test
        crypto.setCleCommune(78910);
        // Vérifie que la clé commune est correctement définie et récupérée
        assertEquals(78910, crypto.getCleCommune(), "La clé commune devrait être correctement définie et récupérée");
    }
}
