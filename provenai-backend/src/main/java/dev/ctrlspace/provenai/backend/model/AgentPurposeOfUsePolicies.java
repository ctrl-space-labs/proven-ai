package dev.ctrlspace.provenai.backend.model;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "agent_purpose_of_use_policies", schema = "proven_ai", catalog = "postgres")
public class AgentPurposeOfUsePolicies {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private UUID id;
    @Basic
    @Column(name = "agent_id")
    private UUID agentId;
    @Basic
    @Column(name = "policy_type_id")
    private UUID policyTypeId;
    @Basic
    @Column(name = "policy_option_id")
    private UUID policyOptionId;
    @Basic
    @Column(name = "value")
    private String value;
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

    public UUID getAgentId() {
        return agentId;
    }

    public void setAgentId(UUID agentId) {
        this.agentId = agentId;
    }

    public UUID getPolicyTypeId() {
        return policyTypeId;
    }

    public void setPolicyTypeId(UUID policyTypeId) {
        this.policyTypeId = policyTypeId;
    }

    public UUID getPolicyOptionId() {
        return policyOptionId;
    }

    public void setPolicyOptionId(UUID policyOptionId) {
        this.policyOptionId = policyOptionId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
        AgentPurposeOfUsePolicies that = (AgentPurposeOfUsePolicies) o;
        return Objects.equals(id, that.id) && Objects.equals(agentId, that.agentId) && Objects.equals(policyTypeId, that.policyTypeId) && Objects.equals(policyOptionId, that.policyOptionId) && Objects.equals(value, that.value) && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt) && Objects.equals(createdBy, that.createdBy) && Objects.equals(updatedBy, that.updatedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, agentId, policyTypeId, policyOptionId, value, createdAt, updatedAt, createdBy, updatedBy);
    }
}
