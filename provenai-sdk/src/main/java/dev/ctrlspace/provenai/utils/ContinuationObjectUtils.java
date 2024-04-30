package dev.ctrlspace.provenai.utils;

import id.walt.credentials.verification.models.PolicyResult;
import id.walt.crypto.keys.LocalKey;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Utility class to create Continuation objects.
 */
public class ContinuationObjectUtils {

    // Method to create a continuation with a wildcard super type
    /**
     * Method to create a super continuation with a wildcard super type
     * @return Continuation object with a wildcard super type
     */
    public static Continuation<? super Object> createSuperContinuation() {
        return new Continuation<>() {
            @Override
            public void resumeWith(Object obj) {
                // Handle the result if needed
            }

            @Override
            public kotlin.coroutines.CoroutineContext getContext() {
                return EmptyCoroutineContext.INSTANCE;
            }
        };
    }

    /**
     * Method to create a plain continuation object without type parameter
     * @return Continuation object without type parameter
     */
    public static Continuation<Unit> createPlainContinuation() {
        return new Continuation<>() {
            @Override
            public void resumeWith(Object obj) {
                // Handle the result if needed
            }

            @Override
            public kotlin.coroutines.CoroutineContext getContext() {
                return EmptyCoroutineContext.INSTANCE;
            }
        };
    }



    }

