package dev.ctrlspace.provenai.backend.authentication;

import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.Payload;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.time.Instant;
import java.util.Map;

@Component
public class KeycloakAuthenticationService {


        private Logger logger = LoggerFactory.getLogger(KeycloakAuthenticationService.class);

        private RestTemplate restTemplate = new RestTemplate();

        private String keycloakServerUrl;

        private String keycloakTokenUrl;

        private String realm;

        private String clientId;

        private String clientSecret;

        private Keycloak keycloakClient;


        public KeycloakAuthenticationService(@Value("${keycloak.base-url}") String keycloakServerUrl,
                                             @Value("${keycloak.token-uri}") String keycloakTokenUrl,
                                             @Value("${keycloak.realm}") String realm,
                                             @Value("${keycloak.client-id}") String clientId,
                                             @Value("${keycloak.client-secret}") String clientSecret) {

                this.keycloakServerUrl = keycloakServerUrl;
                this.keycloakTokenUrl = keycloakTokenUrl;
                this.realm = realm;
                this.clientId = clientId;
                this.clientSecret = clientSecret;

                keycloakClient = KeycloakBuilder.builder()
                        .serverUrl(keycloakServerUrl)
                        .realm(realm)
                        .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                        .clientId(clientId)
                        .clientSecret(clientSecret)
                        .build();

        }

        public Jwt impersonateUser(String username) {

                AccessTokenResponse clientToken = keycloakClient.tokenManager().getAccessToken();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
                map.add("grant_type", OAuth2Constants.TOKEN_EXCHANGE_GRANT_TYPE);
                map.add("client_id", clientId);
                map.add("client_secret", clientSecret);
                map.add("subject_token", clientToken.getToken());
                map.add("requested_subject", username);
                map.add("requested_token_type", OAuth2Constants.ACCESS_TOKEN_TYPE);

                HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);


                ResponseEntity<AccessTokenResponse> impersonationToken = restTemplate.postForEntity(
                        keycloakTokenUrl,
                        request,
                        AccessTokenResponse.class);

                String tokenString = impersonationToken.getBody().getToken();
                // Parse the JWT token
                SignedJWT signedJWT = null;
                try {
                        signedJWT = (SignedJWT) JWTParser.parse(tokenString);
                } catch (ParseException e) {
                        throw new RuntimeException(e);
                }

                // Extract headers and claims
                JWSHeader jwtHeaders = signedJWT.getHeader();
                Payload payload = signedJWT.getPayload();


                // Convert Unix timestamps to Instant
                Map<String, Object> claims = payload.toJSONObject();
                convertTimestampsToInstant(claims);

                // Rebuild the token using Spring Boot's Jwt class
                Jwt jwt = Jwt.withTokenValue(tokenString)
                        .headers(h -> h.putAll(jwtHeaders.toJSONObject()))
                        .claims(c -> c.putAll(claims))
                        .build();


                return jwt;

        }

        private void convertTimestampsToInstant(Map<String, Object> claims) {
                convertToInstant(claims, "exp");
                convertToInstant(claims, "iat");
                convertToInstant(claims, "nbf");
        }


        private void convertToInstant(Map<String, Object> claims, String claimKey) {
                if (claims.containsKey(claimKey)) {
                        Object timestamp = claims.get(claimKey);
                        if (timestamp instanceof Long) {
                                Instant instant = Instant.ofEpochSecond((Long) timestamp);
                                claims.put(claimKey, instant);
                        }
                }
        }


}
