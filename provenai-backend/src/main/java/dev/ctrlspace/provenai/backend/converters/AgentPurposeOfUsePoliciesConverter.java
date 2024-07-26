package dev.ctrlspace.provenai.backend.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.ctrlspace.provenai.backend.exceptions.ProvenAiException;
import dev.ctrlspace.provenai.backend.model.AgentPurposeOfUsePolicies;
import dev.ctrlspace.provenai.backend.model.PolicyOption;
import dev.ctrlspace.provenai.backend.model.PolicyType;
import dev.ctrlspace.provenai.backend.model.dtos.AgentPurposeOfUsePoliciesDTO;
import dev.ctrlspace.provenai.backend.repositories.PolicyTypeRepository;
import dev.ctrlspace.provenai.ssi.model.vc.attestation.Policy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import dev.ctrlspace.provenai.backend.repositories.PolicyOptionRepository;

import java.util.Optional;

@Component
public class AgentPurposeOfUsePoliciesConverter implements ProvenAIConverter<AgentPurposeOfUsePolicies, AgentPurposeOfUsePoliciesDTO>{

    private PolicyOptionRepository policyOptionRepository;
    private PolicyTypeRepository policyTypeRepository;

    @Autowired
    public AgentPurposeOfUsePoliciesConverter(PolicyOptionRepository policyOptionRepository, PolicyTypeRepository policyTypeRepository) {
        this.policyOptionRepository = policyOptionRepository;
        this.policyTypeRepository = policyTypeRepository;
    }


    public Policy toPolicy(AgentPurposeOfUsePolicies agentPurposeOfUsePolicy) {
        Policy policy = new Policy();

        Optional<PolicyOption> policyOption = policyOptionRepository.findById(agentPurposeOfUsePolicy.getPolicyOption().getId());
        Optional<PolicyType> policyType = policyTypeRepository.findById(agentPurposeOfUsePolicy.getPolicyType().getId());
        // Set policy type and value in the Policy object
        policy.setPolicyType(policyType.get().getName());
        policy.setPolicyValue(policyOption.get().getName());
        return policy;
    }

    @Override
    public AgentPurposeOfUsePoliciesDTO toDTO(AgentPurposeOfUsePolicies agentPurposeOfUsePolicies) throws Exception, JsonProcessingException {
        AgentPurposeOfUsePoliciesDTO agentPurposeOfUsePoliciesDTO = new AgentPurposeOfUsePoliciesDTO();

        agentPurposeOfUsePoliciesDTO.setId(agentPurposeOfUsePolicies.getId());
        agentPurposeOfUsePoliciesDTO.setAgentId(agentPurposeOfUsePolicies.getAgentId());
        agentPurposeOfUsePoliciesDTO.setPolicyTypeId(agentPurposeOfUsePolicies.getPolicyType().getId());
        agentPurposeOfUsePoliciesDTO.setPolicyOptionId(agentPurposeOfUsePolicies.getPolicyOption().getId());
        agentPurposeOfUsePoliciesDTO.setValue(agentPurposeOfUsePolicies.getValue());
        agentPurposeOfUsePoliciesDTO.setCreatedAt(agentPurposeOfUsePolicies.getCreatedAt());
        agentPurposeOfUsePoliciesDTO.setUpdatedAt(agentPurposeOfUsePolicies.getUpdatedAt());
        agentPurposeOfUsePoliciesDTO.setCreatedBy(agentPurposeOfUsePolicies.getCreatedBy());
        agentPurposeOfUsePoliciesDTO.setUpdatedBy(agentPurposeOfUsePolicies.getUpdatedBy());

        return agentPurposeOfUsePoliciesDTO;
    }

    @Override
    public AgentPurposeOfUsePolicies toEntity(AgentPurposeOfUsePoliciesDTO agentPurposeOfUsePoliciesDTO) throws ProvenAiException {
        AgentPurposeOfUsePolicies agentPurposeOfUsePolicies = new AgentPurposeOfUsePolicies();

        agentPurposeOfUsePolicies.setId(agentPurposeOfUsePoliciesDTO.getId());
        agentPurposeOfUsePolicies.setAgentId(agentPurposeOfUsePoliciesDTO.getAgentId());

        PolicyType policyType = new PolicyType();
        policyType = policyTypeRepository.findById(agentPurposeOfUsePoliciesDTO.getPolicyTypeId())
                .orElseThrow(() -> new ProvenAiException("POLICY_TYPE_NOT_FOUND","Policy Type not found", HttpStatus.NOT_FOUND));
        agentPurposeOfUsePolicies.setPolicyType(policyType);

        PolicyOption policyOption = new PolicyOption();
        policyOption = policyOptionRepository.findById(agentPurposeOfUsePoliciesDTO.getPolicyOptionId())
                .orElseThrow(() -> new ProvenAiException("POLICY_OPTION_NOT_FOUND","Policy Option not found", HttpStatus.NOT_FOUND));
        agentPurposeOfUsePolicies.setPolicyOption(policyOption);
        agentPurposeOfUsePolicies.setValue(agentPurposeOfUsePoliciesDTO.getValue());
        agentPurposeOfUsePolicies.setCreatedAt(agentPurposeOfUsePoliciesDTO.getCreatedAt());
        agentPurposeOfUsePolicies.setUpdatedAt(agentPurposeOfUsePoliciesDTO.getUpdatedAt());
        agentPurposeOfUsePolicies.setCreatedBy(agentPurposeOfUsePoliciesDTO.getCreatedBy());
        agentPurposeOfUsePolicies.setUpdatedBy(agentPurposeOfUsePoliciesDTO.getUpdatedBy());

        return agentPurposeOfUsePolicies;


    }
}
