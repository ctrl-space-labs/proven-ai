package dev.ctrlspace.provenai.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import kotlinx.serialization.json.JsonLiteral;

import java.io.IOException;

public class JsonLiteralSerializer extends JsonSerializer<JsonLiteral> {

    @Override
    public void serialize(JsonLiteral jsonLiteral, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        if (jsonLiteral.isString()) {
            jsonGenerator.writeString(jsonLiteral.getContent());
        } else {
            // Assuming the content can be a valid number or boolean
            String content = jsonLiteral.getContent();
            if (content.equalsIgnoreCase("true") || content.equalsIgnoreCase("false")) {
                jsonGenerator.writeBoolean(Boolean.parseBoolean(content));
            } else {
                try {
                    jsonGenerator.writeNumber(Double.parseDouble(content));
                } catch (NumberFormatException e) {
                    throw new IOException("Invalid number format in JsonLiteral content", e);
                }
            }
        }
    }
}
