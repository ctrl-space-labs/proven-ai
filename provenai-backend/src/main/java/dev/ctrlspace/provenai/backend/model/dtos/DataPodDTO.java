package dev.ctrlspace.provenai.backend.model.dtos;

import dev.ctrlspace.provenai.backend.model.AclPolicies;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class DataPodDTO {

    private UUID id;

    private UUID organizationId;

    private String podUniqueName;

    private String hostUrl;

    private List<AclPolicies> aclPolicies;

    private Instant createdAt;

    private Instant updatedAt;

    private UUID createdBy;

    private UUID updatedBy;
}
