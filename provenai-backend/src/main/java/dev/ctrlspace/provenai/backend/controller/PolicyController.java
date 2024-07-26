package dev.ctrlspace.provenai.backend.controller;

import dev.ctrlspace.provenai.backend.converters.AclPoliciesConverter;
import dev.ctrlspace.provenai.backend.converters.AgentPurposeOfUsePoliciesConverter;
import dev.ctrlspace.provenai.backend.exceptions.ProvenAiException;
import dev.ctrlspace.provenai.backend.model.AclPolicies;
import dev.ctrlspace.provenai.backend.model.AgentPurposeOfUsePolicies;
import dev.ctrlspace.provenai.backend.model.PolicyOption;
import dev.ctrlspace.provenai.backend.model.dtos.AclPoliciesDTO;
import dev.ctrlspace.provenai.backend.model.dtos.AgentPurposeOfUsePoliciesDTO;
import dev.ctrlspace.provenai.backend.services.AclPoliciesService;
import dev.ctrlspace.provenai.backend.services.AgentPurposeOfUsePoliciesService;
import dev.ctrlspace.provenai.backend.services.PolicyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class PolicyController {

    private PolicyService policyService;


    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;

    }

    @GetMapping("/policy-options")
    public List<PolicyOption> getPolicyOptionsByTypeName(@RequestParam String policyTypeName) throws ProvenAiException {

        return policyService.getPolicyOptionsByPolicyTypeId(policyService.getPolicyTypeByName(policyTypeName).getId());

    }

}
