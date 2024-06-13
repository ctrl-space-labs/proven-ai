package dev.ctrlspace.provenai.backend.model.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class AuditPermissionOfUseDTO {
    private UUID ownerDatapodId;
    private UUID ownerOrganizationId;
    private UUID processorAgentId;
    private UUID processorOrganizationId;
    private Instant bucketStart;
    private Long sumTokens;

    public AuditPermissionOfUseDTO(UUID ownerDatapodId, UUID ownerOrganizationId, UUID processorAgentId, UUID processorOrganizationId, Timestamp bucketStart, Long sumTokens) {
        this.ownerDatapodId = ownerDatapodId;
        this.ownerOrganizationId = ownerOrganizationId;
        this.processorAgentId = processorAgentId;
        this.processorOrganizationId = processorOrganizationId;
        this.bucketStart = bucketStart.toInstant(); // Convert Timestamp to Instant
        this.sumTokens = sumTokens;
    }

}