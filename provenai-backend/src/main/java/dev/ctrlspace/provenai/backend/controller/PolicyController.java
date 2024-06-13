package dev.ctrlspace.provenai.backend.controller;

import dev.ctrlspace.provenai.backend.exceptions.ProvenAiException;
import dev.ctrlspace.provenai.backend.model.PolicyOption;
import dev.ctrlspace.provenai.backend.services.PolicyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
