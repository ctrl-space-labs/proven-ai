package dev.ctrlspace.provenai.utils;

import id.walt.did.helpers.WaltidServices;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

/** Utility class to initialize WaltId services
 *
 * This class is a singleton enum class that initializes WaltId services
 * The enum implementation serialization and thread-safety.
 * The class has a single method that initializes WaltId services.
 * The method initializes WaltId services using the minimalInit method of the WaltIdServices class.
 * The method takes a plain  continuation object as a parameter.
 *
*/

public enum WaltIdServiceInitUtils {
    INSTANCE;

    private final Continuation<Unit> continuationPlain;

    WaltIdServiceInitUtils() {
        this.continuationPlain = ContinuationObjectUtils.createPlainContinuation();
    }

    public void initializeWaltIdServices() {
        WaltidServices.INSTANCE.minimalInit(continuationPlain);
    }
}
