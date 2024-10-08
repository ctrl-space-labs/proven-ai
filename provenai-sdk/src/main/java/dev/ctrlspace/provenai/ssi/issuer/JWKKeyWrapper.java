package dev.ctrlspace.provenai.ssi.issuer;


import dev.ctrlspace.provenai.utils.ContinuationObjectUtils;
import id.walt.crypto.keys.KeyType;
import id.walt.crypto.keys.jwk.JWKKey;
import kotlin.coroutines.Continuation;

/**
 * Wrapper class for JWKKey
 */
public class JWKKeyWrapper {


    private Continuation<? super Object> continuation;
    public JWKKeyWrapper() {
        this.continuation = ContinuationObjectUtils.createSuperContinuation();
    }

    public Object exportJWK(JWKKey jwkKey) {
        return  jwkKey.exportJWK(continuation);
    }

    /**
     * Method to export a LocalKey object to a JWK object
     * @param jwkKey
     * @return Object JWK
     */
    public Object exportJWKObject(JWKKey jwkKey) {return jwkKey.exportJWK(continuation);}

    /**
     * Method to get the public part of the key of a JWKKey object
     * @param jwkKey
     * @return LocalKey (public part)
     */
    public JWKKey getPublicKey(JWKKey jwkKey) {
        return (JWKKey) jwkKey.getPublicKey(continuation);
    }

    /**
     * Method to get the private part of the key of a JWKKey object
     * @param jwkKey
     * @return KeyId object
     */
    public Object getKeyId(JWKKey jwkKey) { return  jwkKey.getKeyId(continuation);}

    /**
     * Method to get the key type of JWKKey
     * @param jwkKey
     * @return KeyType object
     */
    public KeyType getKeyType(JWKKey jwkKey) {return jwkKey.getKeyType();}

   /**
     * Method to get the JWK representation of a JWKKey object
     * @param jwkKey
     * @return String JWK
     */
    public String getJwk(JWKKey jwkKey) {return jwkKey.getJwk();}

    /**
     * Method to get the PEM representation of a JWKKey object
     * @param jwkKey
     * @return String PEM
     */
    public Object exportPem(JWKKey jwkKey) {return  jwkKey.exportPEM(continuation);}

    /**
     * Method to get the public key representation of a JWKKey object
     * @param jwkKey
     * @return Object PublicKey
     */
    public Object getPublicKeyRepresentation(JWKKey jwkKey) {return jwkKey.getPublicKeyRepresentation(continuation);}

    /**
     * Method to get the thumbprint of a JWKKey object
     * @param jwkKey
     * @return Object Thumbprint
     */
    public Object getThumbprint(JWKKey jwkKey) {return jwkKey.getThumbprint(continuation);}

    /**
     * Method to get the key id of a JWKKey object
     * @param jwkKey
     * @return Object KeyId
     */
    public Object Boolean (JWKKey jwkKey) {return jwkKey.getHasPrivateKey();}

    /**
     * Method to get the key id of a JWKKey object
     * @param jwkKey
     * @return Object KeyId
     */
    public JWKKey getLocalKey(JWKKey jwkKey) {
        return jwkKey;
    }



}
