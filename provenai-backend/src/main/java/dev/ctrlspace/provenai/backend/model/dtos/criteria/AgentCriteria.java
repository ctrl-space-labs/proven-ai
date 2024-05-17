package dev.ctrlspace.provenai.backend.model.dtos.criteria;

import dev.ctrlspace.provenai.backend.model.AgentPurposeOfUsePolicies;
import dev.ctrlspace.provenai.ssi.model.vc.attestation.Policy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

}
