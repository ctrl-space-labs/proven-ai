package dev.ctrlspace.provenai.backend.services;

import dev.ctrlspace.provenai.backend.exceptions.ProvenAiException;
import dev.ctrlspace.provenai.backend.model.PolicyOption;
import dev.ctrlspace.provenai.backend.model.PolicyType;
import dev.ctrlspace.provenai.backend.repositories.AclPoliciesRepository;
import dev.ctrlspace.provenai.backend.repositories.PolicyOptionRepository;
import dev.ctrlspace.provenai.backend.repositories.PolicyTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PolicyService {
    private PolicyOptionRepository policyOptionRepository;

    private PolicyTypeRepository policyTypeRepository;

    @Autowired
    public PolicyService(PolicyOptionRepository policyOptionRepository, PolicyTypeRepository policyTypeRepository) {
        this.policyOptionRepository = policyOptionRepository;
        this.policyTypeRepository = policyTypeRepository;
    }


    public PolicyType getPolicyTypeByName(String policyName) throws ProvenAiException {
        return policyTypeRepository.findByName(policyName);
    }


    public List<PolicyOption> getPolicyOptionsByPolicyTypeId(UUID policyTypeId) throws ProvenAiException {
        return policyOptionRepository.findByPolicyTypeId(policyTypeId);
    }


    public List<PolicyOption> getUsagePolicyOptions() throws ProvenAiException {
        return policyOptionRepository.findByPolicyTypeId(policyTypeRepository.findByName("USAGE_POLICY").getId());
    }


}
