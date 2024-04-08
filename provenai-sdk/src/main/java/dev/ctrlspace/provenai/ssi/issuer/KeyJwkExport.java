package dev.ctrlspace.provenai.ssi.issuer;


import dev.ctrlspace.provenai.utils.ContinuationObjectUtils;
import id.walt.crypto.keys.LocalKey;
import kotlin.coroutines.Continuation;

public class KeyJwkExport {
    Continuation<? super Object> continuation = ContinuationObjectUtils.createSuperContinuation();

    public String exportJWK(LocalKey localKey)  {
            return (String) localKey.exportJWK(continuation);
        }

        public String exportJWKObject(LocalKey localKey)  {
            String jwk = (String) localKey.exportJWK(continuation);
            return jwk;
        }
    }


