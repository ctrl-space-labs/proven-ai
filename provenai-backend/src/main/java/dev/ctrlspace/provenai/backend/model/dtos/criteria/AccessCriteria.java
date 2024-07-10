package dev.ctrlspace.provenai.backend.model.dtos.criteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class AccessCriteria {
    private Set<String> orgIds;
    private Set<String> dataPodIds;
    private Set<String> agentIds;

    public boolean isEmpty() {
        return (orgIds == null || orgIds.isEmpty()) &&
                (dataPodIds == null || dataPodIds.isEmpty()) &&
                (agentIds == null || agentIds.isEmpty());
    }
}
