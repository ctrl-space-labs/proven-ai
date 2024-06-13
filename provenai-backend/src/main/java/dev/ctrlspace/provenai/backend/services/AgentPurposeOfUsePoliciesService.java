package dev.ctrlspace.provenai.backend.services;

import dev.ctrlspace.provenai.backend.exceptions.ProvenAiException;
import dev.ctrlspace.provenai.backend.model.*;
import dev.ctrlspace.provenai.backend.repositories.AgentPurposeOfUsePoliciesRepository;
import dev.ctrlspace.provenai.backend.repositories.PolicyOptionRepository;
import dev.ctrlspace.provenai.backend.repositories.PolicyTypeRepository;
import dev.ctrlspace.provenai.ssi.model.vc.attestation.Policy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AgentPurposeOfUsePoliciesService {


    private PolicyOptionRepository policyOptionRepository;

    private AgentPurposeOfUsePoliciesRepository agentPurposeOfUsePoliciesRepository;

    private PolicyTypeRepository policyTypeRepository;

    @Autowired
    public AgentPurposeOfUsePoliciesService(AgentPurposeOfUsePoliciesRepository agentPurposeOfUsePoliciesRepository,
                                            PolicyOptionRepository policyOptionRepository,
                                            PolicyTypeRepository policyTypeRepository) {
        this.agentPurposeOfUsePoliciesRepository = agentPurposeOfUsePoliciesRepository;
        this.policyOptionRepository = policyOptionRepository;
        this.policyTypeRepository = policyTypeRepository;

    }


    public List<AgentPurposeOfUsePolicies> getAgentPurposeOfUsePolicies(UUID agentId) {
        return agentPurposeOfUsePoliciesRepository.findByAgentId(agentId);
    }

    public Page<AgentPurposeOfUsePolicies> getAgentPurposeOfUsePolicies(UUID agentId, Pageable pageable) {
        return agentPurposeOfUsePoliciesRepository.findByAgentId(agentId, pageable);
    }




    public List<AgentPurposeOfUsePolicies> savePoliciesForAgent(Agent savedAgent, List<Policy> policies) {
        List<AgentPurposeOfUsePolicies> savedPolicies = policies.stream()
                .map(policy -> {
                    // Retrieve PolicyOption based on policy type name
                    PolicyOption policyOption = policyOptionRepository.findByName(policy.getPolicyValue());
                    PolicyType policyType = policyTypeRepository.findById(policyOption.getPolicyTypeId())
                            .orElseThrow(() -> new RuntimeException("Policy Type not found"));

                    AgentPurposeOfUsePolicies agentPurposeOfUsePolicy = new AgentPurposeOfUsePolicies();
                    agentPurposeOfUsePolicy.setAgentId(savedAgent.getId());
                    agentPurposeOfUsePolicy.setPolicyOption(policyOption);
                    agentPurposeOfUsePolicy.setValue(policy.getPolicyValue());
                    agentPurposeOfUsePolicy.setPolicyType(policyType);
                    agentPurposeOfUsePolicy.setCreatedAt(Instant.now());
                    agentPurposeOfUsePolicy.setUpdatedAt(Instant.now());

                    return agentPurposeOfUsePolicy;
                })
                .collect(Collectors.toList());

        // Save all the policies
        return agentPurposeOfUsePoliciesRepository.saveAll(savedPolicies);
    }


    public void deleteAgentPurposeOfUsePoliciesByAgentId(UUID agentId) {
        List<AgentPurposeOfUsePolicies> policies = agentPurposeOfUsePoliciesRepository.findByAgentId(agentId);
        agentPurposeOfUsePoliciesRepository.deleteAll(policies);    }

}
