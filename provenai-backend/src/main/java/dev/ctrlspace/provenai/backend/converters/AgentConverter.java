package dev.ctrlspace.provenai.backend.converters;

import dev.ctrlspace.provenai.backend.model.Agent;
import dev.ctrlspace.provenai.backend.model.dtos.AgentDTO;
import org.springframework.stereotype.Component;

@Component
public class AgentConverter implements ProvenAIConverter<Agent, AgentDTO> {

    @Override
    public AgentDTO toDTO(Agent agent) {
        AgentDTO agentDTO = new AgentDTO();

        agentDTO.setId(agent.getId());
        agentDTO.setOrganizationId(agent.getOrganizationId());
        agentDTO.setAgentName(agent.getAgentName());
        agentDTO.setAgentVcJwt(agent.getAgentVcJwt());
        agentDTO.setCreatedAt(agent.getCreatedAt());
        agentDTO.setUpdatedAt(agent.getUpdatedAt());
        agentDTO.setCreatedBy(agent.getCreatedBy());
        agentDTO.setUpdatedBy(agent.getUpdatedBy());

        return agentDTO;

    }


    @Override
    public Agent toEntity(AgentDTO agentDTO) {
        Agent agent = new Agent();

        agent.setId(agentDTO.getId());
        agent.setOrganizationId(agentDTO.getOrganizationId());
        agent.setAgentName(agentDTO.getAgentName());
        agent.setAgentVcJwt(agentDTO.getAgentVcJwt());
        agent.setCreatedAt(agentDTO.getCreatedAt());
        agent.setUpdatedAt(agentDTO.getUpdatedAt());
        agent.setCreatedBy(agentDTO.getCreatedBy());
        agent.setUpdatedBy(agentDTO.getUpdatedBy());

        return agent;
    }
}