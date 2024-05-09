package dev.ctrlspace.provenai.backend.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.ctrlspace.provenai.ssi.converters.AgentIDConverter;
import dev.ctrlspace.provenai.ssi.issuer.CredentialIssuanceApi;
import dev.ctrlspace.provenai.utils.JsonLiteralSerializer;
import id.walt.crypto.keys.Key;
import id.walt.crypto.keys.LocalKey;
import kotlinx.serialization.json.JsonLiteral;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SSICongig {

    //get from properties and env variables the actual keys\
    private String issuerPrivateJwkStr ="{\"kty\":\"OKP\",\"d\":\"HIN9WcVCqhGvwZ8I47WeMtxGceSKpvaEnu5eXAoWyDo\",\"crv\":\"Ed25519\",\"kid\":\"CFQ-Nra5ynyBsfxwy7aNf8duAEUCMlMIrRIrDg6DIy4\",\"x\":\"h5nbw6X9JmI0BvuQ5M0JXfzO8s2eEbPdV29wsHTL9pk\"}";


    @Bean
    public Key provenAIIssuerKey() {
        Key key = new LocalKey(issuerPrivateJwkStr);
        return key;
    }

    @Bean
    public String provenAIIssuerPrivateJwkStr() {
        return issuerPrivateJwkStr;
    }

    @Bean
    public CredentialIssuanceApi credentialIssuanceApi(ObjectMapper kotlinObjectMapper) {
        return new CredentialIssuanceApi(kotlinObjectMapper);
    }

    @Bean
    public AgentIDConverter agentIDConverter(ObjectMapper kotlinObjectMapper) {
        return new AgentIDConverter(kotlinObjectMapper);
    }

    @Bean
    public ObjectMapper kotlinObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());
        SimpleModule module = new SimpleModule();
        module.addSerializer(JsonLiteral.class, new JsonLiteralSerializer());
        objectMapper.registerModule(module);
        return objectMapper;
    }
}
