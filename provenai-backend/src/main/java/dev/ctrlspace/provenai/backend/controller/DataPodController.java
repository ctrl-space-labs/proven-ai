package dev.ctrlspace.provenai.backend.controller;

import dev.ctrlspace.provenai.backend.controller.specs.DataPodControllerSpec;
import dev.ctrlspace.provenai.backend.exceptions.ProvenAiException;
import dev.ctrlspace.provenai.backend.model.DataPod;
import dev.ctrlspace.provenai.backend.model.dtos.criteria.DataPodCriteria;
import dev.ctrlspace.provenai.backend.services.DataPodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataPodController implements DataPodControllerSpec {


    private DataPodService dataPodService;

    @Autowired
    public DataPodController(DataPodService dataPodService) {
        this.dataPodService = dataPodService;
    }

    @GetMapping("/data-pods")
    public Page<DataPod> getAll(DataPodCriteria criteria, Pageable pageable) throws ProvenAiException {
        return dataPodService.getAllDataPods(criteria, pageable);
    }
}
