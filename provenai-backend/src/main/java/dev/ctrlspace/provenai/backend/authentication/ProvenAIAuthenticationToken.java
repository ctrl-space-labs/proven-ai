package dev.ctrlspace.provenai.backend.authentication;

import dev.ctrlspace.provenai.backend.model.authentication.UserProfile;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;

/**
 * This class is used to store the user profile and the original JWT token.
 * The default IDP is Keycloak with OIDC.
 *
 */
public class ProvenAIAuthenticationToken extends AbstractAuthenticationToken {
    private UserProfile userProfile;
    private Jwt jwt;

    public ProvenAIAuthenticationToken(UserProfile userProfile, Jwt jwt, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.userProfile = userProfile;
        this.jwt = jwt;
        setAuthenticated(true); // must use super, as we override the method

    }

    public ProvenAIAuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

    @Override
    public Object getCredentials() {
        return jwt.getTokenValue();
    }

    @Override
    public UserProfile getPrincipal() {
        return userProfile;
    }

    public Jwt getJwt() {
        return jwt;
    }
}
