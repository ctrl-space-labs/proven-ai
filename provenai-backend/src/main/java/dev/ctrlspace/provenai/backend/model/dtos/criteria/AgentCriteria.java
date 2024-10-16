package dev.ctrlspace.provenai.backend.model.dtos.criteria;

import dev.ctrlspace.provenai.backend.model.AgentPurposeOfUsePolicies;
import dev.ctrlspace.provenai.ssi.model.vc.attestation.Policy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class AgentCriteria {

    private String organizationId;
    private String agentName;
    private List<AgentPurposeOfUsePolicies> agentPurposeOfUsePolicies;
    private List<Policy> policyIn;
    private Policy policy;
    private String agentVcJwt;

    private List<String> organizationIdIn;
    private List<UUID> agentIdIn  = new ArrayList<>();;
    private boolean fetchAll;

}
