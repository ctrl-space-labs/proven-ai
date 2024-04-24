package dev.ctrlspace.provenai.ssi.issuer;


import dev.ctrlspace.provenai.utils.ContinuationObjectUtils;
import id.walt.crypto.keys.KeyType;
import id.walt.crypto.keys.LocalKey;
import kotlin.Result;
import kotlin.coroutines.Continuation;

public class LocalKeyWrapper {
    private Continuation<? super Object> continuation;
    public LocalKeyWrapper() {
        this.continuation = ContinuationObjectUtils.createSuperContinuation();
    }

    public Object exportJWK(LocalKey localKey) {
        return  localKey.exportJWK(continuation);
    }

    public Object exportJWKObject(LocalKey localKey) {return localKey.exportJWK(continuation);}



    public LocalKey getPublicKey(LocalKey localKey) {
        return (LocalKey) localKey.getPublicKey(continuation);
    }
    public Object getKeyId(LocalKey localKey) { return  localKey.getKeyId(continuation);}

    public KeyType getKeyType(LocalKey localKey) {return localKey.getKeyType();}

    public String getJwk(LocalKey localKey) {return localKey.getJwk();}

    public Object exportPem(LocalKey localKey) {return  localKey.exportPEM(continuation);}

    public Object getPublicKeyRepresentation(LocalKey localKey) {return localKey.getPublicKeyRepresentation(continuation);}


    public Object getThumbprint(LocalKey localKey) {return localKey.getThumbprint(continuation);}

    public Object Boolean (LocalKey localKey) {return localKey.getHasPrivateKey();}

    public LocalKey getLocalKey(LocalKey localKey) {
        return localKey;
    }
}
