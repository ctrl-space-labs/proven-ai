package dev.ctrlspace.provenai.ssi.verifier;

import id.walt.credentials.verification.Verifier;
import id.walt.credentials.verification.models.PolicyRequest;
import id.walt.credentials.verification.models.PolicyResult;
import id.walt.sdjwt.VerificationResult;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CredentialVerifier {
    private ExecutorService executorService;

    public CredentialVerifier() {
        // Initialize the executor service
        executorService = Executors.newCachedThreadPool();
    }

    public CompletableFuture<List<PolicyResult>> verifyCredentialAsync(
            String signedCredential,
            List<PolicyRequest> policies,
            HashMap<String, Object> presentationContext) {

        // Create CompletableFuture for async execution
        CompletableFuture<List<PolicyResult>> future = new CompletableFuture<>();

        // Execute verifyCredential method asynchronously
        CompletableFuture.runAsync(() -> {
            // Create continuation object
            Continuation<List<? super PolicyResult>> continuationVc = new Continuation<List<? super PolicyResult>>() {
                public List<? super PolicyResult> resume(List<? super PolicyResult> result) {
                    List<PolicyResult> convertedList = new ArrayList<>();
                    for (Object obj : result) {
                        convertedList.add((PolicyResult) obj);
                    }
                    future.complete(convertedList);
                    return result;
                }

                public void resumeWithException(Throwable exception) {
                    future.completeExceptionally(exception);
                }

                @Override
                public void resumeWith(@NotNull Object result) {
                    if (result instanceof List<?>) {
                        resume((List<? super PolicyResult>) result);
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

            // Execute verifyCredential method asynchronously
            Verifier.INSTANCE.verifyCredential(signedCredential, policies, presentationContext, continuationVc);
        }, executorService);

        // Return CompletableFuture for further processing
        return future;
    }
}