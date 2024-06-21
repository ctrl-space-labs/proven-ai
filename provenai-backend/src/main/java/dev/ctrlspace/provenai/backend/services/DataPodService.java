package dev.ctrlspace.provenai.backend.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.ctrlspace.provenai.backend.exceptions.ProvenAiException;
import dev.ctrlspace.provenai.backend.model.AclPolicies;
import dev.ctrlspace.provenai.backend.model.Agent;
import dev.ctrlspace.provenai.backend.model.DataPod;
import dev.ctrlspace.provenai.backend.model.dtos.criteria.DataPodCriteria;
import dev.ctrlspace.provenai.backend.repositories.DataPodRepository;
import dev.ctrlspace.provenai.backend.repositories.specifications.DataPodPredicates;
import dev.ctrlspace.provenai.ssi.model.vc.attestation.Policy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DataPodService {

    private DataPodRepository dataPodRepository;

    private AclPoliciesService aclPoliciesService;

    private AgentService agentService;

    @Autowired
    public DataPodService(DataPodRepository dataPodRepository,
                          AclPoliciesService aclPoliciesService,
                          AgentService agentService) {
        this.dataPodRepository = dataPodRepository;
        this.aclPoliciesService = aclPoliciesService;
        this.agentService = agentService;
    }


    public Optional<DataPod> getOptionalDataPodById(UUID id) {
        return dataPodRepository.findById(id);
    }

    public DataPod getDataPodById(UUID id) throws ProvenAiException {
        return this.getOptionalDataPodById(id)
                .orElseThrow(() -> new ProvenAiException("DATA_POD_NOT_FOUND", "DataPod not found with id: " + id, HttpStatus.NOT_FOUND));
    }


    public Page<DataPod> getAllDataPods(DataPodCriteria criteria, Pageable pageable) throws ProvenAiException {
        if (pageable == null) {
            throw new ProvenAiException("Pageable cannot be null", "pageable.null", HttpStatus.BAD_REQUEST);
        }
        return dataPodRepository.findAll(DataPodPredicates.build(criteria), pageable);
    }

    public Page<AclPolicies> getAclPoliciesByDataPodId(UUID dataPodId, Pageable pageable) throws  ProvenAiException{
        if (pageable == null) {
            throw new ProvenAiException("Pageable cannot be null", "pageable.null", HttpStatus.BAD_REQUEST);
        }
        return aclPoliciesService.getAclPoliciesByDataPodId(dataPodId, pageable);
    }


    public void deleteDataPodById(UUID id) throws ProvenAiException {
        DataPod dataPod = dataPodRepository.findById(id)
                .orElseThrow(() -> new ProvenAiException("DATA_POD_NOT_FOUND", "DataPod not found with id: " + id, HttpStatus.NOT_FOUND));
        aclPoliciesService.deletePoliciesByDataPodId(id);
        dataPodRepository.delete(dataPod);

    }


    public DataPod createDataPod(DataPod dataPod, List<Policy> policies) throws ProvenAiException {

        if (dataPodRepository.existsByPodUniqueName(dataPod.getPodUniqueName())) {
            throw new ProvenAiException("DataPod with the same name already exists", "dataPod.name.duplicate", HttpStatus.BAD_REQUEST);
        }

        Instant now = Instant.now();
        dataPod.setCreatedAt(now);
        dataPod.setUpdatedAt(now);

        DataPod savedDataPod = dataPodRepository.save(dataPod);

        List<AclPolicies> savedPolicies = aclPoliciesService.savePoliciesForDataPod(savedDataPod, policies);

        return savedDataPod;
    }

    public DataPod updateDataPod(DataPod dataPod) throws ProvenAiException {
        UUID dataPodId = dataPod.getId();
        DataPod existingDataPod = dataPodRepository.findById(dataPod.getId())
                .orElseThrow(() -> new ProvenAiException("DATA_POD_NOT_FOUND", "DataPod not found with id: " + dataPod.getId(), HttpStatus.NOT_FOUND));
        existingDataPod.setPodUniqueName(dataPod.getPodUniqueName());
        existingDataPod.setHostUrl(dataPod.getHostUrl());
        existingDataPod.setUpdatedAt(Instant.now());

        return dataPodRepository.save(existingDataPod);


    }

        public List<DataPod> getAccessibleDataPodsForAgent(String agentUsername) throws ProvenAiException, JsonProcessingException {
            Agent agent = agentService.getAgentByUsername(agentUsername);

            return dataPodRepository.findAccessibleDataPodsForAgent(agent.getId());

        }


    public Map<String, List<UUID>> getDataPodsByHostUrl(List<DataPod> dataPods) {
        return dataPods.stream()
                .collect(Collectors.groupingBy(DataPod::getHostUrl,
                        Collectors.mapping(DataPod::getId, Collectors.toList())));
    }


    }

