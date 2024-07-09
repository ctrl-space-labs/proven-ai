package dev.ctrlspace.provenai.backend.model;

import dev.ctrlspace.provenai.backend.model.dtos.AuditPermissionOfUseDTO;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "audit_permission_of_use_vc", schema = "proven_ai")
@SqlResultSetMapping(
        name = "AuditPermissionOfUseDTOMapping",
        classes = @ConstructorResult(
                targetClass = AuditPermissionOfUseDTO.class,
                columns = {
                        @ColumnResult(name = "ownerDatapodId", type = UUID.class),
                        @ColumnResult(name = "ownerOrganizationId", type = UUID.class),
                        @ColumnResult(name = "processorAgentId", type = UUID.class),
                        @ColumnResult(name = "processorOrganizationId", type = UUID.class),
                        @ColumnResult(name = "bucketStart", type = Timestamp.class),
                        @ColumnResult(name = "sumTokens", type = Long.class)
                }
        )
)
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
    @Column(name = "document_iscc")
    private String documentIscc;

    @Basic
    @Column(name = "owner_organization_did")
    private String ownerOrganizationDid;


    @Basic
    @Column(name = "owner_organization_id")
    private UUID ownerOrganizationId;

    @Basic
    @Column(name = "processor_organization_did")
    private String processorOrganizationDid;

    @Basic
    @Column(name = "processor_organization_id")
    private UUID processorOrganizationId;

    @Basic
    @Column(name = "owner_datapod_id")
    private UUID ownerDataPodId;

    @Basic
    @Column(name = "processor_agent_id")
    private UUID processorAgentId;

    @Basic
    @Column(name = "embedding_model")
    private String embeddingModel;

    @Basic
    @Column(name = "tokens")
    private Integer tokens;
    @Basic
    @Column(name = "created_at")
    private Instant createdAt;
    @Basic
    @Column(name = "updated_at")
    private Instant updatedAt;

    public UUID getPermissionOfUseVcId() {
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

    public String getDocumentIscc() {return documentIscc;}

    public void setDocumentIscc(String documentIscc) {this.documentIscc = documentIscc;}

    public String getOwnerOrganizationDid() {return ownerOrganizationDid;}

    public void setOwnerOrganizationDid(String ownerOrganizationDid) {this.ownerOrganizationDid = ownerOrganizationDid;}

    public String getProcessorOrganizationDid() {return processorOrganizationDid;}

    public void setProcessorOrganizationDid(String processorOrganizationDid) {this.processorOrganizationDid = processorOrganizationDid;}

    public UUID getOwnerDataPodId() {return ownerDataPodId;}

    public void setOwnerDataPodId(UUID dataPodId) {this.ownerDataPodId = dataPodId;}

    public String getEmbeddingModel() {return embeddingModel;}

    public void setEmbeddingModel(String embeddingModel) {this.embeddingModel = embeddingModel;}

    public UUID getProcessorAgentId() {
        return processorAgentId;
    }

    public void setProcessorAgentId(UUID processorAgentId) {
        this.processorAgentId = processorAgentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuditPermissionOfUseVc that = (AuditPermissionOfUseVc) o;
        return Objects.equals(permissionOfUseVcId, that.permissionOfUseVcId) && Objects.equals(searchId, that.searchId) && Objects.equals(sectionIscc, that.sectionIscc) && Objects.equals(documentIscc, that.documentIscc) && Objects.equals(ownerOrganizationDid, that.ownerOrganizationDid) && Objects.equals(ownerOrganizationId, that.ownerOrganizationId) && Objects.equals(processorOrganizationDid, that.processorOrganizationDid) && Objects.equals(processorOrganizationId, that.processorOrganizationId) && Objects.equals(ownerDataPodId, that.ownerDataPodId) && Objects.equals(processorAgentId, that.processorAgentId) && Objects.equals(embeddingModel, that.embeddingModel) && Objects.equals(tokens, that.tokens) && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(permissionOfUseVcId, searchId, sectionIscc, documentIscc, ownerOrganizationDid, ownerOrganizationId, processorOrganizationDid, processorOrganizationId, ownerDataPodId, processorAgentId, embeddingModel, tokens, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"permissionOfUseVcId\": ").append("\"").append(permissionOfUseVcId).append("\"").append(",");
        sb.append("\"searchId\": ").append("\"").append(searchId).append("\"").append(",");
        sb.append("\"sectionIscc\": ").append("\"").append(sectionIscc).append("\"").append(",");
        sb.append("\"documentIscc\": ").append("\"").append(documentIscc).append("\"").append(",");
        sb.append("\"ownerOrganizationDid\": ").append("\"").append(ownerOrganizationDid).append("\"").append(",");
        sb.append("\"ownerOrganizationId\": ").append("\"").append(ownerOrganizationId).append("\"").append(",");
        sb.append("\"processorOrganizationDid\": ").append("\"").append(processorOrganizationDid).append("\"").append(",");
        sb.append("\"processorOrganizationId\": ").append("\"").append(processorOrganizationId).append("\"").append(",");
        sb.append("\"ownerDataPodId\": ").append("\"").append(ownerDataPodId).append("\"").append(",");
        sb.append("\"processorAgentId\": ").append("\"").append(processorAgentId).append("\"").append(",");
        sb.append("\"embeddingModel\": ").append("\"").append(embeddingModel).append("\"").append(",");
        sb.append("\"tokens\": ").append("\"").append(tokens).append("\"").append(",");
        sb.append("\"createdAt\": ").append("\"").append(createdAt).append("\"").append(",");
        sb.append("\"updatedAt\": ").append("\"").append(updatedAt).append("\"");
        sb.append("}");
        return sb.toString();
    }


}
