package dev.ctrlspace.provenai.ssi;

import id.walt.credentials.verification.Verifier;
import id.walt.credentials.verification.models.PolicyRequest;
import id.walt.credentials.verification.models.PresentationVerificationResponse;
import id.walt.sdjwt.VerificationResult;
import kotlin.Result;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CredentialVerifier {


//        public static void verifyVerifiableCredential(String signedCredential,
//                                            List<PolicyRequest> policyRequests,
//                                            Map<String, Object> context,
//                                            Continuation<? super Object> continuation) {
//            Verifier.INSTANCE.verifyCredential(signedCredential, policyRequests, context, continuation);
//        }
//    public static List<VerificationResult> verifyVerifiableCredential(String signedCredential,
//                                                                      List<PolicyRequest> policyRequests,
//                                                                      Map<String, Object> context,
//                                                                      Continuation<? super Object> continuation) {
//        // Perform verification and return results
//        return (List<VerificationResult>) Verifier.INSTANCE.verifyCredential(signedCredential, policyRequests, context, continuation);
//}
//    public static void verifyPresentation(String presentationJwt,
//                                          List<PolicyRequest> vpPolicies,
//                                          List<PolicyRequest> globalVcPolicies,
//                                          Map<String, List<PolicyRequest>> specificCredentialPolicies,
//                                          Map<String, Object> presentationContext,
//                                          Continuation<? super Object> continuation) {
//        Verifier.INSTANCE.verifyPresentation(presentationJwt, vpPolicies, globalVcPolicies, specificCredentialPolicies, presentationContext, continuation);
//    }



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

//     Verifiable Credentia
}


