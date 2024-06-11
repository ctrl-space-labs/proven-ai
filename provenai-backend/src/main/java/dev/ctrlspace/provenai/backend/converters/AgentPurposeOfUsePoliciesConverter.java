package dev.ctrlspace.provenai.backend.converters;

import dev.ctrlspace.provenai.backend.model.AgentPurposeOfUsePolicies;
import dev.ctrlspace.provenai.backend.model.PolicyOption;
import dev.ctrlspace.provenai.backend.model.PolicyType;
import dev.ctrlspace.provenai.backend.repositories.PolicyTypeRepository;
import dev.ctrlspace.provenai.ssi.model.vc.attestation.Policy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import dev.ctrlspace.provenai.backend.repositories.PolicyOptionRepository;

import java.util.Optional;

@Component
public class AgentPurposeOfUsePoliciesConverter {

    private PolicyOptionRepository policyOptionRepository;
    private PolicyTypeRepository policyTypeRepository;

    @Autowired
    public AgentPurposeOfUsePoliciesConverter(PolicyOptionRepository policyOptionRepository, PolicyTypeRepository policyTypeRepository) {
        this.policyOptionRepository = policyOptionRepository;
        this.policyTypeRepository = policyTypeRepository;
    }


    public Policy toPolicy(AgentPurposeOfUsePolicies agentPurposeOfUsePolicy) {
        Policy policy = new Policy();

        Optional<PolicyOption> policyOption = policyOptionRepository.findById(agentPurposeOfUsePolicy.getPolicyOptionId());
        Optional<PolicyType> policyType = policyTypeRepository.findById(agentPurposeOfUsePolicy.getPolicyTypeId());
        // Set policy type and value in the Policy object
        policy.setPolicyType(policyType.get().getName());
        policy.setPolicyValue(policyOption.get().getName());
        return policy;
    }
}
