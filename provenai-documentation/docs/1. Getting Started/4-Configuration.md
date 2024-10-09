---
sidebar_position: 2
---

# Configuration

## Application Properties
Customize the application settings in the `src/main/resources/application.yml` file for the core configuration properties. Also you can set specific configuration settings based on the environment. Hence there are also the `./application-local.yml`, `./application-dev.yml`, `./application-prod.yml`. Based on this structure all the configuration settings can be set to different values depending on the environment.

## Server Configuration
- **server.port**: Specifies the port the application will run on. Default is set to `8082`.
- **server.servlet.context-path**: Defines the base path for all API endpoints. Set to `/proven-ai/api/v1`.

## Logging Configuration
- **logging.level.dev.ctrlspace**: Sets the logging level for the `dev.ctrlspace` package. 
- **logging.level.org.springframework**: Sets the logging level for Spring framework logs. 

## Spring MVC Configuration
- **spring.mvc.format.date-time**: Defines the format for date-time fields in the application. The format is set as `yyyy-MM-dd'T'HH:mm:ss.SSSZ`.

## JPA and Hibernate Configuration
- **spring.jpa.show-sql**: If `true`, enables SQL query logging. Set to `true`.
- **spring.jpa.hibernate.ddl-auto**: Defines the schema generation strategy. Set to `none`, which means no automatic schema generation.

## DataSource Configuration
- **spring.datasource.url**: The URL for the database. It is defined from an environment variable `DATABASE_URL`. If the environment variable is not set, defaults to `no-env-variable`.
- **spring.datasource.username**: The database username, configurable via the `DATABASE_USERNAME` environment variable.
- **spring.datasource.password**: The database password, configurable via the `DATABASE_PASSWORD` environment variable.
- **spring.datasource.hikari.maximum-pool-size**: Configures the maximum number of connections in the HikariCP connection pool. 
- **spring.datasource.hikari.minimum-idle**: Configures the minimum number of idle connections in the HikariCP connection pool. 

## Jackson Serialization Configuration
- **spring.jackson.serialization.write-dates-as-timestamps**: Determines whether dates are written as timestamps in JSON responses. Set to `false`, meaning dates will be serialized in ISO 8601 format.

## Application Name
- **spring.application.name**: The name of the application. Set to `proven-ai-api`.

## Security Configuration (OAuth2 and JWT)
- **spring.security.oauth2.resourceserver.jwt.issuer-uri**: Specifies the OAuth2 JWT issuer URI. 
- **spring.security.oauth2.resourceserver.jwt.jwk-set-uri**: Specifies the URI for the JSON Web Key Set (JWKS) used to verify JWT tokens.

## Keycloak Configuration
- **keycloak.base-url**: The base URL of the Keycloak server.
- **keycloak.token-uri**: The URI for obtaining access tokens from Keycloak.
- **keycloak.realm**: The Keycloak realm used for authentication.
- **keycloak.client-id**: The client ID for Keycloak, retrieved via the `KEYCLOAK_CLIENT_ID` environment variable.
- **keycloak.client-secret**: The client secret (password) for Keycloak, retrieved via the `KEYCLOAK_CLIENT_SECRET` environment variable.

## Management and Monitoring
- **management.tracing.sampling.probability**: Defines the probability for tracing sampling. Set to `1.0` it enables full tracing.
- **management.metrics.distribution.percentiles-histogram.http.server.requests**: Enables percentile histograms for HTTP server requests.
- **management.endpoints.web.exposure.include**: Specifies which management endpoints are exposed over the web. Set to `*` to expose all endpoints.
- **management.endpoint.health.show-details**: Shows application endpoints health information details. Set to `always` to show full details.
- **management.observations.key-values.application**: Sets an observation key-value for the application name, which is `proven-ai-api`.

## SpringDoc-Swagger UI
- **springdoc.swagger-ui.path**: Defines the path for accessing the Swagger UI. Set to `/api-documentation`.

## ProvenAI SSI Configuration
- **proven-ai.ssi.issuer-private-jwk**: Specifies the issuer's private JWK, configurable via the `ISSUER_PRIVATE_JWK` environment variable.
- **proven-ai.ssi.issuer-did**: Specifies the issuer's DID (Decentralized Identifier), configurable via the `ISSUER_DID` environment variable.
- **proven-ai.ssi.verifier.url**: The URL for the SSI verifier service.
- **proven-ai.ssi.issuer.url**: The URL for the SSI issuer service.

## OpenAPI Configuration
- **proven-ai.openapi.servers.local-url**: The local OpenAPI server URL, set to `http://localhost:<port>` concatenated with the context path.
- **proven-ai.openapi.servers.dev-url**: The development OpenAPI server URL, set to `https://dev.proven-ai.ctrlspace.dev` concatenated with the context path.
- **proven-ai.openapi.servers.prod-url**: The production OpenAPI server URL, set to `https://app.proven-ai.ctrlspace.dev` concatenated with the context path.

## Gendox Domain API Integration
- **proven-ai.domains.gendox.base-url**: The base URL for Gendox API. It is defined in each `application.properties.yml` file depending on the environment.
- **proven-ai.domains.gendox.context-path**: The context path for Gendox API, set to `/gendox/api/v1`.
- **proven-ai.domains.gendox.apis.user-profile**: The endpoint for user profiles.
- **proven-ai.domains.gendox.apis.semantic-search**: The endpoint for semantic search.
- **proven-ai.domains.gendox.apis.completion**: The endpoint for semantic completion.
- **proven-ai.domains.gendox.apis.web-hook**: The endpoint for webhooks related to ProvenAI events.

## Proven-AI Frontend
- **proven-ai.domains.proven-ai-frontend.base-url**: The base URL for the Proven-AI frontend. It is defined in each `application.properties.yml` file depending on the environment.
- **proven-ai.domains.proven-ai-frontend.context-path**: The context path for the frontend, set to the same as the server context path.

## Blockchain Configuration
- **proven-ai.blockchain.rpc-url**: Specifies the RPC URL for the blockchain, configurable via the `BLOCKCHAIN_RPC_URL` environment variable.
- **proven-ai.blockchain.contract-address**: Specifies the contract address for the blockchain, configurable via the `BLOCKCHAIN_CONTRACT_ADDRESS` environment variable.
- **proven-ai.blockchain.private-key**: Specifies the private key for interacting with the blockchain, configurable via the `BLOCKCHAIN_PRIVATE_KEY` environment variable.
- **proven-ai.blockchain.chain-id**: Specifies the chain ID of the blockchain network, configurable via the `BLOCKCHAIN_CHAIN_ID` environment variable. Default is `0`.
