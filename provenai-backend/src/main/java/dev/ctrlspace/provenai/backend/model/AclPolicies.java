package dev.ctrlspace.provenai.backend.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "acl_policies", schema = "proven_ai")
public class AclPolicies {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "data_pod_id", referencedColumnName = "id", nullable = false)
    private DataPod dataPod;
    @ManyToOne
    @JoinColumn(name = "policy_type_id", referencedColumnName = "id", nullable = false)
    private PolicyType policyType;
    @ManyToOne
    @JoinColumn(name = "policy_option_id", referencedColumnName = "id", nullable = false)
    private PolicyOption policyOption;
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

    public DataPod getDataPod() {
        return dataPod;
    }

    public void setDataPod(DataPod dataPod) {
        this.dataPod = dataPod;
    }

    public PolicyType getPolicyType() {
        return policyType;
    }

    public void setPolicyType(PolicyType policyType) {
        this.policyType = policyType;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AclPolicies that = (AclPolicies) o;
        return Objects.equals(id, that.id) && Objects.equals(dataPod, that.dataPod) && Objects.equals(policyType, that.policyType) && Objects.equals(policyOption, that.policyOption) && Objects.equals(value, that.value) && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt) && Objects.equals(createdBy, that.createdBy) && Objects.equals(updatedBy, that.updatedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dataPod, policyType, policyOption, value, createdAt, updatedAt, createdBy, updatedBy);
    }
}
