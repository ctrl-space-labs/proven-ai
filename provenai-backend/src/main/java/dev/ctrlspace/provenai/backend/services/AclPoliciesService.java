package dev.ctrlspace.provenai.backend.services;

import dev.ctrlspace.provenai.backend.model.*;
import dev.ctrlspace.provenai.backend.repositories.PolicyOptionRepository;
import dev.ctrlspace.provenai.backend.repositories.PolicyTypeRepository;
import dev.ctrlspace.provenai.ssi.model.vc.attestation.Policy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dev.ctrlspace.provenai.backend.repositories.AclPoliciesRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class AclPoliciesService {

    private AclPoliciesRepository aclPoliciesRepository;

    private PolicyOptionRepository policyOptionRepository;

    private PolicyTypeRepository policyTypeRepository;

    @Autowired
    public AclPoliciesService(AclPoliciesRepository aclPoliciesRepository,
                              PolicyOptionRepository policyOptionRepository,
                              PolicyTypeRepository policyTypeRepository) {
        this.aclPoliciesRepository = aclPoliciesRepository;
        this.policyOptionRepository = policyOptionRepository;
        this.policyTypeRepository = policyTypeRepository;
    }

    List<AclPolicies> getAclPoliciesByDataPodId(UUID dataPodId) {
        return aclPoliciesRepository.findByDataPodId(dataPodId);

    }


    List<AclPolicies> savePoliciesForDataPod(DataPod savedDataPod, List<Policy> policies) {
        List<AclPolicies> savedPolicies = policies.stream()
                .map(policy -> {
                    PolicyOption policyOption = policyOptionRepository.findByName(policy.getPolicyValue());
                    PolicyType policyType = policyTypeRepository.findByName(policy.getPolicyType());

                    AclPolicies aclPolicy = new AclPolicies();
                    aclPolicy.setDataPod(savedDataPod);
                    aclPolicy.setPolicyOption(policyOption);
                    aclPolicy.setValue(policy.getPolicyValue());
                    aclPolicy.setPolicyType(policyType);
                    aclPolicy.setCreatedAt(Instant.now());
                    aclPolicy.setUpdatedAt(Instant.now());
                    return aclPolicy;
                }).toList();

        return aclPoliciesRepository.saveAll(savedPolicies);
    }

    public void deletePoliciesByDataPodId(UUID dataPodId) {
        List<AclPolicies> aclPolicies = aclPoliciesRepository.findByDataPodId(dataPodId);
        aclPoliciesRepository.deleteAll(aclPolicies);
    }


}
