package dev.ctrlspace.provenai.backend.configuration;

import java.util.List;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;


@Configuration
@OpenAPIDefinition(
        info = @io.swagger.v3.oas.annotations.info.Info(title = "API Title", version = "v1"),
        security = @SecurityRequirement(name = "Bearer Auth")
)
@SecurityScheme(
        name = "Bearer Auth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenAPIConfig {

    @Value("${proven-ai.openapi.servers.dev-url}")
    private String devUrl;

    @Value("${proven-ai.openapi.servers.prod-url}")
    private String prodUrl;

    @Value("${proven-ai.openapi.servers.local-url}")
    private String localUrl;


    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Server URL in Development environment");

        Server prodServer = new Server();
        prodServer.setUrl(prodUrl);
        prodServer.setDescription("Server URL in Production environment");

        Server localServer = new Server();
        localServer.setUrl(localUrl);
        localServer.setDescription("Server URL in Local environment");


        Contact contact = new Contact();
        contact.setEmail("contact@ctrlspace.dev");
        contact.setName("Ctrl Space Labs");
        contact.setUrl("https://ctrlspace.dev");

        License mitLicense = new License().name("See Github").url("https://github.com/ctrl-space-labs/proven-ai");

        Info info = new Info()
                .title("ProvenAI API")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints for ProvenAI Integration.").termsOfService("https://ctrlspace.dev")
                .license(mitLicense);

        return new OpenAPI().info(info).servers(List.of(localServer, devServer, prodServer));
    }
}
