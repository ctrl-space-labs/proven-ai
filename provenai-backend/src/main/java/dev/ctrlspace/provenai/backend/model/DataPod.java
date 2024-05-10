package dev.ctrlspace.provenai.backend.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "data_pod", schema = "proven_ai")
public class DataPod {
    @Id
    @Column(name = "id")
    private UUID id;
    @Basic
    @Column(name = "organization_id")
    private UUID organizationId;
    @Basic
    @Column(name = "pod_unique_name")
    private String podUniqueName;

    @Basic
    @Column(name = "host_url")
    private String hostUrl;

    @OneToMany(mappedBy = "dataPod")
    private List<AclPolicies> aclPolicies = new ArrayList<>();

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

    public String getPodUniqueName() {
        return podUniqueName;
    }

    public void setPodUniqueName(String podUniqueName) {
        this.podUniqueName = podUniqueName;
    }

    public String getHostUrl() {
        return hostUrl;
    }

    public void setHostUrl(String hostUrl) {
        this.hostUrl = hostUrl;
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
        DataPod dataPod = (DataPod) o;
        return Objects.equals(id, dataPod.id) && Objects.equals(organizationId, dataPod.organizationId) && Objects.equals(podUniqueName, dataPod.podUniqueName) && Objects.equals(hostUrl, dataPod.hostUrl) && Objects.equals(aclPolicies, dataPod.aclPolicies) && Objects.equals(createdAt, dataPod.createdAt) && Objects.equals(updatedAt, dataPod.updatedAt) && Objects.equals(createdBy, dataPod.createdBy) && Objects.equals(updatedBy, dataPod.updatedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, organizationId, podUniqueName, hostUrl, aclPolicies, createdAt, updatedAt, createdBy, updatedBy);
    }
}
