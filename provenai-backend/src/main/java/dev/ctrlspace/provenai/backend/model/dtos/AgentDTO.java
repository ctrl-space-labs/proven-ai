package dev.ctrlspace.provenai.backend.model.dtos;

import com.fasterxml.jackson.databind.util.JSONPObject;
import dev.ctrlspace.provenai.ssi.model.vc.attestation.Policy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class AgentDTO {
    private UUID id;
    private UUID organizationId;
    private String agentVcJwt;
    private Instant createdAt;
    private Instant updatedAt;
    private UUID createdBy;
    private UUID updatedBy;
    private String agentName;
    private List<Policy> usagePolicies;

}
