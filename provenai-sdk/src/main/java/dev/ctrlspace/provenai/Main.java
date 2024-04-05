package dev.ctrlspace.provenai;


import dev.ctrlspace.provenai.ssi.*;
import dev.ctrlspace.provenai.utils.ContinuationObjectUtils;
import id.walt.credentials.vc.vcs.W3CVC;
import id.walt.credentials.verification.VerificationPolicy;
import id.walt.credentials.verification.Verifier;
import id.walt.credentials.verification.models.PolicyRequest;
import id.walt.credentials.verification.models.PolicyResult;
import id.walt.credentials.verification.models.PresentationVerificationResponse;
import id.walt.credentials.verification.policies.ExpirationDatePolicy;
import id.walt.credentials.verification.policies.vp.HolderBindingPolicy;
import id.walt.crypto.keys.Key;
import id.walt.crypto.keys.KeyType;
import id.walt.crypto.keys.LocalKey;
import id.walt.did.dids.registrar.DidResult;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlinx.serialization.json.JsonElement;
import kotlinx.serialization.json.JsonObject;
import kotlinx.serialization.json.JsonPrimitive;
import org.jetbrains.annotations.NotNull;
import id.walt.credentials.verification.policies.JwtSignaturePolicy;
import kotlinx.serialization.json.Json;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    private static final ExecutorService executorService = Executors.newCachedThreadPool();
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.printf("Hello and welcome!");

        for (int i = 1; i <= 5; i++) {
            //TIP Press <shortcut actionId="Debug"/> to start debugging your code. We have set one <icon src="AllIcons.Debugger.Db_set_breakpoint"/> breakpoint
            // for you, but you can always add more by pressing <shortcut actionId="ToggleLineBreakpoint"/>.
            System.out.println("i = " + i);
        }


        DidIssuer didIssuer = new DidIssuer();

//        // Create a DID using the did:key method
//        KeyType keyType = KeyType.Ed25519; // Set the key type
//        DidResult didKeyResult = didIssuer.createDidFromKey(keyType);
//        System.out.println("DID created using did:key method: " + didKeyResult.getDid());
        KeyType keyType = KeyType.secp256r1;


        // Create a DID using the did:web method
        String domain = "example.com";
        String path = "/path/to/did.json";
        DidResult didWebResult = didIssuer.createDidFromWeb(domain, path, keyType);
        System.out.println("DID created using did:web method: " + didWebResult.getDid());


        LocalKey localKey = KeyCreation.generateKey(KeyType.Ed25519, 2048);
        // Create a DID using the createDidFromAutoKey method
        DidResult didResult = didIssuer.createDidFromKey(KeyType.Ed25519, localKey);
        System.out.println("DID created using did:web method: " + didResult.getDid());


        // Create an instance of VerifiableCredentialBuilder
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

        // Prepare parameters for signing
        Key issuerKey = KeyCreation.generateKey(KeyType.Ed25519, 2048); // Provide the issuer's key
        String issuerDid = "did:example:123456789abcdefghi"; // Example issuer DID
        String subjectDid = "did:example:987654321abcdefghi"; // Example subject DID
        Continuation<? super Object> continuation = ContinuationObjectUtils.createSuperContinuation(); // Use the plain continuation object
        Continuation continuationPlain = ContinuationObjectUtils.createPlainContinuation();
        Map<String, String> additionalJwtHeaders = new HashMap<>(); // Provide additional JWT headers if needed
        Map<String, JsonElement> additionalJwtOptions = new HashMap<>(); // Provide additional JWT options if needed

        // Sign the credential
        String signedCredential = credentialBuilder.signCredential(
                issuerKey,
                issuerDid,
                subjectDid,
                continuation,
                additionalJwtHeaders,
                additionalJwtOptions
        );

        // Print the signed credential
//        System.out.println("Signed Verifiable Credential: " + signedCredential);


//        System.out.println(credential.toPrettyJson());
// Create an instance of the Json class
        Json json = Json.Default;
        // Parse the signed credential JSON string into a JsonElement object
        JsonElement credentialJson = json.parseToJsonElement(signedCredential);
        // Convert signedCredential to Gson JsonElement
        VerifiablePresentationBuilder vpBuilder = new VerifiablePresentationBuilder();

        // Set DID and nonce
        vpBuilder.setDid(String.valueOf(issuerDid));
        vpBuilder.setNonce("20394029340");

        // Build and sign the presentation
        String signedPresentation = vpBuilder.buildAndSign(issuerKey, continuation);

        JsonObject policyArgs = new JsonObject(new HashMap<String, JsonPrimitive>());

        PolicyRequest jwtSignaturePolicyRequest = new PolicyRequest(new JwtSignaturePolicy(), policyArgs);

        Map<String, Object> context = new HashMap<>();


        context.put("key2", 123);


        JwtSignaturePolicy jwtSignaturePolicy = new JwtSignaturePolicy();
        PolicyRequest policyRequest = new PolicyRequest(jwtSignaturePolicy, null); // Passing null for args
        List<PolicyRequest> vpPolicies = new ArrayList<>(); // Add your policies here
        List<PolicyRequest> globalVcPolicies = new ArrayList<>(); // Add your global VC policies here
        HashMap<String, List<PolicyRequest>> specificCredentialPolicies = new HashMap<>(); // Add specific credential policies here
        HashMap<String, Object> presentationContext = new HashMap<>(); // Optional context data
        vpPolicies.add(policyRequest);



//
////        Verifiable Presentation Verification
//
//        Continuation<PresentationVerificationResponse> continuationVP = new Continuation<PresentationVerificationResponse>() {
//
//                PresentationVerificationResponse verificationResult;
//
//                public void resume(PresentationVerificationResponse result) {
//                    // Store the result
//                    verificationResult = result;
//
//                    // Print the overall success
//                    System.out.println("Presentation verification results:");
//                    System.out.println("Overall success: " + result.overallSuccess());
//                }
//
//
//
//            public void resumeWithException(Throwable exception) {
//                // Handle the exception
//                exception.printStackTrace();
//                System.out.println("Exception occurred during presentation verification: " + exception.getMessage());
//            }
//
//            @Override
//            public void resumeWith(@NotNull Object result) {
//                if (result instanceof PresentationVerificationResponse) {
//                    resume((PresentationVerificationResponse) result);
//                } else if (result instanceof Throwable) {
//                    resumeWithException((Throwable) result);
//                } else {
//                    throw new IllegalStateException("Unexpected result type: " + result.getClass());
//                }
//            }
//
//            @NotNull
//            @Override
//            public CoroutineContext getContext() {
//                // Return the coroutine context
//                return EmptyCoroutineContext.INSTANCE;
//            }
//        };
//
//        System.out.println("Verifying presentation");
//        Verifier.INSTANCE.verifyPresentation(
//                signedPresentation, vpPolicies, globalVcPolicies,
//                specificCredentialPolicies, presentationContext, continuationVP
//        );

        ExecutorService executorService = Executors.newCachedThreadPool();

        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            // Create continuation object
            Continuation<PresentationVerificationResponse> continuationVP = new Continuation<PresentationVerificationResponse>() {
                PresentationVerificationResponse verificationResult;

                public void resume(PresentationVerificationResponse result) {
                    // Store the result
                    verificationResult = result;

                    // Print the overall success
                    System.out.println("Presentation verification results:");
                    System.out.println("Overall success: " + result.overallSuccess());
                }

                public void resumeWithException(Throwable exception) {
                    // Handle the exception
                    exception.printStackTrace();
                    System.out.println("Exception occurred during presentation verification: " + exception.getMessage());
                }

                @Override
                public void resumeWith(@NotNull Object result) {
                    if (result instanceof PresentationVerificationResponse) {
                        resume((PresentationVerificationResponse) result);
                    } else if (result instanceof Throwable) {
                        resumeWithException((Throwable) result);
                    } else {
                        throw new IllegalStateException("Unexpected result type: " + result.getClass());
                    }
                }

                @NotNull
                @Override
                public CoroutineContext getContext() {
                    // Return the coroutine context
                    return EmptyCoroutineContext.INSTANCE;
                }
            };

            System.out.println("Verifying presentation");

            // Execute verifyPresentation method synchronously
            Verifier.INSTANCE.verifyPresentation(
                    signedPresentation, vpPolicies, globalVcPolicies,
                    specificCredentialPolicies, presentationContext, continuationVP
            );
        }, executorService);

        // Wait for the verification to complete
        future.join();

//     Verifiable Credential Verification Process

//        Continuation<PresentationVerificationResponse> continuationVC = new Continuation<PresentationVerificationResponse>() {
//            public void resume(PresentationVerificationResponse result) {
//                // Handle the result
//                System.out.println("Presentation verification results:");
//                System.out.println("Overall success: " + result.overallSuccess());
//
//
//            }
//
//            public void resumeWithException(Throwable exception) {
//                // Handle the exception
//                exception.printStackTrace();
//            }
//
//            @Override
//            public void resumeWith(@NotNull Object result) {
//                if (result instanceof PresentationVerificationResponse) {
//                    resume((PresentationVerificationResponse) result);
//                } else if (result instanceof Throwable) {
//                    resumeWithException((Throwable) result);
//                } else {
//                    throw new IllegalStateException("Unexpected result type: " + result.getClass());
//                }
//            }
//
//            @NotNull
//            @Override
//            public CoroutineContext getContext() {
//                // Return the coroutine context
//                return EmptyCoroutineContext.INSTANCE;
//            }
//        };
//
//        Verifier.INSTANCE.verifyPresentation(
//                signedPresentation, vpPolicies, globalVcPolicies, specificCredentialPolicies, presentationContext,
//                continuationVC
//        );


    }
}






