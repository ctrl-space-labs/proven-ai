package dev.ctrlspace.provenai.ssi;

import dev.ctrlspace.provenai.utils.ContinuationObjectUtils;
import id.walt.crypto.keys.KeyType;
import id.walt.crypto.keys.LocalKey;
import id.walt.crypto.keys.LocalKeyMetadata;
import id.walt.did.dids.DidService;
import id.walt.did.dids.registrar.DidResult;
import id.walt.did.dids.registrar.dids.DidKeyCreateOptions;
import id.walt.did.dids.registrar.dids.DidWebCreateOptions;
import id.walt.did.helpers.WaltidServices;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

public class DidIssuer {

        //  Kotlin Continuation Objects
        private Continuation<? super Object> continuationSuper;
        private Continuation<Unit> continuationPlain;

        public DidIssuer() {

                this.continuationSuper = ContinuationObjectUtils.createSuperContinuation();
                this.continuationPlain = ContinuationObjectUtils.createPlainContinuation();
                WaltidServices.INSTANCE.minimalInit(continuationPlain);
                WaltidServices.INSTANCE.init(continuationPlain);

        }

        // Method to create a DID using the did:key method
        public DidResult createDidFromKey(KeyType keyType) {
                LocalKey localKey = KeyCreation.generateKey(keyType);
                DidKeyCreateOptions options = new DidKeyCreateOptions();
                DidResult didResult = (DidResult) DidService.INSTANCE.registerByKey("key", localKey,options, continuationSuper);
                return  didResult;

        }



        // Method to create a DID using the did:web method
        public DidResult createDidFromWeb(String domain, String path, KeyType keyType) {
                DidWebCreateOptions options = new DidWebCreateOptions(domain, path, keyType);
                return (DidResult) DidService.INSTANCE.register(options, continuationSuper);
        }


        public LocalKey generateKey(KeyType keyType) {
                return (LocalKey) LocalKey.Companion.generate(keyType, new LocalKeyMetadata(),null);
        }



}
