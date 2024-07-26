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
     * Method to get the public part of the key of a LocalKey object
     * @param jwkKey
     * @return LocalKey (public part)
     */
    public JWKKey getPublicKey(JWKKey jwkKey) {
        return (JWKKey) jwkKey.getPublicKey(continuation);
    }

    /**
     * Method to get the private part of the key of a LocalKey object
     * @param jwkKey
     * @return KeyId object
     */
    public Object getKeyId(JWKKey jwkKey) { return  jwkKey.getKeyId(continuation);}

    /**
     * Method to get the key type of LocalKey
     * @param jwkKey
     * @return KeyType object
     */
    public KeyType getKeyType(JWKKey jwkKey) {return jwkKey.getKeyType();}

    public String getJwk(JWKKey jwkKey) {return jwkKey.getJwk();}

    public Object exportPem(JWKKey jwkKey) {return  jwkKey.exportPEM(continuation);}

    public Object getPublicKeyRepresentation(JWKKey jwkKey) {return jwkKey.getPublicKeyRepresentation(continuation);}


    public Object getThumbprint(JWKKey jwkKey) {return jwkKey.getThumbprint(continuation);}

    public Object Boolean (JWKKey jwkKey) {return jwkKey.getHasPrivateKey();}

    public JWKKey getLocalKey(JWKKey jwkKey) {
        return jwkKey;
    }



}
