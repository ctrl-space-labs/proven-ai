package dev.ctrlspace.provenai.backend.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class DocumentInstanceSectionDTO {

    private UUID id;
    private DocumentDTO documentDTO;
    private DocumentSectionMetadataDTO documentSectionMetadata;
    private String sectionValue;
    private String aiModelName;
    private Double tokenCount;
    private String documentSectionIsccCode;
    private UUID createdBy;
    private UUID updatedBy;
    private Instant createdAt;
    private Instant updatedAt;
    private Double distanceFromQuestion;
    private String distanceModelName;
}
