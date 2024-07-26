package dev.ctrlspace.provenai.ssi.issuer;

import dev.ctrlspace.provenai.utils.ContinuationObjectUtils;
import dev.ctrlspace.provenai.utils.WaltIdServiceInitUtils;
import id.walt.crypto.keys.Key;
import id.walt.crypto.keys.KeyType;
import id.walt.crypto.keys.jwk.JWKKey;
import id.walt.did.dids.DidService;
import id.walt.did.dids.registrar.DidResult;
import id.walt.did.dids.registrar.LocalRegistrar;
import id.walt.did.dids.registrar.dids.DidKeyCreateOptions;
import id.walt.did.dids.registrar.dids.DidWebCreateOptions;
import kotlin.coroutines.Continuation;

public class DidIssuer {

        //  Kotlin Continuation Objects
        private Continuation<? super Object> continuationSuper;

        public DidIssuer() {

                this.continuationSuper = ContinuationObjectUtils.createSuperContinuation();
                WaltIdServiceInitUtils.INSTANCE.initializeWaltIdServices();

        }



        /**
         * Create a DID from a key pair using the did:key method
         *
         * @param keyType The type of key to generate (enum Ed25519, secp256k1, secp256r1, RSA)
         * @param jwkKey The key pair to use for the DID
         * @return The DID result
         */
        public DidResult createDidFromKey(KeyType keyType, JWKKey jwkKey) {
                DidKeyCreateOptions options = new DidKeyCreateOptions(keyType,false);
                return (DidResult) DidService.INSTANCE.registerByKey("key", jwkKey,options, continuationSuper);

        }


        /**
         * Method to create a DID using the did:key method with a key produced internally.
         * @param keyType algorithm to produce cryptographic key
         * @return
         */

        public DidResult createDidFromAutoKey(KeyType keyType) {
                DidKeyCreateOptions options = new DidKeyCreateOptions(keyType,false);
                return  DidService.INSTANCE.registerBlocking(options);

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
                return DidService.INSTANCE.registerBlocking(options);
        }

        /**
         * Method to resolve a key DID to a key
         * @param keyType algorithm to produce cryptographic key
         * @param useJwkJcsPub When useJwkJcsPub is set to true
         *                     the EBSI implementation jwk_jcs-pub encoding is performed
         * @param jwkKey that the did will be resolved to
         * @return DidResult
         */

        public DidResult resolveKeyDidToKey(KeyType keyType, Boolean useJwkJcsPub, JWKKey jwkKey) {
                DidKeyCreateOptions keyDidOptions = new DidKeyCreateOptions(keyType, useJwkJcsPub);

                LocalRegistrar localRegistrar = new LocalRegistrar();

                return localRegistrar.createByKeyBlocking(jwkKey, keyDidOptions);
        }


        /**
         * Method to resolve a web DID to a key
         * @param domain domain to serve did document
         * @param path path where did is located
         * @param keyType algorithm to produce cryptographic key
         * @param jwkKey that the did will be resolved to
         * @return DidResult
         */

        public DidResult resolveWebDidToKey(KeyType keyType, String domain, String path, JWKKey jwkKey) {
                DidWebCreateOptions webDidOptions = new DidWebCreateOptions(domain, path, keyType);
                LocalRegistrar localRegistrar = new LocalRegistrar();
                return localRegistrar.createByKeyBlocking(jwkKey, webDidOptions);
        }


}
