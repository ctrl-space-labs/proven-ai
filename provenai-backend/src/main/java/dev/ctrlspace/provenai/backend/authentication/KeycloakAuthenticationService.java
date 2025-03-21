package dev.ctrlspace.provenai.backend.authentication;

import dev.ctrlspace.provenai.backend.exceptions.ProvenAiException;
import dev.ctrlspace.provenai.backend.model.authentication.UserProfile;
import jakarta.annotation.Nullable;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@Component
public class KeycloakAuthenticationService implements AuthenticationService {


    private Logger logger = LoggerFactory.getLogger(KeycloakAuthenticationService.class);

    private RestTemplate restTemplate = new RestTemplate();

    private String keycloakServerUrl;

    private String keycloakTokenUrl;

    private String realm;

    private String clientId;

    private String clientSecret;

    private Keycloak keycloakClient;

    private JwtDecoder jwtDecoder;

    public KeycloakAuthenticationService(@Value("${keycloak.base-url}") String keycloakServerUrl,
                                         @Value("${keycloak.token-uri}") String keycloakTokenUrl,
                                         @Value("${keycloak.realm}") String realm,
                                         @Value("${keycloak.client-id}") String clientId,
                                         @Value("${keycloak.client-secret}") String clientSecret,
                                         JwtDecoder jwtDecoder) {

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

    @Override
    public Jwt getClientToken() {
        String jwtString = getClientTokenString();
        Jwt jwt = jwtDecoder.decode(jwtString);
        return jwt;
    }

    @Override
    public String getClientTokenString() {
        String jwtString = keycloakClient.tokenManager().getAccessTokenString();
        return jwtString;
    }

    public AccessTokenResponse impersonateUser(String username, @Nullable String scope) {

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
        if (scope != null) {
            map.add("scope", scope);
        }

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);


        ResponseEntity<AccessTokenResponse> impersonationToken = restTemplate.postForEntity(
                keycloakTokenUrl,
                request,
                AccessTokenResponse.class);

        AccessTokenResponse accessTokenResponse = impersonationToken.getBody();

        return accessTokenResponse;
    }

    @Override
    public String createUser(UserProfile user, @Nullable String password, Boolean emailVerified, Boolean tempPassword) throws ProvenAiException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Optional<UserRepresentation> getUsersByUsername(String userName) {
        return Optional.empty();
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
