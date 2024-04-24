package dev.ctrlspace.provenai.backend.model;

import com.fasterxml.jackson.databind.util.JSONPObject;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "agents", schema = "proven_ai")
public class Agent {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private UUID id;
    @Basic
    @Column(name = "organization_id")
    private UUID organizationId;
    @Column(columnDefinition = "jsonb", name = "agent_verifiable_id")
    @Type(JsonBinaryType.class)
    private JSONPObject agentVcId;
    @Basic
    @Column(name = "created_at")
    private Object createdAt;
    @Basic
    @Column(name = "updated_at")
    private Object updatedAt;
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

    public JSONPObject getAgentVcId() {
        return agentVcId;
    }

    public void setAgentVcId(JSONPObject agentVcId) {
        this.agentVcId = agentVcId;
    }

    public Object getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Object createdAt) {
        this.createdAt = createdAt;
    }

    public Object getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Object updatedAt) {
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
        Agent that = (Agent) o;
        return Objects.equals(id, that.id) && Objects.equals(organizationId, that.organizationId) && Objects.equals(agentVcId, that.agentVcId) && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt) && Objects.equals(createdBy, that.createdBy) && Objects.equals(updatedBy, that.updatedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, organizationId, agentVcId, createdAt, updatedAt, createdBy, updatedBy);
    }
}
