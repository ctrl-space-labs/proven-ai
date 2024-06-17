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
    private AclPoliciesService aclPoliciesService;
    private AclPoliciesConverter aclPoliciesConverter;
    private AgentPurposeOfUsePoliciesService agentPurposeOfUsePoliciesService;
    private AgentPurposeOfUsePoliciesConverter agentPurposeOfUsePoliciesConverter;

    public PolicyController(PolicyService policyService,
                            AclPoliciesService aclPoliciesService,
                            AclPoliciesConverter aclPoliciesConverter,
                            AgentPurposeOfUsePoliciesService agentPurposeOfUsePoliciesService,
                            AgentPurposeOfUsePoliciesConverter agentPurposeOfUsePoliciesConverter) {
        this.policyService = policyService;
        this.aclPoliciesService = aclPoliciesService;
        this.aclPoliciesConverter = aclPoliciesConverter;
        this.agentPurposeOfUsePoliciesService = agentPurposeOfUsePoliciesService;
        this.agentPurposeOfUsePoliciesConverter = agentPurposeOfUsePoliciesConverter;
    }

    @GetMapping("/policy-options")
    public List<PolicyOption> getPolicyOptionsByTypeName(@RequestParam String policyTypeName) throws ProvenAiException {

        return policyService.getPolicyOptionsByPolicyTypeId(policyService.getPolicyTypeByName(policyTypeName).getId());

    }


    @PostMapping(value = "/acl-policies", consumes = {"application/json"})
    public AclPolicies createAclPolicy(@RequestBody AclPoliciesDTO aclPoliciesDTO) throws ProvenAiException {
        AclPolicies aclPolicies = aclPoliciesConverter.toEntity(aclPoliciesDTO);
        return aclPoliciesService.createAclPolicy(aclPolicies);
    }

    @PostMapping(value = "/agent-purpose-of-use-policies", consumes = {"application/json"})
    public AgentPurposeOfUsePolicies createAgentPurposeOfUsePolicy(@RequestBody AgentPurposeOfUsePoliciesDTO agentPurposeOfUsePoliciesDTO) throws ProvenAiException {
        AgentPurposeOfUsePolicies agentPurposeOfUsePolicies = agentPurposeOfUsePoliciesConverter.toEntity(agentPurposeOfUsePoliciesDTO);
        return agentPurposeOfUsePoliciesService.createAgentPurposeOfUsePolicy(agentPurposeOfUsePolicies);
    }


    @DeleteMapping("/acl-policies/{aclPolicyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAclPolicy(@PathVariable UUID aclPolicyId) throws ProvenAiException {
        aclPoliciesService.deleteAclPolicy(aclPolicyId);
    }

    @DeleteMapping("/acl-policies")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAclPolicies(@RequestParam List<UUID> aclPolicyIds) throws ProvenAiException {
        aclPoliciesService.deleteAclPolicies(aclPolicyIds);
    }

    @DeleteMapping("/agent-purpose-of-use-policies")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAgentPurposeOfUsePolicies(@RequestParam List<UUID> agentPurposeOfUsePolicyIds) throws ProvenAiException {
        agentPurposeOfUsePoliciesService.deleteAgentPurposeOfUsePolicies(agentPurposeOfUsePolicyIds);
    }










}
