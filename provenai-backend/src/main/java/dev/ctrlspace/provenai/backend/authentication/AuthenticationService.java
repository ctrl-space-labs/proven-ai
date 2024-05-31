package dev.ctrlspace.provenai.backend.authentication;


import dev.ctrlspace.provenai.backend.exceptions.ProvenAiException;
import dev.ctrlspace.provenai.backend.model.authentication.UserProfile;
import jakarta.annotation.Nullable;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

/**
 * Interface for the Authentication Service.
 * This service is used to authenticate users and clients.
 * It is important all different Authentication Services (like Keycloak, LDAP, auth0, etc.) implement this interface.
 * This way, the rest of the application can use the same methods to authenticate users and clients, regardless of the
 * authentication server used.
 *
 */
public interface AuthenticationService {


    /**
     * This method is used to get an access token for the specified Client.
     *
     * @return
     */
    public String getClientTokenString();
    public Jwt getClientToken();


    public Jwt impersonateUser(String username);

    String createUser(UserProfile user, @Nullable String password, Boolean emailVerified, Boolean tempPassword) throws ProvenAiException;

    public Optional<UserRepresentation> getUsersByUsername(String userName);

}
