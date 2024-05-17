package dev.ctrlspace.provenai.backend.services;

import dev.ctrlspace.provenai.backend.model.Agent;
import dev.ctrlspace.provenai.backend.model.AgentPurposeOfUsePolicies;
import dev.ctrlspace.provenai.backend.model.PolicyOption;
import dev.ctrlspace.provenai.backend.repositories.AgentPurposeOfUsePoliciesRepository;
import dev.ctrlspace.provenai.backend.repositories.PolicyOptionRepository;
import dev.ctrlspace.provenai.ssi.model.vc.attestation.Policy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AgentPurposeOfUsePoliciesService {


    private PolicyOptionRepository policyOptionRepository;

    private AgentPurposeOfUsePoliciesRepository agentPurposeOfUsePoliciesRepository;

    @Autowired
    public AgentPurposeOfUsePoliciesService(AgentPurposeOfUsePoliciesRepository agentPurposeOfUsePoliciesRepository,
                                            PolicyOptionRepository policyOptionRepository) {
        this.agentPurposeOfUsePoliciesRepository = agentPurposeOfUsePoliciesRepository;
        this.policyOptionRepository = policyOptionRepository;

    }


    public List<AgentPurposeOfUsePolicies> getAgentPurposeOfUsePolicies(UUID agentId) {
        return agentPurposeOfUsePoliciesRepository.findByAgentId(agentId);
    }


    public List<AgentPurposeOfUsePolicies> savePoliciesForAgent(Agent savedAgent, List<Policy> policies) {
        List<AgentPurposeOfUsePolicies> savedPolicies = policies.stream()
                .map(policy -> {
                    // Retrieve PolicyOption based on policy type name
                    PolicyOption policyOption = policyOptionRepository.findByName(policy.getPolicyValue());

                    AgentPurposeOfUsePolicies agentPurposeOfUsePolicy = new AgentPurposeOfUsePolicies();
                    agentPurposeOfUsePolicy.setAgentId(savedAgent.getId());
                    agentPurposeOfUsePolicy.setPolicyOptionId(policyOption.getId());
                    agentPurposeOfUsePolicy.setValue(policy.getPolicyValue());
                    agentPurposeOfUsePolicy.setPolicyTypeId(policyOption.getPolicyTypeId());
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
