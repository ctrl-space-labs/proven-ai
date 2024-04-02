package dev.ctrlspace.provenai.utils;

import java.time.Instant;

public class KotlinToJavaUtils {

    public static kotlinx.datetime.Instant toKotlinInstant(Instant javaInstant) {
        return kotlinx.datetime.Instant.Companion.fromEpochMilliseconds(javaInstant.toEpochMilli());
    }

    public static Instant toJavaInstant(kotlinx.datetime.Instant kotlinInstant) {
        return Instant.ofEpochMilli(kotlinInstant.toEpochMilliseconds());
    }


}
