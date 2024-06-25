package dev.ctrlspace.provenai.backend.utils;

import com.fasterxml.jackson.databind.JsonNode;
import dev.ctrlspace.provenai.backend.exceptions.ProvenAiException;
import dev.ctrlspace.provenai.backend.model.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class ValidatorUtils {

    private JWTUtils jwtUtils;

    @Autowired
    public ValidatorUtils(JWTUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    public static Set<String> SUPPORTED_TYPES = new HashSet<>(Arrays.asList(
            "NaturalPersonVerifiableID",
            "LegalEntityVerifiableID"
    ));

    private String getCredentialType(JsonNode typeArray) {
        for (JsonNode type : typeArray) {
            if (SUPPORTED_TYPES.contains(type.asText())) {
                return type.asText();
            }
        }
        return null;
    }

    public Boolean validateCredentialSubjectFields(String jwt, Organization organization) throws IOException, ProvenAiException {
        JsonNode payload = jwtUtils.getPayloadFromJwt(jwt);

        if (organization.getIsNaturalPerson().equals(true)) {
            return validateNaturalPersonCredentialSubject(payload, organization);
        } else if (organization.getIsNaturalPerson().equals(false)) {
            return validateLegalEntityCredentialSubject(payload, organization);
        } else {
            throw new ProvenAiException("UNSUPPORTED_CREDENTIAL_TYPE", "Unsupported credential type in VC", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }
    }


    public Boolean validateNaturalPersonCredentialSubject(JsonNode payload, Organization organization) throws IOException {

        JsonNode credentialSubject = payload
                .path("vc")
                .path("credentialSubject");

        String familyName = credentialSubject.get("familyName").asText();
        String firstName = credentialSubject.get("firstName").asText();
        String dateOfBirth = credentialSubject.get("dateOfBirth").asText();
        String personalIdentifier = credentialSubject.get("personalIdentifier").asText();
//        String nationality = credentialSubject.get("nationality").asText();
        String gender = credentialSubject.get("gender").asText();

        // Compare extracted fields with corresponding fields in Organization entity
        return familyName.equals(organization.getFamilyName())
                && firstName.equals(organization.getFirstName())
                && dateOfBirth.equals(organization.getDateOfBirth().toString())
                && personalIdentifier.equals(organization.getPersonalIdentifier())
//                && nationality.equals(organization.getNationality())
                && gender.equals(organization.getGender());
    }

    public Boolean validateLegalEntityCredentialSubject(JsonNode payload, Organization organization) throws IOException {
        // Extract credentialSubject node
        JsonNode credentialSubject = payload
                .path("vc")
                .path("credentialSubject");

        String legalPersonIdentifier = credentialSubject.get("legalPersonIdentifier").asText();
        String legalName = credentialSubject.get("legalName").asText();
        String legalAddress = credentialSubject.get("legalAddress").asText();
        String VATRegistration = credentialSubject.get("VATRegistration").asText();
        String taxReference = credentialSubject.get("taxReference").asText();

        return legalPersonIdentifier.equals(organization.getLegalPersonIdentifier())
                && legalName.equals(organization.getLegalName())
                && legalAddress.equals(organization.getLegalAddress())
                && VATRegistration.equals(organization.getVatNumber())
                && taxReference.equals(organization.getTaxReference());
    }

}
