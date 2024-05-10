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

    private String vatNumber;

    private String verifiablePresentation;

    private String organizationDid;

    private Instant createdAt;

    private Instant updatedAt;

    private UUID createdBy;

    private UUID updatedBy;

}
