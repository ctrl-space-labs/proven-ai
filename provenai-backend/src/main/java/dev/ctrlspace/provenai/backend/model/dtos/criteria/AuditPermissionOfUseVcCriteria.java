package dev.ctrlspace.provenai.backend.model.dtos.criteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class AuditPermissionOfUseVcCriteria {

    private String searchId;
    private String documentIscc;
    private String ownerOrganizationId;
    private String processorOrganizationId;
    private String datapodId;
    private String embeddingModel;



}
