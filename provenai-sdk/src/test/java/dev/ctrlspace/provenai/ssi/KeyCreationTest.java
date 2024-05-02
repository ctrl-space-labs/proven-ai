package dev.ctrlspace.provenai.ssi;

import dev.ctrlspace.provenai.ssi.issuer.KeyCreation;
import id.walt.crypto.keys.KeyType;
import id.walt.crypto.keys.LocalKey;
import org.junit.Test;
import org.mockito.InjectMocks;

import java.util.logging.Logger;

/**
 * Mockito unit test for the KeyCreation class.
 */
public class KeyCreationTest {
    Logger logger = Logger.getLogger(KeyCreationTest.class.getName());

    @InjectMocks
    private KeyCreation keyCreation;


    @Test
    public void testGenerateKey() {


        LocalKey localKey = KeyCreation.generateKey(KeyType.RSA, 2048);


        // Verify the key is not null
        assert(localKey != null);
        logger.info("Key generated successfully");
        logger.info("Key: " + localKey);

    }

    @Test
    public void testGenerateKey_fail_with_small_length() {

        try {
            LocalKey localKey = KeyCreation.generateKey(KeyType.RSA, 512);
            //it should not reach here
            assert(false);
        } catch (IllegalArgumentException e) {
            logger.info("Key generation failed with small length");
            assert(true);
        }
    }

}
