package dev.ctrlspace.provenai.backend.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.ctrlspace.provenai.ssi.converters.AgentIDConverter;
import dev.ctrlspace.provenai.ssi.converters.PermissionOfUseConverter;
import dev.ctrlspace.provenai.ssi.issuer.CredentialIssuanceApi;
import dev.ctrlspace.provenai.ssi.verifier.CredentialVerificationApi;
import dev.ctrlspace.provenai.utils.JsonLiteralSerializer;
import dev.ctrlspace.provenai.utils.SSIJWTUtils;
import kotlinx.serialization.json.JsonLiteral;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SSIConfig {


    @Bean
    public CredentialIssuanceApi credentialIssuanceApi(ObjectMapper kotlinObjectMapper) {
        return new CredentialIssuanceApi(kotlinObjectMapper);
    }

    @Bean
    public SSIJWTUtils ssiJWTUtils() {
        return new SSIJWTUtils();
    }

    @Bean
    public CredentialVerificationApi credentialVerificationApi() {
        return new CredentialVerificationApi();
    }

    @Bean
    public AgentIDConverter agentIDConverter(ObjectMapper kotlinObjectMapper) {
        return new AgentIDConverter(kotlinObjectMapper);
    }

    @Bean
    public PermissionOfUseConverter permissionOfUseConverter(ObjectMapper kotlinObjectMapper) {
        return new PermissionOfUseConverter(kotlinObjectMapper);
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
