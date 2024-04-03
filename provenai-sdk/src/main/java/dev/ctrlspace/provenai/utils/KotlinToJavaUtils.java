package dev.ctrlspace.provenai.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.time.Instant;

public class KotlinToJavaUtils {

    public static kotlinx.datetime.Instant toKotlinInstant(Instant javaInstant) {
        return kotlinx.datetime.Instant.Companion.fromEpochMilliseconds(javaInstant.toEpochMilli());
    }

    public static Instant toJavaInstant(kotlinx.datetime.Instant kotlinInstant) {
        return Instant.ofEpochMilli(kotlinInstant.toEpochMilliseconds());
    }


    public static JsonPrimitive toKotlinJsonElement(JsonElement gsonJsonElement) {
        return new JsonPrimitive(gsonJsonElement.toString());
    }

    public static JsonElement toGsonJsonElement(kotlinx.serialization.json.JsonElement kotlinJsonElement) {
        return JsonParser.parseString(kotlinJsonElement.toString());
    }


}
