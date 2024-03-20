package dev.ctrlspace.provenai.utils;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.EmptyCoroutineContext;

public class ContinuationObjectUtils {

    // Method to create a continuation with a wildcard super type
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

    // Method to create a plain continuation without type parameter
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
