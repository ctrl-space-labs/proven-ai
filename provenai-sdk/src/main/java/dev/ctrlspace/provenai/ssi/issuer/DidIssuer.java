package dev.ctrlspace.provenai.ssi.issuer;

import dev.ctrlspace.provenai.utils.ContinuationObjectUtils;
import id.walt.crypto.keys.Key;
import id.walt.crypto.keys.KeyType;
import id.walt.crypto.keys.LocalKey;
import id.walt.crypto.keys.LocalKeyMetadata;
import id.walt.did.dids.DidService;
import id.walt.did.dids.document.DidDocument;
import id.walt.did.dids.registrar.DidResult;
import id.walt.did.dids.registrar.LocalRegistrar;
import id.walt.did.dids.registrar.dids.DidKeyCreateOptions;
import id.walt.did.dids.registrar.dids.DidWebCreateOptions;
import id.walt.did.dids.resolver.DidResolver;
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

        }

        /**
         * Method to create a DID using the did:key method
         * @param keyType algorithm to produce cryptographic key
         * @param localKey
         * @return
         */

        public DidResult createDidFromKey(KeyType keyType, LocalKey localKey) {
                DidKeyCreateOptions options = new DidKeyCreateOptions(keyType,false);
                DidResult didResult = (DidResult) DidService.INSTANCE.registerByKey("key", localKey,options, continuationSuper);
                return  didResult;

        }

        public DidResult createDidFromAutoKey(KeyType keyType) {
                DidKeyCreateOptions options = new DidKeyCreateOptions(keyType,false);
                DidResult didResult = (DidResult) DidService.INSTANCE.register(options, continuationSuper);
                return  didResult;

        }

        /**
         * Method to create a DID using the did:web method. The key the did is resolved to is produced internally.
         * @param domain domain to serve did document
         * @param path path where did is located
         * @param keyType algorithm to produce cryptographic key
         * @return DidResult
         */

        public DidResult createDidFromWeb(String domain, String path, KeyType keyType) {
                DidWebCreateOptions options = new DidWebCreateOptions(domain, path, keyType);
                return (DidResult) DidService.INSTANCE.register(options, continuationSuper);
        }

        /**
         * Method to resolve a key DID to a key
         * @param keyType algorithm to produce cryptographic key
         * @param useJwkJcsPub When useJwkJcsPub is set to true
         *                     the EBSI implementation jwk_jcs-pub encoding is performed
         * @param localKey that the did will be resolved to
         * @return DidResult
         */

        public DidResult resolveKeyDidToKey(KeyType keyType, Boolean useJwkJcsPub, LocalKey localKey) {
                DidKeyCreateOptions keyDidOptions = new DidKeyCreateOptions(keyType, useJwkJcsPub);

                LocalRegistrar localRegistrar = new LocalRegistrar();

                return (DidResult) localRegistrar.createByKey(localKey, keyDidOptions,continuationSuper);
        }


        /**
         * Method to resolve a web DID to a key
         * @param domain domain to serve did document
         * @param path path where did is located
         * @param keyType algorithm to produce cryptographic key
         * @param localKey that the did will be resolved to
         * @return DidResult
         */

        public DidResult resolveWebDidToKey(KeyType keyType, String domain, String path, LocalKey localKey) {
                DidWebCreateOptions webDidOptions = new DidWebCreateOptions(domain, path, keyType);
                LocalRegistrar localRegistrar = new LocalRegistrar();
                return (DidResult) localRegistrar.createByKey(localKey, webDidOptions,continuationSuper);
        }


        public LocalKey generateKey(KeyType keyType) {
                return (LocalKey) LocalKey.Companion.generate(keyType, new LocalKeyMetadata(),continuationSuper);
        }

}
