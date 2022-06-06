package pl.application.passwordwallet.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;


@Test
class AESencTest {
    @Autowired
    AESenc aeSenc;

    @DataProvider(name = "testEncryptionDataProvider")
    public static Object[][] passwordsAndKeys(){
        return new Object[][] {{"Ukryty tekst", "Abcdefg123"}, {"abcd", "Qwerty1998"}, {"klucz", "Zxcv0"}};
    }

    @Test
    void shouldEncryptAndDecryptCorrectly(String secretMessage, String secretKey) throws Exception {
        String encryptedText = AESenc.encrypt(secretMessage, AESenc.generateKey(secretKey));
        String decryptedText = AESenc.decrypt(encryptedText, AESenc.generateKey(secretKey));
        assertEquals(secretMessage, decryptedText);
    }

}