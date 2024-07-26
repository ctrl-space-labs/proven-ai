package dev.ctrlspace.provenai.backend.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.SignedJWT;
import dev.ctrlspace.provenai.backend.model.authentication.JwtClaimName;
import dev.ctrlspace.provenai.backend.model.authentication.JwtDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class JWTUtils {
    private Logger logger = LoggerFactory.getLogger(JWTUtils.class);

    public JwtDTO toJwtDTO(Jwt jwt) {
        //implement the commented constructor in JwtDTO class
        return JwtDTO.builder()
                .originalClaims(jwt.getClaims())
                .originalHeaders(jwt.getHeaders())
                .iss(jwt.getClaimAsString(JwtClaimName.ISSUER))
                .sub(jwt.getClaimAsString(JwtClaimName.SUBJECT))
                .aud(jwt.getAudience())
                .exp(jwt.getExpiresAt())
                .nbf(jwt.getNotBefore())
                .iat(jwt.getIssuedAt())
                .jti(jwt.getClaimAsString(JwtClaimName.JWT_ID))
                .userId(jwt.getClaimAsString(JwtClaimName.USER_ID))
                .email(jwt.getClaimAsString(JwtClaimName.EMAIL))
                .userName(jwt.getClaimAsString(JwtClaimName.USERNAME))
                .globalRole(jwt.getClaimAsString(JwtClaimName.GLOBAL_ROLE))
                .orgAuthoritiesMap(
                        jwt.getClaimAsStringList(JwtClaimName.SCOPE).stream()
                                .map(s -> s.split(":"))
                                .filter(outGlobalRole())
                                .collect(Collectors.toMap(
                                        s -> s[1],
                                        s -> new JwtDTO.OrganizationAuthorities(new HashSet<>(Arrays.asList(s[0]))),
                                        (orgAuth1, orgAuth2) -> {
                                            orgAuth1.orgAuthorities().addAll(orgAuth2.orgAuthorities());
                                            return orgAuth1;
                                        }))
                )
                .orgProjectsMap(
                        jwt.getClaimAsStringList(JwtClaimName.PROJECTS_ORGANIZATION).stream()
                                .map(s -> s.split(":"))
                                .collect(Collectors.toMap(
                                        s -> s[1],
                                        s -> new JwtDTO.OrganizationProject(new HashSet<>(Arrays.asList(s[0]))),
                                        (orgProj1, orgProj2) -> {
                                            orgProj1.projectIds().addAll(orgProj2.projectIds());
                                            return orgProj1;
                                        }))
                )
                .projectAgentsMap(
                        jwt.getClaimAsStringList(JwtClaimName.PROJECT_AGENTS).stream()
                                .map(s -> s.split(":"))
                                .collect(Collectors.toMap(
                                        s -> s[1],
                                        s -> new JwtDTO.ProjectAgent(new HashSet<>(Arrays.asList(s[0]))),
                                        (projAgent1, projAgent2) -> {
                                            projAgent1.agentIds().addAll(projAgent2.agentIds());
                                            return projAgent1;
                                        }))
                )
                .build();
    }

    private static Predicate<String[]> outGlobalRole() {
        return s -> !s[1].equals("GLOBAL_ROLE");
    }

//    public JWTClaimsSet toClaims(String jwtString) throws JOSEException, ParseException {
//        JWT jwt = SignedJWT.parse(jwtString);
//        return jwt.getJWTClaimsSet();
//    }

    public JwtClaimsSet toClaimsSet(JwtDTO jwtDTO) {
        JwtClaimsSet.Builder builder = JwtClaimsSet.builder()
                .issuer(jwtDTO.getIss())
                .subject(jwtDTO.getSub())
                .audience(jwtDTO.getAud())
                .expiresAt(jwtDTO.getExp())
                .notBefore(jwtDTO.getNbf())
                .issuedAt(jwtDTO.getIat())
                .id(jwtDTO.getJti())
                .claim(JwtClaimName.USER_ID, jwtDTO.getUserId().toString());

        if (jwtDTO.getEmail() != null) {
            builder.claim(JwtClaimName.EMAIL, jwtDTO.getEmail());
        }

        if (jwtDTO.getUserName() != null) {
            builder.claim(JwtClaimName.USERNAME, jwtDTO.getUserName());
        }

        builder.claim(JwtClaimName.GLOBAL_ROLE, jwtDTO.getGlobalRole())
                .claim(JwtClaimName.SCOPE, getAuthorities(jwtDTO))
                .claim(JwtClaimName.PROJECTS_ORGANIZATION, jwtDTO.getOrgProjectsMap().entrySet().stream()
                        .flatMap(entry -> entry.getValue().projectIds().stream().map(s -> s + ":" + entry.getKey().toString()))
                        .collect(Collectors.toList()))
                .claim(JwtClaimName.PROJECT_AGENTS, jwtDTO.getProjectAgentsMap().entrySet().stream()
                        .flatMap(entry -> entry.getValue().agentIds().stream().map(s -> s + ":" + entry.getKey().toString()))
                        .collect(Collectors.toList()));

        return builder.build();
    }


    public List<String> getAuthorities(JwtDTO jwtDTO) {
        List<String> authoritiesMap = jwtDTO.getOrgAuthoritiesMap().entrySet().stream()
                .flatMap(entry -> entry.getValue().orgAuthorities().stream().map(s -> s + ":" + entry.getKey().toString()))
                .collect(Collectors.toList());

        authoritiesMap.add(jwtDTO.getGlobalRole() + ":GLOBAL_ROLE");
        return authoritiesMap;
    }

    private boolean verifyJWTSignature(String jwtString, RSAKey rsaKey) throws JOSEException, ParseException {
        SignedJWT jwt = SignedJWT.parse(jwtString);
        JWSVerifier verifier = new RSASSAVerifier(rsaKey);
        return jwt.verify(verifier);
    }

    // Method to check if token has expired
    private boolean isTokenExpired(Date expirationTime) {
        return expirationTime != null && expirationTime.before(new Date());
    }

    // Method to check if token has been revoked in redis
    private boolean isTokenRevoked(String jwtString) {
        return false;
    }


    public JWT getJWT(String jwtString) throws JOSEException, ParseException {
        JWT jwt = SignedJWT.parse(jwtString);
        return jwt;
    }

    public JsonNode getPayloadFromJwt(String jwt) throws IOException {
        String[] chunks = jwt.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        // Decode payload
        String payload = new String(decoder.decode(chunks[1]));

        ObjectMapper mapper = new ObjectMapper();

        return mapper.readTree(payload);
    }

}