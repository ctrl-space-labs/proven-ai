package dev.ctrlspace.provenai.backend.authentication;

import dev.ctrlspace.provenai.backend.adapters.GendoxProfileAdapter;
import dev.ctrlspace.provenai.backend.converters.JwtDTOUserProfileConverter;
import dev.ctrlspace.provenai.backend.model.authentication.JwtDTO;
import dev.ctrlspace.provenai.backend.model.authentication.UserProfile;
import dev.ctrlspace.provenai.backend.utils.JWTUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.stream.Collectors;

@Component
public class JwtUserProfileConversionFilter extends OncePerRequestFilter {

    private GendoxProfileAdapter gendoxProfileAdapter;
    private JWTUtils jwtUtils;
    private JwtDTOUserProfileConverter jwtDTOUserProfileConverter;


    @Autowired
    public JwtUserProfileConversionFilter(GendoxProfileAdapter gendoxProfileAdapter,
                                          JwtDTOUserProfileConverter jwtDTOUserProfileConverter,
                                          JWTUtils jwtUtils) {
        this.gendoxProfileAdapter = gendoxProfileAdapter;
        this.jwtDTOUserProfileConverter = jwtDTOUserProfileConverter;
        this.jwtUtils = jwtUtils;
    }


    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {

        if (SecurityContextHolder.getContext().getAuthentication() instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            Jwt jwt = (Jwt) jwtAuth.getPrincipal();

            // Extract additional information to populate CustomUserDetails
            String email = jwt.getClaimAsString("preferred_username");
            UserProfile userProfile = gendoxProfileAdapter.getUserProfile(jwt.getTokenValue());
            JwtDTO jwtDTO = jwtDTOUserProfileConverter.jwtDTO(userProfile);
            var authorities = jwtUtils.getAuthorities(jwtDTO).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());


            ProvenAIAuthenticationToken provenAIAuthenticationToken = new ProvenAIAuthenticationToken(userProfile, jwt, authorities);
            SecurityContextHolder.getContext().setAuthentication(provenAIAuthenticationToken);
        }

        filterChain.doFilter(request, response);
    }
}
