package testsUnitaires;

import crypto.Crypto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestCrypto {

    private static Crypto crypto;

    @BeforeAll
    static void setUp() {
        crypto = new Crypto(100);
    }

    @Test
    void testCrypter() {
        String texte = "Bonjour";
        String texteCrypte = crypto.chiffrerVigenere(texte);
        assertNotEquals(texte, texteCrypte);
    }

    @Test
    void testDecrypter() {
        String texte = "Bonjour";
        String texteCrypte = crypto.chiffrerVigenere(texte);
        String texteDecrypte = crypto.dechiffrerVigenere(texteCrypte);
        assertEquals(texte, texteDecrypte);
    }
}