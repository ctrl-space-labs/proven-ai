package dev.ctrlspace.provenai.backend.model.dtos.criteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class DataPodCriteria {

        private String organizationId;

        private String allowAgentId; // the pods that a specific agent is allowed to access
        private String denyAgentId;  // the pods that a specific agent is denied to access

}
