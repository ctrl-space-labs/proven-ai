package dev.ctrlspace.provenai.backend.model.dtos.criteria;

import dev.ctrlspace.provenai.backend.model.dtos.validators.ValidAuditPermissionOfUseVcCriteria;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@ValidAuditPermissionOfUseVcCriteria
public class AuditPermissionOfUseVcCriteria {

    private String searchId;
    private String documentIscc;
    private String ownerOrganizationId;
    private String ownerOrganizationDid;
    private String processorOrganizationId;
    private String processorOrganizationDid;
    private List<String> ownerDataPodIdIn = new ArrayList<>();
    private List<String> processorAgentIdIn = new ArrayList<>();
    private String embeddingModel;

    @NotNull(message = "TimeIntervalInSeconds cannot be null")
    @Min(value = 0, message = "TimeIntervalInSeconds must be positive")
    private Long timeIntervalInSeconds;

    @NotNull(message = "From time cannot be null")
    private Instant from;

    @NotNull(message = "To time cannot be null")
    private Instant to;

}
