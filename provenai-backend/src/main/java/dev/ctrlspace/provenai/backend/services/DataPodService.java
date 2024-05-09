package dev.ctrlspace.provenai.backend.services;

import dev.ctrlspace.provenai.backend.exceptions.ProvenAiException;
import dev.ctrlspace.provenai.backend.model.DataPod;
import dev.ctrlspace.provenai.backend.model.dtos.criteria.DataPodCriteria;
import dev.ctrlspace.provenai.backend.repositories.DataPodRepository;
import dev.ctrlspace.provenai.backend.repositories.specifications.DataPodPredicates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class DataPodService {

    private DataPodRepository dataPodRepository;

    @Autowired
    public DataPodService(DataPodRepository dataPodRepository) {
        this.dataPodRepository = dataPodRepository;
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

//    delete datapodById

    public void deleteDataPodById(UUID id) throws ProvenAiException {
        DataPod dataPod = this.getDataPodById(id);
        dataPodRepository.delete(dataPod);
    }

    public DataPod createDataPod(DataPod dataPod) {
        return dataPodRepository.save(dataPod);
    }

    public DataPod updateDataPod(DataPod dataPod) {
        return dataPodRepository.save(dataPod);
    }





}
