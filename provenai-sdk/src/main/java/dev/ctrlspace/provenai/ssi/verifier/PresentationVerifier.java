package dev.ctrlspace.provenai.ssi.verifier;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.CompletableFuture;

import id.walt.credentials.verification.Verifier;
import id.walt.credentials.verification.models.PolicyRequest;
import id.walt.credentials.verification.models.PresentationVerificationResponse;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import org.jetbrains.annotations.NotNull;

public class PresentationVerifier {
    private ExecutorService executorService;

    public PresentationVerifier() {
        // Initialize the executor service
        executorService = Executors.newCachedThreadPool();
    }

    public CompletableFuture<PresentationVerificationResponse> verifyPresentationAsync(
            String signedPresentation,
            List<PolicyRequest> vpPolicies,
            List<PolicyRequest> globalVcPolicies,
            HashMap<String, List<PolicyRequest>> specificCredentialPolicies,
            HashMap<String, Object> presentationContext) {

        // Create CompletableFuture for async execution
        CompletableFuture<PresentationVerificationResponse> future = new CompletableFuture<>();

        // Execute verifyPresentation method asynchronously
        CompletableFuture.runAsync(() -> {
            // Create continuation object
            Continuation<PresentationVerificationResponse> continuationVp = new Continuation<PresentationVerificationResponse>() {

                public PresentationVerificationResponse resume(PresentationVerificationResponse result) {
                    future.complete(result);
                    return result;
                }

                public void resumeWithException(Throwable exception) {
                    future.completeExceptionally(exception);
                }

                @Override
                public void resumeWith(@NotNull Object result) {
                    if (result instanceof PresentationVerificationResponse) {
                        resume((PresentationVerificationResponse) result);
                    } else if (result instanceof Throwable) {
                        resumeWithException((Throwable) result);
                    } else {
                        future.completeExceptionally(new IllegalStateException("Unexpected result type: " + result.getClass()));
                    }
                }

                @NotNull
                @Override
                public CoroutineContext getContext() {
                    // Return the coroutine context
                    return EmptyCoroutineContext.INSTANCE;
                }
            };

            // Execute verifyPresentation method asynchronously
            Verifier.INSTANCE.verifyPresentation(
                    signedPresentation, vpPolicies, globalVcPolicies,
                    specificCredentialPolicies, presentationContext, continuationVp);
        }, executorService);

        // Return CompletableFuture for further processing
        return future;
    }
}