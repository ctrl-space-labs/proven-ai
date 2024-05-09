package dev.ctrlspace.provenai.backend.services;

import dev.ctrlspace.provenai.backend.model.AgentPurposeOfUsePolicies;
import dev.ctrlspace.provenai.backend.repositories.AgentPurposeOfUsePoliciesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AgentPurposeOfUsePoliciesService {


    private AgentPurposeOfUsePoliciesRepository agentPurposeOfUsePoliciesRepository;

    @Autowired
    public AgentPurposeOfUsePoliciesService(AgentPurposeOfUsePoliciesRepository agentPurposeOfUsePoliciesRepository) {
        this.agentPurposeOfUsePoliciesRepository = agentPurposeOfUsePoliciesRepository;
    }


    public List<AgentPurposeOfUsePolicies> getAgentPurposeOfUsePolicies(UUID agentId) {
        return agentPurposeOfUsePoliciesRepository.findByAgentId(agentId);
    }

}
