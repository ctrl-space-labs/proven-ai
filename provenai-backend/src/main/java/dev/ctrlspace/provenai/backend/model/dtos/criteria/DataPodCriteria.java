package dev.ctrlspace.provenai.backend.model.dtos.criteria;

import dev.ctrlspace.provenai.backend.model.AclPolicies;
import dev.ctrlspace.provenai.ssi.model.vc.attestation.Policy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class DataPodCriteria {

        private String organizationId;


        private String podUniqueName;
        private Policy policy;
        private List<Policy> policyIn;
        private List<UUID> dataPodIdIn = new ArrayList<>();;
        private boolean fetchAll;



}
