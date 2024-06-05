package dev.ctrlspace.provenai.backend.model;

import com.fasterxml.jackson.databind.util.JSONPObject;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "agents", schema = "proven_ai")
public class Agent {
    @Id
    @Column(name = "id")
    private UUID id;
    @Basic
    @Column(name = "organization_id")
    private UUID organizationId;

    @Basic
    @Column(name = "agent_vc_jwt", nullable = true)
    private String agentVcJwt;

    @Basic
    @Column(name = "agent_name")
    private String agentName;

    @Basic
    @Column(name = "created_at")
    private Instant createdAt;
    @Basic
    @Column(name = "updated_at")
    private Instant updatedAt;
    @Basic
    @Column(name = "created_by")
    private UUID createdBy;
    @Basic
    @Column(name = "updated_by")
    private UUID updatedBy;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(UUID organizationId) {
        this.organizationId = organizationId;
    }

    public String getAgentVcJwt() {return agentVcJwt;}

    public void setAgentVcJwt(String agentVcJwt) {this.agentVcJwt = agentVcJwt;}

    public String getAgentName() {return agentName;}

    public void setAgentName(String agentName) {this.agentName = agentName;}

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UUID getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
    }

    public UUID getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(UUID updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Agent agent = (Agent) o;
        return Objects.equals(id, agent.id) && Objects.equals(organizationId, agent.organizationId) && Objects.equals(agentVcJwt, agent.agentVcJwt) && Objects.equals(agentName, agent.agentName) && Objects.equals(createdAt, agent.createdAt) && Objects.equals(updatedAt, agent.updatedAt) && Objects.equals(createdBy, agent.createdBy) && Objects.equals(updatedBy, agent.updatedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, organizationId, agentVcJwt, agentName, createdAt, updatedAt, createdBy, updatedBy);
    }


}
