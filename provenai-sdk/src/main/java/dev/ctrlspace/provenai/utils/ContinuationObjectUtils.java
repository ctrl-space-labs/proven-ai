package dev.ctrlspace.provenai.utils;

import id.walt.credentials.verification.models.PolicyResult;
import id.walt.crypto.keys.LocalKey;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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

    // Method to create a continuation with a wildcard super type for LocalKey
    // Method to create a continuation with a wildcard super type for LocalKey

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

