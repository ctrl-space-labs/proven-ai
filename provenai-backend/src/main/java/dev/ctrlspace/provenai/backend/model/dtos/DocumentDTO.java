package dev.ctrlspace.provenai.backend.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class DocumentDTO {

    private UUID id;
    private UUID organizationId;
    private UUID documentTemplateId;
    private String remoteUrl;
    private String documentIsccCode;
    private UUID createdBy;
    private UUID updatedBy;
    private Instant createAt;
    private Instant updateAt;
    private List<DocumentInstanceSectionDTO> documentInstanceSections = new ArrayList<>();

}
