package dev.ctrlspace.provenai.backend.model.dtos;

import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class OrganizationDTO {
    private UUID id;
    private String name;
    private String country;
    private Boolean naturalPerson;
    private String legalPersonIdentifier;
    private String legalName;
    private String legalAddress;
    private String taxReference;
    private String familyName;
    private String firstName;
    private Instant dateOfBirth;
    private String gender;
    private String nationality;
    private String personalIdentifier;
    private String vatNumber;
    private String organizationVpJwt;
    private String organizationDid;


}
