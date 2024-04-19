package dev.ctrlspace.provenai.backend.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "audit_permission_of_use_vc", schema = "proven_ai")
public class AuditPermissionOfUseVc {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "permission_of_use_vc_id")
    private UUID permissionOfUseVcId;
    @Basic
    @Column(name = "search_id")
    private UUID searchId;
    @Basic
    @Column(name = "section_iscc")
    private String sectionIscc;
    @Basic
    @Column(name = "owner_organization_id")
    private UUID ownerOrganizationId;
    @Basic
    @Column(name = "processor_organization_id")
    private UUID processorOrganizationId;
    @Basic
    @Column(name = "tokens")
    private Integer tokens;
    @Basic
    @Column(name = "created_at")
    private Instant createdAt;
    @Basic
    @Column(name = "updated_at")
    private Instant updatedAt;

    public Object getPermissionOfUseVcId() {
        return permissionOfUseVcId;
    }

    public void setPermissionOfUseVcId(UUID permissionOfUseVcId) {
        this.permissionOfUseVcId = permissionOfUseVcId;
    }

    public UUID getSearchId() {
        return searchId;
    }

    public void setSearchId(UUID searchId) {
        this.searchId = searchId;
    }

    public String getSectionIscc() {
        return sectionIscc;
    }

    public void setSectionIscc(String sectionIscc) {
        this.sectionIscc = sectionIscc;
    }

    public UUID getOwnerOrganizationId() {
        return ownerOrganizationId;
    }

    public void setOwnerOrganizationId(UUID ownerOrganizationId) {
        this.ownerOrganizationId = ownerOrganizationId;
    }

    public UUID getProcessorOrganizationId() {
        return processorOrganizationId;
    }

    public void setProcessorOrganizationId(UUID processorOrganizationId) {
        this.processorOrganizationId = processorOrganizationId;
    }

    public Integer getTokens() {
        return tokens;
    }

    public void setTokens(Integer tokens) {
        this.tokens = tokens;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuditPermissionOfUseVc that = (AuditPermissionOfUseVc) o;
        return Objects.equals(permissionOfUseVcId, that.permissionOfUseVcId) && Objects.equals(searchId, that.searchId) && Objects.equals(sectionIscc, that.sectionIscc) && Objects.equals(ownerOrganizationId, that.ownerOrganizationId) && Objects.equals(processorOrganizationId, that.processorOrganizationId) && Objects.equals(tokens, that.tokens) && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(permissionOfUseVcId, searchId, sectionIscc, ownerOrganizationId, processorOrganizationId, tokens, createdAt, updatedAt);
    }
}
