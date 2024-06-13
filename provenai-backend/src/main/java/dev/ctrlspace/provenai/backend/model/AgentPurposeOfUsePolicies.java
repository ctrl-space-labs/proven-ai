package dev.ctrlspace.provenai.backend.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "agent_purpose_of_use_policies", schema = "proven_ai")
public class AgentPurposeOfUsePolicies {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private UUID id;
    @Basic
    @Column(name = "agent_id")
    private UUID agentId;
    @ManyToOne
    @JoinColumn(name = "policy_type_id", referencedColumnName = "id", nullable = false)
    private PolicyType policyType;
    @ManyToOne
    @JoinColumn(name = "policy_option_id", referencedColumnName = "id", nullable = false)
    private PolicyOption policyOption;
//    @Basic
//    @Column(name = "policy_type_id")
//    private UUID policyTypeId;
//    @Basic
//    @Column(name = "policy_option_id")
//    private UUID policyOptionId;
    @Basic
    @Column(name = "value")
    private String value;
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


    public UUID getAgentId() {
        return agentId;
    }

    public void setAgentId(UUID agentId) {
        this.agentId = agentId;
    }

    public PolicyOption getPolicyOption() {
        return policyOption;
    }

    public void setPolicyOption(PolicyOption policyOption) {
        this.policyOption = policyOption;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

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

    public PolicyType getPolicyType() {
        return policyType;
    }

    public void setPolicyType(PolicyType policyType) {
        this.policyType = policyType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AgentPurposeOfUsePolicies that)) return false;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(agentId, that.agentId)) return false;
        if (!Objects.equals(policyType, that.policyType)) return false;
        if (!Objects.equals(policyOption, that.policyOption)) return false;
        if (!Objects.equals(value, that.value)) return false;
        if (!Objects.equals(createdAt, that.createdAt)) return false;
        if (!Objects.equals(updatedAt, that.updatedAt)) return false;
        if (!Objects.equals(createdBy, that.createdBy)) return false;
        return Objects.equals(updatedBy, that.updatedBy);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (agentId != null ? agentId.hashCode() : 0);
        result = 31 * result + (policyType != null ? policyType.hashCode() : 0);
        result = 31 * result + (policyOption != null ? policyOption.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
        result = 31 * result + (createdBy != null ? createdBy.hashCode() : 0);
        result = 31 * result + (updatedBy != null ? updatedBy.hashCode() : 0);
        return result;
    }
}
