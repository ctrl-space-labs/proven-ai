package dev.ctrlspace.provenai.backend.services;

import dev.ctrlspace.provenai.backend.exceptions.ProvenAiException;
import dev.ctrlspace.provenai.backend.model.*;
import dev.ctrlspace.provenai.backend.repositories.PolicyOptionRepository;
import dev.ctrlspace.provenai.backend.repositories.PolicyTypeRepository;
import dev.ctrlspace.provenai.ssi.model.vc.attestation.Policy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import dev.ctrlspace.provenai.backend.repositories.AclPoliciesRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AclPoliciesService {

    private AclPoliciesRepository aclPoliciesRepository;

    private PolicyOptionRepository policyOptionRepository;

    private PolicyTypeRepository policyTypeRepository;


    @Autowired
    public AclPoliciesService(AclPoliciesRepository aclPoliciesRepository, PolicyOptionRepository policyOptionRepository, PolicyTypeRepository policyTypeRepository) {
        this.aclPoliciesRepository = aclPoliciesRepository;
        this.policyOptionRepository = policyOptionRepository;
        this.policyTypeRepository = policyTypeRepository;
    }

    Page<AclPolicies> getAclPoliciesByDataPodId(UUID dataPodId, Pageable pageable) throws ProvenAiException {

        return aclPoliciesRepository.findByDataPodId(dataPodId, pageable);

    }

    List<AclPolicies> getAclPoliciesByDataPodId(UUID dataPodId)  {

        return aclPoliciesRepository.findByDataPodId(dataPodId);

    }


    public AclPolicies createAclPolicy(AclPolicies aclPolicy) {
        Instant now = Instant.now();
        aclPolicy.setCreatedAt(now);
        aclPolicy.setUpdatedAt(now);
        return aclPoliciesRepository.save(aclPolicy);
    }

    public List<AclPolicies> savePoliciesForDataPod(DataPod savedDataPod, List<Policy> policies) {
        List<AclPolicies> savedPolicies = policies.stream()
                .map(policy -> {
                    // Retrieve PolicyOption based on policy type name
                    PolicyOption policyOption = policyOptionRepository.findByName(policy.getPolicyValue());
                    PolicyType policyType = policyTypeRepository.findById(policyOption.getPolicyTypeId())
                            .orElseThrow(() -> new RuntimeException("Policy Type not found"));

                    AclPolicies aclPolicies = new AclPolicies();
                    aclPolicies.setDataPod(savedDataPod);
                    aclPolicies.setPolicyOption(policyOption);
                    aclPolicies.setValue(policy.getPolicyValue());
                    aclPolicies.setPolicyType(policyType);
                    aclPolicies.setCreatedAt(Instant.now());
                    aclPolicies.setUpdatedAt(Instant.now());

                    return aclPolicies;
                })
                .collect(Collectors.toList());

        // Save all the policies
        return aclPoliciesRepository.saveAll(savedPolicies);
    }

//    List<AclPolicies> savePoliciesForDataPod() {
//        List<AclPolicies> savedPolicies = policies.stream().map(policy -> {
//            PolicyType policyType = policyTypeRepository.findByName(policy.getPolicyType());
//            Instant now = Instant.now();
//
//            AclPolicies aclPolicy = new AclPolicies();
//            aclPolicy.setDataPod(savedDataPod);
//            aclPolicy.setPolicyType(policyType);
//            aclPolicy.setCreatedAt(now);
//            aclPolicy.setUpdatedAt(now);
////          policies for agents access to data pods
//            if (policyType.getName().equals("ALLOW_LIST") || policyType.getName().equals("DENY_LIST")) {
////              the value will be the agent ID
////                PolicyOption policyOption = policyOptionRepository.findByPolicyTypeId(policyType.getId());
////                aclPolicy.setPolicyOption(policyOption);
//                aclPolicy.setValue(policy.getPolicyValue());
//
//            } else {
//                PolicyOption policyOption = policyOptionRepository.findByName(policy.getPolicyValue());
//                aclPolicy.setPolicyOption(policyOption);
//                aclPolicy.setValue(policyOption.getName());
//
//            }
//
//            return aclPolicy;
//        }).toList();
//
//        return aclPoliciesRepository.saveAll(savedPolicies);
//    }


    public void deletePoliciesByDataPodId(UUID dataPodId) {
        List<AclPolicies> aclPolicies = aclPoliciesRepository.findByDataPodId(dataPodId);
        aclPoliciesRepository.deleteAll(aclPolicies);
    }

    public void deleteAclPolicy(UUID aclPolicyId) {
        aclPoliciesRepository.deleteById(aclPolicyId);
    }

    public void deleteAclPolicies(List<UUID> aclPolicyIds) {
        aclPoliciesRepository.deleteAllById(aclPolicyIds);
    }


}
