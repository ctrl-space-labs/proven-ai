package dev.ctrlspace.provenai;


import dev.ctrlspace.provenai.ssi.issuer.DidIssuer;
import dev.ctrlspace.provenai.ssi.issuer.KeyCreation;
import dev.ctrlspace.provenai.ssi.issuer.VerifiableCredentialBuilder;
import dev.ctrlspace.provenai.utils.ContinuationObjectUtils;
import id.walt.credentials.vc.vcs.W3CVC;
import id.walt.credentials.verification.models.PolicyRequest;
import id.walt.credentials.verification.models.PolicyResult;
import id.walt.crypto.keys.Key;
import id.walt.crypto.keys.KeyType;
import id.walt.crypto.keys.LocalKey;
import id.walt.crypto.keys.LocalKeyMetadata;
import id.walt.did.dids.registrar.DidResult;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import id.walt.did.dids.registrar.LocalRegistrar;
import id.walt.did.dids.registrar.dids.DidKeyCreateOptions;
import id.walt.did.dids.registrar.dids.DidWebCreateOptions;
import io.micrometer.observation.Observation;
import kotlin.Result;
import kotlin.coroutines.Continuation;
import kotlinx.serialization.encoding.Decoder;
import kotlinx.serialization.json.JsonElement;
import kotlinx.serialization.json.JsonObject;
import kotlinx.serialization.json.JsonPrimitive;
import id.walt.credentials.verification.policies.JwtSignaturePolicy;
import kotlinx.serialization.json.Json;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    public static String bytesToBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.printf("Hello and welcome!");

        for (int i = 1; i <= 5; i++) {
            //TIP Press <shortcut actionId="Debug"/> to start debugging your code. We have set one <icon src="AllIcons.Debugger.Db_set_breakpoint"/> breakpoint
            // for you, but you can always add more by pressing <shortcut actionId="ToggleLineBreakpoint"/>.
            System.out.println("i = " + i);
        }

//get the private part of a key from its id or the public part


        // Create an instance of the DidIssuer class
        DidIssuer didIssuer = new DidIssuer();

        // Create a DID using the did:key method





//
////        // Create a DID using the did:key method
////        KeyType keyType = KeyType.Ed25519; // Set the key type
////        DidResult didKeyResult = didIssuer.createDidFromKey(keyType);
////        System.out.println("DID created using did:key method: " + didKeyResult.getDid());
        KeyType keyType = KeyType.secp256r1;
//
//        // Create a DID using the did:web method
        String domain = "example.com";
        String path = "/path/to/did.json";
        DidWebCreateOptions didWebCreateOptions = new DidWebCreateOptions(domain, path, keyType);
        DidResult didWebResult = didIssuer.createDidFromWeb(domain, path, keyType);
        System.out.println("DID created using did:web method: " + didWebResult.getDidDocument());

        LocalKey localKey = KeyCreation.generateKey(KeyType.Ed25519, 2048);
        localKey.getJwk();
        localKey.exportJWKObject(ContinuationObjectUtils.createSuperContinuation());

        LocalRegistrar localRegistrar = new LocalRegistrar();
        DidKeyCreateOptions didKeyOptions= new DidKeyCreateOptions(KeyType.Ed25519, false);
        DidResult didWebResult5  = (DidResult) localRegistrar.createByKey(localKey,didWebCreateOptions,ContinuationObjectUtils.createSuperContinuation());
          System.out.println("DID created using did:web method: " + didWebResult5.getDidDocument());
//
// Create a DID using the createDidFromAutoKey method
//        DidResult didResult = didIssuer.createDidFromKey(KeyType.Ed25519, localKey);
//        System.out.println(didResult.component1());
//        System.out.println(didResult.component2());
//        System.out.println(didResult.getDidDocument());
//        System.out.println(DidResult.Companion);
//////        print the local key
//        Continuation<? super Object> continuation = ContinuationObjectUtils.createSuperContinuation(); // Use the plain continuation object
////        Continuation<? super LocalKey>  keyContinuation = ContinuationObjectUtils.createLocalKeySuperContinuation();
////        System.out.println(localKey);
//////        System.out.println(localKey.getJwk());
//        System.out.println("Public Representation" + localKey.getPublicKeyRepresentation(continuation));
//////        System.out.println(localKey.getHasPrivateKey());
//////        System.out.println(localKey.getKeyId(continuation));
////        System.out.println("Public Key" +localKey.getPublicKey(continuation));
////
////       String PublicKey = (String) localKey.getPublicKey(continuation);
//        System.out.println("KeyJwk" +localKey.getJwk());
////        System.out.println(localKey.exportJWKObject());
////        System.out.println(localKey.getPublicKey((Continuation<? super LocalKey>) keyContinuation));
//        LocalKey publicKey = (LocalKey) localKey.getPublicKey(continuation);
//        System.out.println(publicKey.getJwk());
//        Object publicKey1 = localKey.getPublicKey(continuation);
//        LocalKey publicKey2 = (LocalKey) localKey.getPublicKey(continuation);
//        System.out.println("Public Key" + publicKey);
//
//        LocalKey localKeyEd25519 = KeyCreation.generateKey(KeyType.Ed25519, 2048);
//        LocalKey localKeyRSA = KeyCreation.generateKey(KeyType.RSA, 2048);
//        LocalKey localKeysecp256r1 = KeyCreation.generateKey(KeyType.secp256r1, 2048);
//        LocalKey localKeysecp256k1 = KeyCreation.generateKey(KeyType.secp256k1, 2048);
//
//
//        LocalKey publicKeyEd25519 = (LocalKey) localKeyEd25519.getPublicKey(continuation);
//        LocalKey publicKeyRSA = (LocalKey) localKeyRSA.getPublicKey(continuation);
//        LocalKey publicKeysecp256r1 = (LocalKey) localKeysecp256r1.getPublicKey(continuation);
//        LocalKey publicKeysecp256k1 = (LocalKey) localKeysecp256k1.getPublicKey(continuation);
//
//        System.out.println("Public Key Ed25519" + publicKeyEd25519.getJwk());
//        System.out.println("Public Key RSA" + publicKeyRSA.getJwk());
//        System.out.println("Public Key secp256r1" + publicKeysecp256r1.getJwk());
//        System.out.println("Public Key secp256k1" + publicKeysecp256k1.getJwk());
//
//        System.out.println("private Key Ed25519" + localKeyEd25519.getJwk());
//        System.out.println("private Key RSA" + localKeyRSA.getJwk());
//        System.out.println("private Key secp256r1" + localKeysecp256r1.getJwk());
//        System.out.println("private Key secp256k1" + localKeysecp256k1.getJwk());


        // Print the decoded string
//        System.out.println(localKey.getKeyType());
//        System.out.println(localKey.exportJWK(continuation));
//        System.out.println(localKey.exportPEM(continuation));


//
//        // Create an instance of VerifiableCredentialBuilder
        VerifiableCredentialBuilder credentialBuilder = new VerifiableCredentialBuilder();

        // Add context and type
        credentialBuilder.addContext("https://www.w3.org/2018/credentials/v1");
        credentialBuilder.addType("VerifiableId");

        // Set credential ID, issuer DID, and subject DID
        credentialBuilder.setCredentialId("urn:uuid:12345678-1234-5678-1234-567812345678");
        credentialBuilder.setIssuerDid("did:example:123456789abcdefghi");
        credentialBuilder.setSubjectDid("did:example:987654321abcdefghi");

        // Set validity duration
        Duration duration = Duration.ofDays(30); // Example: 30 days validity period
        credentialBuilder.validFor(duration);

        // Build the credential
        W3CVC credential = credentialBuilder.buildCredential();

//        // Prepare parameters for signing
//        Key issuerKey = KeyCreation.generateKey(KeyType.Ed25519, 2048); // Provide the issuer's key
//        String issuerDid = "did:example:123456789abcdefghi"; // Example issuer DID
//        String subjectDid = "did:example:987654321abcdefghi"; // Example subject DID
//        Continuation<? super Object> continuation = ContinuationObjectUtils.createSuperContinuation(); // Use the plain continuation object
//        Continuation continuationPlain = ContinuationObjectUtils.createPlainContinuation();
//        Map<String, String> additionalJwtHeaders = new HashMap<>(); // Provide additional JWT headers if needed
//        Map<String, JsonElement> additionalJwtOptions = new HashMap<>(); // Provide additional JWT options if needed
//
//        // Sign the credential
//        String signedCredential = credentialBuilder.signCredential(
//                issuerKey,
//                issuerDid,
//                subjectDid,
//                continuation,
//                additionalJwtHeaders,
//                additionalJwtOptions
//        );
//
//        // Print the signed credential
////        System.out.println("Signed Verifiable Credential: " + signedCredential);
//
//
////        System.out.println(credential.toPrettyJson());
//// Create an instance of the Json class
//        Json json = Json.Default;
//        // Parse the signed credential JSON string into a JsonElement object
//        JsonElement credentialJson = json.parseToJsonElement(signedCredential);
//        // Convert signedCredential to Gson JsonElement
//        VerifiablePresentationBuilder vpBuilder = new VerifiablePresentationBuilder();
//
//        // Set DID and nonce
//        vpBuilder.setDid(String.valueOf(issuerDid));
//        vpBuilder.setNonce("20394029340");
//
//        // Build and sign the presentation
//        String signedPresentation = vpBuilder.buildAndSign(issuerKey, continuation);
//
//        JsonObject policyArgs = new JsonObject(new HashMap<String, JsonPrimitive>());
//
//        PolicyRequest jwtSignaturePolicyRequest = new PolicyRequest(new JwtSignaturePolicy(), policyArgs);
//
//        Map<String, Object> context = new HashMap<>();
//
//
//        context.put("key2", 123);
//
//
//        JwtSignaturePolicy jwtSignaturePolicy = new JwtSignaturePolicy();
//        PolicyRequest policyRequest = new PolicyRequest(jwtSignaturePolicy, null); // Passing null for args
//        List<PolicyRequest> vpPolicies = new ArrayList<>(); // Add your policies here
//        List<PolicyRequest> globalVcPolicies = new ArrayList<>(); // Add your global VC policies here
//        HashMap<String, List<PolicyRequest>> specificCredentialPolicies = new HashMap<>(); // Add specific credential policies here
//        HashMap<String, Object> presentationContext = new HashMap<>(); // Optional context data
//        vpPolicies.add(policyRequest);
//
////
////        // Wait for the verification to complete
////        future.join();
////
//////     Verifiable Credential Verification Process
//
//        PresentationVerifier presentationVerifier = new PresentationVerifier();
//        // Call the verifyPresentationAsync method
//        presentationVerifier.verifyPresentationAsync(
//                signedPresentation,
//                vpPolicies,
//                globalVcPolicies,
//                specificCredentialPolicies,
//                presentationContext
//        ).thenAccept(result -> {
//            // Check the overall success using methods of PresentationVerificationResponse
////            System.out.println("Presentation verification results:");
////            System.out.println("Overall success: " + result.overallSuccess());
////            System.out.println(result.getResults());
//            // Further processing using other methods of PresentationVerificationResponse
//        }).exceptionally(ex -> {
//            System.out.println("Error occurred during presentation verification: " + ex.getMessage());
//            return null;
//        }).join(); // Wait for the CompletableFuture to complete
//
//        CredentialVerifier credentialVerifier = new CredentialVerifier();
//        credentialVerifier.verifyCredentialAsync(signedCredential, vpPolicies, presentationContext)
//                .thenAcceptAsync(verificationResults -> {
//                    // Process verification results
////                    for (PolicyResult result : verificationResults) {
////                        // Handle each verification result
////                        System.out.println("Credential Verification result: " + result);
////                        System.out.println("Credential Verification result request: " + result.getRequest());
////                        System.out.println("Credential Verification result success: " + result.isSuccess());
////                        System.out.println("Credential Verification result success: " + result.toJsonResult());
////                        System.out.println("Credential Verification result success: " + result.component1());
////
////
////                    }
//                })
//                .exceptionally(exception -> {
//                    // Handle exceptions
//                    System.out.println("An error occurred during verification: " + exception.getMessage());
//                    return null;
//                });

    }


}










