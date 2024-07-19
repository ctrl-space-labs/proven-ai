package dev.ctrlspace.provenai.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.time.Instant;
import java.util.Base64;

public class SSIJWTUtils {

    public String getVCJwtFromVPJwt(String vpToken) throws IOException, JsonProcessingException {
        String[] vpChunks = vpToken.split("\\.");

        // Base64Url decoder
        Base64.Decoder decoder = Base64.getUrlDecoder();

        // Decode the VP payload
        String vpPayload = new String(decoder.decode(vpChunks[1]));

        // Parse the VP payload
        ObjectMapper mapper = new ObjectMapper();
        JsonNode vpNode = mapper.readTree(vpPayload);

        // Extract the VC token from the VP payload
        String vcToken = vpNode.get("vp").get("verifiableCredential").get(0).asText();

        return vcToken;
    }

}
