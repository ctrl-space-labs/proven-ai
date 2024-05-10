package dev.ctrlspace.provenai.backend.services;

import dev.ctrlspace.provenai.backend.exceptions.ProvenAiException;
import dev.ctrlspace.provenai.backend.model.AclPolicies;
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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DataPodService {

    private DataPodRepository dataPodRepository;

    private AclPoliciesService aclPoliciesService;

    @Autowired
    public DataPodService(DataPodRepository dataPodRepository,
                          AclPoliciesService aclPoliciesService) {
        this.dataPodRepository = dataPodRepository;
        this.aclPoliciesService = aclPoliciesService;
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


        DataPod savedDataPod = dataPodRepository.save(dataPod);

        List<AclPolicies> savedPolicies = aclPoliciesService.savePoliciesForDataPod(savedDataPod, policies);

        return savedDataPod;
    }

    public DataPod updateDataPod(DataPod dataPod) {
        return dataPodRepository.save(dataPod);
    }


}
